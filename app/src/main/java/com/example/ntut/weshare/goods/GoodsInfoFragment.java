package com.example.ntut.weshare.goods;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ntut.weshare.R;

/**
 * Created by NTUT on 2017/6/28.
 */

public class GoodsInfoFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.goodsbox_info_fragment, container, false);

        return view;
    }
}
