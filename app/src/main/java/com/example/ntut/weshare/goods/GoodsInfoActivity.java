package com.example.ntut.weshare.goods;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        //退回按鍵
        final Button backButton = (Button)findViewById(R.id.btBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //修改按鈕
        final Button updateButton = (Button)findViewById(R.id.btUpdate);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //刪除按鈕
        final Button deleteButton = (Button)findViewById(R.id.btDel);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }


    private void findViews() {
        ivgimage= (ImageView) findViewById(R.id.ivGImage);
        tvgname=(TextView) findViewById(R.id.tvGName);
        tvgtype=(TextView) findViewById(R.id.tvGType);
        tvgdate=(TextView) findViewById(R.id.tvGDate);
        tvnote=(TextView) findViewById(R.id.tvNote);
        tvgcount=(TextView) findViewById(R.id.tvGCount);
        tvplace=(TextView) findViewById(R.id.tvGPlace);

    }

}
