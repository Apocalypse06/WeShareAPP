package com.example.ntut.weshare.member;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.MainActivity;
import com.example.ntut.weshare.R;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;


public class MamberRegisterOrgPageTwoActivity extends AppCompatActivity {
    private final static String TAG = "UserInsertActivity";
    private User user;
    private ImageView ivOrg;
    private byte[] imageOrg = null;
    private EditText etLeader;
    private Spinner spOrgType;
    private EditText etRegisterNo;
    private EditText etRaiseNo;
    private EditText etContext;

    int orgType;
    String leader;
    String registerNo;
    String raiseNo;
    String intRo;


    String imageBase64;
    String imageOrgBase64;

    private static final int REQUEST_PICK_IMAGEORG = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_register_org_pagetwo_activityc);
        getUpdata();
        findViews();
    }

    private void getUpdata() {
        Bundle bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable("user");
        imageBase64 = bundle.getString("imageBase64");
        orgType = bundle.getInt("orgType");
    }


    private void findViews() {
        ivOrg = (ImageView) findViewById(R.id.ivOrg);
        etLeader = (EditText) findViewById(R.id.etLeader);
        spOrgType = (Spinner) findViewById(R.id.spOrgType);
        etRegisterNo = (EditText) findViewById(R.id.etRegisterNo);
        etRaiseNo = (EditText) findViewById(R.id.etRaiseNo);
        etContext = (EditText) findViewById(R.id.etContext);


        final String[] classes = {"兒少福利", "偏鄉教育", "老人福利", "身障福利", "其他"};
        ArrayAdapter<String> classList = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                classes);
        spOrgType.setAdapter(classList);
    }

    public void onPickPictureOrgClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGEORG);
    }

    private void ChoseOreType() {
        switch (spOrgType.getSelectedItem().toString()) {
            case "兒少福利":
                orgType = 1;
                break;
            case "偏鄉教育":
                orgType = 1;
                break;
            case "老人福利":
                orgType = 2;
                break;
            case "身障福利":
                orgType = 3;
                break;
            case "其他":
                orgType = 4;
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK_IMAGEORG:
                    Uri uri = intent.getData();
                    String[] columns = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, columns, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        String imagePath = cursor.getString(0);
                        cursor.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                        ivOrg.setImageBitmap(bitmap);
                        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out2);
                        imageOrg = out2.toByteArray();
                    }
                    break;
            }
        }
    }


    public void onRegisterClick(View view) {
        leader = etLeader.getText().toString().trim();
        registerNo = etRegisterNo.getText().toString().trim();
        raiseNo = etRaiseNo.getText().toString().trim();
        intRo = etContext.getText().toString().trim();
        ChoseOreType();
        if (imageOrg == null) {
            Resources res = getResources();
            Drawable drawable = res.getDrawable(R.drawable.user_default);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            imageOrg = stream.toByteArray();
        }


        Timestamp createDate = new Timestamp(new java.util.Date().getTime());
        user.setCreateDate(createDate);

        int count = 0;
        String url = Common.URL + "UserServlet";
        String action = "userInRegister";
        if (Common.networkConnected(this)) {//傳送到server端
            String account = user.getUserId();
            InstiutionBean ins = new InstiutionBean(account, leader, orgType, registerNo, raiseNo, intRo, createDate);
            String imageBase64Org = Base64.encodeToString(imageOrg, Base64.DEFAULT);//圖片資料
            try {
                count = new UserUpdateTask().execute(url, action, user, imageBase64, ins, imageBase64Org).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (count == 0) {
                Common.showToast(this, R.string.msg_RegisterFail);
            } else if (count == -1) {
                Common.showToast(this, R.string.msg_AccountRepeat);
            } else {
                Common.showToast(this, R.string.msg_RegisterSuccess);
                Intent homeIntent = new Intent();
                homeIntent.setClass(this, MainActivity.class);
                startActivity(homeIntent);
            }
        } else {
            Common.showToast(this, R.string.msg_NoNetwork);
        }

    }

    public void onUpStateClick(View view) {
        finish();
    }

    public void onHelpClick(View view) {
        etLeader.setText("林怡君院長");
        etRegisterNo.setText("F12849924");
        etRaiseNo.setText("AFS10S98G4");
        etContext.setText("這裡是愛心育幼院，請大家多多指教");
    }
}
