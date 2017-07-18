package com.example.ntut.weshare.dealDetail;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ntut.weshare.R;

public class CarDealedDialogFragment extends DialogFragment {//必須繼承DialogFragment，並實作OnClickListener監聽器
    private final static String TAG = "DealDialogFragment";

    private TextView tvShipNo;
    private Button btCancel;

    private Dealed ref;

    public void setRef(Dealed ref) {
        this.ref = ref;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dealed_car_dialog_fragment, container);
        tvShipNo = (TextView) view.findViewById(R.id.tvShipNo);
        btCancel = (Button) view.findViewById(R.id.btCancel);

        tvShipNo.setText(ref.dealStatic.getShipNo());
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }

}