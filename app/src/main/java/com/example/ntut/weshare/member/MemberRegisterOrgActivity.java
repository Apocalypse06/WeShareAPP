package com.example.ntut.weshare.member;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.ntut.weshare.R;

import java.util.ArrayList;
import java.util.List;


public class MemberRegisterOrgActivity extends AppCompatActivity {
    private List<Fragment> fragmentList;
    private ViewPager vpager;
    private TabLayout mTablayout;
    private Button btCancel;
    private Button btEnter;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_register_org_activityc);
        //List<Member> memberList = getMemberList();//新增好友
        initData();
        initView();
    }

    public void onCancelClick(View view) {
        finish();
    }

    public void onEnterClick(View view) {
//        Fragment fragment = fragmentManager.findFragmentByTag(TAG);
//        if (fragment == null) {
//            showToast("fragment doesn't exists");
//        } else {
//            if (!fragment.isDetached()) {
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.detach(fragment);
//                transaction.commit();
//            } else {
//                showToast("fragment detached");
//            }
//        }
        MamberRegisterOrgPageOne fragment = new MamberRegisterOrgPageOne();//View圖片
        Bundle args = new Bundle();
        args.putSerializable("Enter", "Enter");//資料Data
        fragment.setArguments(args);
        //Common.showToast(MemberRegisterOrgActivity.this, "OK");

        fragment.test1();

    }

    private void initData() {
        btCancel = (Button) findViewById(R.id.btCancel);
        btEnter = (Button) findViewById(R.id.btEnter);
        fragmentList = new ArrayList<>();
        fragmentList.add(new MamberRegisterOrgPageOne());
        fragmentList.add(new MamberRegisterOrgPageTwo());
    }


    private void initView() {
        mTablayout = (TabLayout) findViewById(R.id.tabs);
        mTablayout.addTab(mTablayout.newTab().setText("個人資料"));
        mTablayout.addTab(mTablayout.newTab().setText("社福資料"));

        FragmentAdapter fAdapter = new FragmentAdapter(getSupportFragmentManager(), fragmentList);//Adapter有多個項目，(,顯示內容)
        vpager = (ViewPager) findViewById(R.id.pager);
        vpager.setAdapter(fAdapter);

        //mViewPager = (ViewPager) findViewById(R.id.pager);
        //vpager.setAdapter(memberAdapter);
        initListener();
    }


    private void initListener() {
        mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        vpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
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
            Fragment member = fragmentList.get(position);
            //MemberFragment fragment = new MemberFragment();//View圖片
            //Bundle args = new Bundle();
            //args.putSerializable("member", member);//資料Data
            //fragment.setArguments(args);
            return fragmentList.get(position);
        }
    }

}
