package com.example.ntut.weshare;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ntut.weshare.R;
import com.example.ntut.weshare.goods.Goods;
import com.example.ntut.weshare.goods.GoodsBoxPageGive;
import com.example.ntut.weshare.goods.GoodsBoxPageWish;
import com.example.ntut.weshare.member.*;
import com.example.ntut.weshare.message.MessageReplyFragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class HomeGoodsDetailActivity extends AppCompatActivity {
    private static final String TAG = "HomeGoodsActivity";
    private List<Fragment> fragmentList;
    private ViewPager vpager;
    private TabLayout gTablayout;
    private ImageView ivGoods;
    private Button btEnter;

    private Goods good;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_detail);

        Bundle bundle = this.getIntent().getBundleExtra("intentGoods");
        good = (Goods) bundle.getSerializable("Goods");

        //sendGoodsDetail();

        initData();
        initView();
        showGoods();
    }

    private void sendGoodsDetail() {
        Fragment fragment = new GoodsDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Goods", good);
        fragment.setArguments(bundle);

//      switchFragment(fragment);


    }


    private void showGoods() {
        if (Common.networkConnected(this)) {//檢查網路
            String url = Common.URL + "GoodsServlet";
            int gid = good.getGoodsNo();
            int imageSize = 800;
            //Bitmap bitmap = null;
            try {

                //這邊啟動AsyncTask，抓圖片
                //不用.get()，不然會卡畫面，這邊利用SpotGetImageTask(myViewHolder.imageView)放圖，myViewHolder.imageView將imageView元件傳給AsyncTask，再用onPostExecute()將圖貼上
                new GoodsGetImageTask(ivGoods).execute(url, gid, imageSize);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
//            if (imageSize == 500) {
//                Common.showToast(this, R.string.msg_NoImage);
//            }  else {
//
//            }
        } else {
            Common.showToast(this, R.string.msg_NoNetwork);
        }
    }

    private void initData() {
        ivGoods = (ImageView) findViewById(R.id.ivGoods);
        fragmentList = new ArrayList<>();
        fragmentList.add(new GoodsDetailFragment());
//        fragmentList.add(new GoodsBoxPageGive());
//        fragmentList.add(new GoodsBoxPageWish());
    }

    private void initView() {
        gTablayout = (TabLayout) findViewById(R.id.tabs);
        gTablayout.addTab(gTablayout.newTab().setText("資訊"));
        gTablayout.addTab(gTablayout.newTab().setText("評價"));
        gTablayout.addTab(gTablayout.newTab().setText("私訊"));

        FragmentAdapter fAdapter = new FragmentAdapter(getSupportFragmentManager(), fragmentList);//Adapter有多個項目，(,顯示內容)
        vpager = (ViewPager) findViewById(R.id.pager);
        vpager.setAdapter(fAdapter);

        //mViewPager = (ViewPager) findViewById(R.id.pager);
        //vpager.setAdapter(memberAdapter);
        initListener();
    }

    private void initListener() {
        gTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        vpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(gTablayout));
    }

    private class FragmentAdapter extends FragmentStatePagerAdapter {//FragmentManager
        List<Fragment> fragmentList;

        private FragmentAdapter(FragmentManager fm, List<Fragment> memberList) {
            super(fm);
            this.fragmentList = memberList;
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {//position第幾個項目
            Fragment goods = fragmentList.get(position);
            //MemberFragment fragment = new MemberFragment();//View圖片
            //Bundle args = new Bundle();
            //args.putSerializable("member", member);//資料Data
            //fragment.setArguments(args);
            return fragmentList.get(position);
        }
    }
}
