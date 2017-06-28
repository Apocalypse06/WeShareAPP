package com.example.ntut.weshare.viewtest;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.example.ntut.weshare.R;

import java.util.ArrayList;
import java.util.List;

public class ViewActivity extends AppCompatActivity {
    private TabLayout mTablayout;
    private ViewPager mViewPager;
    private List<PageView> pageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        initData();
        initView();
    }

    private void initData() {
        pageList = new ArrayList<>();
        pageList.add(new PageOne(ViewActivity.this));
        pageList.add(new PageTwo(ViewActivity.this));
        pageList.add(new PageThree(ViewActivity.this));
    }

    private void initView() {
        mTablayout = (TabLayout) findViewById(R.id.tabs);
        mTablayout.addTab(mTablayout.newTab().setText("Page one"));
        mTablayout.addTab(mTablayout.newTab().setText("Page two"));
        mTablayout.addTab(mTablayout.newTab().setText("Page three"));

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new SamplePagerAdapter());
//        mViewPager.setAdapter(new SamplePagerAdapter());
//        initListener();
    }

    private void initListener() {
        mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
    }


    private class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(pageList.get(position));
            return pageList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
}
