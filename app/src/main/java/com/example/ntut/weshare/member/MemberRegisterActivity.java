package com.example.ntut.weshare.member;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.R;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;

public class MemberRegisterActivity extends AppCompatActivity {
    private final static String TAG = "UserInsertActivity";

    private ImageView ivUser;
    private byte[] image = new byte[0];
    //    private byte[] image = null;
    private EditText etAccount;
    private EditText etPassword;
    private EditText etName;
    private EditText etTal;
    private EditText etEmail;
    private EditText etAddress;
    private RadioGroup rgType;
    private LinearLayout llInstiution;
    int idType = 1;
    String action;


    private ImageView ivIn;
    private byte[] imageIn;
    private EditText etLeader;
    private EditText etInType;
    private EditText etRegisterNo;
    private EditText etRaiseNo;
    private EditText etContext;
    String leader;
    String orgType;
    String registerNo;
    String raiseNo;
    String intRo;


    private static final int REQUEST_PICK_IMAGE_IN = 0;
    private static final int REQUEST_PICK_IMAGE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_register_fragment);
        findViews();
    }

    private void findViews() {
        ivUser = (ImageView) findViewById(R.id.ivUser);
        etAccount = (EditText) findViewById(R.id.etAccount);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etName = (EditText) findViewById(R.id.etName);
        etTal = (EditText) findViewById(R.id.etTal);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etAddress = (EditText) findViewById(R.id.etAddress);
        rgType = (RadioGroup) findViewById(R.id.rgType);
        llInstiution = (LinearLayout) findViewById(R.id.llInstiution);
        llInstiution.setVisibility(View.GONE);

        ivIn = (ImageView) findViewById(R.id.ivIn);
        etLeader = (EditText) findViewById(R.id.etLeader);
        etInType = (EditText) findViewById(R.id.etInType);
        etRegisterNo = (EditText) findViewById(R.id.etRegisterNo);
        etRaiseNo = (EditText) findViewById(R.id.etRaiseNo);
        etContext = (EditText) findViewById(R.id.etContext);

    }


    public void onPickPictureClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    public void onPickPictureInClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK_IMAGE:
                    Uri uri = intent.getData();
                    String[] columns = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, columns, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        String imagePath = cursor.getString(0);
                        cursor.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                        ivUser.setImageBitmap(bitmap);
                        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out2);
                        image = out2.toByteArray();
                    }
                    break;
                case REQUEST_PICK_IMAGE_IN:
                    Uri uriIn = intent.getData();
                    String[] columnsIn = {MediaStore.Images.Media.DATA};
                    Cursor cursorIn = getContentResolver().query(uriIn, columnsIn, null, null, null);
                    if (cursorIn != null && cursorIn.moveToFirst()) {
                        String imagePathIn = cursorIn.getString(0);
                        cursorIn.close();
                        Bitmap bitmapIn = BitmapFactory.decodeFile(imagePathIn);
                        ivIn.setImageBitmap(bitmapIn);
                        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                        bitmapIn.compress(Bitmap.CompressFormat.JPEG, 100, out2);
                        imageIn = out2.toByteArray();
                    }
                    break;
            }
        }
    }


    public void onRegisterClick(View view) {
        String account = etAccount.getText().toString().trim();
        if (account.length() <= 0) {
            Toast.makeText(this, R.string.msg_accountNull,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String password = etPassword.getText().toString().trim();
        if (password.length() <= 0) {
            Toast.makeText(this, R.string.msg_passwordNull,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String name = etName.getText().toString().trim();
        if (password.length() <= 0) {
            Toast.makeText(this, R.string.msg_nameNull,
                    Toast.LENGTH_SHORT).show();
            return;
        }
//        if (image == null) {
//            Common.showToast(this, "沒有圖片");
//            return;
//        }

        //依選取項目顯示不同訊息
        switch (rgType.getCheckedRadioButtonId()) {
            case R.id.rbPersonal:
                idType = 1;
                action = "userRegister";
                break;
            case R.id.rbInstiution:
                idType = 2;
                action = "userInRegister";
                leader = etLeader.getText().toString().trim();
                orgType = etInType.getText().toString().trim();
                registerNo = etRegisterNo.getText().toString().trim();
                raiseNo = etRaiseNo.getText().toString().trim();
                intRo = etContext.getText().toString().trim();
                break;
        }

        String tal = etTal.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        Timestamp createDate = new Timestamp(new java.util.Date().getTime());

        int count = 0;
        String url = Common.URL + "UserServlet";

        if (Common.networkConnected(this)) {//傳送到server端
            if (idType == 1) {

                User user = new User(account, password, name, tal, email, address, idType, createDate);//傳送文字資料
                String imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);//圖片資料

                try {
                    count = new UserUpdateTask().execute(url, action, user, imageBase64).get();
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            } else if (idType == 2) {
                if (idType == 2) {
                    User user = new User(account, password, name, tal, email, address, idType, createDate);//傳送文字資料
                    String imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);//圖片資料

                    InstiutionBean ins = new InstiutionBean(account, leader, orgType, registerNo, raiseNo, intRo, createDate);
                    String imageBase64In = Base64.encodeToString(imageIn, Base64.DEFAULT);//圖片資料
                    try {
                        count = new UserUpdateTask().execute(url, action, user, imageBase64, ins, imageBase64In).get();
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }
            }
            if (count == 0) {
                Common.showToast(MemberRegisterActivity.this, R.string.msg_RegisterFail);
            } else if (count == -1) {
                Common.showToast(MemberRegisterActivity.this, R.string.msg_AccountRepeat);
            } else {
                Common.showToast(MemberRegisterActivity.this, R.string.msg_RegisterSuccess);
                finish();
            }
        } else {
            Common.showToast(this, R.string.msg_NoNetwork);
        }

    }


    public void onInstiutionClick(View view) {
        llInstiution.setVisibility(View.VISIBLE);
    }

    public void onPersonalClick(View view) {
        llInstiution.setVisibility(View.GONE);
    }

    public void onCancelClick(View view) {
        finish();
    }
}
//