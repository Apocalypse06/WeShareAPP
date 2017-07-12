package com.example.ntut.weshare.dealDetail;

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
import java.util.List;


public class FeedbackedDialogFragment extends DialogFragment {//必須繼承DialogFragment，並實作OnClickListener監聽器
    private final static String TAG = "DealDialogFragment";

    private EditText etFeedbackText;
    private ImageView ivNumber;
    private ImageView ivFeedbackImage;
    private RatingBar ratingBar;
    private Button btFeedback;
    private Button btCancel;

    private Dealed ref;
    private byte[] image = null;
    private List<FeedbackBean> fb = null;

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

        showFeedback();

        ratingBar.setRating((float) (fb.get(0).getFbScore() / 2.0));
        ratingBar.setIsIndicator(true);

        etFeedbackText.setText(fb.get(0).getFbText());
        etFeedbackText.setFocusable(false);
        if (fb.get(0).getFbFileName() != null) {
            int imageSize = 450;
            new GetFeedbackImageTask(ivFeedbackImage).execute(fb.get(0).getDealNo(), imageSize);
        } else {
            ivFeedbackImage.setVisibility(View.GONE);
        }

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
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

    private void showFeedback() {
        if (Common.networkConnected(getActivity())) {
            String action = "getFeedback";
            try {
                fb = new GetFeedbacklTask().execute(action, "" + ref.dealStatic.getDealNo()).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
    }
}