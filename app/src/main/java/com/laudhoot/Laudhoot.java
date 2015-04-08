package com.laudhoot;


import android.app.Application;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.laudhoot.di.module.SystemServicesModule;
import com.laudhoot.di.module.WebServicesModule;
import com.laudhoot.util.NetworkStateManager;
import com.laudhoot.web.TestAPI;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;

/**
 * Created by apurve on 2/3/15.
 */
public class Laudhoot extends Application {

    public static final String LOG_TAG = "LAUDHOOT-LOG";
    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        initializeServices();
        inject(this);
    }

    private void initializeServices() {
        objectGraph = ObjectGraph.create(getModules().toArray());
    }

    private List<Object> getModules() {
        return Arrays.<Object>asList(
                new SystemServicesModule(Laudhoot.this),
                new WebServicesModule(Laudhoot.this)
        );
    }

    public void inject(Object object) { objectGraph.inject(object); }

    public ObjectGraph createScopedGraph(Object... modules) {
        return objectGraph.plus(modules);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }

}