package com.laudhoot.util;

import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Contract specification as the name suggests.
 *
 * Created by root on 7/10/15.
 */
public interface LocationAware {
    public Location getLocation();

    public Location getLastLocation();
}
