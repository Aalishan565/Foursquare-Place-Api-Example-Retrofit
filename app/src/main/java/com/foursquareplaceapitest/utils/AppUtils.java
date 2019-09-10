package com.foursquareplaceapitest.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


public final class AppUtils {

    private static ProgressDialog progress;

    private AppUtils() {

    }

    public static boolean getNetworkConnectivityState(Context context) {
        boolean connected = false;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                connected = true;
        }
        return connected;
    }

    public static void showToastWithMsg(Context ctx,String message) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }

    public static void hideKeyboard(Context mContext, View v) {
        if (v != null) {
            InputMethodManager inputManager = (InputMethodManager) mContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public static void showProgressDialog(Context mContext) {
        progress = new ProgressDialog(mContext);
        progress.setTitle("");
        progress.setMessage("Please Wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

    }

    public static void dismissProgressDialog() {
        if (progress.isShowing()) {
            progress.dismiss();
        }

    }

}

