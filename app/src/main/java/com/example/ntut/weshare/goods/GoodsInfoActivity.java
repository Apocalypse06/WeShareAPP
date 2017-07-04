package com.example.ntut.weshare.goods;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.MainActivity;
import com.example.ntut.weshare.R;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;


public class GoodsInfoActivity extends AppCompatActivity{
    private static final String TAG = "GoodsInfoActivity";
    public ImageView ivgimage;
    public TextView tvgname;
    public TextView tvgtype;
    public TextView tvgdate;
    public TextView tvnote;
    public TextView tvgcount;
    public TextView tvplace;
    public Goods good;


    String action;
    List<Goods> GoodsOrigin = null;




    @Override
    public void onResume() {
        super.onResume();
        // 從偏好設定檔中取得登入狀態來決定是否顯示「登出」
        SharedPreferences pref = this.getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
        String user = pref.getString("user","");
        if(user==""){
            Toast.makeText(this, "請註冊登入WeShare後，再過來設定您的物資箱喔~",
                    Toast.LENGTH_SHORT).show();
            finish();
            Intent MainIntent = new Intent(this, MainActivity.class);
            startActivity(MainIntent);
        }else{
            Toast.makeText(this, user,
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goodsbox_info_fragment);
        final Button updateButton = (Button) findViewById(R.id.btUpdate);
        final Button infobackButton = (Button) findViewById(R.id.btBack);
        //final Button deleteButton = (Button) findViewById(R.id.btDel);
        Bundle bundleFromList=this.getIntent().getBundleExtra("intentGoods");
        good =(Goods) bundleFromList.getSerializable("goods");


        //退回按鍵
        infobackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            finish();
            }
        });

        //修改按鈕
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(GoodsInfoActivity.this, GoodsUpdateActivity.class);
                Bundle bundle = new Bundle();

                bundle.putSerializable("goods",good);
                intent.putExtra("intentGoods",bundle);
                startActivity(intent);
            }
        });

//        //刪除按鈕
//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Common.networkConnected(new GoodsInfoActivity())) {
//                    String url = Common.URL + "GoodsServlet";
//                    String action = "goodsDelete";
//                    int count = 0;
//                    try {
//                        count = new GoodsUpdateTask().execute(url, action, good).get();
//                    } catch (Exception e) {
//                        Log.e(TAG, e.toString());
//                    }
//                    if (count == 0) {
//                        Common.showToast(new GoodsInfoActivity(), R.string.msg_DeleteFail);
//                    } else {
//                        Common.showToast(new GoodsInfoActivity(), R.string.msg_DeleteSuccess);
//                    }
//                } else {
//                    Common.showToast(new GoodsInfoActivity(), R.string.msg_NoNetwork);
//                }
//            }
//        });
        findViews();
    }



    private void findViews() {
        ivgimage= (ImageView) findViewById(R.id.ivGImage);
        tvgname=(TextView) findViewById(R.id.tvGName);
        tvgtype=(TextView) findViewById(R.id.tvGType);
        tvgdate=(TextView) findViewById(R.id.tvGDate);
        tvnote=(TextView) findViewById(R.id.tvNote);
        tvgcount=(TextView) findViewById(R.id.tvGCount);
        tvplace=(TextView) findViewById(R.id.tvGPlace);


        showInfo(good);
    }

    private void showInfo(Goods good) {
        tvgname.setText(good.getGoodsName());
        tvgtype.setText("類型："+changeType2String(good.getGoodsType()));
        tvplace.setText("所在地："+changeLoc2String(good.getGoodsLoc()));
        tvgcount.setText("數量："+good.getQty());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String exdate = sdf.format(good.getDeadLine());
        tvgdate.setText("到期日："+exdate);

        tvnote.setText(good.getGoodsNote());
        tvnote.setMovementMethod(new ScrollingMovementMethod());
//        try {
//            InputStream is =;
//            BufferedInputStream bis=new BufferedInputStream(is);
//            ivgimage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//            ivgimage.setAdjustViewBounds(true);
//            ivgimage.setImageBitmap(BitmapFactory.decodeStream(bis));
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.body, fragment);
        fragmentTransaction.commit();
    }

        public String changeLoc2String(int loc) {
        String gloc = "";
        switch (loc) {
            case 1:
                gloc = "北";
                break;
            case 2:
                gloc = "中";
                break;
            case 3:
                gloc = "南";
                break;
        }
        return gloc;
    }

        public String changeType2String(int type) {
        String gtype = "";
        switch (type) {
            case 1:
                gtype = "食";
                break;
            case 2:
                gtype = "衣";
                break;
            case 3:
                gtype = "住";
                break;
            case 4:
                gtype = "行";
                break;
            case 5:
                gtype = "育";
                break;
            case 6:
                gtype = "樂";
                break;
        }
        return gtype;
    }

    public void btdelete(View view) {
        if (Common.networkConnected(this)) {
            String url = Common.URL + "GoodsServlet";
            String action = "goodsDelete";
            int count = 0;
            try {
                count = new GoodsUpdateTask().execute(url, action, good,null).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (count == 0) {
                Common.showToast(this, R.string.msg_DeleteFail);
            } else {
                Common.showToast(this, R.string.msg_DeleteSuccess);
                finish();
            }
        } else {
            Common.showToast(this, R.string.msg_NoNetwork);
        }
    }
}
