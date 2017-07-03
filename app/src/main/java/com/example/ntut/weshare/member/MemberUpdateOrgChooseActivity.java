package com.example.ntut.weshare.member;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.ntut.weshare.MainActivity;
import com.example.ntut.weshare.R;

public class MemberUpdateOrgChooseActivity extends AppCompatActivity {
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