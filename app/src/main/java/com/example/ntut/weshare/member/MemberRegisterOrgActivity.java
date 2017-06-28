package com.example.ntut.weshare.member;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.R;
import com.example.ntut.weshare.viewpage.PageView;
import com.example.ntut.weshare.viewpage.ViewActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
//
//public class MemberRegisterOrgActivity extends AppCompatActivity {
//    private final static String TAG = "UserInsertActivity";
//    private TabLayout mTablayout;
//    private ViewPager mViewPager;
//    private List<PageView> pageList;
//    //List<Fragment> fragmentList = new ArrayList<Fragment>();
//    private List<Fragment> fragmentList;
//
//    private ImageView ivUser;
//    //private byte[] image = new byte[0];
//    private byte[] image = null;
//    private EditText etAccount;
//    private EditText etPassword;
//    private EditText etName;
//    private EditText etTal;
//    private EditText etEmail;
//    private EditText etAddress;
//    private RadioGroup rgType;
//    private LinearLayout llInstiution;
//    int idType = 2;
//
//    private static final int REQUEST_PICK_IMAGE = 1;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.member_register_org_activityc);
//        initData();
//        initView();
//    }
//
//    private void initData() {
//       // pageList = new ArrayList<>();
//        fragmentList = new ArrayList<>();
//        fragmentList.add(new MamberRegisterOrgPageOne());
//        fragmentList.add(new MamberRegisterOrgPageTwo());
////        pageList.add(new Fragment(MemberRegisterOrgActivity.this));
////        pageList.add(new MamberRegisterOrgPageTwo(MemberRegisterOrgActivity.this));
//    }
//
//    private void initView() {
//        mTablayout = (TabLayout) findViewById(R.id.tabs);
//        mTablayout.addTab(mTablayout.newTab().setText("個人資料"));
//        mTablayout.addTab(mTablayout.newTab().setText("社福團體資料"));
//
//        mViewPager = (ViewPager) findViewById(R.id.pager);
//        mViewPager.setAdapter(new MemberRegisterOrgActivity.SamplePagerAdapter());
//        initListener();
//    }
//
//    private void initListener() {
//        mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                mViewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
//    }
//
//    private class SamplePagerAdapter extends PagerAdapter {
//
//        @Override
//        public int getCount() {
//            return fragmentList.size();
//           // return pageList.size();
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object o) {
//            return o == view;
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
////            container.addView(pageList.get(position));
////            return pageList.get(position);
//            container.addView(fragmentList.get(position));
//            return fragmentList.get(1);
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView((View) object);
//        }
//    }
//
//
//    public void onPickPictureMemClick(View view) {
//        Intent intent = new Intent(Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, REQUEST_PICK_IMAGE);
//    }
//
//    public void onHelpClick(View view) {
//        //Common.showToast(MemberRegisterOrgActivity.this, pageList.get(0).gete);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case REQUEST_PICK_IMAGE:
//                    Uri uri = intent.getData();
//                    String[] columns = {MediaStore.Images.Media.DATA};
//                    Cursor cursor = getContentResolver().query(uri, columns, null, null, null);
//                    if (cursor != null && cursor.moveToFirst()) {
//                        String imagePath = cursor.getString(0);
//                        cursor.close();
//                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//                        ivUser.setImageBitmap(bitmap);
//                        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out2);
//                        image = out2.toByteArray();
//                    }
//                    break;
////                case REQUEST_PICK_IMAGE_IN:
////                    Uri uriIn = intent.getData();
////                    String[] columnsIn = {MediaStore.Images.Media.DATA};
////                    Cursor cursorIn = getContentResolver().query(uriIn, columnsIn, null, null, null);
////                    if (cursorIn != null && cursorIn.moveToFirst()) {
////                        String imagePathIn = cursorIn.getString(0);
////                        cursorIn.close();
////                        Bitmap bitmapIn = BitmapFactory.decodeFile(imagePathIn);
////                        ivIn.setImageBitmap(bitmapIn);
////                        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
////                        bitmapIn.compress(Bitmap.CompressFormat.JPEG, 100, out2);
////                        imageIn = out2.toByteArray();
////                    }
////                    break;
//            }
//        }
//    }
//}


public class MemberRegisterOrgActivity extends FragmentActivity {

    List<Fragment> fragmentList = new ArrayList<Fragment>();
    List<String> titleList = new ArrayList<String>();

    private TabLayout mTablayout;
    private ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_register_org_activityc);

//        mTablayout = (TabLayout) findViewById(R.id.tabs);
//        mTablayout.addTab(mTablayout.newTab().setText("個人資料"));
//        mTablayout.addTab(mTablayout.newTab().setText("社福團體資料"));


        ViewPager vp = (ViewPager) findViewById(R.id.pager);
        fragmentList.add(new MamberRegisterOrgPageOne());
        fragmentList.add(new MamberRegisterOrgPageTwo());

        titleList.add("title 1 ");
        titleList.add("title 2 ");
        vp.setAdapter(new myPagerAdapter(getSupportFragmentManager(), fragmentList, titleList));
//        vp.setAdapter(new MemberRegisterOrgActivity.SamplePagerAdapter());
//        initListener();
    }

    private void initListener() {
        mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
    }

    private class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //container.addView(fragmentList.get(position));
            return fragmentList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 定义适配器
     *
     * @author trinea.cn 2012-11-15
     */
    class myPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;
        private List<String> titleList;

        public myPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList) {
            super(fm);
            this.fragmentList = fragmentList;
            this.titleList = titleList;
        }

        /**
         * 得到每个页面
         */
        @Override
        public Fragment getItem(int arg0) {
            return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
        }

        /**
         * 每个页面的title
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return (titleList.size() > position) ? titleList.get(position) : "";
        }

        /**
         * 页面的总个数
         */
        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }
    }
}
