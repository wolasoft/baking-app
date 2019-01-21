package com.wolasoft.bakingapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;

        networkInfo = manager != null ? manager.getActiveNetworkInfo() : null;

        return networkInfo != null && networkInfo.isConnected();
    }
}
