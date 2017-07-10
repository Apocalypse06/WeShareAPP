package com.example.ntut.weshare.icon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ntut.weshare.Common;

import com.example.ntut.weshare.R;
import com.example.ntut.weshare.goods.Goods;
import com.example.ntut.weshare.goods.GoodsGetImageTask;
import com.example.ntut.weshare.goods.GoodsInfoActivity;
import com.example.ntut.weshare.homeGoodsDetail.HomeGoodsDetailActivity;

import java.text.SimpleDateFormat;
import java.util.List;


public class SearchFragment extends Fragment {
    private static final String TAG ="SearchFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    EditText search;
    ImageView ivSearch;
    Spinner spClass;
    Spinner spLoc;
    CheckBox cbWish;
    CheckBox cbGive;
    CheckBox cbChage;
    String all="";
    String finalQuery;
    private RecyclerView rvSrcGoods;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        search = (EditText) view.findViewById(R.id.et_search);
        ivSearch = (ImageView) view.findViewById(R.id.iv_searchBt);
        spClass = (Spinner) view.findViewById(R.id.sp_srcClass);
        spLoc = (Spinner) view.findViewById(R.id.sp_srcLoc);
        cbWish = (CheckBox) view.findViewById(R.id.cb_srcWish);
        cbGive = (CheckBox) view.findViewById(R.id.cb_srcGive);
        cbChage = (CheckBox) view.findViewById(R.id.cb_srcChage);
        search.setText("");

//下拉式更新
        swipeRefreshLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.search_swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                showAllGoods();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        rvSrcGoods = (RecyclerView) view.findViewById(R.id.rvSrcGoods);
        rvSrcGoods.setLayoutManager(new LinearLayoutManager(getActivity()));

//建立spinner選項
        final String[] classes = {"所有類別", "食品", "服飾配件", "生活用品", "家電機器", "其他"};
        ArrayAdapter<String> classList = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, classes);
        spClass.setAdapter(classList);

        final String[] loc = {"所有地區", "苗栗縣", "桃園市", "基隆市", "新北市", "新竹市", "新竹縣", "臺北市", "南投縣", "雲林縣", "嘉義市", "嘉義縣", "彰化縣"
                , "臺中市", "屏東縣", "高雄市", "臺南市", "宜蘭縣", "花蓮縣", "臺東縣", "金門縣", "連江縣", "澎湖縣"};
        ArrayAdapter<String> locList = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, loc);
        spLoc.setAdapter(locList);

//submit按鈕點擊事件(放大鏡)
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//判斷物品名稱
                if(!search.getText().toString().equals("")){
                all=all+"g.goodsName LIKE '%"+search.getText().toString()+"%' AND ";
                }

//判斷所在地選項
                if (spLoc.getSelectedItem().toString().equals(loc[0])) {
                } else {
                    for (int n = 1; n < loc.length; n++) {
                        if (spLoc.getSelectedItem().toString().equals(loc[n])) {
                            all =all+"g.goodsLoc=" + n + " AND ";
                            break;
                        }
                    }
                }

//判斷類別選項
                if (spClass.getSelectedItem().toString().equals(classes[0])) {
                } else {
                    for (int n = 1; n < classes.length; n++) {
                        if (spClass.getSelectedItem().toString().equals(classes[n])) {
                            all = all+"g.goodsType=" + n + " AND ";
                            break;
                        }
                    }
                }
 //判斷分類checkbox
                if (cbWish.isChecked() | cbGive.isChecked() | cbChage.isChecked()){
                    all=all+"(";
                }
                if (cbWish.isChecked()) {
                    all = all + "g.goodsStatus=1 OR ";
                }
                if (cbGive.isChecked()) {
                    all = all + "g.goodsStatus=2 OR ";
                }
                if (cbChage.isChecked()) {
                    all = all + "g.goodsStatus=3 OR ";
                }
                if(!all.equals("")){
                if (!cbWish.isChecked() & !cbGive.isChecked() & !cbChage.isChecked()) {
                    String s = all.substring(0, all.length() - 5);
                    all = s;
                } else {
                    String s = all.substring(0, all.length() - 4);
                    s=s+")";
                    all = s;
                }}

                Toast.makeText(view.getContext(), all,
                        Toast.LENGTH_SHORT).show();
                if(!all.equals("")){
                finalQuery="WHERE "+all;}
                all="";
//最後結果為finalQuery內的字串，將ALL清空以便進行新查詢
                showAllGoods();
            }
        });
        return view;
    }
    private void showAllGoods() {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "GoodsQueryServlet";
            String ACTION="getGoodsQuery";
            String queryString=finalQuery;
            List<Goods> goods = null;
            try {
                goods = new GoodsQueryTask().execute(url, queryString, ACTION).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (goods == null || goods.isEmpty()) {
                // Common.showToast(getActivity(), R.string.msg_NoGoodsFound);
            } else {
                rvSrcGoods.setAdapter(new GoodsRecyclerViewAdapter(getActivity(), goods));
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
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
            View itemView = layoutInflater.inflate(R.layout.goods_recycleview_wish1, parent, false);
            return new SearchFragment.GoodsRecyclerViewAdapter.MyViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(GoodsRecyclerViewAdapter.MyViewHolder myViewHolder, int position) {
            final Goods good = goods.get(position);
            String url = Common.URL + "GoodsServlet";
            int gid = good.getGoodsNo();
            int imageSize = 250;
            new GoodsGetImageTask(myViewHolder.imageView).execute(url, gid, imageSize);
            myViewHolder.tvGoodsTitle.setText(good.getGoodsName());
//        myViewHolder.tvGoodsClass.setText("類型：" + changeType2String(good.getGoodsType()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            final String exdate = sdf.format(good.getDeadLine());
            myViewHolder.tvNeedTime.setText("到期日：" + exdate);
            myViewHolder.tvNeedNum.setText("數量：" + good.getQty());

            if(good.getGoodsStatus()==1){
                myViewHolder.background.setBackgroundColor(Color.rgb(255,151,151));
            }else if (good.getGoodsStatus()==2){
                myViewHolder.background.setBackgroundColor(Color.rgb(239,123,0));
            }else if(good.getGoodsStatus()==3){
                myViewHolder.background.setBackgroundColor(Color.rgb(1,180,104));
            }

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), HomeGoodsDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Goods", good);
                    intent.putExtra("intentGoods", bundle);
                    startActivity(intent);

//                    Intent intent = new Intent();
//                    intent.setClass(getActivity(),HomeGoodsDetailActivity.class);
//                    Bundle bundle = new Bundle();
//
//                    bundle.putSerializable("goods",good);
//                    intent.putExtra("intentGoods",bundle);
//                    startActivity(intent);


                }
            });
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView,ivMenu;
            TextView tvGoodsTitle, tvNeedTime, tvNeedNum;
            LinearLayout background;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_image);
            tvGoodsTitle = (TextView) itemView.findViewById(R.id.tv_goodsTitle);
            tvNeedTime = (TextView) itemView.findViewById(R.id.tv_needTime);
            tvNeedNum = (TextView) itemView.findViewById(R.id.tv_needNum);
            ivMenu=(ImageView)itemView.findViewById(R.id.icon_menu);
            background=(LinearLayout)itemView.findViewById(R.id.lnwish);
        }
    }
}
}
