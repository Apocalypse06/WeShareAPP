package com.example.ntut.weshare.feedback;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.MainActivity;
import com.example.ntut.weshare.R;
import com.example.ntut.weshare.message.MessageBean;
import com.example.ntut.weshare.message.SendMsgTask;

import java.io.ByteArrayOutputStream;

import static com.example.ntut.weshare.R.drawable.picture_icon;


public class FeedbackFragment1 extends Fragment {
    private static final String TAG = "FeedbackFragment1";
    View view;
    private TextView tvFrom;
    private TextView tvPicTx;
    private Spinner spClass;
    private ImageView ivAddPic;
    private LinearLayout llAddPicBt;
    private EditText etFeedback;
    private Button btCommit;
    private byte[] image;
    private String gm;
    String user = "";
    private static final int REQUEST_PICK_IMAGE = 1;


    public void onResume() {
        super.onResume();
        // 從偏好設定檔中取得登入狀態來決定是否顯示「登出」
        SharedPreferences pref = this.getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
        user = pref.getString("user", "");
        if (user == "") {
            Toast.makeText(this.getActivity(), "註冊登入WeShare後才能進行意見回饋喔",
                    Toast.LENGTH_SHORT).show();
            getActivity().finish();
            Intent MainIntent = new Intent(getActivity(), MainActivity.class);
            startActivity(MainIntent);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.feedback_fragment, container, false);
        findView();

//新增圖片
        llAddPicBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_PICK_IMAGE);
            }
        });

//會員名稱(寄件人)載入
        SharedPreferences pref = this.getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
        user = pref.getString("user", "");
        tvFrom.setText("寄件人 ： " + user);

//意見類型
        final String[] classes = {"請選擇您的意見類別", "錯誤回報", "使用疑問", "交易疑問", "帳號疑問", "功能建議"};
        ArrayAdapter<String> stateList = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, classes);
        spClass.setAdapter(stateList);


//下面這五個是gm的帳號記得要先新增才能有傳送對象
//        gm_bug
//        gm_tech
//        gm_trade
//        gm_account
//        gm_suggestion

        spClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                              @Override
                                              public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                  switch (spClass.getSelectedItem().toString()) {
                                                      case "請選擇您的意見類別":
                                                          gm="";
                                                          break;
                                                      case "錯誤回報":
                                                          gm = "gm_bug";
                                                          break;
                                                      case "使用疑問":
                                                          gm = "gm_tech";
                                                          break;
                                                      case "交易疑問":
                                                          gm = "gm_trade";
                                                          break;
                                                      case "帳號疑問":
                                                          gm = "gm_account";
                                                          break;
                                                      case "功能建議":
                                                          gm = "gm_suggestion";
                                                          break;
                                                  }
                                              }

                                              @Override
                                              public void onNothingSelected(AdapterView<?> adapterView) {
                                              }
                                          }
        );

        btCommit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(etFeedback.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "請輸入意見後再送出",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(etFeedback.getText().length()>200){
                    Toast.makeText(getActivity(), "單筆意見不可超過200個字元",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(gm.equals("")){
                    Toast.makeText(getActivity(), "請選擇意見類別",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(user.equals("")||user.isEmpty()){
                    Toast.makeText(getActivity(), "註冊登入WeShare後才能進行意見回饋喔",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String text=etFeedback.getText().toString().trim();
                int count = 0;
                String url = Common.URL + "MsgServlet";
                String action = "sendMsg";
                MessageBean msgg = null;
                if (Common.networkConnected(getActivity())) {
                msgg = new MessageBean(1, 1, user, gm, text, 1);
                    try {
                        if (image != null) {
                            msgg.setMsgFileName("MsgFileName");
                            String imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);//圖片資料
                            count = new SendMsgTask().execute(url, action, msgg, imageBase64).get();
                        } else {
                            count = new SendMsgTask().execute(url, action, msgg, null).get();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    if (count == 0) {
                        Common.showToast(getActivity(), R.string.msg_SendFail);
                    }else{Common.showToast(getActivity(), R.string.msg_SendSuccess1);
                        etFeedback.setText("");
                        spClass.setSelection(0);
                        ivAddPic.setImageBitmap(null);
                        tvPicTx.setVisibility(View.VISIBLE);
                        ivAddPic.setImageResource(R.drawable.picture_icon);
                        image=null;
                    }
                }else {
                        Common.showToast(getActivity(), R.string.msg_NoNetwork);
                    }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK_IMAGE:
                    Uri uri = intent.getData();
                    String[] columns = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(uri, columns,
                            null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        String imagePath = cursor.getString(0);
                        cursor.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                        ivAddPic.setImageBitmap(bitmap);
                        tvPicTx.setVisibility(View.GONE);
                        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out2);
                        image = out2.toByteArray();
                    }
                    break;
            }
        }

    }


    public void findView() {
        tvFrom = (TextView) view.findViewById(R.id.tv_fbFrom);
        tvPicTx = (TextView) view.findViewById(R.id.tv_fbAddPicTx);
        spClass = (Spinner) view.findViewById(R.id.sp_fbClass);
        ivAddPic = (ImageView) view.findViewById(R.id.iv_fbAddPic);
        llAddPicBt = (LinearLayout) view.findViewById(R.id.ll_fbAddPic);
        etFeedback = (EditText) view.findViewById(R.id.et_fbText);
        btCommit = (Button) view.findViewById(R.id.bt_fbCommit);


    }
}
