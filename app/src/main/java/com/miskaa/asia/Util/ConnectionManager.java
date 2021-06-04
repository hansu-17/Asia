package com.miskaa.asia.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionManager {

    public Boolean checkConnecctivity(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

            if (activeNetwork.isConnected()) {
                return activeNetwork.isConnected();
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

}
