package com.example.ntut.weshare.message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.ntut.weshare.R;

public class MessageReplyActivity extends AppCompatActivity {
    private final static String TAG = "MessageReplyActivity";

    private EditText edText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_reply_fragment);
        findViews();
    }

    private void findViews() {
        edText = (EditText) findViewById(R.id.edText);
    }

    public void onSendMsgClick(View view) {
    }

    public void onSendPicClick(View view) {
    }
}