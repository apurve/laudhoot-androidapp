package com.laudhoot.view.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.laudhoot.Laudhoot;
import com.laudhoot.R;
import com.laudhoot.persistence.model.ClientDetails;
import com.laudhoot.persistence.model.Geofence;
import com.laudhoot.persistence.repository.ClientDetailsRepository;
import com.laudhoot.persistence.repository.GeoFenceRepository;
import com.laudhoot.service.LocationApi;
import com.laudhoot.util.LocationApiErrorHandler;
import com.laudhoot.util.LocationAware;
import com.laudhoot.util.Toaster;
import com.laudhoot.web.WebConstants;
import com.laudhoot.web.model.GeoFenceTO;
import com.laudhoot.web.model.TokenResponse;
import com.laudhoot.web.services.ClientAPI;
import com.laudhoot.web.services.LaudhootAPI;
import com.laudhoot.web.util.AuthorizationUtil;
import com.laudhoot.web.util.BaseCallback;
import com.laudhoot.util.LocationStateManager;
import com.laudhoot.util.NetworkStateManager;
import com.laudhoot.web.model.ClientDetailTO;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Android activity for initialization operations.
 * <p/>
 * The activity checks for availability of data network and gps. It also registers a client if one is not present
 * in the local sqlite database.
 * <p/>
 * Created by apurve on 6/6/15.
 */
public class InitializationActivity extends Activity implements LocationAware, LocationApiErrorHandler {

    public static final String CLIENT_ID = "client_id";
    public static final String GEOFENCE_CODE = "GEOFENCE_CODE";

    private static final String DIALOG_ERROR = "dialog_error";

    private boolean resolvingLocationError = false;

    private LocationApi locationApi;

    private ClientDetails clientDetails;

    @InjectView(R.id.init_message)
    public TextView initMessages;

    @Inject
    NetworkStateManager networkStateManager;

    @Inject
    LocationStateManager locationStateManager;

    @Inject
    TelephonyManager telephonyManager;

    @Inject
    ClientDetailsRepository clientDetailsRepository;

    @Inject
    GeoFenceRepository geoFenceRepository;

    @Inject
    ClientAPI clientAPI;

    @Inject
    LaudhootAPI laudhootAPI;

    @Inject
    Toaster toaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialization);
        ButterKnife.inject(this);
        ((Laudhoot) (getApplication())).inject(this);
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
        locationApi.saveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationApi.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startInitializationAnimation();
        String clientId = telephonyManager.getDeviceId();
        if (handleNetworkServices()) {
            if (clientDetailsRepository.isClientSetup()) {
                clientDetails = clientDetailsRepository.findByClientId(clientId);
                handleAccessToken();
            } else {
                initMessages.setText(getApplicationContext().getString(R.string.registering_anonymous_client));
                ClientDetailTO clientDetailTO = new ClientDetailTO(clientId);
                clientAPI.clientRegistration(clientDetailTO, new BaseCallback<ClientDetailTO>(getApplicationContext(), true) {
                    @Override
                    protected void success(ClientDetailTO clientDetailTO, Response response, Context context) {
                        clientDetails = clientDetailsRepository.create(clientDetailTO.getClientId(), clientDetailTO.getClientSecret());
                        handleAccessToken();
                    }

                    @Override
                    protected void failure(RetrofitError error, Context context) {
                        toaster.makeToast(getApplicationContext().getString(R.string.failure_registering_anonymous_client));
                        finish();
                    }
                });
            }
        }
    }

    private void handleAccessToken() {
        if (clientDetailsRepository.isAccessTokenValid(clientDetails.getClientId())) {
            if (handelLocationServices()) {
                resolveLocationAndGeoFence();
            }
        } else {
            initMessages.setText(getApplicationContext().getString(R.string.getting_tokens));
            String authorizationHeader = AuthorizationUtil.basicAuthorization(clientDetails.getClientId(), clientDetails.getClientSecret());
            clientAPI.requestClientToken(authorizationHeader,
                    WebConstants.CLIENT_GRANT_TYPE, WebConstants.TOKEN_RESPONSE_TYPE,
                    new BaseCallback<TokenResponse>(getApplicationContext()) {
                        @Override
                        protected void success(TokenResponse tokenResponse, Response response, Context context) {
                            clientDetailsRepository.updateTokens(clientDetails.getClientId(), tokenResponse);
                            if (handelLocationServices()) {
                                resolveLocationAndGeoFence();
                            }
                        }

                        @Override
                        protected void failure(RetrofitError error, Context context) {
                            toaster.makeToast(getApplicationContext().getString(R.string.failure_getting_tokens));
                            //clientDetailsRepository.purgeRepository();
                        }
                    });
        }
    }

    private void resolveLocationAndGeoFence() {
        new LocationTask().execute(this);
    }

    private static class LocationTask extends AsyncTask<InitializationActivity, String, InitializationActivity> {

        Location location;

        @Override
        protected InitializationActivity doInBackground(InitializationActivity... params) {
            location = params[0].getLocation();
            while (location == null) {
                try {
                    Thread.sleep(300);
                    doInBackground(params);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return params[0];
        }

        @Override
        protected void onPostExecute(InitializationActivity response) {
            response.resolveGeofence(location);
        }
    }

    private void resolveGeofence(Location location) {
        initMessages.setText(getApplicationContext().getString(R.string.resolving_geofence));
        laudhootAPI.findGeoFence(location.getLatitude(), location.getLongitude(),
                AuthorizationUtil.authorizationToken(clientDetails),
                new BaseCallback<GeoFenceTO>(getApplicationContext()) {
            @Override
            protected void success(GeoFenceTO geoFenceTO, Response response, Context context) {
                if(geoFenceTO != null && geoFenceTO.getCode() != null) {
                    Geofence geofence = geoFenceRepository.save(geoFenceTO);
                    clientDetailsRepository.updateVisitingGeofence(geofence, clientDetails.getClientId());
                    moveToHome(geofence.getCode());
                } else {
                    Geofence oldGeofence = clientDetailsRepository.getLastVisitedGeofence(clientDetails.getClientId());
                    if(oldGeofence != null)
                        moveToHome(oldGeofence.getCode());
                    else {
                        toaster.makeToast(getApplicationContext().getString(R.string.failure_resolving_geofence));
                        finish();
                    }
                }
            }

            @Override
            protected void failure(RetrofitError error, Context context) {
                Geofence oldGeofence = clientDetailsRepository.getLastVisitedGeofence(clientDetails.getClientId());
                if(oldGeofence != null)
                    moveToHome(oldGeofence.getCode());
                else {
                    toaster.makeToast(getApplicationContext().getString(R.string.failure_resolving_geofence));
                    finish();
                }
            }
        });
    }

    private boolean handleNetworkServices() {
        if (networkStateManager.isConnected()) return true;
        else networkStateManager.requestNetworkServicesEnable(this);
        return false;
    }

    private boolean handelLocationServices() {
        if (locationStateManager.isGPSEnabled()) return true;
        else locationStateManager.requestLocationServicesEnable(this);
        return false;
    }

    private void moveToHome(String geofenceCode) {
        stopInitializationAnimation();
        Intent i = new Intent(InitializationActivity.this, MainActivity.class);
        i.putExtra(CLIENT_ID, clientDetails.getClientId());
        i.putExtra(GEOFENCE_CODE, geofenceCode);
        startActivity(i);
        finish();
    }

    private void stopInitializationAnimation() {
        //TODO - initialization animation
    }

    private void startInitializationAnimation() {
        //TODO - initialization animation
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
            ((InitializationActivity) getActivity()).resolvedError();
        }
    }
}
