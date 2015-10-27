package com.laudhoot.persistence.model;

import com.activeandroid.annotation.Column;
import com.laudhoot.web.model.CoordinateTO;
import com.laudhoot.web.model.GeoFenceTO;

/**
 * Persistence unit for a geofence.
 * <p/>
 * Created by root on 2/10/15.
 */
public class Geofence extends BaseModel {

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "center_latitude")
    private Double centerLatitude;

    @Column(name = "center_longitude")
    private Double centerLongitude;

    @Column(name = "radius_in_meters")
    private Integer radiusInMeters;

    @Column(name = "expires_in_hours")
    private Integer expiresInHours;

    public Geofence() {
        super();
    }

    public Geofence(String code, Double centerLatitude, Double centerLongitude, Integer radiusInMeters, Integer expiresInHours) {
        this.code = code;
        this.centerLatitude = centerLatitude;
        this.centerLongitude = centerLongitude;
        this.radiusInMeters = radiusInMeters;
        this.expiresInHours = expiresInHours;
    }

    public Geofence(String name, String code, String description, Double centerLatitude, Double centerLongitude, Integer radiusInMeters, Integer expiresInHours) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.centerLatitude = centerLatitude;
        this.centerLongitude = centerLongitude;
        this.radiusInMeters = radiusInMeters;
        this.expiresInHours = expiresInHours;
    }

    public Geofence(GeoFenceTO geoFenceTO) {
        this.name = geoFenceTO.getName();
        this.code = geoFenceTO.getCode();
        this.description = geoFenceTO.getDescription();
        this.centerLatitude = geoFenceTO.getCenter().getLatitude();
        this.centerLongitude = geoFenceTO.getCenter().getLongitude();
        this.radiusInMeters = geoFenceTO.getRadiusInMeters();
        this.expiresInHours = geoFenceTO.getExpiresInHours();
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

    public Double getCenterLatitude() {
        return centerLatitude;
    }

    public void setCenterLatitude(Double centerLatitude) {
        this.centerLatitude = centerLatitude;
    }

    public Double getCenterLongitude() {
        return centerLongitude;
    }

    public void setCenterLongitude(Double centerLongitude) {
        this.centerLongitude = centerLongitude;
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
