package com.laudhoot.web.model;

/**
 * Transfer object for geo location coordinates.
 *
 * Created by root on 2/6/15.
 */
public class CoordinateTO extends BaseTO {

    private Double latitude;

    private Double longitude;

    public CoordinateTO(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
