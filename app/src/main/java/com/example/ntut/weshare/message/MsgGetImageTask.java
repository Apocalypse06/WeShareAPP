package com.example.ntut.weshare.message;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.ntut.weshare.R;
import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;


class MsgGetImageTask extends AsyncTask<Object, Integer, Bitmap> {
    private final static String TAG = "MsgtGetImageTask";
    private final static String ACTION = "getImage";
    //WeakReference<ImageView>是避免在極端的狀態下(user快速滑動造成大量載入)造成OOM，WeakReference是會在資料沒有再參照下，馬上會直接回收資源(記憶體)
    private final WeakReference<ImageView> imageViewWeakReference;

    MsgGetImageTask(ImageView imageView) {
        this.imageViewWeakReference = new WeakReference<>(imageView);
    }

    @Override
    protected Bitmap doInBackground(Object... params) {
        String url = params[0].toString();
        int id = Integer.parseInt(params[1].toString());
        int imageSize = Integer.parseInt(params[2].toString());
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", ACTION);//動作，得到圖
        jsonObject.addProperty("id", id);//圖的id
        jsonObject.addProperty("imageSize", imageSize);//縮圖

        Bitmap bitmap;
        try {
            bitmap = getRemoteImage(url, jsonObject.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            return null;
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {//顯示圖片的畫面，前面有給myViewHolder.imageView
        if (isCancelled()) {
            bitmap = null;
        }
        ImageView imageView = imageViewWeakReference.get();
        if (imageView != null) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);//顯示圖片
            } else {
                imageView.setImageResource(R.drawable.hand);
            }
        }
        super.onPostExecute(bitmap);
    }

    private Bitmap getRemoteImage(String url, String jsonOut) throws IOException {
        Bitmap bitmap = null;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoInput(true); // allow inputs
        connection.setDoOutput(true); // allow outputs
        connection.setUseCaches(false); // do not use a cached copy
        connection.setRequestMethod("POST");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        bw.write(jsonOut);
        Log.d(TAG, "jsonOut: " + jsonOut);
        bw.close();

        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            bitmap = BitmapFactory.decodeStream(connection.getInputStream());//取的server端送的圖片，.decodeStream解析
        } else {
            Log.d(TAG, "response code: " + responseCode);
        }
        connection.disconnect();
        return bitmap;
    }
}