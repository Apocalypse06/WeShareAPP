package com.example.ntut.weshare;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ntut.weshare.goods.Goods;
import com.example.ntut.weshare.message.*;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class wishFragment extends Fragment {
    private static final String TAG = "WishListFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvWish;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_wish_fragment, container, false);

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

        rvWish = (RecyclerView) view.findViewById(R.id.rvWish);
        rvWish.setLayoutManager(
                new StaggeredGridLayoutManager(
                        2, StaggeredGridLayoutManager.VERTICAL));

        rvWish.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    private void showAllMsgs() {
        if (Common.networkConnected(getActivity())) {//檢查網路
            String url = Common.URL + "GoodsServlet";
            String action = "getHome";
            List<Goods> wishGoods = null;
            try {//抓全部景點
                wishGoods = new homeGetAllTask().execute(url, action , 1).get();//.get()要請SpotGetAllTask()的執行結果回傳給我，會等他抓完資料(doInBackground的回傳結果)才會往下執行
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (wishGoods == null || wishGoods.isEmpty()) {
                Common.showToast(getActivity(), R.string.msg_NoMsgsFound);
            } else {
                //Common.showToast(getActivity(), R.string.msg_NoMsgsFound);
                rvWish.setAdapter(new WishRecyclerViewAdapter(getActivity(), wishGoods));//畫面RecyclerView(畫面,資料)，getActivity()取的他所依附的頁面(主頁面)
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

    private class WishRecyclerViewAdapter extends RecyclerView.Adapter<WishRecyclerViewAdapter.MyViewHolder> {//CH05 RecyclerView
        private LayoutInflater layoutInflater;
        private List<Goods> wishGoods;


        public WishRecyclerViewAdapter(Context context, List<Goods> wishGoods) {
            layoutInflater = LayoutInflater.from(context);
            this.wishGoods = wishGoods;
        }

        @Override
        public int getItemCount() {
            return wishGoods.size();
        }

        @Override
        public WishRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.wish_item, parent, false);
            return new WishRecyclerViewAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(WishRecyclerViewAdapter.MyViewHolder myViewHolder, int position) {//將圖文顯示出來
            final Goods wishGood = wishGoods.get(position);//文字資料
            int imageSize = 250;//要縮圖的大小像素，要放在250*250的框

            //這邊啟動AsyncTask，抓圖片
            //不用.get()，不然會卡畫面，這邊利用SpotGetImageTask(myViewHolder.imageView)放圖，myViewHolder.imageView將imageView元件傳給AsyncTask，再用onPostExecute()將圖貼上
//            new com.example.ntut.weshare.message.UserGetImageTask(myViewHolder.ivUserPic).execute(url, accountId, imageSize);//.execute(網址, 圖片id, 這邊做縮圖imageSize，在server端縮圖完再傳過來)
//
//            SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
//            String account = pref.getString("user", "");

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent();
//                    intent.setClass(getActivity(), MessageReplyActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("msg", msg);
//                    intent.putExtra("intentMsgs", bundle);
//                    startActivity(intent);
//
//                    Fragment fragment = new MessageReplyFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("msg", msg);
//                    fragment.setArguments(bundle);
//                    switchFragment(fragment);
                }
            });
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivGoods;
            TextView tvWish, tvName;

            public MyViewHolder(View itemView) {
                super(itemView);
                ivGoods = (ImageView) itemView.findViewById(R.id.ivGoods);
                tvWish = (TextView) itemView.findViewById(R.id.tvWish);
                tvName = (TextView) itemView.findViewById(R.id.tvName);
            }
        }
    }



}
