package com.example.ntut.weshare.member;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.MainActivity;
import com.example.ntut.weshare.R;

public class MemberUpdateOrgChooseActivity extends AppCompatActivity {

    @Override
    public void onResume() {
        super.onResume();
        // 從偏好設定檔中取得登入狀態來決定是否顯示「登出」

        SharedPreferences pref = getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
        String user = pref.getString("user", "");
        if (user == "") {
            Toast.makeText(this, "請先註冊登入WeShare~",
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
//            Toast.makeText(this, user,
//                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_update_org_choose);
    }

    public void onUpIndClick(View view) {
        Intent updateIntent = new Intent();
        updateIntent.setClass(this, MemberUpdateIndActivity.class);
        startActivity(updateIntent);
    }


    public void onUpOrgClick(View view) {
        Intent updateIntent = new Intent();
        updateIntent.setClass(this, MemberUpdateOrgActivity.class);
        startActivity(updateIntent);
    }

    public void onLeaveClick(View view) {
        Intent updateIntent = new Intent();
        updateIntent.setClass(this, MainActivity.class);
        startActivity(updateIntent);
    }
}