package com.example.ntut.weshare.goods;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.MainActivity;
import com.example.ntut.weshare.R;


import java.text.SimpleDateFormat;
import java.util.List;


public class GoodsInfoActivity extends AppCompatActivity {
    private static final String TAG = "GoodsInfoActivity";
    public ImageView ivgimage;
    public TextView tvgname;
    public TextView tvgtype;
    public TextView tvgdate;
    public TextView tvnote;
    public TextView tvgcount;
    public TextView tvplace;
    public Goods good;
    private final String[] locString={"苗栗縣","桃園市","基隆市","新北市","新竹市","新竹縣","臺北市","南投縣","雲林縣","嘉義市","嘉義縣","彰化縣"
            ,"臺中市","屏東縣","高雄市","臺南市","宜蘭縣","花蓮縣","臺東縣","金門縣","連江縣","澎湖縣"};

    String action;
    List<Goods> GoodsOrigin = null;

    @Override
    public void onResume() {
        super.onResume();
        // 從偏好設定檔中取得登入狀態來決定是否顯示「登出」
        SharedPreferences pref = this.getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
        String user = pref.getString("user", "");
        if (user == "") {
            Toast.makeText(this, "請註冊登入WeShare後，再過來設定您的物資箱喔~",
                    Toast.LENGTH_SHORT).show();
            finish();
            Intent MainIntent = new Intent(this, MainActivity.class);
            startActivity(MainIntent);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goodsbox_info_fragment);
        final Button updateButton = (Button) findViewById(R.id.btUpdate);
        final Button infobackButton = (Button) findViewById(R.id.btBack);
        LinearLayout back=(LinearLayout)findViewById(R.id.infoBackground);
        //final Button deleteButton = (Button) findViewById(R.id.btDel);
        Bundle bundleFromList = this.getIntent().getBundleExtra("intentGoods");
        good = (Goods) bundleFromList.getSerializable("goods");
        if(good.getGoodsStatus()==1){
            back.setBackgroundColor(Color.rgb(251, 225, 232));
        }else if(good.getGoodsStatus()==2){
            back.setBackgroundColor(Color.rgb(248, 188, 124));
        }else if(good.getGoodsStatus()==3){
            back.setBackgroundColor(Color.rgb(68, 248, 172));
        }


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

                bundle.putSerializable("goods", good);
                intent.putExtra("intentGoods", bundle);
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
        ivgimage = (ImageView) findViewById(R.id.ivGImage);
        tvgname = (TextView) findViewById(R.id.tvGName);
        tvgtype = (TextView) findViewById(R.id.tvGType);
        tvgdate = (TextView) findViewById(R.id.tvGDate);
        tvnote = (TextView) findViewById(R.id.tvNote);
        tvgcount = (TextView) findViewById(R.id.tvGCount);
        tvplace = (TextView) findViewById(R.id.tvGPlace);


        showInfo(good);
    }


    private void showInfo(Goods good) {

        String url = Common.URL + "GoodsServlet";
        int gid = good.getGoodsNo();
        int imageSize = 400;
        new GoodsGetImageTask(ivgimage).execute(url, gid, imageSize);

        tvgname.setText(good.getGoodsName());
        tvgtype.setText("類型：" + changeType2String(good.getGoodsType()));
        tvplace.setText("所在地：" + changeLoc2String(good.getGoodsLoc()));
        tvgcount.setText("數量：" + good.getQty());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String exdate = sdf.format(good.getDeadLine());
        tvgdate.setText("到期日：" + exdate);

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
        gloc=locString[(loc-1)];
        return gloc;
    }

    public String changeType2String(int type) {
        String gtype = "";
        switch (type) {
            case 1:
                gtype = "食品";
                break;
            case 2:
                gtype = "服飾配件";
                break;
            case 3:
                gtype = "生活用品";
                break;
            case 4:
                gtype = "家電機器";
                break;
            case 5:
                gtype = "其他";
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
                count = new GoodsUpdateTask().execute(url, action, good, null).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (count == 0) {
                Common.showToast(this, R.string.msg_deleteFail);
            } else {
                Common.showToast(this, R.string.msg_deleteSuccess);
                finish();
            }
        } else {
            Common.showToast(this, R.string.msg_NoNetwork);
        }
    }
}
