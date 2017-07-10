package com.example.ntut.weshare.member;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by NTUT on 2017/7/9.
 */

public class InstitutionSearchFragment extends Fragment {
    private static final String TAG = "InstitutionSearchFragment";
    private TextView search;
    private ImageView ivSearch;
    private Spinner spType;
    private Spinner spLoc;
    private String all = "";
    private String finalQuery;
    private RecyclerView rv_srcIstt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.institution_fragment, container, false);
        rv_srcIstt.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        search=(TextView)view.findViewById(R.id.searchorg);
        ivSearch=(ImageView)view.findViewById(R.id.iv_searchorg);
        spType=(Spinner)view.findViewById(R.id.sp_orgType);
        spLoc=(Spinner)view.findViewById(R.id.sp_orgLoc);
        rv_srcIstt=(RecyclerView)view.findViewById(R.id.rv_SrcOrg);
        search.setText("");

        final String[] type = {"社福種類","兒少福利", "偏鄉教育", "老人福利", "身障福利", "其他"};
        ArrayAdapter<String> classList = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, type);
        spType.setAdapter(classList);

        final String[] loc = {"所有地區", "苗栗縣", "桃園市", "基隆市", "新北市", "新竹市", "新竹縣", "臺北市", "南投縣", "雲林縣", "嘉義市", "嘉義縣", "彰化縣"
                , "臺中市", "屏東縣", "高雄市", "臺南市", "宜蘭縣", "花蓮縣", "臺東縣", "金門縣", "連江縣", "澎湖縣"};
        ArrayAdapter<String> locList = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, loc);
        spLoc.setAdapter(locList);

        return view;
    }


    private void showAllItt() {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "InstitutionQueryServlet";
            String ACTION="getInstitutionQuery";
            String queryString=finalQuery;
            List<User> user = null;
            try {
                user = new InstitutionQueryTask().execute(url, queryString, ACTION).get();
            } catch (Exception e) {
            }
            if ( user == null ||  user.isEmpty()) {
                // Common.showToast(getActivity(), R.string.msg_NoGoodsFound);
            } else {
                rv_srcIstt.setAdapter(new InstitutionRecyclerViewAdapter(getActivity(),  user));
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
    }



    private class InstitutionRecyclerViewAdapter extends RecyclerView.Adapter<InstitutionRecyclerViewAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<User>  users;

        public InstitutionRecyclerViewAdapter(Context context, List<User> user) {
            layoutInflater = LayoutInflater.from(context);
            this. users =  user;
        }
        @Override
        public int getItemCount() {
            return users.size();
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.institution_fragment_cardview, parent, false);
            return new InstitutionSearchFragment.InstitutionRecyclerViewAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(InstitutionRecyclerViewAdapter.MyViewHolder myViewHolder, int position) {
//            final User user = users.get(position);
//            String url = Common.URL + "GoodsServlet";
//            String uid = user.getUserId();
//            int imageSize = 250;
//            new UserGetOrgImageTask(myViewHolder.imageView).execute(url, uid, imageSize);
//            myViewHolder.tvIsttCardTitle.setText(user.getName());
//            myViewHolder.tvIsttCardPhone.setText(user.getTal());


            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), HomeGoodsDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("Goods", good);
//                intent.putExtra("intentGoods", bundle);
//                startActivity(intent);

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
            ImageView imageView;
            TextView tvIsttCardTitle, tvIsttCardPhone;


            public MyViewHolder(View itemView) {
                super(itemView);
                imageView = (CircleImageView) itemView.findViewById(R.id.iv_istt_card);
                tvIsttCardTitle = (TextView) itemView.findViewById(R.id.tv_istt_cardName);
                tvIsttCardPhone = (TextView) itemView.findViewById(R.id.tv_istt_cardphone);

            }
        }
    }
}