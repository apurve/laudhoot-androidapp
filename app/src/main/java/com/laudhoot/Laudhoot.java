package com.laudhoot;


import android.app.Application;

import com.activeandroid.ActiveAndroid;

/**
 * Created by apurve on 2/3/15.
 */
public class Laudhoot extends Application {

    public static final String LOG_TAG = "LAUDHOOT-LOG";

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        initializeRepositories();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }

    private void initializeRepositories(){

    }
}
