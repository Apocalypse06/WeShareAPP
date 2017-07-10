package com.example.ntut.weshare.dealDetail;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.R;
import com.example.ntut.weshare.homeGoodsDetail.DealBean;

import java.util.List;


public class NotDeal extends Fragment {
    private static final String TAG = "NotDealFragment";
    private static SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvNotDeal;
    private static String user;
    static DealBean dd = null;

    public void onResume() {
        super.onResume();
        // 從偏好設定檔中取得登入狀態來決定是否顯示「登出」
        SharedPreferences pref = this.getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
        String user = pref.getString("user", "");
        if (user == "") {
            Toast.makeText(this.getActivity(), "請註冊登入WeShare後，再過來設定您的物資箱喔~",
                    Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.deal_not_fragment, container, false);

        swipeRefreshLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.gb_swipeRefreshLayoutW);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                showAllDeals();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        rvNotDeal = (RecyclerView) view.findViewById(R.id.rvNotDeal);
        rvNotDeal.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }


    public void showAllDeals() {
        if (Common.networkConnected(getActivity())) {
            SharedPreferences pref = this.getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
            String user = pref.getString("user", "");
            String ACTION = "getNotDeal";
            List<DealBean> deals = null;
            try {
                deals = new GetDealTask().execute(ACTION, user).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (deals == null || deals.isEmpty()) {
                Common.showToast(getActivity(), "沒有未同意的交易訂單");
            } else {
                rvNotDeal.setAdapter(new NotDealRecyclerViewAdapter(getActivity(), deals));
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        showAllDeals();
    }

    private class NotDealRecyclerViewAdapter extends RecyclerView.Adapter<NotDealRecyclerViewAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<DealBean> deals;

        public NotDealRecyclerViewAdapter(Context context, List<DealBean> deals) {
            layoutInflater = LayoutInflater.from(context);
            this.deals = deals;
        }


        @Override
        public int getItemCount() {
            return deals.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.deal_not_recycleview_item, parent, false);
            return new NotDealRecyclerViewAdapter.MyViewHolder(itemView);

        }

        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, int position) {
            final DealBean deal = deals.get(position);
            String url = Common.URL + "DealServlet";
            int dealNo = deal.getDealNo();
            int imageSize = 250;
            new DealGetImageTask(myViewHolder.ivDealImage).execute(url, dealNo, imageSize);

            myViewHolder.tvDealGoods.setText(deal.getGoodsName());

            SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
            user = pref.getString("user", "");


            myViewHolder.tvDealQty.setText("數量：" + deal.getDealQty());
            if (user.equalsIgnoreCase(deal.getSourceId())) {
                myViewHolder.tvDealUser.setText("帳號：" + deal.getEndId());
            } else if (user.equalsIgnoreCase(deal.getEndId())) {
                myViewHolder.tvDealUser.setText("帳號：" + deal.getSourceId());
            }
            myViewHolder.tvDealTime.setText("交易時間：" + deal.getPostDate());

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dd = deal;
//                    NotDeal.AlertDialogFragment alertFragment = new NotDeal.AlertDialogFragment();//建立物件
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    alertFragment.show(fragmentManager, "alert");//顯示警示框

                    AlertDialogFragment detail = new AlertDialogFragment();
                    detail.setRef(NotDeal.this);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    detail.show(fragmentManager, "alert");//顯示警示框
                }
            });

            myViewHolder.ivMail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dd = deal;
                    MsgDialogFragment msg = new MsgDialogFragment();
                    msg.setRef(NotDeal.this);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    msg.show(fragmentManager, "alert");//顯示警示框

                }
            });

        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivDealImage, ivMail;
            TextView tvDealGoods, tvDealQty, tvDealUser, tvDealTime;

            public MyViewHolder(View itemView) {
                super(itemView);
                ivDealImage = (ImageView) itemView.findViewById(R.id.ivDealImage);
                ivMail = (ImageView) itemView.findViewById(R.id.ivMail);
                tvDealGoods = (TextView) itemView.findViewById(R.id.tvDealGoods);
                tvDealQty = (TextView) itemView.findViewById(R.id.tvDealQty);
                tvDealUser = (TextView) itemView.findViewById(R.id.tvDealUser);
                tvDealTime = (TextView) itemView.findViewById(R.id.tvDealTime);
            }
        }
    }


