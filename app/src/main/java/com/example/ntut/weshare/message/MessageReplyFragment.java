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
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
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

import java.io.ByteArrayOutputStream;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MessageReplyFragment extends Fragment {
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

    private TextView tvsendMsg, tvgetMsg;

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

        return view;
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
                rvMsgs.setAdapter(new MessageReplyFragment.MsgsRecyclerViewAdapter(getActivity(), msgs));//畫面RecyclerView(畫面,資料)，getActivity()取的他所依附的頁面(主頁面)
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
                    msgg = new MessageBean(1, 2, account, msgs.get(0).getMsgEndId(), text, msgs.get(0).getRoomNo());
                } else if (account.equalsIgnoreCase(msgs.get(0).getMsgEndId())) {
                    msgg = new MessageBean(1, 2, account, msgs.get(0).getMsgSourceId(), text, msgs.get(0).getRoomNo());
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
                    image = null;
                    rvMsgs.scrollToPosition(msgs.size() - 1);
                    showAllMsgs();
                }
            } else {
                Common.showToast(getActivity(), R.string.msg_NoNetwork);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        showAllMsgs();
    }

    private class MsgsRecyclerViewAdapter extends RecyclerView.Adapter<MessageReplyFragment.MsgsRecyclerViewAdapter.MyViewHolder> {//CH05 RecyclerView
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
        public MessageReplyFragment.MsgsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.message_sent_item, parent, false);
            return new MessageReplyFragment.MsgsRecyclerViewAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MessageReplyFragment.MsgsRecyclerViewAdapter.MyViewHolder myViewHolder, int position) {//將圖文顯示出來
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

}