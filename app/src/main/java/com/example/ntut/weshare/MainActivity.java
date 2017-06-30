package com.example.ntut.weshare;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ntut.weshare.feedback.FeedbackFragment;
import com.example.ntut.weshare.goods.GoodsListFragment;
import com.example.ntut.weshare.goods.GoodsMsgFragment;
import com.example.ntut.weshare.icon.InstitutionkFragment;
import com.example.ntut.weshare.icon.MessageFragment;
import com.example.ntut.weshare.icon.SearchFragment;
import com.example.ntut.weshare.member.MemberLoginActivity;
import com.example.ntut.weshare.member.MemberRegisterTypeActivity;
import com.example.ntut.weshare.member.MemberUpdateActivity;
import com.example.ntut.weshare.member.MemberUpdateIndActivity;
import com.example.ntut.weshare.member.MemberUpdateOrgActivity;
import com.example.ntut.weshare.member.historyFragment;
import java.util.ArrayList;
import java.util.List;

//import com.example.ntut.weshare.member.MemberLoginActivity;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private ImageView ivUser;
    private TextView testUser;

    Bitmap bitmap = null;

    private TabLayout mTablayout;
    private ViewPager mViewPager;
    private List<PageView> pageList;


    private final static int REQ_PERMISSIONS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpActionBar();
        initDrawer();//初始化抽屜
        initBody();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 從偏好設定檔中取得登入狀態來決定是否顯示「登出」
        SharedPreferences pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        boolean login = pref.getBoolean("login", false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        askPermissions();
    }


    private void showAllSpots() {
        if (Common.networkConnected(this)) {//檢查網路
            String url = Common.URL + "UserServlet";
            SharedPreferences pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
            String account = pref.getString("user", "");
//            Toast.makeText(this, account, Toast.LENGTH_LONG).show();
//            List<User> user = null;
            int imageSize = 100;
//            Bitmap bitmap = null;
            try {
                // passing null and calling get() means not to run FindImageByIdTask.onPostExecute()
//            bitmap = new UserGetImageTask(null).execute(url, account, imageSize).get();
                //user = new UserGetAllTask().execute(url, account).get();//.get()要請UserGetAllTask()的執行結果回傳給我，會等他抓完資料(doInBackground的回傳結果)才會往下執行
                bitmap = new UserGetImageTask().execute(url, account, imageSize).get();//.execute(網址, 圖片id, 這邊做縮圖imageSize，在server端縮圖完再傳過來)
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (bitmap == null) {
                Common.showToast(this, "不可能");
            } else {
                //rvSpots.setAdapter(new SpotsRecyclerViewAdapter(getActivity(), spots));//畫面RecyclerView(畫面,資料)，getActivity()取的他所依附的頁面(主頁面)
            }
        } else {
            Common.showToast(this, R.string.msg_NoNetwork);
        }
    }


    private void askPermissions() {
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        int result = ContextCompat.checkSelfPermission(this, permissions[0]);
        if (result != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    permissions,
                    REQ_PERMISSIONS);
        }
    }


    private void cleanPreferences() {
        SharedPreferences pref = getSharedPreferences(Common.PREF_FILE,
                MODE_PRIVATE);
        pref.edit()
                .putString("user", "")
                .putString("password", "")
                .putString("name", "")
                .putBoolean("login", false)
                .apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//監聽menu的監聽器
        MenuInflater inflater = getMenuInflater();//getMenuInflater()載入器
        inflater.inflate(R.menu.icon_menu, menu);//將options_menu內容載入到menu
        return true;
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState) {//必須要加，如果要按鈕跟抽屜同步化
        super.onPostCreate(savedInstanceState);
        // home icon will keep still without calling syncState()
        actionBarDrawerToggle.syncState();//syncState是抽屜指示器，拉開跟按鈕同步化，必須放在onPostCreate裡
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
            case R.id.item_welfareInstitution:
                fragment = new InstitutionkFragment();
                switchFragment(fragment);
                setTitle(R.string.tx_welfareInstitution);
                break;
            case R.id.item_search:
                fragment = new SearchFragment();
                switchFragment(fragment);
                setTitle(R.string.tx_search);
                break;
            case R.id.item_message:
                fragment = new MessageFragment();
                switchFragment(fragment);
                setTitle(R.string.tx_message);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void initBody() {
//        Fragment fragment = new HomeFragment();
//        switchFragment(fragment);
//        setTitle(R.string.tx_homeIndex);
        //會跟viewPage衝突

        mTablayout = (TabLayout) findViewById(R.id.tabs);
        mTablayout.addTab(mTablayout.newTab().setText("許願池"));
        mTablayout.addTab(mTablayout.newTab().setText("送愛心"));
        mTablayout.addTab(mTablayout.newTab().setText("以物易物"));

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new SamplePagerAdapter());

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


    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.body, fragment);
        fragmentTransaction.commit();
    }


    private void initDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.home_index);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.tx_open, R.string.tx_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        ///
        pageList = new ArrayList<>();
        pageList.add(new PageOne(MainActivity.this));
        pageList.add(new PageTwo(MainActivity.this));
        pageList.add(new PageThree(MainActivity.this));
        ///

        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        SharedPreferences pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        boolean login = pref.getBoolean("login", false);

        MenuItem loginItem = view.getMenu().findItem(R.id.item_login);
        MenuItem registerItem = view.getMenu().findItem(R.id.item_register);
        MenuItem logoutItem = view.getMenu().findItem(R.id.item_logout);

        View header = view.inflateHeaderView(R.layout.navigate_header);
        ImageView ivUser = (ImageView) header.findViewById(R.id.ivUser);
        TextView tvUserName = (TextView) header.findViewById(R.id.tvUserName);

        pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        String name = pref.getString("name", "尚未登入，請進行登入");
        login = pref.getBoolean("login", false);
        if (login == true) {
            showAllSpots();
            ivUser.setImageBitmap(bitmap);
            tvUserName.setText(name + "  歡迎回來");
        }


        if (login) {
            loginItem.setVisible(false);    // true 为显示，false 为隐藏
            registerItem.setVisible(false);
            logoutItem.setVisible(true);
        } else {
            loginItem.setVisible(true);
            registerItem.setVisible(true);
            logoutItem.setVisible(false);
        }
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                Fragment fragment;
                Intent updateIntent;
                switch (menuItem.getItemId()) {
                    case R.id.item_home:
                        updateIntent = new Intent();
                        updateIntent.setClass(MainActivity.this, MainActivity.class);
                        startActivity(updateIntent);
                        break;
                    case R.id.item_updateMember:
                        updateIntent = new Intent();
                        updateIntent.setClass(MainActivity.this, MemberUpdateIndActivity.class);
                        startActivity(updateIntent);
                        break;
                    case R.id.item_history:
                        fragment = new historyFragment();
                        switchFragment(fragment);
                        setTitle(R.string.tx_history);
                        break;
                    case R.id.item_goodsBox:
                        fragment = new GoodsListFragment();
                        switchFragment(fragment);
                        setTitle(R.string.tx_goodsBox);
                        break;
                    case R.id.item_goodsMsg:
                        fragment = new GoodsMsgFragment();
                        switchFragment(fragment);
                        setTitle(R.string.tx_goodsMsg);
                        break;
                    case R.id.item_feedback:
//                        fragment = new FeedbackFragment();
//                        switchFragment(fragment);
//                        setTitle(R.string.tx_feedback);
                        updateIntent = new Intent();
                        updateIntent.setClass(MainActivity.this, MemberUpdateOrgActivity.class);
                        startActivity(updateIntent);
                        break;
                    case R.id.item_login:
                        updateIntent = new Intent();
                        updateIntent.setClass(MainActivity.this, MemberLoginActivity.class);
                        startActivity(updateIntent);
                        break;
                    case R.id.item_register:
                        updateIntent = new Intent();
                        updateIntent.setClass(MainActivity.this, MemberRegisterTypeActivity.class);
                        startActivity(updateIntent);
                        break;
                    case R.id.item_logout:
                        cleanPreferences();
                        updateIntent = new Intent();
                        updateIntent.setClass(MainActivity.this, MainActivity.class);
                        startActivity(updateIntent);
                        break;
                    default:
                        initBody();
                        break;

                }
                return true;
            }

        });

    }


}
