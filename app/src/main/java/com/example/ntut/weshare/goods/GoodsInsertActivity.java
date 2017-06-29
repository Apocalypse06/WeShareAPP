package com.example.ntut.weshare.goods;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.MainActivity;
import com.example.ntut.weshare.R;
import com.example.ntut.weshare.icon.InstitutionkFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;


public class GoodsInsertActivity extends AppCompatActivity {
    private final static String TAG = "GoodsInsertActivity";
    private EditText etName;
    private EditText etQty;
    private EditText etComment;
    private Spinner spClass;
    private int intspClass;
    private Spinner spLoc;
    private int intspLoc;
    private Spinner spDlvWay;
    private int intspDlvWay;
    private ImageView ivImage;
    private int mYear,mMonth,mDay;
    private long deadlinedate;
    private  byte[] image;
    private String fileName;
    private File file;
    private Calendar cld;
    Timestamp now= new Timestamp(System.currentTimeMillis());
    private static final int REQUEST_TAKE_PICTURE = 0;
    private static final int REQUEST_PICK_IMAGE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_new_fragment);

        spClass=(Spinner) findViewById(R.id.sp_class);
        final String[] classes = {"食","衣","住","行","育","樂"};
        ArrayAdapter<String> classList = new ArrayAdapter<>(GoodsInsertActivity.this,
                android.R.layout.simple_spinner_dropdown_item, classes);
        spClass.setAdapter(classList);


        spLoc=(Spinner) findViewById(R.id.sp_loc);
        final String[] loc = {"北","中","南"};
        ArrayAdapter<String> locList = new ArrayAdapter<>(GoodsInsertActivity.this,
                android.R.layout.simple_spinner_dropdown_item, loc);
        spLoc.setAdapter(locList);
        findViews();

        spDlvWay=(Spinner) findViewById(R.id.sp_dlvWay);
        final  String[] dlv={"面交","寄送","面交寄送皆可"};
        ArrayAdapter<String> dlvList = new ArrayAdapter<>(GoodsInsertActivity.this,
                android.R.layout.simple_spinner_dropdown_item, dlv);
        spDlvWay.setAdapter(dlvList);
        findViews();


        final Button backButton = (Button)findViewById(R.id.bt_BackGBList);
        final Button dateButton = (Button)findViewById(R.id.bt_date);
        dateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(GoodsInsertActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String format = setDateFormat(year, month, day);
//                        tvDate.setText(format);
                        dateButton.setText(format);
                        cld=c;
                        cld.set(year,month,day);
                        long nowtime=c.getTime().getTime();
                        deadlinedate=cld.getTime().getTime();
                        Log.e("d",""+cld.getTime().getTime());
                        if(deadlinedate<=nowtime){
                            

                        }
                    }
                }, mYear,mMonth, mDay).show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new GoodsListFragment();
                switchFragment(fragment);
            }
        });

    }
    private String setDateFormat(int year,int monthOfYear,int dayOfMonth){
        return String.valueOf(year) + "-"
                + String.valueOf(monthOfYear + 1) + "-"
                + String.valueOf(dayOfMonth);
    }

    private void findViews() {
        ivImage = (ImageView) findViewById(R.id.iv_image);
        etName = (EditText) findViewById(R.id.et_goodsname);
        etQty = (EditText) findViewById(R.id.et_qty);
        etComment = (EditText) findViewById(R.id.et_comment);


    }
