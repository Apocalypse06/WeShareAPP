package com.example.ntut.weshare.member;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.R;
import com.example.ntut.weshare.goods.Goods;
import com.example.ntut.weshare.goods.GoodsGetAllTask;
import com.example.ntut.weshare.goods.GoodsGetImageTask;
import com.example.ntut.weshare.homeGoodsDetail.HomeGoodsDetailActivity;
import java.util.List;

public class instGetGoodsFragment extends Fragment {
    private static final String TAG ="instGetGoodsFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvInstNeed;
    private Button btback;
    String user;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inst_showgoodsneed, container, false);
        rvInstNeed = (RecyclerView) view.findViewById(R.id.rvInstNeed);
        rvInstNeed.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        btback=(Button)view.findViewById(R.id.bt_BackInstInfo);
        swipeRefreshLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        user=this.getArguments().getString("user");
        Log.e("",user);

        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               FragmentManager manager =getFragmentManager();
              FragmentTransaction transaction=manager.beginTransaction();
                Fragment fragment=manager.findFragmentByTag(TAG);
                transaction.remove(fragment);
                transaction.commit();

            }
        });


        return view;
    }
    private void showAllGoods() {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "GoodsServlet";
            String user = this.user;
            String ACTION1 = "getAll";
            List<Goods> goods = null;
            try {
                goods = new GoodsGetAllTask().execute(url, user, ACTION1).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (goods == null || goods.isEmpty()) {
                // Common.showToast(getActivity(), R.string.msg_NoGoodsFound);
            } else {
                rvInstNeed.setAdapter(new instGetGoodsFragment.GoodsRecyclerViewAdapter(getActivity(), goods));

            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        showAllGoods();
    }
    private class GoodsRecyclerViewAdapter extends RecyclerView.Adapter<GoodsRecyclerViewAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Goods> goods;

        public GoodsRecyclerViewAdapter(Context context, List<Goods> goods) {
            layoutInflater = LayoutInflater.from(context);
            this.goods = goods;
        }

        @Override
        public int getItemCount() {
            return goods.size();
        }

        @Override
        public GoodsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.home_wish_item, parent, false);
            return new GoodsRecyclerViewAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(GoodsRecyclerViewAdapter.MyViewHolder myViewHolder, int position) {
            final Goods good = goods.get(position);
            String url = Common.URL + "GoodsServlet";
            int gid = good.getGoodsNo();
            int imageSize = 250;
            new GoodsGetImageTask(myViewHolder.imageView).execute(url, gid, imageSize);

            myViewHolder.tvGoodsTitle.setText(good.getGoodsName());
           myViewHolder.tvNeedNum.setText(""+good.getQty());
            //myViewHolder.background.setBackgroundColor(Color.rgb(239, 123, 0));
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), HomeGoodsDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Goods", good);
                    intent.putExtra("intentGoods", bundle);
                    startActivity(intent);
                }
            });
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView tvGoodsTitle, tvNeedNum;
           // LinearLayout background;

            public MyViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.ivGoods);
                tvGoodsTitle = (TextView) itemView.findViewById(R.id.tvWish);
                tvNeedNum = (TextView) itemView.findViewById(R.id.tvNumber);
//                background=(LinearLayout) itemView.findViewById(R.id.cardview);

            }
        }
    }
}
