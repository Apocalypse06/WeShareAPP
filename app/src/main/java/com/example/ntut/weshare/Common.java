package com.example.ntut.weshare;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Common {
    // public static String URL = "http://192.168.1.108:8080/WeShare/";
    public static String URL = "http://10.0.2.2:8080/WeShare/";
    public final static String PREF_FILE = "preference";


    // check if the device connect to the network
    public static boolean networkConnected(Activity activity) {
        ConnectivityManager conManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void showToast(Context context, int messageResId) {
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

//    public static void loadPreferences() {
//        getSharedPreferences("data", MODE_PRIVATE).getBoolean("LoginOK", false);
//
//        sharedPreferences = ctxt.getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);
//    }
}


