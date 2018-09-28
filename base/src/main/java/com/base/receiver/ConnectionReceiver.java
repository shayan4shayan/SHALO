package com.base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by shayan on 7/7/2017.
 */

public class ConnectionReceiver extends BroadcastReceiver {

    private ConnectionInterface listener;

    public ConnectionReceiver(ConnectionInterface listener){

        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            listener.onNoConnection();
        } else if (networkInfo.isConnected()) {
            listener.onConnected();
        }

    }
}
