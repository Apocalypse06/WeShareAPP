package com.example.ntut.weshare.member;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.MainActivity;
import com.example.ntut.weshare.R;

import java.io.ByteArrayOutputStream;
import java.util.List;


public class MemberUpdateActivity extends AppCompatActivity {
    private final static String TAG = "UserUpdateActivity";


    private ImageView ivUser;
    private byte[] image = null;
    private EditText etOldPassword;
    private EditText etNewPassword1;
    private EditText etNewPassword2;
    private EditText etNumber;
    private EditText etEmail;
    private EditText etAddress;
    private Button btChange;
    private Button btCancel;
    private LinearLayout llchangePassword;


    String action;
    List<User> user = null;
    boolean changePassword = false;

    private static final int REQUEST_PICK_IMAGE_IN = 0;
    private static final int REQUEST_PICK_IMAGE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_uadate_fragment);
        findViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 從偏好設定檔中取得登入狀態來決定是否顯示「登出」
        SharedPreferences pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        boolean login = pref.getBoolean("login", false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    private void findViews() {
        ivUser = (ImageView) findViewById(R.id.ivUser);
        etOldPassword = (EditText) findViewById(R.id.etOldPassword);
        etNewPassword1 = (EditText) findViewById(R.id.etNewPassword1);
        etNewPassword2 = (EditText) findViewById(R.id.etNewPassword2);
        etNumber = (EditText) findViewById(R.id.etNumber);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etAddress = (EditText) findViewById(R.id.etAddress);
        btChange = (Button) findViewById(R.id.btChange);
        btCancel = (Button) findViewById(R.id.btCancel);
        btCancel.setVisibility(View.GONE);
        llchangePassword = (LinearLayout) findViewById(R.id.llchangePassword);
        llchangePassword.setVisibility(View.GONE);


        showAllSpots();
    }

    public void onChangeClick(View view) {
        llchangePassword.setVisibility(View.VISIBLE);
        btCancel.setVisibility(View.VISIBLE);
        btChange.setVisibility(View.GONE);
        changePassword = true;
    }

    public void onCancelChangeClick(View view) {
        llchangePassword.setVisibility(View.GONE);
        btChange.setVisibility(View.VISIBLE);
        btCancel.setVisibility(View.GONE);
        changePassword = false;
    }

    private void showAllSpots() {
        if (Common.networkConnected(this)) {//檢查網路
            String url = Common.URL + "UserServlet";
            SharedPreferences pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
            String account = pref.getString("user", "");
//            Toast.makeText(this, account, Toast.LENGTH_LONG).show();
//            List<User> user = null;
            int imageSize = 400;
            Bitmap bitmap = null;
            try {
                // passing null and calling get() means not to run FindImageByIdTask.onPostExecute()
//            bitmap = new UserGetImageTask(null).execute(url, account, imageSize).get();
                user = new UserGetAllTask().execute(url, account).get();//.get()要請UserGetAllTask()的執行結果回傳給我，會等他抓完資料(doInBackground的回傳結果)才會往下執行
                bitmap = new UserGetImageTask().execute(url, account, imageSize).get();//.execute(網址, 圖片id, 這邊做縮圖imageSize，在server端縮圖完再傳過來)
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (user == null) {
                Common.showToast(this, "不可能");
            } else {
                //rvSpots.setAdapter(new SpotsRecyclerViewAdapter(getActivity(), spots));//畫面RecyclerView(畫面,資料)，getActivity()取的他所依附的頁面(主頁面)
                ivUser.setImageBitmap(bitmap);
                etNumber.setText(user.get(0).getTal());
                etEmail.setText(user.get(0).getEmail());
                etAddress.setText(user.get(0).getAddress());
            }
        } else {
            Common.showToast(this, R.string.msg_NoNetwork);
        }
    }

    private void cleanPreferences() {
        SharedPreferences pref = getSharedPreferences(Common.PREF_FILE,
                MODE_PRIVATE);
        pref.edit()
                .putString("user", "")
                .putString("password", "")
                .putBoolean("login", false)
                .apply();
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
        String oldPassword = pref.getString("password", "");
        String oldPW = etOldPassword.getText().toString().trim();
        String NewPW = etNewPassword1.getText().toString().trim();
        String NewPW2 = etNewPassword2.getText().toString().trim();
        if (changePassword == true) {
            if (oldPW.length() <= 0) {
                Toast.makeText(this, R.string.msg_passwordNull,
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (!oldPW.equals(oldPassword)) {
                Toast.makeText(this, R.string.msg_PWNotRight,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (NewPW.length() <= 0) {
                Toast.makeText(this, R.string.msg_NewPasswordNull,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (NewPW2.length() <= 0) {
                Toast.makeText(this, R.string.msg_checkPasswordNull,
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (!NewPW.equals(NewPW2)) {
                Toast.makeText(this, R.string.msg_PWNotSame,
                        Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            NewPW = oldPassword;
        }

        //依選取項目顯示不同訊息
        if (user.get(0).getIdType() == 1) {
            action = "userUpdate";
        } else if (user.get(0).getIdType() == 2) {
            action = "userInUpdate";
        }
//        switch (rgType.getCheckedRadioButtonId()) {
//            case R.id.rbPersonal:
//                idType = 1;
//
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


        String tal = etNumber.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        int count = 0;
        String url = Common.URL + "UserServlet";

        if (Common.networkConnected(this)) {//傳送到server端
//            if (idType == 1) {
            account = pref.getString("user", "");
            User user = new User(account, NewPW, tal, email, address);//傳送文字資料
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
                Common.showToast(MemberUpdateActivity.this, R.string.msg_updateFail);
            } else {
                if (changePassword == true) {
                    Common.showToast(MemberUpdateActivity.this, R.string.msg_updateSuccessAndLogin);
                    cleanPreferences();
                    Intent updateIntent = new Intent();
                    updateIntent.setClass(MemberUpdateActivity.this, MemberLoginActivity.class);
                    startActivity(updateIntent);
                }
                else{
                    Common.showToast(MemberUpdateActivity.this, R.string.msg_updateSuccess);
                    finish();
                }
            }
        } else {
            Common.showToast(this, R.string.msg_NoNetwork);
        }

    }


    public void onCancelClick(View view) {
        finish();
    }
}
