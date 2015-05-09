package com.laudhoot.view.activity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.laudhoot.Laudhoot;

/**
 * Created by root on 12/4/15.
 */
public class LocationAwareActivity extends PagerActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient locationApiClient;

    private boolean resolvingError = false;
    public static final int REQUEST_RESOLVE_ERROR = 1001;
    private static final String STATE_RESOLVING_ERROR = "resolving_error";
    private static final String DIALOG_ERROR = "dialog_error";

    private Location lastLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildLocationApiClient();
        resolvingError = savedInstanceState != null
                && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR);
    }

    private void buildLocationApiClient() {
        locationApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!resolvingError) {
            locationApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(locationApiClient);
        if(lastLocation != null){
            makeToast("Latitude - "+lastLocation.getLatitude()
            +" | Longitude - "+lastLocation.getLongitude());
        }else{
            makeShortToast("Location unavailable.");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //TODO
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (resolvingError) {
            return;
        } else if (connectionResult.hasResolution()) {
            try {
                resolvingError = true;
                connectionResult.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // there was an error with the resolution intent. Try again / notify user.
                makeToast("Cannot connect to web services");
                Log.e(Laudhoot.LOG_TAG, e.getMessage());
                //locationApiClient.connect();
            }
        } else {
            showErrorDialog(connectionResult.getErrorCode());
            resolvingError = true;
        }
    }

    private void showErrorDialog(int errorCode) {
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getFragmentManager(), "errordialog");
    }

    public void onDialogDismissed() {
        resolvingError = false;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR, resolvingError);
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationApiClient.disconnect();
    }

    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GooglePlayServicesUtil.getErrorDialog(errorCode, this.getActivity(), REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((LocationAwareActivity) getActivity()).onDialogDismissed();
        }
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public GoogleApiClient getLocationApiClient() {
        return locationApiClient;
    }
}
