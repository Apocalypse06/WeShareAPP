package com.example.ntut.weshare.member;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.example.ntut.weshare.R;

import java.io.ByteArrayOutputStream;


public class MamberRegisterOrgPageOne extends Fragment {

    View view;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.member_register_org_pageone_activityc, container, false);
        findViews();
        return view;
    }

    private void findViews() {
        ivUser = (ImageView) view.findViewById(R.id.ivUser);
        etAccount = (EditText) view.findViewById(R.id.etAccount);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        etName = (EditText) view.findViewById(R.id.etName);
        etTal = (EditText) view.findViewById(R.id.etTal);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etAddress = (EditText) view.findViewById(R.id.etAddress);
    }

    public void onPickPictureClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
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


}
