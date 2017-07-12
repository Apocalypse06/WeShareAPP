package com.example.ntut.weshare.dealDetail;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.R;
import com.example.ntut.weshare.homeGoodsDetail.FeedbackBean;

import java.io.ByteArrayOutputStream;


public class FeedbackDialogFragment extends DialogFragment {//必須繼承DialogFragment，並實作OnClickListener監聽器
    private final static String TAG = "DealDialogFragment";

    private EditText etFeedbackText;
    private ImageView ivNumber;
    private ImageView ivFeedbackImage;
    private RatingBar ratingBar;
    private Button btFeedback;
    private Button btCancel;

    private Dealed ref;
    private int fbScore = 5;
    private byte[] image = null;

    private static final int REQUEST_PICK_IMAGE = 1;

    public void setRef(Dealed ref) {
        this.ref = ref;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dealed_feedback_dialog_fragment, container);
        ivNumber = (ImageView) view.findViewById(R.id.ivNumber);
        ivFeedbackImage = (ImageView) view.findViewById(R.id.ivFeedbackImage);
        etFeedbackText = (EditText) view.findViewById(R.id.etFeedbackText);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        btFeedback = (Button) view.findViewById(R.id.btFeedback);
        btCancel = (Button) view.findViewById(R.id.btCancel);

        ratingBar.setRating((float) 2.5);


        btFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFeadback();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                double star = ratingBar.getRating();
                fbScore = (int) (star * 2);
//                Toast.makeText(getActivity(), "原本=" + star + ",乘2=" + (star * 2),
//                        Toast.LENGTH_SHORT).show();
//                etFeedbackText.setText("" + fbScore);
                String a = "R.drawable.number_" + "1" + "_icon";
                // ivNumber.setImageResource(R.drawable.a);
            }
        });

        ivFeedbackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_PICK_IMAGE);
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
                    Cursor cursor = getActivity().getContentResolver().query(uri, columns, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        String imagePath = cursor.getString(0);
                        cursor.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                        ivFeedbackImage.setImageBitmap(bitmap);
                        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out2);
                        image = out2.toByteArray();
                    }
                    break;
            }
        }
    }


    private void sendFeadback() {
        String fbText = etFeedbackText.getText().toString().trim();
        if (fbText.length() <= 0) {
            Toast.makeText(getActivity(), R.string.msg_feedbackNull,
                    Toast.LENGTH_SHORT).show();
            return;
        }

//        if (image == null) {
//            Common.showToast(getActivity(), "圖片空白");
//            return;
//        }

        int count = 0;
        String action = "snedFeedback";
        if (Common.networkConnected(getActivity())) {//傳送到server端
            FeedbackBean fb = new FeedbackBean(ref.dealStatic.getDealNo(), ref.dealStatic.getEndId(), ref.dealStatic.getSourceId(), fbText, fbScore, "fbFileName");
            try {
                if (image == null) {
                    fb.setFbFileName(null);
                    count = new NewFeedbackTask().execute(action, fb, null).get();
                } else {
                    String imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);//圖片資料
                    count = new NewFeedbackTask().execute(action, fb, imageBase64).get();
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

            if (count == 0) {
                Common.showToast(getActivity(), R.string.msg_fbFail);
            } else {
                Common.showToast(getActivity(), R.string.msg_fbSuccess);
                ondd();
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }


    }

//    @Override
//    public void onDismiss(DialogInterface frag) {
//        ref.showAllDeals();
//        super.onDismiss(frag);
//    }

    public void ondd() {
        ref.showAllDeals();
        dismiss();
    }
}