//
//    public static class AlertDialogFragment extends DialogFragment {//必須繼承DialogFragment，並實作OnClickListener監聽器
//        private final static String TAG = "DealDialogFragment";
//        private ImageView ivGoodsImage;
//        private TextView tvAccount;
//        private TextView tvGoodsName;
//        private TextView tvPostTime;
//        private TextView tvQty;
//        private TextView tvLoc;
//        private TextView tvWay;
//        private TextView tvGoodsNote;
//        private TextView tvAccountNote;
//        private Button btAccept;
//        private Button btRefuse;
//        private Button btCancel;
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View view = inflater.inflate(R.layout.detail_info_fragment, container);
//
//            ivGoodsImage = (ImageView) view.findViewById(R.id.ivGoodsImage);
//            tvAccount = (TextView) view.findViewById(R.id.tvAccount);
//            tvGoodsName = (TextView) view.findViewById(R.id.tvGoodsName);
//            tvPostTime = (TextView) view.findViewById(R.id.tvPostTime);
//            tvQty = (TextView) view.findViewById(R.id.tvQty);
//            tvLoc = (TextView) view.findViewById(R.id.tvLoc);
//            tvWay = (TextView) view.findViewById(R.id.tvWay);
//            tvGoodsNote = (TextView) view.findViewById(R.id.tvGoodsNote);
//            tvAccountNote = (TextView) view.findViewById(R.id.tvAccountNote);
//            btAccept = (Button) view.findViewById(R.id.btAccept);
//            btCancel = (Button) view.findViewById(R.id.btCancel);
//            btRefuse = (Button) view.findViewById(R.id.btRefuse);
//
//
//            if (user.equalsIgnoreCase(dd.getSourceId())) {
//                tvAccount.setText("帳號：" + dd.getEndId());
//            } else if (user.equalsIgnoreCase(dd.getEndId())) {
//                tvAccount.setText("帳號：" + dd.getSourceId());
//            }
//
//            tvGoodsName.setText(dd.getGoodsName());
//            tvPostTime.setText("交易時間：" + dd.getPostDate());
//            tvQty.setText("數量：" + dd.getDealQty());
//
//            List<Local> local = null;
//            String action = "getLocal";
//            String loc = "" + dd.getGoodsLoc();
//            if (Common.networkConnected(getActivity())) {//傳送到server端
//                try {
//                    local = new homeGetLocalTask().execute(action, loc).get();
//                } catch (Exception e) {
//                    Log.e("Local", e.toString());
//                }
//                if (local == null) {
//                    Common.showToast(getActivity(), "錯誤");
//                } else {
//                    tvLoc.setText("地點：" + local.get(0).getLocalName());
//                }
//            } else {
//                Common.showToast(getActivity(), R.string.msg_NoNetwork);
//            }
//
//            if (dd.getEndShipWay() == 0) {
//                tvWay.setText("面交");
//            } else if (dd.getEndShipWay() == 1) {
//                tvWay.setText("物流");
//            }
//
//            tvGoodsNote.setText(dd.getGoodsNote());
//            tvAccountNote.setText(dd.getDealNote());
//
//
//            btAccept.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    acceptDeal();
//                }
//            });
//
//            btCancel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dismiss();
//                }
//            });
//
//            btRefuse.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    RefuseDeal();
//                }
//            });
//
//            return view;
//        }
//
//
//        private void acceptDeal() {
//            if (Common.networkConnected(getActivity())) {
//                String ACTION = "acceptDeal";
//                int count = 0;
//                try {
//                    count = new UpdateDealTask().execute(ACTION, dd.getDealNo()).get();
//                } catch (Exception e) {
//                    Log.e(TAG, e.toString());
//                }
//                if (count == 0) {
//                    Common.showToast(getActivity(), R.string.msg_dealFail);
//                } else {
//                    Common.showToast(getActivity(), R.string.msg_dealSuccess);
//
//                    Intent updateIntent = new Intent();
//                    updateIntent.setClass(getActivity(), dealDetailActivity.class);
//                    startActivity(updateIntent);
//
//                    dismiss();
//                }
//            } else {
//                Common.showToast(getActivity(), R.string.msg_NoNetwork);
//            }
//        }
//
//        @Override
//        public void onDismiss(DialogInterface frag) {
////            (dealDetailActivity)getActivity().showAllDeals();
////            getFragmentManager().onStart();
//
//            //FragmentManager fm = getActivity().getSupportFragmentManager();
//            showAllDeals();
//            //d.show(fm, "fragment_name");
//
//            super.onDismiss(frag);
//
//        }
//
//
//        private void RefuseDeal() {
//            if (Common.networkConnected(getActivity())) {
//                String ACTION = "refuseDeal";
//                int count = 0;
//                try {
//                    count = new UpdateDealTask().execute(ACTION, dd.getDealNo()).get();
//                } catch (Exception e) {
//                    Log.e(TAG, e.toString());
//                }
//                if (count == 0) {
//                    Common.showToast(getActivity(), R.string.msg_dealFail);
//                } else {
//                    Common.showToast(getActivity(), R.string.msg_dealRefuse);
//                    dismiss();
//                }
//            } else {
//                Common.showToast(getActivity(), R.string.msg_NoNetwork);
//            }
//        }
//    }
}