//    public void onTakePictureClick(View view) {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        file = Environment
//                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        file = new File(file, "picture.jpg");
//        Uri contentUri = FileProvider.getUriForFile(
//                this, getPackageName() + ".provider", file);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
//        if (isIntentAvailable(this, intent)) {
//            startActivityForResult(intent, REQUEST_TAKE_PICTURE);
//        } else {
//            Toast.makeText(this, R.string.msg_NoCameraAppsFound,
//                    Toast.LENGTH_SHORT).show();
//        }
//    }
//    private boolean isIntentAvailable(Context context, Intent intent) {
//        PackageManager packageManager = context.getPackageManager();
//        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
//                PackageManager.MATCH_DEFAULT_ONLY);
//        return list.size() > 0;
//    }

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
//                case REQUEST_TAKE_PICTURE:
//                    Bitmap picture = BitmapFactory.decodeFile(file.getPath());
//                    ivImage.setImageBitmap(picture);
//                    ByteArrayOutputStream out1 = new ByteArrayOutputStream();
//                    picture.compress(Bitmap.CompressFormat.JPEG, 100, out1);
//                    image = out1.toByteArray();
//                    break;
                case REQUEST_PICK_IMAGE:
                    Uri uri = intent.getData();
                    String[] columns = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, columns,
                            null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        String imagePath = cursor.getString(0);
                        cursor.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                        ivImage.setImageBitmap(bitmap);
                        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out2);
                        image = out2.toByteArray();
                    }
                    break;
            }
        }
    }
    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.body, fragment);
        fragmentTransaction.commit();
    }
    public void onNewGoodsResult(View view) {
        //物品名稱驗證
        String name = etName.getText().toString().trim();
        if (name.length() <= 0) {
            Toast.makeText(this, R.string.msg_NameIsInvalid,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        //數量驗證
        if (etQty.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, R.string.msg_QtyIsInvalid,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            int qtyParseInt = Integer.parseInt(etQty.getText().toString().trim());
        }catch (Exception e){
            Toast.makeText(this, R.string.msg_QtyIncorrect,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        //日期選擇驗證
        final Button dateButton = (Button)findViewById(R.id.bt_date);
        if(dateButton.getText().equals("日期選擇")){
            Toast.makeText(this, R.string.msg_DateIsInvalid,
                    Toast.LENGTH_SHORT).show();
            return;
        }


        //相片驗證
    if (image == null) {
        Common.showToast(this, R.string.msg_NoImage);
        return;
    }



        if (Common.networkConnected(this)) {
            int qtyParseInt=Integer.parseInt(etQty.getText().toString().trim());
            String comment=etComment.getText().toString().trim();
            String goodName=etName.getText().toString().trim();

            //判斷依照地區給予int值
            switch (spLoc.getSelectedItem().toString()) {
                case "北":
                    intspLoc = 1;
                    break;
                case "中":
                    intspLoc = 2;
                    break;
                case "南":
                    intspLoc = 3;
                    break;
                default:
                    intspLoc=0;
            }

            //判斷依照物品種類給予int值
            switch (spClass.getSelectedItem().toString()){
                case "食":
                    intspClass=1;
                    break;
                case "衣":
                    intspClass=2;
                    break;
                case "住":
                    intspClass=3;
                    break;
                case "行":
                    intspClass=4;
                    break;
                case "育":
                    intspClass=5;
                    break;
                case "樂":
                    intspClass=6;
                    break;
            }

            switch (spDlvWay.getSelectedItem().toString()) {
                case "面交":
                    intspDlvWay = 1;
                    break;
                case "寄送":
                    intspDlvWay = 2;
                    break;
                case "面交寄送皆可":
                    intspDlvWay = 3;
                    break;
            }

            String url = Common.URL + "GoodsServlet";
            Goods goods = new Goods(1, 1, now, "kitty",goodName ,intspClass,
                    qtyParseInt, intspLoc, comment, intspDlvWay, deadlinedate);
            String imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);
            String action = "goodsInsert";
            int count = 0;
            try {
                count = new GoodsUpdateTask().execute(url, action, goods, imageBase64).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (count == 0) {
                Common.showToast(GoodsInsertActivity.this, R.string.msg_InsertFail);
            } else {
                Common.showToast(GoodsInsertActivity.this, R.string.msg_InsertSuccess);
            }
        } else {
            Common.showToast(this, R.string.msg_NoNetwork);
        }
       finish();

    }
}








