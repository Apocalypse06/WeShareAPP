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
import com.example.ntut.weshare.homeGoodsDetail.DealBean;
import com.example.ntut.weshare.message.MessageBean;

import java.sql.Timestamp;

import static android.content.Context.MODE_PRIVATE;

public class SendTextDialogFragment extends DialogFragment {//必須繼承DialogFragment，並實作OnClickListener監聽器
    private final static String TAG = "DealDialogFragment";

    private EditText etSendContext;
    private Button btSend;
    private Button btCancel;

    private Dealing ref;

    public void setRef(Dealing ref) {
        this.ref = ref;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detailing_note_dialog_fragment, container);

        etSendContext = (EditText) view.findViewById(R.id.etSendContext);
        btSend = (Button) view.findViewById(R.id.btSedn);
        btCancel = (Button) view.findViewById(R.id.btCancel);


        btSend.setOnClickListener(new View.OnClickListener() {
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
        if (!"".equals(etSendContext.getText().toString().trim())) {
            if (etSendContext.getText().toString().trim().length() > 20) {
                Common.showToast(getActivity(), "字數不可超過20字");
                return;
            }
            String text = etSendContext.getText().toString().trim();
            int count = 0;
            String url = Common.URL + "DealServlet";
            String action = "sendDealContext";
            if (Common.networkConnected(getActivity())) {//傳送到server端
                try {
                    count = new SendDealingContextTask().execute(url, action, ref.dealStatic.getDealNo(), text).get();
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