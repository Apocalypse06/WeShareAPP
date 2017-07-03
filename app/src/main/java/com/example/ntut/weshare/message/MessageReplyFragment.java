package com.example.ntut.weshare.message;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    private ImageView ivUserPic;
    private TextView tvsendMsg, tvgetMsg;
    private LinearLayout llsend, llget;


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

            }
        });


        return view;
    }

    public void onSendClick(View view) {
        String text = edText.getText().toString().trim();
        int count = 0;
        String url = Common.URL + "MsgServlet";
        String action = "sendMsg";
        MessageBean msgg = null;
        if (Common.networkConnected(getActivity())) {//傳送到server端
            SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
            String account = pref.getString("user", "");
            if(account.equalsIgnoreCase(msgs.get(0).getMsgSourceId())){
                msgg = new MessageBean(1, 1, account, msgs.get(0).getMsgEndId(), text, msgs.get(0).getRoomNo());
            }else if (account.equalsIgnoreCase(msgs.get(0).getMsgEndId())){
                msgg = new MessageBean(1, 1, account, msgs.get(0).getMsgSourceId(), text, msgs.get(0).getRoomNo());
            }
            //String imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);//圖片資料
            try {
                count = new SendMsgTask().execute(url, action, msgg).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

            if (count == 0) {
                Common.showToast(getActivity(), R.string.msg_SendFail);
            }else {
                //Common.showToast(getActivity(), R.string.msg_RegisterSuccess);
                showAllMsgs();
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }

    }


    private void showAllMsgs() {
        if (Common.networkConnected(getActivity())) {//檢查網路
            String url = Common.URL + "MsgServlet";
            SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
            String account = pref.getString("user", "");
            try {
                if (account.equalsIgnoreCase(msg.getMsgSourceId())) {
                    msgs = new MsgGetOneTask().execute(url, account, msg.getMsgEndId()).get();
                } else if (account.equalsIgnoreCase(msg.getMsgEndId())) {
                    msgs = new MsgGetOneTask().execute(url, account, msg.getMsgSourceId()).get();
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (msgs == null || msgs.isEmpty()) {
                Common.showToast(getActivity(), R.string.msg_NoMsgsFound);
            } else {
                //Common.showToast(getActivity(), R.string.msg_NoMsgsFound);
                rvMsgs.setAdapter(new MessageReplyFragment.MsgsRecyclerViewAdapter(getActivity(), msgs));//畫面RecyclerView(畫面,資料)，getActivity()取的他所依附的頁面(主頁面)
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
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
//            String url = Common.URL + "MsgServlet";
//            int id = msg.getMsgNo();//取的文字的id
//            int imageSize = 250;//要縮圖的大小像素，要放在250*250的框
//            //這邊啟動AsyncTask，抓圖片
            //不用.get()，不然會卡畫面，這邊利用SpotGetImageTask(myViewHolder.imageView)放圖，myViewHolder.imageView將imageView元件傳給AsyncTask，再用onPostExecute()將圖貼上
            //new MsgGetImageTask(myViewHolder.ivUserPic).execute(url, id, imageSize);//.execute(網址, 圖片id, 這邊做縮圖imageSize，在server端縮圖完再傳過來)

            SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
            String account = pref.getString("user", "");
            if (account.equalsIgnoreCase(msg.getMsgSourceId())) {
                llsend.setVisibility(View.GONE);
                llget.setVisibility(View.VISIBLE);
                myViewHolder.tvgetMsg.setText(msg.getMsgText());
            } else if (account.equalsIgnoreCase(msg.getMsgEndId())) {
                llsend.setVisibility(View.VISIBLE);
                llget.setVisibility(View.GONE);
                myViewHolder.tvsendMsg.setText(msg.getMsgText());
            }
            //myViewHolder.tvMsgOne.setText(msg.getMsgText());
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            //            ImageView ivUserPic;
            TextView tvsendMsg, tvgetMsg;
//            LinearLayout llsend,llget;

            public MyViewHolder(View itemView) {
                super(itemView);
                ivUserPic = (ImageView) itemView.findViewById(R.id.ivUserPic);
                llsend = (LinearLayout) itemView.findViewById(R.id.llsend);
                llget = (LinearLayout) itemView.findViewById(R.id.llget);
                tvsendMsg = (TextView) itemView.findViewById(R.id.tvsendMsg);
                tvgetMsg = (TextView) itemView.findViewById(R.id.tvgetMsg);
                llsend.setVisibility(View.GONE);
                llget.setVisibility(View.GONE);
            }
        }
    }

}