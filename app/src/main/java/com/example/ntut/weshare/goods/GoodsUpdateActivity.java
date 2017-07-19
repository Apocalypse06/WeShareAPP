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
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class GoodsUpdateActivity extends AppCompatActivity {
    private static final String TAG = "GoodsUpdateActivity";
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
    private int mYear,mMonth,mDay;
    private long deadlinedate;
    private int  qtyParseInt;
    private  byte[] image;
    private String fileName;
    private File file;
    private Calendar cld;
    private Goods good;
    TextView tvname;
    TextView tvqty;
    TextView tvtype;
    TextView tvclass;
    TextView tvloc;
    TextView tvdlv;
    TextView tvdate;
    TextView tvnote;
    ScrollView sv;
    LinearLayout lnlt;
    private int countLoc;
    private String[] loc2;
    Timestamp now= new Timestamp(System.currentTimeMillis());
    private static final int REQUEST_PICK_IMAGE = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_update_fragment);

        //接收從上一頁傳來的資料
        Bundle bundleFromList=this.getIntent().getBundleExtra("intentGoods");
        good =(Goods) bundleFromList.getSerializable("goods");

        findViews();

        loadTextInfo();



        //返回按鈕
        Button backGood=(Button) findViewById(R.id.bt_BackGood);
        backGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //預先載入圖片
        String url = Common.URL + "GoodsServlet";
        int gid = good.getGoodsNo();
        int imageSize = 400;
        new GoodsGetImageTask(ivImage).execute(url, gid, imageSize);


        spState = (Spinner) findViewById(R.id.sp_upstate);
        final String[] states = {"許願池(募資)", "送愛心(捐贈)", "以物易物"};
        ArrayAdapter<String> stateList = new ArrayAdapter<>(GoodsUpdateActivity.this,
                android.R.layout.simple_spinner_dropdown_item, states);
        spState.setAdapter(stateList);

        if (good.getGoodsStatus() == 1) {
            spState.setSelection(0);
        } else if  (good.getGoodsStatus() == 2) {
            spState.setSelection(1);
        }else if (good.getGoodsStatus() == 3) {
            spState.setSelection(2);
        }
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                              @Override
                                              public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                  switch (spState.getSelectedItem().toString()) {
                                                      case "許願池(募資)":
                                                          colorChange(255, 151, 151);
                                                          break;
                                                      case "送愛心(捐贈)":
                                                          colorChange(239, 123, 0);
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



        //預覽先前的"類別"選項並可被修改

        spClass=(Spinner) findViewById(R.id.sp_upclass);
        final String[] classes = {"食品" ,"服飾配件","生活用品","家電機器","其他"};
        ArrayAdapter<String> classList = new ArrayAdapter<>(GoodsUpdateActivity.this,
                android.R.layout.simple_spinner_dropdown_item, classes);
        spClass.setAdapter(classList);
        if (good.getGoodsType() == 1) {
            spClass.setSelection(0);
        } else if  (good.getGoodsType() == 2) {
            spClass.setSelection(1);
        }else if (good.getGoodsType() == 3) {
            spClass.setSelection(2);
        }else if (good.getGoodsType() == 4) {
            spClass.setSelection(3);
        }else if (good.getGoodsType() == 5) {
            spClass.setSelection(4);
        }



        //預覽先前的"所在地"選項並可被修改
        spLoc=(Spinner) findViewById(R.id.sp_uploc);
        final String[] loc =  {"苗栗縣","桃園市","基隆市","新北市","新竹市","新竹縣","臺北市","南投縣","雲林縣","嘉義市","嘉義縣","彰化縣"
                ,"臺中市","屏東縣","高雄市","臺南市","宜蘭縣","花蓮縣","臺東縣","金門縣","連江縣","澎湖縣"};
        ArrayAdapter<String> locList = new ArrayAdapter<>(GoodsUpdateActivity.this,
                android.R.layout.simple_spinner_dropdown_item, loc);
        spLoc.setAdapter(locList);
        loc2=loc;
        spLoc.setSelection((good.getGoodsLoc()-1));

//        if (good.getGoodsLoc() == 1) {
//            spLoc.setSelection(0);
//        } else if  (good.getGoodsLoc() == 2) {
//            spLoc.setSelection(1);
//        }else if (good.getGoodsLoc() == 3) {
//            spLoc.setSelection(2);
//        }


        //預覽先前的"運送方式"選項並可被修改
        spDlvWay=(Spinner) findViewById(R.id.sp_updlvWay);
        final  String[] dlv={"面交","寄送","面交寄送皆可"};
        ArrayAdapter<String> dlvList = new ArrayAdapter<>(GoodsUpdateActivity.this,
                android.R.layout.simple_spinner_dropdown_item, dlv);
        spDlvWay.setAdapter(dlvList);
        if (good.getGoodsShipWay() == 1) {
            spDlvWay.setSelection(0);
        } else if  (good.getGoodsShipWay() == 2) {
            spDlvWay.setSelection(1);
        }else if (good.getGoodsShipWay() == 3) {
            spDlvWay.setSelection(2);
        }



        //日期預先輸入
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String exdate = sdf.format(good.getDeadLine());
        final Button upDateButton = (Button)findViewById(R.id.bt_update);
        upDateButton.setText(exdate);
        upDateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(GoodsUpdateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String format = setDateFormat(year, month, day);
//                        tvDate.setText(format);
                        upDateButton.setText(format);
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

    }
    private String setDateFormat(int year,int monthOfYear,int dayOfMonth){
        return String.valueOf(year) + "-"
                + String.valueOf(monthOfYear + 1) + "-"
                + String.valueOf(dayOfMonth);
    }

    public void loadTextInfo(){
        etName.setText(good.getGoodsName());
        etQty.setText(""+good.getQty());
        etComment.setText((good.getGoodsNote()));
    }
    private void findViews() {
        ivImage = (ImageView) findViewById(R.id.iv_upimage);
        etName = (EditText) findViewById(R.id.et_upgoodsname);
        etQty = (EditText) findViewById(R.id.et_upqty);
        etComment = (EditText) findViewById(R.id.et_upcomment);
        tvname = (TextView) findViewById(R.id.textView9U);
        tvqty = (TextView) findViewById(R.id.textView10U);
        tvtype = (TextView) findViewById(R.id.txgoodstateU);
        tvclass = (TextView) findViewById(R.id.textView11U);
        tvloc = (TextView) findViewById(R.id.textView12U);
        tvdlv = (TextView) findViewById(R.id.textView15U);
        tvdate = (TextView) findViewById(R.id.textView13U);
        tvnote = (TextView) findViewById(R.id.textView14U);
        sv=(ScrollView)findViewById(R.id.sv_upgood);
        lnlt = (LinearLayout) findViewById(R.id.upback);
    }

    public void onPickPictureClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }


    //圖片選擇
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
    public void onUpdateGoodsResult(View view) {
        //物品名稱驗證

        if (etName.getText().toString().trim().length() <= 0) {
            etName.setText(etName.getHint());

        }
        //數量驗證
        if (etQty.getText().toString().trim().isEmpty()) {
            etQty.setText(etQty.getHint());
        }


        //相片驗證
        if (image == null) {
            String url = Common.URL + "GoodsServlet";
            int gid = good.getGoodsNo();
            int imageSize = 400;
            try {
                Bitmap bitmap= new GoodsGetImageTask(ivImage).execute(url, gid, imageSize).get();
                ivImage.setImageBitmap(bitmap);
                ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out2);
                image = out2.toByteArray();
            }catch (Exception e){}

        }



        if (Common.networkConnected(this)) {
            int qtyParseInt=Integer.parseInt(etQty.getText().toString().trim());
            String comment=etComment.getText().toString().trim();
            String goodName=etName.getText().toString().trim();
            SharedPreferences pref = this.getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
            String user = pref.getString("user","");


            //判斷依照地區給予int值
            for( int n=0; n<loc2.length;n++) {
                if (spLoc.getSelectedItem().toString() == loc2[n].toString()) {
                    countLoc = n + 1;
                    break;
                }
            }


            //判斷依照物品種類給予int值

            switch (spClass.getSelectedItem().toString()){
                case "食品":
                    intspClass=1;
                    break;
                case "服飾配件":
                    intspClass=2;
                    break;
                case "生活用品":
                    intspClass=3;
                    break;
                case "家電機器":
                    intspClass=4;
                    break;
                case "其他":
                    intspClass=5;
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
            Goods goods = new Goods(good.getGoodsNo(), good.getGoodsStatus(), now, user,goodName ,intspClass,
                    qtyParseInt, countLoc, comment, intspDlvWay, deadlinedate,"goodsfilename");
            String imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);
            String action = "goodsUpdate";
            int count = 0;
            try {
                count = new GoodsUpdateTask().execute(url, action, goods, imageBase64).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (count == 0) {
                Common.showToast(GoodsUpdateActivity.this, R.string.msg_updateFail);
            } else {
                Common.showToast(GoodsUpdateActivity.this, R.string.msg_updateSuccess);
            }
        } else {
            Common.showToast(this, R.string.msg_NoNetwork);
        }
        Intent intent = new Intent();
        intent.setClass(GoodsUpdateActivity.this, GoodsBoxPageActivity.class);
        startActivity(intent);


    }

    public int colorChange(int r,int g,int b) {
        tvname.setBackgroundColor(Color.rgb(r,g,b));
        tvqty.setBackgroundColor(Color.rgb(r,g,b));
        tvtype.setBackgroundColor(Color.rgb(r,g,b));
        tvclass.setBackgroundColor(Color.rgb(r,g,b));
        tvloc.setBackgroundColor(Color.rgb(r,g,b));
        tvdlv.setBackgroundColor(Color.rgb(r,g,b));
        tvdate.setBackgroundColor(Color.rgb(r,g,b));
        tvnote.setBackgroundColor(Color.rgb(r,g,b));
        sv.setBackgroundColor(Color.WHITE);
        lnlt.setBackgroundColor(Color.rgb(r,g,b));
        return 0;
    }
}
