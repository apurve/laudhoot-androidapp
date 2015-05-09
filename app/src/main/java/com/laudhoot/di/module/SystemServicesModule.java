package com.laudhoot.di.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

import com.laudhoot.Laudhoot;
import com.laudhoot.view.fragment.PlaceholderFragment;
import com.laudhoot.util.NetworkStateManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by apurve on 4/4/15.
 */

@Module(
        complete = false,
        library = true,
        injects = {
                Laudhoot.class,
                PlaceholderFragment.class
        }
)
public class SystemServicesModule {

    private final Laudhoot application;

    public SystemServicesModule(Laudhoot application) {
        this.application = application;
    }

    @Provides @Singleton
    Application provideApplication(){
        return application;
    }

    @Provides @Singleton
    Context provideApplicationContext() { return application.getApplicationContext(); }

    @Provides @Singleton
    SharedPreferences providePreferenceManager() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides @Singleton
    ConnectivityManager provideConnectivityManager() {
        return (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides @Singleton
    NetworkStateManager provideNetworkStateManager(ConnectivityManager connectivityManager) {
        return new NetworkStateManager(connectivityManager);
    }

    @Provides @Singleton
    LocationManager provideLocationManager() {
        return (LocationManager) application.getSystemService(LOCATION_SERVICE);
    }

}