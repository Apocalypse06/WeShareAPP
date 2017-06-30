package com.example.ntut.weshare.member;

import android.app.Activity;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.ntut.weshare.R;

import java.io.ByteArrayOutputStream;


public class MamberRegisterOrgPageTwo extends Fragment {

    View view;

    private ImageView ivOrg;
    private byte[] imageOrg = null;
    private EditText etLeader;
    private Spinner spOrgType;
    private EditText etRegisterNo;
    private EditText etRaiseNo;
    private EditText etContext;

    int orgType;

    private static final int REQUEST_PICK_IMAGEORG = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //member = (Member) getArguments().getSerializable("member");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.member_register_org_pagetwo_activityc, container, false);
        findViews();
        return view;
    }

    private void findViews() {
        ivOrg = (ImageView) view.findViewById(R.id.ivOrg);
        etLeader = (EditText) view.findViewById(R.id.etLeader);
        spOrgType = (Spinner) view.findViewById(R.id.spOrgType);
        etRegisterNo = (EditText) view.findViewById(R.id.etRegisterNo);
        etRaiseNo = (EditText) view.findViewById(R.id.etRaiseNo);
        etContext = (EditText) view.findViewById(R.id.etContext);


        final String[] classes = {"兒少福利", "偏鄉教育", "老人福利", "身障福利", "其他"};
        ArrayAdapter<String> classList = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                classes);
        spOrgType.setAdapter(classList);


        ivOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_PICK_IMAGEORG);
            }
        });
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
    public static Context contextOfApplication;

    public static Context getContextOfApplication()//要使用getContentResolver()
    {
        return contextOfApplication;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK_IMAGEORG:
                    Uri uri = intent.getData();
                    String[] columns = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(uri, columns, null, null, null);
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
}
