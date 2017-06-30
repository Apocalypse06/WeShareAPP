package com.example.ntut.weshare.member;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.example.ntut.weshare.MainActivity;
import com.example.ntut.weshare.R;


public class MemberRegisterTypeActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_register_type_fragment);
    }

    public void onReIndClick(View view) {
        Intent updateIntent = new Intent();
        updateIntent.setClass(MemberRegisterTypeActivity.this, MemberRegisterIndActivity.class);
        startActivity(updateIntent);
    }


    public void onReOrgClick(View view) {
        Intent updateIntent = new Intent();
        updateIntent.setClass(MemberRegisterTypeActivity.this, MamberRegisterOrgPageOneActivity.class);
        startActivity(updateIntent);
    }

    public void onLeaveClick(View view) {
        Intent updateIntent = new Intent();
        updateIntent.setClass(MemberRegisterTypeActivity.this, MainActivity.class);
        startActivity(updateIntent);
    }
}
