package com.laudhoot.service;

import android.app.Activity;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.laudhoot.Laudhoot;
import com.laudhoot.util.LocationApiErrorHandler;
import com.laudhoot.util.LocationAware;
import com.laudhoot.util.Toaster;

/**
 * Google location api supported location provider, for usage of this api with proper error handling one must
 * implement
 * <p/>
 * Created by root on 7/10/15.
 */
public class LocationApi implements LocationAware, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Activity activity;
    private GoogleApiClient locationApiClient;

    private boolean resolvingError = false;
    public static final int REQUEST_RESOLVE_ERROR = 1001;
    private static final String STATE_RESOLVING_ERROR = "resolving_error";

    LocationApiErrorHandler errorHandler;
    private Location lastLocation;

    private Toaster toaster;

    public LocationApi(Activity activity, LocationApiErrorHandler errorHandler) {
        this.activity = activity;
        this.errorHandler = errorHandler;
    }

    public void create(Bundle savedInstanceState) {
        toaster = new Toaster(activity.getApplicationContext());
        buildLocationApiClient();
        resolvingError = savedInstanceState != null
                && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR);
    }

    public void start() {
        if (!resolvingError && !errorHandler.resolvingError()) {
            locationApiClient.connect();
        }
    }

    public void stop() {
        locationApiClient.disconnect();
    }

    public void saveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_RESOLVING_ERROR, resolvingError);
    }

    private void buildLocationApiClient() {
        locationApiClient = new GoogleApiClient.Builder(activity.getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public Location getLastLocation() {
        return lastLocation;
    }

    @Override
    public Location getLocation() {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(locationApiClient);
        return lastLocation;
    }

    @Override
    public void onConnectionSuspended(int i) {
        //TODO
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (resolvingError && errorHandler.resolvingError()) {
            return;
        } else if (connectionResult.hasResolution()) {
            try {
                resolvingError = true;
                connectionResult.startResolutionForResult(activity, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // there was an error with the resolution intent. Try again / notify user.
                toaster.makeToast("Cannot connect to web services");
                Log.e(Laudhoot.LOG_TAG, e.getMessage());
                //locationApiClient.connect();
            }
        } else {
            errorHandler.handleError(connectionResult.getErrorCode());
            resolvingError = false;
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        lastLocation = getLocation();
        if (lastLocation != null) {
            if(Laudhoot.D) {
                toaster.makeToast("Latitude - " + lastLocation.getLatitude()
                        + " | Longitude - " + lastLocation.getLongitude());
            }
            Log.d(Laudhoot.LOG_TAG, "Latitude - " + lastLocation.getLatitude()
                    + " | Longitude - " + lastLocation.getLongitude());
        } else {
            toaster.makeLongToast("Location unavailable.");
        }
    }

    public GoogleApiClient getLocationApiClient() {
        return locationApiClient;
    }

}
