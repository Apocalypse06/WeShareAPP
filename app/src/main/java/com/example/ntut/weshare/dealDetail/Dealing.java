package com.example.ntut.weshare.dealDetail;

import android.content.Context;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Dealing extends Fragment {
    private static final String TAG = "NotDealFragment";
    private static SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvNotDeal;
    private static String user;
    static DealBean dealStatic = null;

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
            String ACTION = "getDealing";
            List<DealBean> deals = null;
            try {
                deals = new GetDealTask().execute(ACTION, user).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (deals == null || deals.isEmpty()) {
                Common.showToast(getActivity(), "沒有正在進行的交易訂單");
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

            if (deal.getSourceId().equalsIgnoreCase(user)) {
                myViewHolder.ivCar.setImageResource(R.drawable.car_icon);
            }else{
                myViewHolder.ivCar.setVisibility(View.GONE);
            }


            myViewHolder.tvDealQty.setText("數量：" + deal.getDealQty());
            if (user.equalsIgnoreCase(deal.getSourceId())) {
                myViewHolder.tvDealUser.setText("帳號：" + deal.getEndId());
            } else if (user.equalsIgnoreCase(deal.getEndId())) {
                myViewHolder.tvDealUser.setText("帳號：" + deal.getSourceId());
            }


            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            String postTime = sdFormat.format(deal.getShipDate());
            myViewHolder.tvDealTime.setText("訂單時間：" + postTime);
//            myViewHolder.tvDealTime.setText("訂單時間：" + deal.getShipDate());


            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dealStatic = deal;
//                    NotDeal.AlertDialogFragment alertFragment = new NotDeal.AlertDialogFragment();//建立物件
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    alertFragment.show(fragmentManager, "alert");//顯示警示框

                    DealingDialogFragment detail = new DealingDialogFragment();
                    detail.setRef(Dealing.this);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    detail.show(fragmentManager, "alert");//顯示警示框
                }
            });


            myViewHolder.ivMail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dealStatic = deal;
                    MsgDealingDialogFragment msg = new MsgDealingDialogFragment();
                    msg.setRef(Dealing.this);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    msg.show(fragmentManager, "alert");//顯示警示框

                }
            });

            myViewHolder.ivCar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dealStatic = deal;
                    SendTextDialogFragment msg = new SendTextDialogFragment();
                    msg.setRef(Dealing.this);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    msg.show(fragmentManager, "alert");//顯示警示框

                }
            });

        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivDealImage, ivMail, ivCar;
            TextView tvDealGoods, tvDealQty, tvDealUser, tvDealTime;

            public MyViewHolder(View itemView) {
                super(itemView);
                ivDealImage = (ImageView) itemView.findViewById(R.id.ivDealImage);
                ivMail = (ImageView) itemView.findViewById(R.id.ivMail);
                ivCar = (ImageView) itemView.findViewById(R.id.ivCar);
                tvDealGoods = (TextView) itemView.findViewById(R.id.tvDealGoods);
                tvDealQty = (TextView) itemView.findViewById(R.id.tvDealQty);
                tvDealUser = (TextView) itemView.findViewById(R.id.tvDealUser);
                tvDealTime = (TextView) itemView.findViewById(R.id.tvDealTime);
            }
        }
    }

}