package com.laudhoot.view.activity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.laudhoot.Laudhoot;
import com.laudhoot.R;
import com.laudhoot.persistence.model.Geofence;
import com.laudhoot.persistence.repository.ClientDetailsRepository;
import com.laudhoot.persistence.repository.GeoFenceRepository;
import com.laudhoot.service.LocationApi;
import com.laudhoot.util.LocationApiErrorHandler;
import com.laudhoot.util.LocationAware;
import com.laudhoot.web.model.CoordinateTO;
import com.laudhoot.web.model.GeoFenceTO;
import com.laudhoot.web.services.LaudhootAPI;
import com.laudhoot.web.util.BaseCallback;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * The activity is aware of the location of mobile device to host respective user interacting fragments.
 * <p/>
 * Created by root on 12/4/15.
 */
public class MainActivity extends PagerActivity implements LocationAware, LocationApiErrorHandler {

    private static final String DIALOG_ERROR = "dialog_error";

    private String clientId;

    private String geofenceCode;

    private GeoFenceTO geoFenceTO;

    private boolean resolvingLocationError = false;

    private LocationApi locationApi;

    @Inject
    protected ClientDetailsRepository clientDetailsRepository;

    @Inject
    protected GeoFenceRepository geoFenceRepository;

    @Inject
    protected LaudhootAPI laudhootAPI;

    @Inject
    protected TelephonyManager telephonyManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Laudhoot) (getApplication())).inject(this);
        clientId = getIntent().getStringExtra(InitializationActivity.CLIENT_ID);
        geofenceCode = getIntent().getStringExtra(InitializationActivity.GEOFENCE_CODE);
        locationApi = new LocationApi(this, this);
        locationApi.create(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationApi.start();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(InitializationActivity.CLIENT_ID, clientId);
        outState.putString(InitializationActivity.GEOFENCE_CODE, geofenceCode);
        locationApi.saveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationApi.stop();
    }

    public void refreshGeofence() {
        Location location = getLocation();
        laudhootAPI.findGeoFence(location.getLatitude(), location.getLongitude(), "",new BaseCallback<GeoFenceTO>(getApplicationContext()) {
            @Override
            protected void success(GeoFenceTO geoFenceTO, Response response, Context context) {
                Geofence geofence = geoFenceRepository.save(geoFenceTO);
                clientDetailsRepository.updateVisitingGeofence(geofence, clientId);
                geofenceCode = geofence.getCode();
            }

            @Override
            protected void failure(RetrofitError error, Context context) {
                toaster.makeToast(getApplicationContext().getString(R.string.failure_resolving_geofence));
            }
        });
    }

    public Geofence getLastVisitedGeofence() {
        return clientDetailsRepository.getLastVisitedGeofence(clientId);
    }

    public String getClientId() {
        return clientId;
    }

    public String getGeofenceCode() {
        return geofenceCode;
    }

    @Override
    public Location getLocation() {
        return locationApi.getLocation();
    }

    @Override
    public Location getLastLocation() {
        return locationApi.getLastLocation();
    }

    @Override
    public void handleError(int errorCode) {
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        resolvingLocationError = true;
        dialogFragment.show(this.getFragmentManager(), "errordialog");
    }

    @Override
    public boolean resolvingError() {
        return resolvingLocationError;
    }

    public void resolvedError() {
        resolvingLocationError = false;
    }

    public static class ErrorDialogFragment<T> extends DialogFragment {

        public ErrorDialogFragment() {

        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GooglePlayServicesUtil.getErrorDialog(errorCode, this.getActivity(), LocationApi.REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((MainActivity) getActivity()).resolvedError();
        }
    }

    public GoogleApiClient getLocationApiClient() {
        return locationApi.getLocationApiClient();
    }

}
