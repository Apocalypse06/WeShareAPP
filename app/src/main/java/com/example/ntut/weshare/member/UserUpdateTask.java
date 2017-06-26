package com.example.ntut.weshare.member;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.ntut.weshare.R;
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
class UserUpdateTask extends AsyncTask<Object, Integer, Integer> {
    private final static String TAG = "UserUpdateInTask";

    @Override
    protected Integer doInBackground(Object... params) {
        String result ;
        JsonObject jsonObject = new JsonObject();
        String url = params[0].toString();
        String action = params[1].toString();
        jsonObject.addProperty("action", action);//動作，新增

        if(action.equals("userRegister") || action.equals("userUpdate")) {
            User user = (User) params[2];
            jsonObject.addProperty("user", new Gson().toJson(user));//純文字
            if (params[3] != null) {
                String imageBase64 = params[3].toString();
                jsonObject.addProperty("imageBase64", imageBase64);//圖片
            } else {
                String imageBase64 = "0";
                jsonObject.addProperty("imageBase64", imageBase64);//圖片
            }

        }else if(action.equals("userInRegister") ){
            User user = (User) params[2];
            jsonObject.addProperty("user", new Gson().toJson(user));//純文字
            if (params[3] != null) {
                String imageBase64 = params[3].toString();
                jsonObject.addProperty("imageBase64", imageBase64);//圖片
            } else {
                String imageBase64 = "0";
                jsonObject.addProperty("imageBase64", imageBase64);//圖片
            }
            InstiutionBean ins = (InstiutionBean) params[4];
            jsonObject.addProperty("ins", new Gson().toJson(ins));//純文字
            if (params[5] != null) {
                String imageBase64In = params[5].toString();
                jsonObject.addProperty("imageBase64In", imageBase64In);//圖片
            } else {
                String imageBase64 = "0";
                jsonObject.addProperty("imageBase64In", imageBase64);//圖片
            }
        }
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