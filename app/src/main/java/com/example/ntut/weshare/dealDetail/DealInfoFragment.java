package com.example.ntut.weshare.dealDetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ntut.weshare.R;
import com.example.ntut.weshare.homeGoodsDetail.DealBean;


public class DealInfoFragment extends Fragment {
    private static final String TAG = "DealInfoFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private DealBean deal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_info_fragment, container, false);
        //deal = (DealBean) getArguments().getSerializable("deal");
        return view;
    }


    private void showAllDeals() {
//        if (Common.networkConnected(getActivity())) {
//            SharedPreferences pref = this.getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
//            String user = pref.getString("user", "");
//            String ACTION = "getNotDeal";
//            List<DealBean> deals = null;
//            try {
//                deals = new GetDealTask().execute(ACTION, user).get();
//            } catch (Exception e) {
//                Log.e(TAG, e.toString());
//            }
//            if (deals == null || deals.isEmpty()) {
//                Common.showToast(getActivity(), "沒有未同意的交易訂單");
//            } else {
//                rvNotDeal.setAdapter(new NotDealRecyclerViewAdapter(getActivity(), deals));
//            }
//        } else {
//            Common.showToast(getActivity(), R.string.msg_NoNetwork);
//        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//       // showAllDeals();
//    }

}