package com.example.ntut.weshare.homeGoodsDetail;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.MainActivity;
import com.example.ntut.weshare.R;
import com.example.ntut.weshare.dealDetail.AlertDialogFragment;
import com.example.ntut.weshare.dealDetail.NotDeal;
import com.example.ntut.weshare.goods.Goods;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;


public class GoodsDetailFragment extends Fragment {

    View view;

    private TextView tvGoodsName;
    private TextView tvdeadTime;
    private TextView tvQty;
    private TextView tvLoc;
    private TextView tvWay;
    private TextView tvAccount;
    private TextView tvNote;
    private Button btAccept;
    private Button btCancel;
    private Button btRefuse;

    private static Goods good;
    private static int Way;
    private static String account;

    static Goods goodsPublic = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_detail_goods, container, false);

        Bundle bundle = getActivity().getIntent().getBundleExtra("intentGoods");
        good = (Goods) bundle.getSerializable("Goods");

        findViews();
        return view;
    }

    public void findViews() {
        tvGoodsName = (TextView) view.findViewById(R.id.tvGoodsName);
        tvdeadTime = (TextView) view.findViewById(R.id.tvdeadTime);
        tvQty = (TextView) view.findViewById(R.id.tvQty);
        tvLoc = (TextView) view.findViewById(R.id.tvLoc);
        tvWay = (TextView) view.findViewById(R.id.tvWay);
        tvAccount = (TextView) view.findViewById(R.id.tvAccount);
        tvNote = (TextView) view.findViewById(R.id.tvNote);
        btAccept = (Button) view.findViewById(R.id.btAccept);
        btCancel = (Button) view.findViewById(R.id.btCancel);
        btRefuse = (Button) view.findViewById(R.id.btRefuse);

        tvGoodsName.setText(good.getGoodsName());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String exdate = sdf.format(good.getDeadLine());
        tvdeadTime.setText("到期日：" + exdate);
        tvQty.setText("需求數量 " + good.getQty());

        List<Local> local = null;
        String action = "getLocal";
        String loc = "" + good.getGoodsLoc();
        if (Common.networkConnected(getActivity())) {//傳送到server端
            try {
                local = new homeGetLocalTask().execute(action, loc).get();
            } catch (Exception e) {
                Log.e("Local", e.toString());
            }
            if (local == null) {
                Common.showToast(getActivity(), "+++++++" + good.getGoodsLoc());
            } else {
                tvLoc.setText("地點 " + local.get(0).getLocalName());
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
        //tvLoc.setText("地點 " + good.getGoodsLoc());

        if (good.getGoodsShipWay() == 1) {
            tvWay.setText("面交");
        } else if (good.getGoodsShipWay() == 2) {
            tvWay.setText("物流");
        } else if (good.getGoodsShipWay() == 3) {
            tvWay.setText("皆可");
        } else {
            tvWay.setText("不允許");
        }

        tvAccount.setText(good.getIndId());
        tvNote.setText(good.getGoodsNote());

        btAccept.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
                                            account = pref.getString("user", "");
                                            if (account == "") {
                                                Toast.makeText(getActivity(), "請註冊登入WeShare後，再過來設定您的物資箱喔~",
                                                        Toast.LENGTH_SHORT).show();
                                                return;
                                            } else if (account.equalsIgnoreCase(good.getIndId())) {
                                                Common.showToast(getActivity(), R.string.msg_sameAccount);
                                            } else {
                                                goodsPublic = good;
//                                                AlertDialogFragment alertFragment = new AlertDialogFragment();//建立物件
//                                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                                                alertFragment.show(fragmentManager, "alert");//顯示警示框}
                                                GoodsDialogFragment detail = new GoodsDialogFragment();
                                                detail.setRef(GoodsDetailFragment.this);
                                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                detail.show(fragmentManager, "alert");//顯示警示框

                                            }
                                        }
                                    }

        );

        btCancel.setOnClickListener(new View.OnClickListener()

                                    {
                                        @Override
                                        public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), MainActivity.class);
//                startActivity(intent);
                                            getActivity().finish();
                                        }
                                    }

        );
    }


