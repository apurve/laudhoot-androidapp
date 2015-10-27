package com.laudhoot.di.module;

import com.laudhoot.persistence.repository.ClientDetailsRepository;
import com.laudhoot.persistence.repository.GeoFenceRepository;
import com.laudhoot.service.GeofenceTransitionsIntentService;
import com.laudhoot.view.activity.InitializationActivity;
import com.laudhoot.view.activity.MainActivity;
import com.laudhoot.view.fragment.ShoutFeedFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module used to provide persistence services.
 *
 * Created by apurve on 3/5/15.
 */

@Module(
        complete = false,
        library = true,
        injects = {
                GeofenceTransitionsIntentService.class,
                ShoutFeedFragment.class,
                InitializationActivity.class,
                MainActivity.class
        }
)
public class PersistenceServicesModule {

    GeoFenceRepository geoFenceRepository;

    ClientDetailsRepository clientDetailsRepository;

    public PersistenceServicesModule() {
        super();
        geoFenceRepository = new GeoFenceRepository();
        clientDetailsRepository = new ClientDetailsRepository();
    }

    @Provides @Singleton
    GeoFenceRepository provideGeoFenceRepository(){
        return geoFenceRepository;
    }

    @Provides @Singleton
    ClientDetailsRepository provideClientDetailsRepository(){
        return clientDetailsRepository;
    }

}
