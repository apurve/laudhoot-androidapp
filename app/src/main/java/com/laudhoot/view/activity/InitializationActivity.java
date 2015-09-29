package com.laudhoot.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.laudhoot.Laudhoot;
import com.laudhoot.R;
import com.laudhoot.persistence.model.ClientDetails;
import com.laudhoot.persistence.repository.ClientDetailsRepository;
import com.laudhoot.web.WebConstants;
import com.laudhoot.web.model.TokenResponse;
import com.laudhoot.web.services.ClientAPI;
import com.laudhoot.web.util.AuthorizationUtil;
import com.laudhoot.web.util.BaseCallback;
import com.laudhoot.util.LocationStateManager;
import com.laudhoot.util.NetworkStateManager;
import com.laudhoot.web.model.ClientDetailTO;

import javax.inject.Inject;

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
public class InitializationActivity extends Activity {

    @Inject
    NetworkStateManager networkStateManager;

    @Inject
    LocationStateManager locationStateManager;

    @Inject
    TelephonyManager telephonyManager;

    @Inject
    ClientDetailsRepository clientDetailsRepository;

    @Inject
    ClientAPI clientAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialization);
        ((Laudhoot) (getApplication())).inject(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startInitializationAnimation();
        if (handleNetworkServices()) {
            if (clientDetailsRepository.isClientSetup()) {
                handleAccessToken(telephonyManager.getDeviceId());
            } else {
                ClientDetailTO clientDetailTO = new ClientDetailTO(telephonyManager.getDeviceId());
                clientAPI.clientRegistration(clientDetailTO, new BaseCallback<ClientDetailTO>(getApplicationContext(), true) {
                    @Override
                    protected void success(ClientDetailTO clientDetailTO, Response response, Context context) {
                        clientDetailsRepository.create(clientDetailTO.getClientId(), clientDetailTO.getClientSecret());
                        handleAccessToken(clientDetailTO.getClientId());
                    }

                    @Override
                    protected void failure(RetrofitError error, Context context) {
                        finish();
                    }
                });
            }
        }
    }

    private void handleAccessToken(final String clientId) {
        if (clientDetailsRepository.isAccessTokenValid(clientId)) {
            if (handelLocationServices()) {
                moveToHome();
            }
        } else {
            ClientDetails clientDetails = clientDetailsRepository.findByClientId(clientId);
            String authorizationHeader = AuthorizationUtil.basicAuthorization(clientId, clientDetails.getClientSecret());
            clientAPI.requestClientToken(authorizationHeader,
                    WebConstants.CLIENT_GRANT_TYPE, WebConstants.TOKEN_RESPONSE_TYPE,
                    new BaseCallback<TokenResponse>(getApplicationContext()) {
                        @Override
                        protected void success(TokenResponse tokenResponse, Response response, Context context) {
                            clientDetailsRepository.updateTokens(clientId, tokenResponse);
                            if (handelLocationServices()) {
                                moveToHome();
                            }
                        }

                        @Override
                        protected void failure(RetrofitError error, Context context) {
                            //TODO - failure handling
                            //clientDetailsRepository.purgeRepository();
                        }
                    });
        }
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

    private void moveToHome() {
        stopInitializationAnimation();
        Intent i = new Intent(InitializationActivity.this, LocationAwareActivity.class);
        startActivity(i);
        finish();
    }

    private void stopInitializationAnimation() {
        //TODO - initialization animation
    }

    private void startInitializationAnimation() {
        //TODO - initialization animation
    }

}
