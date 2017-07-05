package com.example.ntut.weshare;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ntut.weshare.goods.Goods;

import java.text.SimpleDateFormat;


public class GoodsDetailFragment extends Fragment {

    View view;

    private TextView tvGoodsName;
    private TextView tvdeadTime;
    private TextView tvQty;
    private TextView tvLoc;
    private TextView tvWay;
    private TextView tvAccount;
    private TextView tvNote;

    private Goods good;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_detail_goods, container, false);

        Bundle bundle = getActivity().getIntent().getBundleExtra("intentGoods");;
        good = (Goods) bundle.getSerializable("Goods");

        findViews();
        return view;
    }

    private void findViews() {
        tvGoodsName = (TextView) view.findViewById(R.id.tvGoodsName);
        tvdeadTime = (TextView) view.findViewById(R.id.tvdeadTime);
        tvQty = (TextView) view.findViewById(R.id.tvQty);
        tvLoc = (TextView) view.findViewById(R.id.tvLoc);
        tvWay = (TextView) view.findViewById(R.id.tvWay);
        tvAccount = (TextView) view.findViewById(R.id.tvAccount);
        tvNote = (TextView) view.findViewById(R.id.tvNote);

        tvGoodsName.setText(good.getGoodsName());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String exdate = sdf.format(good.getDeadLine());
        tvdeadTime.setText("到期日：" + exdate);

        tvQty.setText(""+good.getQty());
        tvLoc.setText(""+good.getGoodsLoc());
        tvWay.setText(""+good.getGoodsShipWay());
        tvAccount.setText(good.getIndId());
        tvNote.setText(good.getGoodsNote());
    }


}
