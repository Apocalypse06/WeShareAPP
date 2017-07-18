package com.example.ntut.weshare.homeGoodsDetail;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.R;
import com.example.ntut.weshare.goods.Goods;
import com.example.ntut.weshare.message.MessageBean;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class GoodsFeedbackFragment extends Fragment {
    private static final String TAG = "MsgListFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvMsgs;
    private ImageView ivNoFeedback;
    private static Goods good;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fb_fragment, container, false);

        Bundle bundle = getActivity().getIntent().getBundleExtra("intentGoods");
        good = (Goods) bundle.getSerializable("Goods");

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

        rvMsgs = (RecyclerView) view.findViewById(R.id.rvMsgs);
        rvMsgs.setLayoutManager(new LinearLayoutManager(getActivity()));
        ivNoFeedback = (ImageView) view.findViewById(R.id.ivNoFeedback);
        ivNoFeedback.setVisibility(View.GONE);
        return view;
    }

    private void showAllMsgs() {
        if (Common.networkConnected(getActivity())) {//檢查網路
            List<FeedbackBean> fbs = null;
            try {
                fbs = new FbGetAllTask().execute(good.getIndId()).get();//.get()要請SpotGetAllTask()的執行結果回傳給我，會等他抓完資料(doInBackground的回傳結果)才會往下執行
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (fbs == null || fbs.isEmpty()) {
//                Common.showToast(getActivity(), "此人尚未有評價");
                ivNoFeedback.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);
            } else {
                //Common.showToast(getActivity(), R.string.msg_NoMsgsFound);
                rvMsgs.setAdapter(new FbsRecyclerViewAdapter(getActivity(), fbs));//畫面RecyclerView(畫面,資料)，getActivity()取的他所依附的頁面(主頁面)
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

    private class FbsRecyclerViewAdapter extends RecyclerView.Adapter<FbsRecyclerViewAdapter.MyViewHolder> {//CH05 RecyclerView
        private LayoutInflater layoutInflater;
        private List<FeedbackBean> fbs;


        public FbsRecyclerViewAdapter(Context context, List<FeedbackBean> fbs) {
            layoutInflater = LayoutInflater.from(context);
            this.fbs = fbs;
        }

        @Override
        public int getItemCount() {
            return fbs.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.home_fb_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, int position) {//將圖文顯示出來
            final FeedbackBean fb = fbs.get(position);//文字資料

            myViewHolder.tvAccount.setText("帳號：" + fb.getFbSourceId());
            myViewHolder.tvFb.setText("評價：" + fb.getFbText());
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvAccount, tvFb;

            public MyViewHolder(View itemView) {
                super(itemView);
                tvAccount = (TextView) itemView.findViewById(R.id.tvAccount);
                tvFb = (TextView) itemView.findViewById(R.id.tvFb);

            }
        }
    }


}
