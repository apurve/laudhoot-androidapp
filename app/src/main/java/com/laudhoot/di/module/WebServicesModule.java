package com.laudhoot.di.module;

import android.app.Application;

import com.laudhoot.Laudhoot;
import com.laudhoot.view.activity.InitializationActivity;
import com.laudhoot.view.fragment.GeoFenceFragment;
import com.laudhoot.view.fragment.PlaceholderFragment;
import com.laudhoot.web.LaudhootRestClient;
import com.laudhoot.web.services.LaudhootAPI;
import com.laudhoot.web.services.TestAPI;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by apurve on 5/4/15.
 */

@Module(
        complete = false,
        library = true,
        injects = {
                Laudhoot.class,
                PlaceholderFragment.class,
                GeoFenceFragment.class,
                InitializationActivity.class
        }
)
public class WebServicesModule {

    private final LaudhootRestClient laudhootRestClient;

    public WebServicesModule(Application application) {
        laudhootRestClient = new LaudhootRestClient(application);
    }

    @Provides @Singleton
    LaudhootAPI provideLaudhootWebAPI() {
        return laudhootRestClient.getShoutWebService();
    }

    @Provides @Singleton
    TestAPI provideTestWebAPI() {
        return laudhootRestClient.getTestWebService();
    }

}
