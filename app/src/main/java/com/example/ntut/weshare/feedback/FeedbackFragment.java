package com.example.ntut.weshare.feedback;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ntut.weshare.R;


public class FeedbackFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
//        View view = inflater.inflate(R.layout., container, false);
        View view = inflater.inflate(R.layout.message_sent_item, container, false);

        return view;
    }
}
