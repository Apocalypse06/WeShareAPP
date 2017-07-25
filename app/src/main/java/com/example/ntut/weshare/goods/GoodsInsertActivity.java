package com.example.ntut.weshare.goods;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Timestamp;
import java.util.Calendar;

import com.example.ntut.weshare.goods.GoodsInsertChangeColorTask;


public class GoodsInsertActivity extends AppCompatActivity {
    private final static String TAG = "GoodsInsertActivity";
    private EditText etName;
    private EditText etQty;
    private EditText etComment;
    private Spinner spClass;
    private int intspClass;
    private Spinner spLoc;
    private int intspLoc;
    private Spinner spState;
    private int intspState;
    private Spinner spDlvWay;
    private int intspDlvWay;
    private ImageView ivImage;
    private int mYear, mMonth, mDay;
    private long deadlinedate;
    private byte[] image;
    private String fileName;
    private File file;
    private Calendar cld;
    private TextView tvname;
    private TextView tvqty;
    private TextView tvtype;
    private TextView tvclass;
    private TextView tvloc;
    private TextView tvdlv;
    private TextView tvdate;
    private TextView tvnote;
    private ScrollView sv;
    private String[] loc2;
    private LinearLayout lnlt;
    private LinearLayout wish1;
    private LinearLayout wish2;
    private LinearLayout give1;
    private int countLoc;
    private Timestamp now = new Timestamp(System.currentTimeMillis());
    private static final int REQUEST_PICK_IMAGE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_new_fragment);
        findViews();


        spState = (Spinner) findViewById(R.id.sp_state);
        final String[] states = {"許願池(募資)", "送愛心(捐贈)", "以物易物"};
        ArrayAdapter<String> stateList = new ArrayAdapter<>(GoodsInsertActivity.this,
                android.R.layout.simple_spinner_dropdown_item, states);
        spState.setAdapter(stateList);
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                              @Override
                                              public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                  switch (spState.getSelectedItem().toString()) {
                                                      case "許願池(募資)":
                                                          colorChange(255, 151, 151);
                                                          break;
                                                      case "送愛心(捐贈)":
                                                          colorChange(255, 119, 68);
                                                          break;
                                                      case "以物易物":
                                                          colorChange(1, 180, 104);
                                                          break;
                                                  }
                                              }

                                              @Override
                                              public void onNothingSelected(AdapterView<?> adapterView) {
                                                  colorChange(255, 255, 255);
                                              }
                                          }
        );

        spClass = (Spinner) findViewById(R.id.sp_class);
        final String[] classes = {"食品", "服飾配件", "生活用品", "家電機器", "其他"};
        ArrayAdapter<String> classList = new ArrayAdapter<>(GoodsInsertActivity.this,
                android.R.layout.simple_spinner_dropdown_item, classes);
        spClass.setAdapter(classList);


        spLoc = (Spinner) findViewById(R.id.sp_loc);
        final String[] loc = {"苗栗縣","桃園市","基隆市","新北市","新竹市","新竹縣","臺北市","南投縣","雲林縣","嘉義市","嘉義縣","彰化縣"
                ,"臺中市","屏東縣","高雄市","臺南市","宜蘭縣","花蓮縣","臺東縣","金門縣","連江縣","澎湖縣"};
        ArrayAdapter<String> locList = new ArrayAdapter<>(GoodsInsertActivity.this,
                android.R.layout.simple_spinner_dropdown_item, loc);
        spLoc.setAdapter(locList);
        loc2=loc;

        spDlvWay = (Spinner) findViewById(R.id.sp_dlvWay);
        final String[] dlv = {"面交", "寄送", "面交寄送皆可"};
        ArrayAdapter<String> dlvList = new ArrayAdapter<>(GoodsInsertActivity.this,
                android.R.layout.simple_spinner_dropdown_item, dlv);
        spDlvWay.setAdapter(dlvList);
        findViews();


        final Button backButton = (Button) findViewById(R.id.bt_BackGBList);
        final Button dateButton = (Button) findViewById(R.id.bt_date);

        dateButton.setOnClickListener(new View.OnClickListener() {
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
                        cld = c;
                        cld.set(year, month, day);
                        long nowtime = c.getTime().getTime();
                        deadlinedate = cld.getTime().getTime();
                        Log.e("d", "" + cld.getTime().getTime());

                        if (deadlinedate <= nowtime) {

                        }
                    }
                }, mYear, mMonth, mDay).show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

    }

    private String setDateFormat(int year, int monthOfYear, int dayOfMonth) {
        return String.valueOf(year) + "-"
                + String.valueOf(monthOfYear + 1) + "-"
                + String.valueOf(dayOfMonth);
    }

    private void findViews() {
        ivImage = (ImageView) findViewById(R.id.iv_image);
        etName = (EditText) findViewById(R.id.et_goodsname);
        etQty = (EditText) findViewById(R.id.et_qty);
        etComment = (EditText) findViewById(R.id.et_comment);
        tvname = (TextView) findViewById(R.id.textView9);
        tvqty = (TextView) findViewById(R.id.textView10);
        tvtype = (TextView) findViewById(R.id.txgoodstate);
        tvclass = (TextView) findViewById(R.id.textView11);
        tvloc = (TextView) findViewById(R.id.textView12);
        tvdlv = (TextView) findViewById(R.id.textView15);
        tvdate = (TextView) findViewById(R.id.textView13);
        tvnote = (TextView) findViewById(R.id.textView14);
        sv = (ScrollView) findViewById(R.id.sv_newgood);
        lnlt = (LinearLayout) findViewById(R.id.newback);
        wish1= (LinearLayout) findViewById(R.id.help_wish1);
        wish2= (LinearLayout) findViewById(R.id.help_wish2);
        give1= (LinearLayout) findViewById(R.id.help_give1);
//        sv=(ScrollView)findViewById(R.id.sv_newgood);
//        sv.fullScroll(View.FOCUS_DOWN);
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
        } catch (Exception e) {
            Toast.makeText(this, R.string.msg_QtyIncorrect,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        //日期選擇驗證
        final Button dateButton = (Button) findViewById(R.id.bt_date);
        if (dateButton.getText().equals("日期選擇")) {
            Toast.makeText(this, R.string.msg_DateIsInvalid,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        //日期驗證
        if(deadlinedate<now.getTime()){
            Common.showToast(GoodsInsertActivity.this, R.string.msg_DeadLineIsInvalid);
            return;
        }


        //相片驗證
        if (image == null) {
            Common.showToast(this, R.string.msg_NoImage);
            return;
        }


        if (Common.networkConnected(this)) {
            int qtyParseInt = Integer.parseInt(etQty.getText().toString().trim());
            String comment = etComment.getText().toString().trim();
            String goodName = etName.getText().toString().trim();
            SharedPreferences pref = this.getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
            String user = pref.getString("user", "");

            //判斷依照地區給予int值

            switch (spState.getSelectedItem().toString()) {
                case "許願池(募資)":
                    intspState = 1;
                    break;
                case "送愛心(捐贈)":
                    intspState = 2;
                    break;
                case "以物易物":
                    intspState = 3;
                    break;
            }


            for( int n=0; n<loc2.length;n++){
                if (spLoc.getSelectedItem().toString().equals(loc2[n].toString())){
                    countLoc=n+1;
                    break;
                }
            }
//            switch (spLoc.getSelectedItem().toString()) {
//                case "北":
//                    intspLoc = 1;
//                    break;
//                case "中":
//                    intspLoc = 2;
//                    break;
//                case "南":
//                    intspLoc = 3;
//                    break;
//                default:
//                    intspLoc = 0;
//            }


            //判斷依照物品種類給予int值

            switch (spClass.getSelectedItem().toString()) {
                case "食品":
                    intspClass = 1;
                    break;
                case "服飾配件":
                    intspClass = 2;
                    break;
                case "生活用品":
                    intspClass = 3;
                    break;
                case "家電機器":
                    intspClass = 4;
                    break;
                case "其他":
                    intspClass = 5;
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
            Goods goods = new Goods(1, intspState, now, user, goodName, intspClass,
                    qtyParseInt, countLoc, comment, intspDlvWay, deadlinedate, "goodsfilename");
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

    public void onHelpWish1(View view){
        etName.setText("玩偶");
        etQty.setText("10");
        spState.setSelection(0);
        spClass.setSelection(4);
        spLoc.setSelection(3);
        spDlvWay.setSelection(2);
        etComment.setText("希望有人能夠提供玩偶陪伴需要的小孩子們。");
    }
    public void onHelpWish2(View view){
        etName.setText("衣服");
        etQty.setText("20");
        spState.setSelection(0);
        spClass.setSelection(1);
        spLoc.setSelection(3);
        spDlvWay.setSelection(2);
        etComment.setText("最近天氣越來越冷希望能有好心人提供禦寒衣物。");
    }
    public void onHelpGive1(View view){
        etName.setText("包包");
        etQty.setText("5");
        spState.setSelection(1);
        spClass.setSelection(1);
        spLoc.setSelection(3);
        spDlvWay.setSelection(2);
        etComment.setText("多的包包。");
    }
    public int colorChange(int r, int g, int b) {
        tvname.setBackgroundColor(Color.rgb(r, g, b));
        tvqty.setBackgroundColor(Color.rgb(r, g, b));
        tvtype.setBackgroundColor(Color.rgb(r, g, b));
        tvclass.setBackgroundColor(Color.rgb(r, g, b));
        tvloc.setBackgroundColor(Color.rgb(r, g, b));
        tvdlv.setBackgroundColor(Color.rgb(r, g, b));
        tvdate.setBackgroundColor(Color.rgb(r, g, b));
        tvnote.setBackgroundColor(Color.rgb(r, g, b));
        sv.setBackgroundColor(Color.WHITE);
        lnlt.setBackgroundColor(Color.rgb(r, g, b));
        return 0;
    }


}








