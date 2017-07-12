package com.example.ntut.weshare.dealDetail;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ntut.weshare.message.MessageBean;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

// for insert, ic_update, ic_delete a spot
class SendDealingContextTask extends AsyncTask<Object, Integer, Integer> {
    private final static String TAG = "SendMagTask";

    @Override
    protected Integer doInBackground(Object... params) {
        String url = params[0].toString();
        String action = params[1].toString();
        String dealNo = params[2].toString();
        String shipNo = params[3].toString();
        String result;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", action);//動作，新增
        jsonObject.addProperty("dealNo", dealNo);
        jsonObject.addProperty("shipNo", shipNo);
        try {
            result = getRemoteData(url, jsonObject.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            return null;
        }
        return Integer.parseInt(result);
    }

    private String getRemoteData(String url, String jsonOut) throws IOException {
        StringBuilder sb = new StringBuilder();
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoInput(true); // allow inputs
        connection.setDoOutput(true); // allow outputs
        connection.setUseCaches(false); // do not use a cached copy
        connection.setRequestMethod("POST");
        connection.setRequestProperty("charset", "UTF-8");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        bw.write(jsonOut);
        Log.d(TAG, "jsonOut: " + jsonOut);
        bw.close();

        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } else {
            Log.d(TAG, "response code: " + responseCode);
        }
        connection.disconnect();
        Log.d(TAG, "jsonIn: " + sb);
        return sb.toString();
    }
}