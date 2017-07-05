package com.example.ntut.weshare.goods;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.example.ntut.weshare.R;

public class GoodsInsertChangeColorTask extends View {

    TextView tvname;
    TextView tvqty;
    TextView tvtype;
    TextView tvclass;
    TextView tvloc;
    TextView tvdlv;
    TextView tvdate;
    TextView tvnote;
    ScrollView sv;
    LinearLayout lnlt;

    public GoodsInsertChangeColorTask(Context context) {

        super(context);

    }

    public void onCreate() {


        findView();
    }

    public int colorChange(int r,int g,int b) {
        tvname.setBackgroundColor(Color.rgb(r,g,b));
        tvqty.setBackgroundColor(Color.rgb(r,g,b));
        tvtype.setBackgroundColor(Color.rgb(r,g,b));
        tvclass.setBackgroundColor(Color.rgb(r,g,b));
        tvloc.setBackgroundColor(Color.rgb(r,g,b));
        tvdlv.setBackgroundColor(Color.rgb(r,g,b));
        tvdate.setBackgroundColor(Color.rgb(r,g,b));
        tvnote.setBackgroundColor(Color.rgb(r,g,b));
        sv.setBackgroundColor(Color.WHITE);
        lnlt.setBackgroundColor(Color.rgb(r,g,b));
        return 0;
    }

    public void findView() {
        tvname = (TextView) findViewById(R.id.textView9);
        tvqty = (TextView) findViewById(R.id.textView10);
        tvtype = (TextView) findViewById(R.id.txgoodstate);
        tvclass = (TextView) findViewById(R.id.textView11);
        tvloc = (TextView) findViewById(R.id.textView12);
        tvdlv = (TextView) findViewById(R.id.textView15);
        tvdate = (TextView) findViewById(R.id.textView13);
        tvnote = (TextView) findViewById(R.id.textView14);
        sv=(ScrollView)findViewById(R.id.sv_newgood);
        lnlt = (LinearLayout) findViewById(R.id.newback);
    }
}
