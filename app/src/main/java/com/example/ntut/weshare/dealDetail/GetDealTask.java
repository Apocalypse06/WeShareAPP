package com.example.ntut.weshare.dealDetail;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.homeGoodsDetail.DealBean;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;

// for insert, ic_update, ic_delete a spot
class GetDealTask extends AsyncTask<Object, Integer, List<DealBean>> {
    private final static String TAG = "GetDealTask";
    private String url = Common.URL + "DealServlet";

    @Override
    protected List<DealBean> doInBackground(Object... params) {
        String action = params[0].toString();
        String user = params[1].toString();

        String jsonIn;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", action);//動作，新增
        jsonObject.addProperty("user", user);//純文字

        try {
            jsonIn = getRemoteData(url, jsonObject.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            return null;
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<List<DealBean>>() {
        }.getType();

//        List<DealBean> items = gson.fromJson(jsonIn, listType);
//        return items;
        return gson.fromJson(jsonIn, listType);
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