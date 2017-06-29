package com.example.ntut.weshare.goods;

import android.app.FragmentHostCallback;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.example.ntut.weshare.MainActivity;
import com.example.ntut.weshare.R;

import java.text.SimpleDateFormat;
import java.util.List;


public class GoodsListFragment extends Fragment {
    private static final String TAG = "GoodsListFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvGoods;


    @Override
    public void onResume() {
        super.onResume();
        // 從偏好設定檔中取得登入狀態來決定是否顯示「登出」

        SharedPreferences pref = this.getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
        String user = pref.getString("user","");
        if(user==""){
            Toast.makeText(this.getActivity(), "請註冊登入WeShare後，再過來設定您的物資箱喔~",
                    Toast.LENGTH_SHORT).show();
            getActivity().finish();
            Intent MainIntent = new Intent(getActivity(), MainActivity.class);
            startActivity(MainIntent);
        }else{
            Toast.makeText(this.getActivity(), user,
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.goodsbox_fragment, container, false);

        swipeRefreshLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.gb_swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                showAllGoods();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        rvGoods = (RecyclerView) view.findViewById(R.id.rvGoods);
        rvGoods.setLayoutManager(new LinearLayoutManager(getActivity()));
        FloatingActionButton btAdd = (FloatingActionButton) view.findViewById(R.id.btAdd);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent insertIntent = new Intent(getActivity(), GoodsInsertActivity.class);
                startActivity(insertIntent);
            }
        });
        return view;
    }

    private void showAllGoods() {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "GoodsServlet";
            SharedPreferences pref = this.getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
            String user = pref.getString("user","");
            List<Goods> goods = null;
            try {
                goods = new GoodsGetAllTask().execute(url,user).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (goods == null || goods.isEmpty()) {
               // Common.showToast(getActivity(), R.string.msg_NoGoodsFound);
            } else {
                rvGoods.setAdapter(new GoodsRecyclerViewAdapter(getActivity(), goods));
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
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.goods_recycleview_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, int position) {
            final Goods good = goods.get(position);
            String url = Common.URL + "GoodsServlet";
            int gid = good.getGoodsNo();
            int imageSize = 250;
            new GoodsGetImageTask(myViewHolder.imageView).execute(url, gid, imageSize);

            myViewHolder.tvGoodsTitle.setText(good.getGoodsName());
            myViewHolder.tvGoodsClass.setText("類型：" + good.getGoodsType());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String exdate = sdf.format(good.getDeadLine());
            myViewHolder.tvNeedTime.setText("到期日：" + exdate);
            myViewHolder.tvNeedNum.setText("數量：" + good.getQty());


            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = new GoodsInfoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("goods", good);
                    fragment.setArguments(bundle);
                    switchFragment(fragment);
                }
            });


        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView tvGoodsTitle, tvGoodsClass, tvNeedTime, tvNeedNum;

            public MyViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.iv_image);
                tvGoodsTitle = (TextView) itemView.findViewById(R.id.tv_goodsTitle);
                tvGoodsClass = (TextView) itemView.findViewById(R.id.tv_goodsClass);
                tvNeedTime = (TextView) itemView.findViewById(R.id.tv_needTime);
                tvNeedNum = (TextView) itemView.findViewById(R.id.tv_needNum);

            }
        }
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.body, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}


