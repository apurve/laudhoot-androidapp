package com.laudhoot.util;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by apurve on 4/4/15.
 */

public class NetworkStateManager {

    private final ConnectivityManager connectivityManager;

    public NetworkStateManager(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    public boolean isConnected(){
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo.isConnected();
    }

    public boolean isNotConnected(){
        return !isConnected();
    }

    public boolean isConnectedOrConnecting(){
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo.isConnectedOrConnecting();
    }

}