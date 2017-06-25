package com.example.ntut.weshare.member;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.R;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Member;
import java.util.List;

public class MemberUpdateActivity extends AppCompatActivity {
    private final static String TAG = "UserInsertActivity";


    private ImageView ivUser;
    private byte[] image = new byte[0];
    //    private byte[] image = null;
    private EditText etOldPassword;
    private EditText etNewPassword1;
    private EditText etNewPassword2;
    private EditText etNumber;
    private EditText etEmail;
    private EditText etAddress;


    String action;
    List<User> user = null;
    User uu ;

    private static final int REQUEST_PICK_IMAGE_IN = 0;
    private static final int REQUEST_PICK_IMAGE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_uadate_fragment);
        findViews();
    }

    @Override
    public void onStart() {
        super.onStart();
        showAllSpots();
    }

    private void showAllSpots() {
        if (Common.networkConnected(this)) {//檢查網路
            String url = Common.URL + "UserServlet";
            SharedPreferences pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
            String account = pref.getString("user", "aaa");
            Toast.makeText(this, account, Toast.LENGTH_LONG).show();
//            List<User> user = null;
            int imageSize = 400;
            Bitmap bitmap = null;
            try {
                // passing null and calling get() means not to run FindImageByIdTask.onPostExecute()
//            bitmap = new UserGetImageTask(null).execute(url, account, imageSize).get();
                user = new UserGetAllTask().execute(url, account).get();//.get()要請UserGetAllTask()的執行結果回傳給我，會等他抓完資料(doInBackground的回傳結果)才會往下執行
           // uu = user.get(0);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (user == null) {
                Common.showToast(this, "不可能");
            } else {
                //rvSpots.setAdapter(new SpotsRecyclerViewAdapter(getActivity(), spots));//畫面RecyclerView(畫面,資料)，getActivity()取的他所依附的頁面(主頁面)
                etNumber.setText(user.get(0).getTal());
                etEmail.setText(user.get(0).getEmail());
                etAddress.setText(user.get(0).getAddress());
            }
        } else {
            Common.showToast(this, R.string.msg_NoNetwork);
        }
    }


    private void findViews() {
        ivUser = (ImageView) findViewById(R.id.ivUser);
        etOldPassword = (EditText) findViewById(R.id.etOldPassword);
        etNewPassword1 = (EditText) findViewById(R.id.etNewPassword1);
        etNewPassword2 = (EditText) findViewById(R.id.etNewPassword2);
        etNumber = (EditText) findViewById(R.id.etNumber);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etAddress = (EditText) findViewById(R.id.etAddress);


        //showAllSpots();
//        User user = (User) getIntent().getExtras().getSerializable("user");
//        if (user == null) {
//            Common.showToast(this, "sb");
//            finish();
//            return;
//        }


//        if (bitmap != null) {
//            ivSpot.setImageBitmap(bitmap);
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//            image = out.toByteArray();
//        } else {
//            ivSpot.setImageResource(R.drawable.default_image);
//        }
//        ivSpot.setImageBitmap(bitmap);
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
//                case REQUEST_PICK_IMAGE_IN:
//                    Uri uriIn = intent.getData();
//                    String[] columnsIn = {MediaStore.Images.Media.DATA};
//                    Cursor cursorIn = getContentResolver().query(uriIn, columnsIn, null, null, null);
//                    if (cursorIn != null && cursorIn.moveToFirst()) {
//                        String imagePathIn = cursorIn.getString(0);
//                        cursorIn.close();
//                        Bitmap bitmapIn = BitmapFactory.decodeFile(imagePathIn);
//                        ivIn.setImageBitmap(bitmapIn);
//                        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
//                        bitmapIn.compress(Bitmap.CompressFormat.JPEG, 100, out2);
//                        imageIn = out2.toByteArray();
//                    }
//                    break;
            }
        }
    }


    public void onUpdateClick(View view) {
        SharedPreferences pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        String account = pref.getString("account", "aaa");
        String oldPassword = pref.getString("password", "123");
        String oldPW = etOldPassword.getText().toString().trim();
        if (oldPW.length() <= 0) {
            Toast.makeText(this, R.string.msg_passwordNull,
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (!oldPW.equals(oldPassword)) {
            Toast.makeText(this, R.string.msg_PWNotRight,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String NewPW = etNewPassword1.getText().toString().trim();
        if (NewPW.length() <= 0) {
            Toast.makeText(this, R.string.msg_passwordNull,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String NewPW2 = etNewPassword2.getText().toString().trim();
        if (NewPW2.length() <= 0) {
            Toast.makeText(this, R.string.msg_passwordNull,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!NewPW.equals(NewPW2)) {
            Toast.makeText(this, R.string.msg_PWNotSame,
                    Toast.LENGTH_SHORT).show();
        }

//        if (image == null) {
//            Common.showToast(this, "沒有圖片");
//            return;
//        }

        //依選取項目顯示不同訊息
//        switch (rgType.getCheckedRadioButtonId()) {
//            case R.id.rbPersonal:
//                idType = 1;
//                action = "userRegister";
//                break;
//            case R.id.rbInstiution:
//                idType = 2;
//                action = "userInRegister";
//                leader = etLeader.getText().toString().trim();
//                orgType = etInType.getText().toString().trim();
//                registerNo = etRegisterNo.getText().toString().trim();
//                raiseNo = etRaiseNo.getText().toString().trim();
//                intRo = etContext.getText().toString().trim();
//                break;
//        }


        action = "userUpdate";//暫時

        String tal = etNumber.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        int count = 0;
        String url = Common.URL + "UserServlet";

        if (Common.networkConnected(this)) {//傳送到server端
//            if (idType == 1) {

            User user = new User(account, oldPW, tal, email, address);//傳送文字資料
            String imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);//圖片資料
            try {
                count = new UserUpdateTask().execute(url, action, user, imageBase64).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
//            }
//            else if (idType == 2) {
//                if (idType == 2) {
//                    User user = new User(account, password, name, tal, email, address, idType, createDate);//傳送文字資料
//                    String imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);//圖片資料
//
//                    InstiutionBean ins = new InstiutionBean(account, leader, orgType, registerNo, raiseNo, intRo, createDate);
//                    String imageBase64In = Base64.encodeToString(imageIn, Base64.DEFAULT);//圖片資料
//                    try {
//                        count = new UserUpdateTask().execute(url, action, user, imageBase64, ins, imageBase64In).get();
//                    } catch (Exception e) {
//                        Log.e(TAG, e.toString());
//                    }
//                }
//            }
            if (count == 0) {
                Common.showToast(MemberUpdateActivity.this, R.string.msg_RegisterFail);
            } else if (count == -1) {
                Common.showToast(MemberUpdateActivity.this, R.string.msg_AccountRepeat);
            } else {
                Common.showToast(MemberUpdateActivity.this, R.string.msg_RegisterSuccess);
                finish();
            }
        } else {
            Common.showToast(this, R.string.msg_NoNetwork);
        }

    }


    public void onCancelClick(View view) {
        finish();
    }
}
//