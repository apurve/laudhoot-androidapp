package com.laudhoot.util;

import com.laudhoot.persistence.model.Geofence;
import com.laudhoot.web.model.GeoFenceTO;

/**
 * Contract specification as the name suggests.
 *
 * Created by root on 1/10/15.
 */
public interface GeofenceProvider extends LocationAware {
    public GeoFenceTO getGeofence();
    public Geofence getLastVisitedGeofence();
}
