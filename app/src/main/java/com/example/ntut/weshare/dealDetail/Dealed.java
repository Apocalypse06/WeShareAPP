package com.example.ntut.weshare.dealDetail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.example.ntut.weshare.homeGoodsDetail.FeedbackBean;

import java.text.SimpleDateFormat;
import java.util.List;


public class Dealed extends Fragment {
    private static final String TAG = "NotDealFragment";
    private static SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvNotDeal;
    private ImageView ivNoDeal;
    private static String user;
    private int status = 0;
    static DealBean dealStatic = null;
    static FeedbackBean fbStatic = null;

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
        ivNoDeal = (ImageView) view.findViewById(R.id.ivNoDeal);
        ivNoDeal.setVisibility(View.GONE);
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
            String ACTION = "getDealed";
            List<DealBean> deals = null;
            try {
                deals = new GetDealTask().execute(ACTION, user).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (deals == null || deals.isEmpty()) {
//                ivNoDeal.setVisibility(View.VISIBLE);
//                swipeRefreshLayout.setVisibility(View.GONE);
//                Common.showToast(getActivity(), "沒有完成的交易訂單");
                ivNoDeal.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);
                ivNoDeal.setBackgroundResource(R.drawable.not_dealed_page);
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
            View itemView = layoutInflater.inflate(R.layout.dealed_recycleview_item, parent, false);
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

            List<FeedbackBean> fb = null;
            String action = "checkFbExist";
            int count = 0;
            if (Common.networkConnected(getActivity())) {
                try {
                    count = new FbCheckTask().execute(action, "" + deal.getDealNo()).get();
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                myViewHolder.ivFbNot.setVisibility(View.GONE);
                myViewHolder.ivFbed.setVisibility(View.GONE);
                myViewHolder.ivRmNot.setVisibility(View.GONE);
                myViewHolder.ivRmed.setVisibility(View.GONE);
                if (count == 1) {
                    if (user.equalsIgnoreCase(deal.getEndId())) {
                        myViewHolder.ivFbed.setVisibility(View.VISIBLE);
                        myViewHolder.ivFbed.setImageResource(R.drawable.feedbacked_icon);
                    } else {
                        myViewHolder.ivRmed.setVisibility(View.VISIBLE);
                        myViewHolder.ivRmed.setImageResource(R.drawable.recommend_icon);
                    }
                } else {
                    if (user.equalsIgnoreCase(deal.getEndId())) {
                        myViewHolder.ivFbNot.setVisibility(View.VISIBLE);
                        myViewHolder.ivFbNot.setImageResource(R.drawable.feedback_not_icon);
                    } else {
                        myViewHolder.ivRmNot.setVisibility(View.VISIBLE);
                        myViewHolder.ivRmNot.setImageResource(R.drawable.recommend_not_icon);
                    }
                }
            } else {
                Common.showToast(getActivity(), R.string.msg_NoNetwork);
            }


            myViewHolder.tvDealQty.setText("數量：" + deal.getDealQty());
            if (user.equalsIgnoreCase(deal.getSourceId())) {
                myViewHolder.tvDealUser.setText("帳號：" + deal.getEndId());
            } else if (user.equalsIgnoreCase(deal.getEndId())) {
                myViewHolder.tvDealUser.setText("帳號：" + deal.getSourceId());
            }

            if (deal.getEndShipWay() == 0) {
                myViewHolder.ivCar.setVisibility(View.GONE);
                myViewHolder.ivMap.setImageResource(R.drawable.map_icon);
            } else {
                myViewHolder.ivMap.setVisibility(View.GONE);
                myViewHolder.ivCar.setImageResource(R.drawable.car_icon);
            }

            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            String postTime = sdFormat.format(deal.getShipDate());
            myViewHolder.tvDealTime.setText("完成交易：" + postTime);

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dealStatic = deal;
//                    NotDeal.AlertDialogFragment alertFragment = new NotDeal.AlertDialogFragment();//建立物件
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    alertFragment.show(fragmentManager, "alert");//顯示警示框

                    DealedDialogFragment detail = new DealedDialogFragment();
                    detail.setRef(Dealed.this);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    detail.show(fragmentManager, "alert");//顯示警示框
                }
            });


            myViewHolder.ivFbNot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dealStatic = deal;
                    FeedbackDialogFragment msg = new FeedbackDialogFragment();
                    msg.setRef(Dealed.this);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    msg.show(fragmentManager, "alert");//顯示警示框
                }
            });

            myViewHolder.ivFbed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dealStatic = deal;
                    FeedbackedDialogFragment msg = new FeedbackedDialogFragment();
                    msg.setRef(Dealed.this);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    msg.show(fragmentManager, "alert");//顯示警示框
                }
            });

            myViewHolder.ivRmNot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dealStatic = deal;
                    MsgRecommendDialogFragment msg = new MsgRecommendDialogFragment();
                    msg.setRef(Dealed.this);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    msg.show(fragmentManager, "alert");//顯示警示框
                }
            });

            myViewHolder.ivRmed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dealStatic = deal;
                    FeedbackedDialogFragment msg = new FeedbackedDialogFragment();
                    msg.setRef(Dealed.this);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    msg.show(fragmentManager, "alert");//顯示警示框
                }
            });

            myViewHolder.ivCar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dealStatic = deal;
                    CarDealedDialogFragment msg = new CarDealedDialogFragment();
                    msg.setRef(Dealed.this);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    msg.show(fragmentManager, "alert");
                }
            });

            myViewHolder.ivMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), MapActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("deal", deal);
                    intent.putExtra("intentdDeal", bundle);
                    startActivity(intent);
                }
            });

        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivDealImage, ivFbNot, ivFbed, ivRmNot, ivRmed, ivCar, ivMap;
            TextView tvDealGoods, tvDealQty, tvDealUser, tvDealTime;

            public MyViewHolder(View itemView) {
                super(itemView);
                ivDealImage = (ImageView) itemView.findViewById(R.id.ivDealImage);
                ivFbNot = (ImageView) itemView.findViewById(R.id.ivFbNot);
                ivFbed = (ImageView) itemView.findViewById(R.id.ivFbed);
                ivRmNot = (ImageView) itemView.findViewById(R.id.ivRmNot);
                ivRmed = (ImageView) itemView.findViewById(R.id.ivRmed);
                ivCar = (ImageView) itemView.findViewById(R.id.ivCar);
                ivMap = (ImageView) itemView.findViewById(R.id.ivMap);
                tvDealGoods = (TextView) itemView.findViewById(R.id.tvDealGoods);
                tvDealQty = (TextView) itemView.findViewById(R.id.tvDealQty);
                tvDealUser = (TextView) itemView.findViewById(R.id.tvDealUser);
                tvDealTime = (TextView) itemView.findViewById(R.id.tvDealTime);

                ivFbNot.setVisibility(View.GONE);
                ivFbed.setVisibility(View.GONE);
                ivRmNot.setVisibility(View.GONE);
                ivRmed.setVisibility(View.GONE);
            }
        }
    }

}