package com.example.ntut.weshare.message;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.R;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class MessageReplyWebSocketFragment extends Fragment {
    private static final String TAG = "MsgListFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvMsgs;
    private EditText edText;
    private Button btSend;
    private Button btPic;
    private MessageBean msg;
    List<MessageBean> msgs = null;
    Bitmap bitmap = null;
    private byte[] image = null;

    private String account = null;

    private MyWebSocketClient myWebSocketClient;
    private static final String SERVER_URI = "ws://10.0.2.2:8080/WeShare/message/MyWebSocketServer/";
    private static final String USER_NAME = "Android";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_MESSAGE = "message";

    private static final int REQUEST_PICK_IMAGE = 1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        msg = (MessageBean) getArguments().getSerializable("msg");
        View view = inflater.inflate(R.layout.message_reply_fragment, container, false);

        swipeRefreshLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {//監聽有沒有發生下拉，有執行onRefresh()
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);//播放動畫
                showAllMsgs();//更新
                swipeRefreshLayout.setRefreshing(false);//停止動畫
            }
        });
        rvMsgs = (RecyclerView) view.findViewById(R.id.rvMsgs);
        rvMsgs.setLayoutManager(new LinearLayoutManager(getActivity()));

        edText = (EditText) view.findViewById(R.id.edText);
        btSend = (Button) view.findViewById(R.id.btSend);
        btPic = (Button) view.findViewById(R.id.btPic);

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSendClick(view);
            }
        });

        btPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_PICK_IMAGE);
            }
        });

        SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        account = pref.getString("user", "");

        URI uri = null;
        try {
            String serverURI = SERVER_URI + "/" + account + "/" + "1";
//            uri = new URI(SERVER_URI);
            uri = new URI(serverURI);
        } catch (URISyntaxException e) {
            Log.e(TAG, e.toString());
        }
        myWebSocketClient = new MyWebSocketClient(uri);
        myWebSocketClient.connect();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        showAllMsgs();
    }

    private void showAllMsgs() {
        if (Common.networkConnected(getActivity())) {//檢查網路
            String url = Common.URL + "MsgServlet";
            SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
            String account = pref.getString("user", "");

            String urll = Common.URL + "UserServlet";
            int imageSize = 80;
            try {
                if (account.equalsIgnoreCase(msg.getMsgSourceId())) {
                    msgs = new MsgGetOneTask().execute(url, account, msg.getMsgEndId()).get();
                    bitmap = new UserGetImageTask(null).execute(urll, msg.getMsgEndId(), imageSize).get();//.execute(網址, 圖片id, 這邊做縮圖imageSize，在server端縮圖完再傳過來)
                } else if (account.equalsIgnoreCase(msg.getMsgEndId())) {
                    msgs = new MsgGetOneTask().execute(url, account, msg.getMsgSourceId()).get();
                    bitmap = new UserGetImageTask(null).execute(urll, msg.getMsgSourceId(), imageSize).get();
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (msgs == null || msgs.isEmpty()) {
                Common.showToast(getActivity(), R.string.msg_NoMsgsFound);
            } else {
                rvMsgs.setAdapter(new MessageReplyWebSocketFragment.MsgsRecyclerViewAdapter(getActivity(), msgs));//畫面RecyclerView(畫面,資料)，getActivity()取的他所依附的頁面(主頁面)
                rvMsgs.scrollToPosition(msgs.size() - 1);
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
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
                        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out2);
                        image = out2.toByteArray();
                    }
                    break;
            }
        }
        onSendClick(getView());
    }

    public void onSendClick(View view) {
        if (!"".equals(edText.getText().toString().trim()) || image != null) {
            String text = edText.getText().toString().trim();
            int count = 0;
            String url = Common.URL + "MsgServlet";
            String action = "sendMsg";
            MessageBean msgg = null;
            if (Common.networkConnected(getActivity())) {//傳送到server端
                SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
                String account = pref.getString("user", "");
                if (account.equalsIgnoreCase(msgs.get(0).getMsgSourceId())) {
                    msgg = new MessageBean(1, 1, account, msgs.get(0).getMsgEndId(), text, msgs.get(0).getRoomNo());
                } else if (account.equalsIgnoreCase(msgs.get(0).getMsgEndId())) {
                    msgg = new MessageBean(1, 1, account, msgs.get(0).getMsgSourceId(), text, msgs.get(0).getRoomNo());
                }

                try {
                    if (image != null) {
                        msgg.setMsgFileName("MsgFileName");
                        String imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);//圖片資料
                        count = new SendMsgTask().execute(url, action, msgg, imageBase64).get();
                    } else {
                        count = new SendMsgTask().execute(url, action, msgg, null).get();
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                if (count == 0) {
                    Common.showToast(getActivity(), R.string.msg_SendFail);
                } else {
                    edText.setText("");
                    rvMsgs.scrollToPosition(msgs.size() - 1);
                    showAllMsgs();
                }
            } else {
                Common.showToast(getActivity(), R.string.msg_NoNetwork);
            }
        }
    }


    private class MsgsRecyclerViewAdapter extends RecyclerView.Adapter<MessageReplyWebSocketFragment.MsgsRecyclerViewAdapter.MyViewHolder> {//CH05 RecyclerView
        private LayoutInflater layoutInflater;
        private List<MessageBean> msgs;

        public MsgsRecyclerViewAdapter(Context context, List<MessageBean> msgs) {
            layoutInflater = LayoutInflater.from(context);
            this.msgs = msgs;
        }

        @Override
        public int getItemCount() {
            return msgs.size();
        }

        @Override
        public MessageReplyWebSocketFragment.MsgsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.message_sent_item, parent, false);
            return new MessageReplyWebSocketFragment.MsgsRecyclerViewAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MessageReplyWebSocketFragment.MsgsRecyclerViewAdapter.MyViewHolder myViewHolder, int position) {//將圖文顯示出來
            final MessageBean msg = msgs.get(position);//文字資料
            //要縮圖的大小像素，要放在250*250的框
            //這邊啟動AsyncTask，抓圖片
            SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
            String account = pref.getString("user", "");


            if (account.equalsIgnoreCase(msg.getMsgSourceId())) {
                myViewHolder.llSendAll.setVisibility(View.GONE);
//                myViewHolder.llsend.setVisibility(View.GONE);
                myViewHolder.llget.setVisibility(View.VISIBLE);
                if (msg.getMsgFileName() != null) {
                    int imageSize = 500;
                    new GetMsgImageTask(myViewHolder.ivMeImg).execute(msg.getMsgNo(), imageSize);
                    myViewHolder.ivMeImg.setVisibility(View.VISIBLE);
                    myViewHolder.ivYouImg.setVisibility(View.GONE);
                    myViewHolder.tvgetMsg.setVisibility(View.GONE);
                } else {
                    myViewHolder.ivMeImg.setVisibility(View.GONE);
                    myViewHolder.ivYouImg.setVisibility(View.GONE);
                    myViewHolder.tvgetMsg.setText(msg.getMsgText());
                }
            } else if (account.equalsIgnoreCase(msg.getMsgEndId())) {
                myViewHolder.llsend.setVisibility(View.VISIBLE);
                myViewHolder.llget.setVisibility(View.GONE);
                myViewHolder.ivUserOnePic.setImageBitmap(bitmap);
                if (msg.getMsgFileName() != null) {
                    int imageSize = 500;
                    new GetMsgImageTask(myViewHolder.ivYouImg).execute(msg.getMsgNo(), imageSize);
                    myViewHolder.ivMeImg.setVisibility(View.GONE);
                    myViewHolder.ivYouImg.setVisibility(View.VISIBLE);
                    myViewHolder.tvsendMsg.setVisibility(View.GONE);
                } else {
                    myViewHolder.ivMeImg.setVisibility(View.GONE);
                    myViewHolder.ivYouImg.setVisibility(View.GONE);
                    myViewHolder.tvsendMsg.setText(msg.getMsgText());
                }
            }
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivUserOnePic, ivYouImg, ivMeImg;
            TextView tvsendMsg, tvgetMsg;
            LinearLayout llsend, llget, llSendAll;

            public MyViewHolder(View itemView) {
                super(itemView);
                ivUserOnePic = (ImageView) itemView.findViewById(R.id.ivUserOnePic);
                ivMeImg = (ImageView) itemView.findViewById(R.id.ivMeImg);
                ivYouImg = (ImageView) itemView.findViewById(R.id.ivYouImg);
                llsend = (LinearLayout) itemView.findViewById(R.id.llsend);
                llget = (LinearLayout) itemView.findViewById(R.id.llget);
                llSendAll = (LinearLayout) itemView.findViewById(R.id.llSendAll);
                tvsendMsg = (TextView) itemView.findViewById(R.id.tvsendMsg);
                tvgetMsg = (TextView) itemView.findViewById(R.id.tvgetMsg);
                llsend.setVisibility(View.GONE);
                llget.setVisibility(View.GONE);
                ivMeImg.setVisibility(View.GONE);
                ivYouImg.setVisibility(View.GONE);
            }
        }
    }

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
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(message);
                        String userName = jsonObject.get(KEY_USER_NAME).toString();
                        String message = jsonObject.get(KEY_MESSAGE).toString();
                        String text = userName + ": " + message + "\n";
//                        tvMessage.append(text);
//                        scrollView.fullScroll(View.FOCUS_DOWN);
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

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (myWebSocketClient != null) {
//                myWebSocketClient.close();
//                showToast(R.string.text_LeftChatRoom);
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }

}