package com.laudhoot.persistence.repository;

import com.activeandroid.query.Select;
import com.laudhoot.persistence.model.ClientDetails;
import com.laudhoot.persistence.model.GeoFenceTransition;
import com.laudhoot.persistence.model.Geofence;
import com.laudhoot.web.model.GeoFenceTO;

import java.util.List;

/**
 * Persistence operations repository for geo fence transition model.
 *
 * Created by apurve on 9/5/15.
 */
public class GeoFenceRepository extends ActiveAndroidRepository {

    public GeoFenceRepository() {
        super(Geofence.class);
    }

    public Geofence save(GeoFenceTO geoFenceTO) {
        Geofence geofence = findByCode(geoFenceTO.getCode());
        if (geofence == null) {
            geofence = new Geofence(geoFenceTO);
        } else {
            geofence.setRadiusInMeters(geoFenceTO.getRadiusInMeters());
            geofence.setCenterLatitude(geoFenceTO.getCenter().getLatitude());
            geofence.setCenterLongitude(geoFenceTO.getCenter().getLongitude());
            geofence.setExpiresInHours(geoFenceTO.getExpiresInHours());
            geoFenceTO.setName(geoFenceTO.getName());
        }
        saveOrUpdate(geofence);
        return geofence;
    }

    public Geofence findByCode(String code) {
        return (Geofence) new Select()
                .from(Geofence.class)
                .where("code = ?", code)
                .executeSingle();
    }
}
