package com.example.ntut.weshare.member;

import android.app.Activity;
import android.content.Context;
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
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.R;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;


public class MamberRegisterOrgPageOne extends Fragment {

    View view;
    User user;
    private ImageView ivUser;
    private ImageView ivHelp;
    private byte[] image = null;
    private EditText etAccount;
    private EditText etPassword;
    private EditText etName;
    private EditText etTal;
    private EditText etEmail;
    private EditText etAddress;
    private Button btTest;
    //    private Button btEnter;
    int idType = 2;
    String action = "userRegister";

    private static final int REQUEST_PICK_IMAGE = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //
    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            //String OK = getArguments().getString("Enter");
            Common.showToast(getActivity(), "OK");
        } else {
            Common.showToast(getActivity(), "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.member_register_org_pageone_activityc, container, false);
        findViews();
        return view;
    }

    private void findViews() {
        ivUser = (ImageView) view.findViewById(R.id.ivUser);
        ivHelp = (ImageView) view.findViewById(R.id.ivHelp);
        etAccount = (EditText) view.findViewById(R.id.etAccount);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        etName = (EditText) view.findViewById(R.id.etName);
        etTal = (EditText) view.findViewById(R.id.etTal);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etAddress = (EditText) view.findViewById(R.id.etAddress);

       // btTest = (Button) view.findViewById(R.id.btTest);
//        btEnter = (Button)  view.findViewById(R.id.btEnter);

        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_PICK_IMAGE);
            }
        });

        ivHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                etAccount.setText("jackOrg2");
//                etPassword.setText("123");
//                etName.setText("林巧琳");
//                etTal.setText("0955847657");
//                etEmail.setText("jacktyl@gmail.com");
//                etAddress.setText("南投縣中山路山明區");
                test();
            }
        });
    }

    public void test1() {
//        Toast.makeText(getActivity(), "傳送完畢",
//                Toast.LENGTH_SHORT).show();
//        Log.i("MyFragment", "去吧皮卡丘");
    }

    public void test() {
        String account = etAccount.getText().toString().trim();
        if (account.length() <= 0) {
            Toast.makeText(getActivity(), R.string.msg_accountNull,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String password = etPassword.getText().toString().trim();
        if (password.length() <= 0) {
            Toast.makeText(getActivity(), R.string.msg_passwordNull,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String name = etName.getText().toString().trim();
        if (password.length() <= 0) {
            Toast.makeText(getActivity(), R.string.msg_nameNull,
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

        user = new User(account, password, name, tal, email, address, idType, createDate);//傳送文字資料
        String imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);//圖片資料


        Intent updateIntent = new Intent(getActivity(),
                MemberRegisterOrgActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("userBean", user);
        bundle.putSerializable("imageBase64", imageBase64);
        updateIntent.putExtras(bundle);
        startActivity(updateIntent);
        Toast.makeText(getActivity(), "傳送完畢",
                Toast.LENGTH_SHORT).show();

    }


//    public static Context contextOfApplication;
//
//    public static Context getContextOfApplication()//要使用getContentResolver()
//    {
//        return contextOfApplication;
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK_IMAGE:
                    Uri uri = intent.getData();
                    String[] columns = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(uri, columns, null, null, null);
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


}
