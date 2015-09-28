package com.laudhoot.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.laudhoot.Laudhoot;
import com.laudhoot.R;
import com.laudhoot.persistence.repository.ClientDetailsRepository;
import com.laudhoot.util.LocationStateManager;
import com.laudhoot.util.NetworkStateManager;
import com.laudhoot.util.WebTask;
import com.laudhoot.web.model.ClientDetailTO;
import com.laudhoot.web.services.LaudhootAPI;

import javax.inject.Inject;

import retrofit.RetrofitError;

/**
 * Android activity for initialization operations.
 *
 * The activity checks for availability of data network and gps. It also registers a client if one is not present
 * in the local sqlite database.
 *
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
    LaudhootAPI laudhootAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialization);
        ((Laudhoot) (getApplication())).inject(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        startInitializationAnimate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleNetworkServices();
    }

    private void handleNetworkServices() {
        if(networkStateManager.isConnected()) {
            if(clientDetailsRepository.isClientSetup()) {
                handelLocationServices();
            } else {
                new ClientRegistrationWebTask().execute();
            }
        } else {
            networkStateManager.requestNetworkServicesEnable(this);
        }
    }

    private void handelLocationServices() {
        if(locationStateManager.isGPSEnabled()){
            moveToHome();
        }else{
            locationStateManager.requestLocationServicesEnable(this);
        }
    }

    class ClientRegistrationWebTask extends WebTask {

        public ClientRegistrationWebTask(){
            super(InitializationActivity.this, networkStateManager);
        }

        @Override
        protected String doInBackground(String... params) {
            super.doInBackground(params);
            ClientDetailTO clientDetailTO = new ClientDetailTO(telephonyManager.getDeviceId());
            try {
                clientDetailTO = laudhootAPI.clientRegistration(clientDetailTO);
            }catch(RetrofitError error) {
                Log.d(Laudhoot.LOG_TAG, error.getMessage());
            }catch(Exception error){
                Log.d(Laudhoot.LOG_TAG, error.getMessage());
            }
            if(clientDetailTO.hasError()) {
                return clientDetailTO.getError();
            } else {
                clientDetailsRepository.create(clientDetailTO.getClientId(), clientDetailTO.getClientSecret());
                return null; // success but we do not to show any message
            }
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                makeToast(response);
            } else {
                handelLocationServices();
            }
        }
    }

    private void moveToHome() {
        startInitializationAnimate();
        Intent i = new Intent(InitializationActivity.this, LocationAwareActivity.class);
        startActivity(i);
        finish();
    }

    private void startInitializationAnimate() {
        //Thread.sleep(3000); TODO - initialization animation
    }

    public void makeToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void makeShortToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
