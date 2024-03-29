package com.laudhoot.di.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

import com.laudhoot.Laudhoot;
import com.laudhoot.util.LocationStateManager;
import com.laudhoot.util.Toaster;
import com.laudhoot.view.activity.ContactUsActivity;
import com.laudhoot.view.activity.FAQSActivity;
import com.laudhoot.view.activity.InitializationActivity;
import com.laudhoot.util.NetworkStateManager;
import com.laudhoot.view.activity.MainActivity;
import com.laudhoot.view.activity.PostShoutActivity;
import com.laudhoot.view.activity.UserShoutsActivity;
import com.laudhoot.view.activity.ViewShoutActivity;
import com.laudhoot.view.fragment.ExploreFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.LOCATION_SERVICE;

/**
 * DI module used to provide system services.
 *
 * Created by apurve on 4/4/15.
 */

@Module(
        complete = false,
        library = true,
        injects = {
                InitializationActivity.class,
                MainActivity.class,
                PostShoutActivity.class,
                ViewShoutActivity.class,
                UserShoutsActivity.class,
                ExploreFragment.class,
                ContactUsActivity.class,
                FAQSActivity.class
        }
)
public class SystemServicesModule {

    private final Laudhoot application;

    public SystemServicesModule(Laudhoot application) {
        this.application = application;
    }

    @Provides @Singleton
    Laudhoot provideApplication(){
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
    LocationStateManager provideLocationStateManager(LocationManager locationManager) {
        return new LocationStateManager(locationManager);
    }

    @Provides @Singleton
    LocationManager provideLocationManager() {
        return (LocationManager) application.getSystemService(LOCATION_SERVICE);
    }

    @Provides @Singleton
    TelephonyManager provideTelephonyManager() {
        return (TelephonyManager) application.getSystemService(Context.TELEPHONY_SERVICE);
    }

    @Provides @Singleton
    Toaster providerToaster() {
        return new Toaster(application.getApplicationContext());
    }

}