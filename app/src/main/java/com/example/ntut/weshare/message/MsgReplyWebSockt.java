package com.example.ntut.weshare.message;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.R;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class MsgReplyWebSockt extends AppCompatActivity {
    private static final String TAG = "MsgReplyWebSockt";
//    private static final String SERVER_URI = "ws://192.168.1.108:8080/WeShare/MyWebSocketServer/2222/1";
    private static final String SERVER_URI = "ws://10.0.2.2:8080/WeShare/MyWebSocketServer/2222/1";
    private static final String USER_NAME = "Android";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_MESSAGE = "message";

    private MyWebSocketClient myWebSocketClient;
    private TextView tvMessage;
    private EditText etMessage;
    private ScrollView scrollView;

    class MyWebSocketClient extends WebSocketClient {

        public MyWebSocketClient(URI serverURI) {
            // Draft_17是連接協議，就是標準的RFC 6455（JSR256）
            super(serverURI, new Draft_17());
        }

        @Override
        public void onOpen(ServerHandshake handshakedata) {
            Log.d(TAG, "onOpen: handshakedata.toString() = " + handshakedata.toString());
        }

        @Override
        public void onMessage(final String message) {
            Log.d(TAG, "onMessage: " + message);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(message);
                        String userName = jsonObject.get(KEY_USER_NAME).toString();
                        String message = jsonObject.get(KEY_MESSAGE).toString();
                        String text = userName + ": " + message + "\n";
                        tvMessage.append(text);
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    } catch (JSONException e) {
                        Log.e(TAG, e.toString());
                    }
                }
            });
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            String text = String.format(Locale.getDefault(),
                    "code = %d, reason = %s, remote = %b",
                    code, reason, remote);
            Log.d(TAG, "onClose: " + text);
        }

        @Override
        public void onError(Exception ex) {
            Log.d(TAG, "onError: exception = " + ex.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg_activity_main);
        findViews();
        URI uri = null;
        try {
            uri = new URI(SERVER_URI);
        } catch (URISyntaxException e) {
            Log.e(TAG, e.toString());
        }
        myWebSocketClient = new MyWebSocketClient(uri);
        myWebSocketClient.connect();
    }

    private void findViews() {
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        etMessage = (EditText) findViewById(R.id.etMessage);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
    }


    public void onSendClick(View view) {
        String message = etMessage.getText().toString();
        if (message.trim().isEmpty()) {
            showToast(R.string.text_MessageEmpty);
            return;
        }
        SharedPreferences pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        String account = pref.getString("user", "");
        Map<String, String> map = new HashMap<>();
//        map.put(KEY_USER_NAME, USER_NAME);
        map.put(KEY_USER_NAME, account);
        map.put(KEY_MESSAGE, message);
        if (myWebSocketClient != null) {
            myWebSocketClient.send(new JSONObject(map).toString());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (myWebSocketClient != null) {
                myWebSocketClient.close();
                showToast(R.string.text_LeftChatRoom);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showToast(int messageId) {
        Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show();
    }
}
