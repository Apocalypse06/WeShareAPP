package com.example.ntut.weshare.dealDetail;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.R;
import com.example.ntut.weshare.message.MessageBean;

import java.sql.Timestamp;

import static android.content.Context.MODE_PRIVATE;

public class MsgRecommendDialogFragment extends DialogFragment {//必須繼承DialogFragment，並實作OnClickListener監聽器
    private final static String TAG = "DealDialogFragment";

    private TextView tvAccount;
    private EditText etMsgContext;
    private Button btSedn;
    private Button btCancel;

    private Dealed ref;

    public void setRef(Dealed ref) {
        this.ref = ref;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_msg_dialog_fragment, container);

        tvAccount = (TextView) view.findViewById(R.id.tvAccount);
        etMsgContext = (EditText) view.findViewById(R.id.etMsgContext);
        btSedn = (Button) view.findViewById(R.id.btSedn);
        btCancel = (Button) view.findViewById(R.id.btCancel);

        tvAccount.setText("帳號：" + ref.dealStatic.getEndId());

        btSedn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMsg();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }


    private void sendMsg() {
        if (!"".equals(etMsgContext.getText().toString().trim())) {
            String text = etMsgContext.getText().toString().trim();
            int count = 0;
            String url = Common.URL + "MsgServlet";
            String action = "dealSendMsg";
            MessageBean msg = null;
            Timestamp createDate = new Timestamp(new java.util.Date().getTime());

            if (Common.networkConnected(getActivity())) {//傳送到server端
                SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
                String account = pref.getString("user", "");


                msg = new MessageBean(1, 2, createDate, account, ref.dealStatic.getEndId(), text);
                try {
                    count = new SendMsgTask().execute(url, action, msg).get();
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }

                if (count == 0) {
                    Common.showToast(getActivity(), R.string.msg_SendFail);
                } else {
                    Common.showToast(getActivity(), "寄送成功");
                    onDis();
                }
            } else {
                Common.showToast(getActivity(), R.string.msg_NoNetwork);
            }
        }
    }

//    @Override
//    public void onDismiss(DialogInterface frag) {
//        ref.showAllDeals();
//        super.onDismiss(frag);
//    }

    public void onDis() {
        ref.showAllDeals();
        dismiss();
    }
}