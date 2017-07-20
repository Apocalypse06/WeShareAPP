package com.example.ntut.weshare.homeGoodsDetail;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.R;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;

public class GoodsDialogFragment extends DialogFragment {//必須繼承DialogFragment，並實作OnClickListener監聽器
    private final static String TAG = "DealDialogFragment";

    private LinearLayout llBackground;
    private TextView tvTitle;
    private TextView tvGoodsName;
    private EditText etQty;
    private Spinner spWay;
    private Button btAccept;
    private Button btCancel;
    private EditText etDealNote;

    private GoodsDetailFragment ref;

    public void setRef(GoodsDetailFragment ref) {
        this.ref = ref;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.accept_dialog, container);

        llBackground = (LinearLayout) view.findViewById(R.id.llBackground);
        tvGoodsName = (TextView) view.findViewById(R.id.tvGoodsName);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        etQty = (EditText) view.findViewById(R.id.etQty);
        spWay = (Spinner) view.findViewById(R.id.spWay);
        btAccept = (Button) view.findViewById(R.id.btAccept);
        btCancel = (Button) view.findViewById(R.id.btCancel);
        etDealNote = (EditText) view.findViewById(R.id.etDealNote);


        if (ref.goodsPublic.getGoodsStatus() == 1) {
            llBackground.setBackgroundColor(Color.parseColor("#fffbe1e8"));
        } else if (ref.goodsPublic.getGoodsStatus() == 2) {
            llBackground.setBackgroundColor(Color.parseColor("#fff8bc7c"));
            tvTitle.setText("募集");
            btAccept.setText("要求");
        } else if (ref.goodsPublic.getGoodsStatus() == 3) {
            llBackground.setBackgroundColor(Color.parseColor("#ff44f8ac"));
        }

            etQty.setHint("最高可捐獻數量為 "+ref.goodsPublic.getQty());
        tvGoodsName.setText(ref.goodsPublic.getGoodsName());
        if (ref.goodsPublic.getGoodsShipWay() == 1) {
            final String[] classes = {"面交"};
            ArrayAdapter<String> classList = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_spinner_dropdown_item,
                    classes);
            spWay.setAdapter(classList);
            spWay.setEnabled(false);
        } else if (ref.goodsPublic.getGoodsShipWay() == 2) {
            final String[] classes = {"物流"};
            ArrayAdapter<String> classList = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_spinner_dropdown_item,
                    classes);
            spWay.setAdapter(classList);
            spWay.setEnabled(false);
        } else {
            final String[] classes = {"面交", "物流"};
            ArrayAdapter<String> classList = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_spinner_dropdown_item,
                    classes);
            spWay.setAdapter(classList);
        }
        btAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptDeal();
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


    private void acceptDeal() {
        String qty = etQty.getText().toString().trim();
        String dealNote = etDealNote.getText().toString().trim();
        int intValue = 0;
        if (qty.length() <= 0) {
            Common.showToast(getActivity(), R.string.msg_qtyNull);
            return;
        } else {
            intValue = Integer.valueOf(qty);
            if (intValue > ref.goodsPublic.getQty()) {
                Common.showToast(getActivity(), "高過需求\n需求數量為" + ref.goodsPublic.getQty());
                return;
            }
        }
        int endShipWay = ChoseOreType();
        Timestamp postDate = new Timestamp(new java.util.Date().getTime());
        int count = 0;
        String action = "newDeal";

        Bitmap bitmap = null;
        String url = Common.URL + "GoodsServlet";
        int gid = ref.goodsPublic.getGoodsNo();
        int imageSize = 800;
        byte[] image = null;
        DealBean deal = null;

        SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
        String account = pref.getString("user", "");

        if (Common.networkConnected(getActivity())) {//傳送到server端
            if (ref.goodsPublic.getGoodsStatus() == 1) {
                deal = new DealBean(postDate, account, ref.goodsPublic.getIndId(), 0, endShipWay, null, null, ref.goodsPublic.getGoodsName(), ref.goodsPublic.getGoodsStatus(), intValue, ref.goodsPublic.getGoodsfilename(), ref.goodsPublic.getGoodsType(), ref.goodsPublic.getGoodsLoc(), ref.goodsPublic.getGoodsNote(), dealNote);
            } else if (ref.goodsPublic.getGoodsStatus() == 2) {
                deal = new DealBean(postDate, ref.goodsPublic.getIndId(), account, 0, endShipWay, null, null, ref.goodsPublic.getGoodsName(), ref.goodsPublic.getGoodsStatus(), intValue, ref.goodsPublic.getGoodsfilename(), ref.goodsPublic.getGoodsType(), ref.goodsPublic.getGoodsLoc(), ref.goodsPublic.getGoodsNote(), dealNote);
            }
            try {
                bitmap = new GoodsGetImageTask(null).execute(url, gid, imageSize).get();

                ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out2);
                image = out2.toByteArray();
                String imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);//圖片資料

                count = new NewDealTask().execute(action, deal, "" + ref.goodsPublic.getGoodsNo(), imageBase64).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (count == 0) {
                Common.showToast(getActivity(), R.string.msg_dealFail);
            } else {
                Common.showToast(getActivity(), R.string.msg_dealSuccess);
                dismiss();
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
        ref.goodsPublic.setQty(ref.goodsPublic.getQty() - intValue);
        dismiss();
    }

    private int ChoseOreType() {
        int endShipWay = 0;
        switch (spWay.getSelectedItem().toString()) {
            case "面交":
                endShipWay = 0;
                break;
            case "物流":
                endShipWay = 1;
                break;
        }
        return endShipWay;
    }

    @Override
    public void onDismiss(DialogInterface frag) {
//            (dealDetailActivity)getActivity().showAllDeals();
//            getFragmentManager().onStart();

        //FragmentManager fm = getActivity().getSupportFragmentManager();
        ref.findViews();
        //d.show(fm, "fragment_name");

        super.onDismiss(frag);


    }

}