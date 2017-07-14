package com.example.ntut.weshare.homeGoodsDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.R;
import com.example.ntut.weshare.goods.Goods;

import java.util.List;


public class changeFragment extends Fragment {

    private static final String TAG = "ChangeListFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvWish;
    private int userType = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_wish_fragment, container, false);

        rvWish = (RecyclerView) view.findViewById(R.id.rvWish);
        rvWish.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        swipeRefreshLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {//監聽有沒有發生下拉，有執行onRefresh()
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);//播放動畫
                showAllMsgs();//更新
                swipeRefreshLayout.setRefreshing(false);//停止動畫
            }
        });

//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));//這里用線性宮格顯示 類似于瀑布流
//        mRecyclerView.setAdapter(new NormalRecyclerViewAdapter(this)); 原文網址：https://ifun01.com/88LDTF8.html
        return view;
    }


    private void showAllMsgs() {
        if (Common.networkConnected(getActivity())) {//檢查網路
            String url = Common.URL + "GoodsServlet";
            String action = "getHome";
            List<Goods> wishGoods = null;
            try {//抓全部景點
                wishGoods = new homeGetAllTask().execute(url, action, 3).get();//.get()要請SpotGetAllTask()的執行結果回傳給我，會等他抓完資料(doInBackground的回傳結果)才會往下執行
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (wishGoods == null || wishGoods.isEmpty()) {
                Common.showToast(getActivity(), R.string.msg_NoMsgsFound);
            } else {
                //Common.showToast(getActivity(), R.string.msg_NoMsgsFound);

                rvWish.setAdapter(new ChangeRecyclerViewAdapter(getActivity(), wishGoods));//畫面RecyclerView(畫面,資料)，getActivity()取的他所依附的頁面(主頁面)
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        showAllMsgs();
    }

    private class ChangeRecyclerViewAdapter extends RecyclerView.Adapter<ChangeRecyclerViewAdapter.MyViewHolder> {//CH05 RecyclerView
        private LayoutInflater layoutInflater;
        private Context context;
        private List<Goods> wishGoods;


        public ChangeRecyclerViewAdapter(Context context, List<Goods> wishGoods) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
            this.wishGoods = wishGoods;
        }

        @Override
        public int getItemCount() {
            return wishGoods.size();
        }

        @Override
        public ChangeRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);//建立View
            View itemView = layoutInflater.inflate(R.layout.home_change_item, parent, false);
            return new ChangeRecyclerViewAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ChangeRecyclerViewAdapter.MyViewHolder myViewHolder, int position) {//將圖文顯示出來
            final Goods wishGood = wishGoods.get(position);//文字資料

            String url = Common.URL + "GoodsServlet";
            int gid = wishGood.getGoodsNo();
            int imageSize = 450;
            //這邊啟動AsyncTask，抓圖片
            //不用.get()，不然會卡畫面，這邊利用SpotGetImageTask(myViewHolder.imageView)放圖，myViewHolder.imageView將imageView元件傳給AsyncTask，再用onPostExecute()將圖貼上
            new GoodsGetImageTask(myViewHolder.ivGoods).execute(url, gid, imageSize);

            try {
                userType = new GetIndTypelTask().execute(wishGood.getIndId()).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (userType == 1) {
                myViewHolder.ivIndType.setImageResource(R.drawable.member_icon);
            } else if (userType == 2) {
                myViewHolder.ivIndType.setImageResource(R.drawable.org_icon2);
            }

            myViewHolder.tvWish.setText(wishGood.getGoodsName());
            myViewHolder.tvNumber.setText("" + wishGood.getQty());

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), HomeGoodsDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Goods", wishGood);
                    intent.putExtra("intentGoods", bundle);
                    startActivity(intent);
                }
            });
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivGoods, ivIndType;
            TextView tvWish, tvNumber;

            public MyViewHolder(View itemView) {
                super(itemView);
                ivGoods = (ImageView) itemView.findViewById(R.id.ivGoods);
                ivIndType = (ImageView) itemView.findViewById(R.id.ivIndType);
                tvWish = (TextView) itemView.findViewById(R.id.tvWish);
                tvNumber = (TextView) itemView.findViewById(R.id.tvNumber);
            }
        }
    }
}