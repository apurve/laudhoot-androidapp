package com.laudhoot.di.module;

import android.app.Application;

import com.laudhoot.view.activity.FAQSActivity;
import com.laudhoot.view.activity.InitializationActivity;
import com.laudhoot.view.activity.MainActivity;
import com.laudhoot.view.activity.PostShoutActivity;
import com.laudhoot.view.activity.UserShoutsActivity;
import com.laudhoot.view.activity.ViewShoutActivity;
import com.laudhoot.view.adapter.ShoutAdapter;
import com.laudhoot.view.fragment.ShoutFeedFragment;
import com.laudhoot.web.LaudhootRestClient;
import com.laudhoot.web.services.ClientAPI;
import com.laudhoot.web.services.LaudhootAPI;
import com.laudhoot.web.services.TestAPI;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * DI module used to provide web services.
 *
 * Created by apurve on 5/4/15.
 */

@Module(
        complete = false,
        library = true,
        injects = {
                ShoutFeedFragment.class,
                InitializationActivity.class,
                MainActivity.class,
                PostShoutActivity.class,
                ViewShoutActivity.class,
                UserShoutsActivity.class,
                ShoutAdapter.class,
                FAQSActivity.class
        }
)
public class WebServicesModule {

    private final LaudhootRestClient laudhootRestClient;

    public WebServicesModule(Application application) {
        laudhootRestClient = new LaudhootRestClient(application);
    }

    @Provides @Singleton
    ClientAPI provideClientWebAPI() {
        return laudhootRestClient.getClientWebServices();
    }

    @Provides @Singleton
    LaudhootAPI provideLaudhootWebAPI() {
        return laudhootRestClient.getLaudhootWebService();
    }

    @Provides @Singleton
    TestAPI provideTestWebAPI() {
        return laudhootRestClient.getTestWebService();
    }

}
