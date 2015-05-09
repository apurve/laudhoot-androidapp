package com.laudhoot.di.module;

import com.laudhoot.persistence.repository.GeoFenceRepository;
import com.laudhoot.service.GeofenceTransitionsIntentService;
import com.laudhoot.view.fragment.GeoFenceFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by apurve on 3/5/15.
 */

@Module(
        complete = false,
        library = true,
        injects = {
                GeofenceTransitionsIntentService.class,
                GeoFenceFragment.class
        }
)
public class PersistenceServicesModule {
    GeoFenceRepository geoFenceRepository;

    public PersistenceServicesModule() {
        super();
        geoFenceRepository = new GeoFenceRepository();
    }

    @Provides @Singleton
    GeoFenceRepository provideGeoFenceRepository(){
        return geoFenceRepository;
    }
}
