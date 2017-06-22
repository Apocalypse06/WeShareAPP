package com.example.ntut.weshare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ntut.weshare.feedback.FeedbackFragment;
import com.example.ntut.weshare.goods.GoodsListFragment;
import com.example.ntut.weshare.goods.GoodsMsgFragment;
import com.example.ntut.weshare.home.HomeFragment;
import com.example.ntut.weshare.icon.InstitutionkFragment;
import com.example.ntut.weshare.icon.MessageFragment;
import com.example.ntut.weshare.icon.SearchFragment;
import com.example.ntut.weshare.member.MemberFragment;
import com.example.ntut.weshare.member.MemberLoginActivity;
import com.example.ntut.weshare.member.MemberRegisterActivity;
import com.example.ntut.weshare.member.historyFragment;

//import com.example.ntut.weshare.member.MemberLoginActivity;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private ImageView ivUser;
    private TextView tvUserName;

    private MenuView.ItemView item_login;
    private MenuView.ItemView item_logout;
    private MenuView.ItemView item_register;

    //SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findVV();
        setUpActionBar();
        initDrawer();//初始化抽屜
        initBody();
    }

    private void findVV() {

//        boolean kk = getSharedPreferences("data", MODE_PRIVATE).getBoolean("LoginOK", false);
//        if (kk == true) {
//            ivUser = (ImageView) findViewById(R.id.ivUser);
//
//            tvUserName = (TextView) findViewById(R.id.tvUserName);
//            tvUserName.setText(getSharedPreferences("data", MODE_PRIVATE).getString("account", "您尚未登入"));
//
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//監聽menu的監聽器
        MenuInflater inflater = getMenuInflater();//getMenuInflater()載入器
        inflater.inflate(R.menu.icon_menu, menu);//將options_menu內容載入到menu


//        Toast.makeText(this, "" + getSharedPreferences("data", MODE_PRIVATE).getString("LoginOK", "no"),
//                Toast.LENGTH_LONG).show();
        boolean kk = getSharedPreferences("data", MODE_PRIVATE).getBoolean("LoginOK", false);
        if (kk == true) {
            ivUser = (ImageView) findViewById(R.id.ivUser);

            tvUserName = (TextView) findViewById(R.id.tvUserName);
            tvUserName.setText(getSharedPreferences("data", MODE_PRIVATE).getString("account", "您尚未登入"));

        }

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
//              Fragment fragment = new InstitutionkFragment();
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
        Fragment fragment = new HomeFragment();
        switchFragment(fragment);
        setTitle(R.string.tx_homeIndex);
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.body, fragment);
        fragmentTransaction.commit();
    }


//    public void onLoginClick(View view) {
//        Intent updateIntent;
//        updateIntent = new Intent();
//        updateIntent.setClass(MainActivity.this, MemberLoginActivity.class);
//        startActivity(updateIntent);
//    }
//
//    public void onRegisterClick(View view) {
//        Intent updateIntent;
//        updateIntent = new Intent();
//        updateIntent.setClass(MainActivity.this, MemberRegisterActivity.class);
//        startActivity(updateIntent);
//    }


    private void initDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.home_index);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.tx_open, R.string.tx_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);


        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                Fragment fragment;
                Intent updateIntent;
                switch (menuItem.getItemId()) {
                    case R.id.item_home:
                        initBody();
                        break;
                    case R.id.item_updateMember:
                        fragment = new MemberFragment();//取代舊的Fragment方法
                        switchFragment(fragment);
                        setTitle(R.string.tx_updateMember);
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
                        fragment = new FeedbackFragment();
                        switchFragment(fragment);
                        setTitle(R.string.tx_feedback);
                        break;
                    case R.id.item_login:
                        updateIntent = new Intent();
                        updateIntent.setClass(MainActivity.this, MemberLoginActivity.class);
                        startActivity(updateIntent);
                        break;
                    case R.id.item_register:
                        updateIntent = new Intent();
                        updateIntent.setClass(MainActivity.this, MemberRegisterActivity.class);
                        startActivity(updateIntent);
                        break;
                    case R.id.item_logout:
                        getSharedPreferences("data", MODE_PRIVATE).edit()
                                .putString("account", "")
                                .putString("password", "")
                                .putBoolean("LoginOK", false)
                                .apply();

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
