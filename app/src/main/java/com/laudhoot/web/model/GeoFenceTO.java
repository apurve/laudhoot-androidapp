package com.laudhoot.web.model;

/**
 * Transfer object for geofence.
 *
 * Created by root on 2/6/15.
 */
public class GeoFenceTO extends BaseTO {

    private String name;

    private String code;

    private String description;

    private CoordinateTO center;

    private Integer radiusInMeters;

    private Integer expiresInHours;

    public GeoFenceTO() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CoordinateTO getCenter() {
        return center;
    }

    public void setCenter(CoordinateTO center) {
        this.center = center;
    }

    public Integer getRadiusInMeters() {
        return radiusInMeters;
    }

    public void setRadiusInMeters(Integer radiusInMeters) {
        this.radiusInMeters = radiusInMeters;
    }

    public Integer getExpiresInHours() {
        return expiresInHours;
    }

    public void setExpiresInHours(Integer expiresInHours) {
        this.expiresInHours = expiresInHours;
    }
}
