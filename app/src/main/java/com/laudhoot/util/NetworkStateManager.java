package com.laudhoot.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.laudhoot.R;
import com.laudhoot.view.activity.InitializationActivity;

/**
 * Utility class to provide data network management.
 *
 * Created by apurve on 4/4/15.
 */

public class NetworkStateManager {

    private final ConnectivityManager connectivityManager;

    public NetworkStateManager(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    public boolean isConnected(){
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            return activeNetworkInfo.isConnected();
        }
        return false;
    }

    public boolean isNotConnected(){
        return !isConnected();
    }

    public boolean isConnectedOrConnecting(){
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            return activeNetworkInfo.isConnectedOrConnecting();
        }
        return false;
    }

    public void requestNetworkServicesEnable(Activity activity) {
        final Activity dialogActivity = activity;
        if (!isConnected()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(dialogActivity);
            dialogBuilder.setCancelable(false);
            dialogBuilder.setTitle(R.string.title_enable_data_network);
            dialogBuilder.setMessage(dialogActivity.getResources().getString(R.string.enable_data_network));
            dialogBuilder.setPositiveButton(dialogActivity.getResources().getString(R.string.wifi), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int paramInt) {
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    dialogActivity.startActivity(intent);
                    dialog.dismiss();
                }
            });
            dialogBuilder.setNeutralButton(dialogActivity.getString(R.string.data), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int paramInt) {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(
                            "com.android.settings",
                            "com.android.settings.Settings$DataUsageSummaryActivity"));
                    dialogActivity.startActivity(intent);
                    dialog.dismiss();
                }
            });
            AlertDialog alert = dialogBuilder.create();
            alert.show();
        }
    }

}