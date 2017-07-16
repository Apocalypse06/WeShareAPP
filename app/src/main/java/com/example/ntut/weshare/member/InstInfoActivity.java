package com.example.ntut.weshare.member;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.R;
import com.example.ntut.weshare.dealDetail.NotDeal;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class InstInfoActivity extends AppCompatActivity implements OnMapReadyCallback {
    private User user;
    private InstiutionBean inst;
    private ImageView iv_logo;
    private TextView tv_name;
    private TextView tv_number;
    private TextView tv_type;
    private TextView tv_leader;
    private TextView tv_contact;
    private TextView tv_phoneNum;
    private TextView tv_email;
    private TextView tv_addr;
    private TextView tv_note;
    private Button bt_msg;
    private Button bt_need;
    private Button bt_gmap;
    private String typestr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.institution_info_fragment);
        findView();
//接收會員資料
        final Bundle bundle = this.getIntent().getBundleExtra("intentUser");
        user = (User) bundle.getSerializable("user");
        inst = (InstiutionBean) bundle.getSerializable("inst");
        Log.e("", user.getName());
//用會員id抓圖片
        String url = Common.URL + "UserServlet";
        String uid = user.getUserId();
        int imageSize = 250;
        new instGetImageTask(iv_logo).execute(url, uid, imageSize);
//用上一頁送來的bundle取資料顯示
        tv_name.setText(user.getName());
        tv_number.setText("勸募許可文號：" + inst.getRaiseNo());
        tv_leader.setText(inst.getLeader());
        tv_contact.setText(user.getUserId());
        tv_phoneNum.setText(user.getTal());
        tv_email.setText(user.getEmail());
        tv_addr.setText(user.getAddress());
        tv_note.setText(inst.getIntRo());

        switch (inst.getOrgType()) {
            case 1:
                typestr = "兒少福利";
            case 2:
                typestr = "偏鄉教育";
            case 3:
                typestr = "老人福利";
            case 4:
                typestr = "身障福利";
            case 5:
                typestr = "其他類型";
        }
        tv_type.setText(typestr);

//        bt_msg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MsgDialogFragment msg = new MsgDialogFragment();
//                msg.setRef(nd);
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                msg.show(fragmentManager, "alert");//顯示警示框
//            }
//        });

        bt_gmap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(view.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.inst_mapdialog);
                dialog.show();
                MapView mMapView;
                MapsInitializer.initialize(view.getContext());

                mMapView = (MapView) dialog.findViewById(R.id.instmap);
                mMapView.onCreate(dialog.onSaveInstanceState());
                mMapView.onResume();// needed to get the map to display immediately
                mMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        LatLng posisiabsen = new LatLng(25.039334, 121.549554); ////your lat lng
                        googleMap.addMarker(new MarkerOptions().position(posisiabsen).title("Yout title"));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(posisiabsen));
                        googleMap.getUiSettings().setZoomControlsEnabled(true);
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                    }
                });
            }
        });

        bt_need.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bd = new Bundle();
                bd.putString("user", user.getUserId());
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment getgoods = new instGetGoodsFragment();
                getgoods.setArguments(bd);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.body1, getgoods);
                fragmentTransaction.commit();

            }
        });

    }


    private void findView() {
        iv_logo = (ImageView) findViewById(R.id.iv_instlogo);
        tv_name = (TextView) findViewById(R.id.tv_instname);
        tv_number = (TextView) findViewById(R.id.tv_instnumber);
        tv_type = (TextView) findViewById(R.id.tv_insttype);
        tv_leader = (TextView) findViewById(R.id.tv_instleader);
        tv_contact = (TextView) findViewById(R.id.tv_instcontact);
        tv_phoneNum = (TextView) findViewById(R.id.tv_instphonenum);
        tv_email = (TextView) findViewById(R.id.tv_instemail);
        tv_addr = (TextView) findViewById(R.id.tv_instaddr);
        tv_note = (TextView) findViewById(R.id.tv_instnote);
        bt_msg = (Button) findViewById(R.id.bt_instmsg);
        bt_need = (Button) findViewById(R.id.bt_instgoods);
        bt_gmap = (Button) findViewById(R.id.bt_instgmap);
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.body1, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        android.app.FragmentManager fm = this.getFragmentManager();
        if (fm.getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            fm.popBackStack();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}