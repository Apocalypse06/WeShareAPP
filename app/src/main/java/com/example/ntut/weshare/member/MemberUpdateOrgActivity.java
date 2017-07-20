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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.MainActivity;
import com.example.ntut.weshare.R;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.List;


public class MemberUpdateOrgActivity extends AppCompatActivity {
    private final static String TAG = "UserUpdateOrgActivity";

    private ImageView ivOrg;
    private byte[] imageOrg = null;
    private EditText etLeader;
    private Spinner spOrgType;
    private EditText etRegisterNo;
    private EditText etRaiseNo;
    private EditText etIntRo;

    String action;
    List<User> userOld = null;
    List<InstiutionBean> orgOld = null;
    int orgType;
    private static final int REQUEST_PICK_IMAGEOrg = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_uadate_org_activity);
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
        ivOrg = (ImageView) findViewById(R.id.ivOrg);
        etLeader = (EditText) findViewById(R.id.etLeader);
        spOrgType = (Spinner) findViewById(R.id.spOrgType);
        etRegisterNo = (EditText) findViewById(R.id.etRegisterNo);
        etRaiseNo = (EditText) findViewById(R.id.etRaiseNo);
        etIntRo = (EditText) findViewById(R.id.etIntRo);

        final String[] classes = {"兒少福利", "偏鄉教育", "老人福利", "身障福利", "其他"};
        ArrayAdapter<String> classList = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                classes);
        spOrgType.setAdapter(classList);

        showAllSpots();
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
                //userOld = new UserGetAllTask().execute(url, account).get();
                orgOld = new UserGetAllOrgTask().execute(url, account).get();//.get()要請UserGetAllTask()的執行結果回傳給我，會等他抓完資料(doInBackground的回傳結果)才會往下執行
                bitmap = new UserGetOrgImageTask().execute(url, account, imageSize).get();//.execute(網址, 圖片id, 這邊做縮圖imageSize，在server端縮圖完再傳過來)
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (orgOld == null) {
                Common.showToast(this, "不可能");
            } else {
                //rvSpots.setAdapter(new SpotsRecyclerViewAdapter(getActivity(), spots));//畫面RecyclerView(畫面,資料)，getActivity()取的他所依附的頁面(主頁面)

                ivOrg.setImageBitmap(bitmap);

                ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out2);
                imageOrg = out2.toByteArray();

                etLeader.setText(orgOld.get(0).getLeader());
                etRegisterNo.setText(orgOld.get(0).getRegisterNo());
                etRaiseNo.setText(orgOld.get(0).getRaiseNo());
                etIntRo.setText(orgOld.get(0).getIntRo());

                setOreType();
            }
        } else {
            Common.showToast(this, R.string.msg_NoNetwork);
        }
    }


    private void setOreType() {
        switch (orgOld.get(0).getOrgType()) {
            case 1:
                spOrgType.setSelection(0);
                break;
            case 2:
                spOrgType.setSelection(1);
                break;
            case 3:
                spOrgType.setSelection(2);
                break;
            case 4:
                spOrgType.setSelection(3);
                break;
            case 5:
                spOrgType.setSelection(4);
                break;
        }
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

    public void onPickPictureClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGEOrg);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK_IMAGEOrg:
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


    public void onUpdateClick(View view) {
        SharedPreferences pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        String account = pref.getString("user", "");


        String leader = etLeader.getText().toString().trim();
        String registerNo = etRegisterNo.getText().toString().trim();
        String raiseNo = etRaiseNo.getText().toString().trim();
        String intRo = etIntRo.getText().toString().trim();

        if (leader.length() <= 0) {
            Toast.makeText(this, "負責人不可以空白",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (intRo.length() <= 0) {
            Toast.makeText(this, "簡介不可空白",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        ChoseOreType();
        Timestamp updatetime = new Timestamp(new java.util.Date().getTime());

        InstiutionBean org = new InstiutionBean(account, leader, orgType, registerNo, raiseNo, intRo, updatetime);//傳送文字資料

        int count = 0;
        String url = Common.URL + "UserServlet";
        String action = "updateOrg";
        if (Common.networkConnected(this)) {//傳送到server端
            String imageBase64Org = Base64.encodeToString(imageOrg, Base64.DEFAULT);//圖片資料
            try {
                count = new UserUpdateTask().execute(url, action, org, imageBase64Org).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (count == 0) {
                Common.showToast(MemberUpdateOrgActivity.this, R.string.msg_updateFail);
            } else {
                Common.showToast(MemberUpdateOrgActivity.this, R.string.msg_updateSuccessAndLogin);
                cleanPreferences();
                Intent updateIntent = new Intent();
                updateIntent.setClass(MemberUpdateOrgActivity.this, MemberLoginActivity.class);
                startActivity(updateIntent);
            }
        } else

        {
            Common.showToast(this, R.string.msg_NoNetwork);
        }

    }


    public void onCancelClick(View view) {
        finish();
    }

    private void cleanPreferences() {
        SharedPreferences pref = getSharedPreferences(Common.PREF_FILE,
                MODE_PRIVATE);
        pref.edit()
                .putString("user", "")
                .putString("password", "")
                .putString("name", "")
                .putBoolean("login", false)
                .apply();
    }
}