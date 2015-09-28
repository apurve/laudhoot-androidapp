package com.laudhoot.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

import com.laudhoot.Laudhoot;
import com.laudhoot.R;
import com.laudhoot.view.activity.InitializationActivity;

/**
 * Created by root on 6/6/15.
 */
public class LocationStateManager {

    private final LocationManager locationManager;

    public LocationStateManager(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    public boolean isGPSEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public boolean isNetworkEnabled() {
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public boolean hasGPS() {
        return locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER);
    }

    public void requestLocationServicesEnable(Activity activity) {
        final Activity dialogActivity = activity;
        if (!isGPSEnabled() && !isNetworkEnabled()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(dialogActivity);
            dialogBuilder.setCancelable(false);
            dialogBuilder.setTitle(R.string.title_enable_gps_network);
            dialogBuilder.setMessage(dialogActivity.getResources().getString(R.string.enable_gps_network));
            dialogBuilder.setPositiveButton(dialogActivity.getResources().getString(R.string.laud), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int paramInt) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    dialogActivity.startActivity(intent);
                    dialog.dismiss();
                }
            });
            dialogBuilder.setNegativeButton(dialogActivity.getString(R.string.hoot), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int paramInt) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = dialogBuilder.create();
            alert.show();
        }
    }

}
