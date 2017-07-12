package com.example.ntut.weshare.member;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.style.UpdateLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.R;

import java.util.List;

public class InstitutionSearchFragment extends Fragment {
    private static final String TAG = "InstSearchFragment";
    private TextView search;
    private ImageView ivSearch;
    private Spinner spType;
    private Spinner spLoc;
    private String all = "";
    private String all1 = "";
    private String finalQuery = "";
    private String finalInstQury = "";
    private RecyclerView rv_srcIstt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.institution_fragment, container, false);
        search = (TextView) view.findViewById(R.id.searchorg);
        ivSearch = (ImageView) view.findViewById(R.id.iv_searchorg);
        spType = (Spinner) view.findViewById(R.id.sp_orgType);
        spLoc = (Spinner) view.findViewById(R.id.sp_orgLoc);
        search.setText("");
        rv_srcIstt = (RecyclerView) view.findViewById(R.id.rv_SrcOrg);
        rv_srcIstt.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        final String[] type = {"社福種類", "兒少福利", "偏鄉教育", "老人福利", "身障福利", "其他"};
        ArrayAdapter<String> classList = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, type);
        spType.setAdapter(classList);

        final String[] loc = {"所有地區", "苗栗縣", "桃園市", "基隆市", "新北市", "新竹市", "新竹縣", "臺北市", "南投縣", "雲林縣", "嘉義市", "嘉義縣", "彰化縣"
                , "臺中市", "屏東縣", "高雄市", "臺南市", "宜蘭縣", "花蓮縣", "臺東縣", "金門縣", "連江縣", "澎湖縣"};
        ArrayAdapter<String> locList = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, loc);
        spLoc.setAdapter(locList);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!search.getText().toString().equals("")) {
                    all = all + "m.name LIKE '%" + search.getText().toString() + "%' AND ";
                }
                if (spLoc.getSelectedItem().toString().equals(loc[0])) {
                } else {
                    for (int n = 1; n < loc.length; n++) {
                        if (spLoc.getSelectedItem().toString().equals(loc[n])) {
                            all = all + "m.address LIKE '%" + loc[n] + "%' AND ";
                            break;
                        }
                    }
                }
                if (!all.equals("")) {
                    String s = all.substring(0, all.length() - 5);
                    all = s;
                }

                if (spType.getSelectedItem().toString().equals(type[0])) {
                } else {
                    for (int n = 1; n < type.length; n++) {
                        if (spType.getSelectedItem().toString().equals(type[n])) {
                            all1 = all1 + "i.orgType=" + n;
                            break;
                        }
                    }
                }
                if (!all.equals("")) {
                    finalQuery = "WHERE m.idType=2 AND " + all;
                } else {
                    finalQuery = "WHERE m.idType=2";
                }
                all = "";
                finalInstQury = "WHERE " + all1;
                if (!all1.equals("")) {
                    finalInstQury = finalInstQury + " AND ";
                }
                Toast.makeText(view.getContext(), finalQuery,
                        Toast.LENGTH_SHORT).show();
                all1 = "";

                showAll();
            }
        });
        return view;
    }

    private void showAll() {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "InstQueryServlet";
            String ACTION = "getMemQuery";
            String ACTION2 = "getInstQuery";//==================
            String queryString = finalQuery;
            String query4Type = finalInstQury;//===================
            String qquery="";
            List<User> users = null;
            List<InstiutionBean> instiutionBeens; //===================
            try {
                users = new InstQueryUserTask().execute(url, queryString, ACTION).get();

                int usize = users.size();
                if (usize != 0) {
                    query4Type=query4Type+"(";
                    for (int n = 0; n < usize; n++) {
                        query4Type = query4Type + "i.indId = '" + users.get(n).getUserId() + "' OR ";
                    }
                    String s = query4Type.substring(0, query4Type.length() - 4);
                    query4Type = s;
                    query4Type=query4Type+")";
                    Toast.makeText(getContext(), query4Type,
                            Toast.LENGTH_SHORT).show();
                }

                instiutionBeens = new InstitutionQueryTask().execute(url, query4Type, ACTION2).get();//================

                qquery="WHERE (";
               int isize= instiutionBeens.size();
                if(isize!=0){
                for(int n =0;n<isize;n++){
                    qquery=qquery+"m.userId='"+instiutionBeens.get(n).getIndId()+"' OR ";
                }
                    String s = qquery.substring(0, qquery.length() - 4);
                    qquery=s;
                    qquery=qquery+")";
                    users = new InstQueryUserTask().execute(url, qquery, ACTION).get();
                }else{
//                    users=null;
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (users == null || users.isEmpty()) {
                Common.showToast(getActivity(), R.string.msg_NoInstFound);

            } else {
                Common.showToast(getActivity(),"111111" );
                rv_srcIstt.setAdapter(new InstRecyclerViewAdapter(getActivity(), users));
                Common.showToast(getActivity(),"5555" );
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
    }


    private class InstRecyclerViewAdapter extends RecyclerView.Adapter<InstRecyclerViewAdapter.MyViewHolder> {

        private LayoutInflater layoutInflater;
        private List<User> user;
//        private Context mContext;


        public InstRecyclerViewAdapter(Context context, List<User> users) {
            layoutInflater = LayoutInflater.from(context);
            this.user = users;
//            this.mContext=context;



        }

        @Override
        public int getItemCount() {
            return user.size();
        }

//        public LayoutInflater getInflater(){
//            return LayoutInflater.from(mContext);
//        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            if(viewType==-1){
//                View v=getInflater().inflate(R.layout.institution_fragment_cardview,parent,false);
//                return new MyEmptyViewHolder(v);
//            }
            View itemView = layoutInflater.inflate(R.layout.institution_fragment_cardview, parent, false);
            return new InstRecyclerViewAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(InstRecyclerViewAdapter.MyViewHolder myViewHolder, int position) {
            final User user = this.user.get(position);

            String url = Common.URL + "UserServlet";
            String uid = user.getUserId();
            int imageSize = 250;
            Common.showToast(getActivity(),"526656516516516516" );
            myViewHolder.tvIsttCardTitle.setText(user.getName());
            myViewHolder.tvIsttCardPhone.setText(user.getTal());

             new instGetImageTask(myViewHolder.imageView).execute(url, uid, imageSize);


//            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent();
//                    intent.setClass(getActivity(), HomeGoodsDetailActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("Goods", good);
//                    intent.putExtra("intentGoods", bundle);
//                    startActivity(intent);
//
//                    Intent intent = new Intent();
//                    intent.setClass(getActivity(),HomeGoodsDetailActivity.class);
//                    Bundle bundle = new Bundle();
//
//                    bundle.putSerializable("goods",good);
//                    intent.putExtra("intentGoods",bundle);
//                    startActivity(intent);
//
//
//                }
//            });
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView tvIsttCardTitle, tvIsttCardPhone;

            public MyViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.iv_istt_card);
                tvIsttCardTitle = (TextView) itemView.findViewById(R.id.tv_istt_cardName);
                tvIsttCardPhone = (TextView) itemView.findViewById(R.id.tv_istt_cardphone);

            }
        }
//        class MyEmptyViewHolder extends RecyclerView.ViewHolder{
//
//            public MyEmptyViewHolder(View itemView) {
//                super(itemView);
//            }
//        }
//
   }


}