//    public static class AlertDialogFragment
//            extends DialogFragment {//必須繼承DialogFragment，並實作OnClickListener監聽器
//        private final static String TAG = "DealDialogFragment";
//
//        private TextView tvGoodsName;
//        private EditText etQty;
//        private Spinner spWay;
//        private Button btAccept;
//        private Button btCancel;
//        private EditText etDealNote;
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View view = inflater.inflate(R.layout.accept_dialog, container);
//
//            tvGoodsName = (TextView) view.findViewById(R.id.tvGoodsName);
//            etQty = (EditText) view.findViewById(R.id.etQty);
//            spWay = (Spinner) view.findViewById(R.id.spWay);
//            btAccept = (Button) view.findViewById(R.id.btAccept);
//            btCancel = (Button) view.findViewById(R.id.btCancel);
//            etDealNote = (EditText) view.findViewById(R.id.etDealNote);
//
//            tvGoodsName.setText(good.getGoodsName());
//            if (good.getGoodsShipWay() == 1) {
//                final String[] classes = {"面交"};
//                ArrayAdapter<String> classList = new ArrayAdapter<>(getActivity(),
//                        android.R.layout.simple_spinner_dropdown_item,
//                        classes);
//                spWay.setAdapter(classList);
//                spWay.setEnabled(false);
//            } else if (good.getGoodsShipWay() == 2) {
//                final String[] classes = {"物流"};
//                ArrayAdapter<String> classList = new ArrayAdapter<>(getActivity(),
//                        android.R.layout.simple_spinner_dropdown_item,
//                        classes);
//                spWay.setAdapter(classList);
//                spWay.setEnabled(false);
//            } else {
//                final String[] classes = {"面交", "物流"};
//                ArrayAdapter<String> classList = new ArrayAdapter<>(getActivity(),
//                        android.R.layout.simple_spinner_dropdown_item,
//                        classes);
//                spWay.setAdapter(classList);
//            }
//            btAccept.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    acceptDeal();
//                }
//            });
//
//            btCancel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dismiss();
//                }
//            });
//            return view;
//        }
//
//
//        private void acceptDeal() {
//            String qty = etQty.getText().toString().trim();
//            String dealNote = etDealNote.getText().toString().trim();
//            int intValue = 0;
//            if (qty.length() <= 0) {
//                Common.showToast(getActivity(), R.string.msg_qtyNull);
//                return;
//            } else {
//                intValue = Integer.valueOf(qty);
//                if (intValue > good.getQty()) {
//                    Common.showToast(getActivity(), "高過需求\n需求數量為" + good.getQty());
//                    return;
//                }
//            }
//            int endShipWay = ChoseOreType();
//            Timestamp postDate = new Timestamp(new java.util.Date().getTime());
//            int count = 0;
//            String action = "newDeal";
//
//            Bitmap bitmap = null;
//            String url = Common.URL + "GoodsServlet";
//            int gid = good.getGoodsNo();
//            int imageSize = 800;
//            byte[] image = null;
//
//            if (Common.networkConnected(getActivity())) {//傳送到server端
//                DealBean deal = new DealBean(postDate, account, good.getIndId(), 0, endShipWay, null, null, good.getGoodsName(), intValue, good.getGoodsfilename(), good.getGoodsType(), good.getGoodsLoc(), good.getGoodsNote(), dealNote);
//                try {
//                    bitmap = new GoodsGetImageTask(null).execute(url, gid, imageSize).get();
//
//                    ByteArrayOutputStream out2 = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out2);
//                    image = out2.toByteArray();
//                    String imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);//圖片資料
//
//                    count = new NewDealTask().execute(action, deal, "" + good.getGoodsNo(), imageBase64).get();
//                } catch (Exception e) {
//                    Log.e(TAG, e.toString());
//                }
//                if (count == 0) {
//                    Common.showToast(getActivity(), R.string.msg_dealFail);
//                } else {
//                    Common.showToast(getActivity(), R.string.msg_dealSuccess);
//                    dismiss();
//                }
//            } else {
//                Common.showToast(getActivity(), R.string.msg_NoNetwork);
//            }
//            good.setQty(good.getQty() - intValue);
//            dismiss();
//        }
//
//        private int ChoseOreType() {
//            int endShipWay = 0;
//            switch (spWay.getSelectedItem().toString()) {
//                case "面交":
//                    endShipWay = 0;
//                    break;
//                case "物流":
//                    endShipWay = 1;
//                    break;
//            }
//            return endShipWay;
//        }
//
//    }


}
