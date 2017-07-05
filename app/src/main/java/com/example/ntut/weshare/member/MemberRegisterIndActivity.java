package com.example.ntut.weshare.member;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.MainActivity;
import com.example.ntut.weshare.R;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;

public class MemberRegisterIndActivity extends AppCompatActivity {
    private final static String TAG = "UserInsertActivity";

    private ImageView ivUser;
    private byte[] image = null;
    private EditText etAccount;
    private EditText etPassword;
    private EditText etName;
    private EditText etTal;
    private EditText etEmail;
    private EditText etAddress;
    private RadioGroup rgType;
    private LinearLayout llInstiution;
    int idType = 1;
    String action = "userRegister";

    private static final int REQUEST_PICK_IMAGE = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_register_ind_activityc);
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
    }


    public void onPickPictureClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
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
        if (image == null) {
            Resources res = getResources();
            Drawable drawable = res.getDrawable(R.drawable.member_default);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            image = stream.toByteArray();

        }

        String tal = etTal.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        Timestamp createDate = new Timestamp(new java.util.Date().getTime());

        int count = 0;
        String url = Common.URL + "UserServlet";

        if (Common.networkConnected(this)) {//傳送到server端
            User user = new User(account, password, name, tal, email, address, idType, createDate);//傳送文字資料
            String imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);//圖片資料
            try {
                count = new UserUpdateTask().execute(url, action, user, imageBase64).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

            if (count == 0) {
                Common.showToast(MemberRegisterIndActivity.this, R.string.msg_RegisterFail);
            } else if (count == -1) {
                Common.showToast(MemberRegisterIndActivity.this, R.string.msg_AccountRepeat);
            } else {
                Common.showToast(MemberRegisterIndActivity.this, R.string.msg_RegisterSuccess);
                finish();
            }
        } else {
            Common.showToast(this, R.string.msg_NoNetwork);
        }

    }

    public void onCancelClick(View view) {
        finish();
    }

    public void onHelpClick(View view) {
        etAccount.setText("jack");
        etPassword.setText("123");
        etName.setText("王大明");
        etTal.setText("0226227777");
        etEmail.setText("jack@yahoo.com.tw");
        etAddress.setText("新北市北投區");
    }
}
//