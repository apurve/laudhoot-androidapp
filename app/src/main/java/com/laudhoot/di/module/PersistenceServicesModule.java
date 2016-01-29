package com.laudhoot.di.module;

import com.laudhoot.persistence.repository.ClientDetailsRepository;
import com.laudhoot.persistence.repository.GeoFenceRepository;
import com.laudhoot.persistence.repository.NotificationRepository;
import com.laudhoot.persistence.repository.ReplyRepository;
import com.laudhoot.persistence.repository.ShoutRepository;
import com.laudhoot.service.GeofenceTransitionsIntentService;
import com.laudhoot.view.activity.InitializationActivity;
import com.laudhoot.view.activity.MainActivity;
import com.laudhoot.view.activity.PostShoutActivity;
import com.laudhoot.view.activity.UserShoutsActivity;
import com.laudhoot.view.activity.ViewShoutActivity;
import com.laudhoot.view.adapter.ShoutAdapter;
import com.laudhoot.view.fragment.ShoutFeedFragment;
import com.laudhoot.view.fragment.UserFragment;

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
                MainActivity.class,
                PostShoutActivity.class,
                ViewShoutActivity.class,
                UserShoutsActivity.class,
                ShoutAdapter.class,
                UserFragment.class
        }
)
public class PersistenceServicesModule {

    GeoFenceRepository geoFenceRepository;

    ClientDetailsRepository clientDetailsRepository;

    ShoutRepository shoutRepository;

    ReplyRepository replyRepository;

    NotificationRepository notificationRepository;

    public PersistenceServicesModule() {
        super();
        geoFenceRepository = new GeoFenceRepository();
        clientDetailsRepository = new ClientDetailsRepository();
        replyRepository = new ReplyRepository();
        shoutRepository = new ShoutRepository(replyRepository);
        notificationRepository = new NotificationRepository();
    }

    @Provides @Singleton
    GeoFenceRepository provideGeoFenceRepository(){
        return geoFenceRepository;
    }

    @Provides @Singleton
    ClientDetailsRepository provideClientDetailsRepository(){
        return clientDetailsRepository;
    }

    @Provides @Singleton
    ShoutRepository provideShoutRepository() {
        return shoutRepository;
    }

    @Provides @Singleton
    ReplyRepository provideReplyRepository() {return replyRepository;}

    @Provides @Singleton
    NotificationRepository provideNotificationRepository() {return notificationRepository;}

}
