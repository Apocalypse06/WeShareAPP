package com.example.ntut.weshare.dealDetail;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.R;
import com.example.ntut.weshare.homeGoodsDetail.Local;
import com.example.ntut.weshare.homeGoodsDetail.homeGetLocalTask;

import java.util.List;

public class DealedDialogFragment extends DialogFragment {//必須繼承DialogFragment，並實作OnClickListener監聽器
    private final static String TAG = "DealDialogFragment";
    private ImageView ivGoodsImage;
    private TextView tvAccount;
    private TextView tvGoodsName;
    private TextView tvPostTime;
    private TextView tvQty;
    private TextView tvLoc;
    private TextView tvWay;
    private TextView tvGoodsNote;
    private TextView tvAccountNote;
    private Button btCancel;

    private Dealed ref;

    public void setRef(Dealed ref) {
        this.ref = ref;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dealed_info_fragment, container);

        ivGoodsImage = (ImageView) view.findViewById(R.id.ivGoodsImage);
        tvAccount = (TextView) view.findViewById(R.id.tvAccount);
        tvGoodsName = (TextView) view.findViewById(R.id.tvGoodsName);
        tvPostTime = (TextView) view.findViewById(R.id.tvPostTime);
        tvQty = (TextView) view.findViewById(R.id.tvQty);
        tvLoc = (TextView) view.findViewById(R.id.tvLoc);
        tvWay = (TextView) view.findViewById(R.id.tvWay);
        tvGoodsNote = (TextView) view.findViewById(R.id.tvGoodsNote);
        tvAccountNote = (TextView) view.findViewById(R.id.tvAccountNote);
        btCancel = (Button) view.findViewById(R.id.btCancel);

        SharedPreferences pref = this.getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
        String user = pref.getString("user", "");

        String url = Common.URL + "DealServlet";
        int dealNo = ref.dealStatic.getDealNo();
        int imageSize = 250;
        new DealGetImageTask(ivGoodsImage).execute(url, dealNo, imageSize);


        if (user.equalsIgnoreCase(ref.dealStatic.getSourceId())) {
            tvAccount.setText("帳號：" + ref.dealStatic.getEndId());
        } else if (user.equalsIgnoreCase(ref.dealStatic.getEndId())) {
            tvAccount.setText("帳號：" + ref.dealStatic.getSourceId());
        }
 
        tvGoodsName.setText(ref.dealStatic.getGoodsName());
        tvPostTime.setText("交易時間：" + ref.dealStatic.getPostDate());
        tvQty.setText("數量：" + ref.dealStatic.getDealQty());

        List<Local> local = null;
        String action = "getLocal";
        String loc = "" + ref.dealStatic.getGoodsLoc();
        if (Common.networkConnected(getActivity())) {//傳送到server端
            try {
                local = new homeGetLocalTask().execute(action, loc).get();
            } catch (Exception e) {
                Log.e("Local", e.toString());
            }
            if (local == null) {
                Common.showToast(getActivity(), "錯誤");
            } else {
                tvLoc.setText("地點：" + local.get(0).getLocalName());
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
        if (ref.dealStatic.getEndShipWay() == 0) {
            tvWay.setText("面交");
        } else if (ref.dealStatic.getEndShipWay() == 1) {
            tvWay.setText("物流");
        }


        tvGoodsNote.setText(ref.dealStatic.getGoodsNote());
        tvAccountNote.setText(ref.dealStatic.getDealNote());


        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }


    public void onDis() {
        ref.showAllDeals();
        dismiss();
    }
}