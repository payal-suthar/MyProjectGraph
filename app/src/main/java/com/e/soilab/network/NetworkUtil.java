package com.e.soilab.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkUtil
{

    public static boolean isNetworkAvailable(Context mContext) {

        boolean isResult = Boolean.FALSE;
        final ConnectivityManager connManager = (ConnectivityManager) mContext.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = connManager.getActiveNetworkInfo();
        if (netInfo == null)
        {
            Toast.makeText(mContext,"Slow or no internet connection.Please check your internet settings.",Toast.LENGTH_LONG).show();
        }else
        {
            if (netInfo.isConnected() && netInfo.isAvailable()||
                    (netInfo.getType() != ConnectivityManager.TYPE_WIFI
                            && netInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                isResult = true;
            }
        }

        return isResult;
    }

    public static boolean isInternetOn(Context mContext) {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) mContext.getSystemService(Context
                        .CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {

            // if connected with internet
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }
}

