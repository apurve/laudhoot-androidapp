package com.laudhoot.persistence.model;

import com.activeandroid.annotation.Column;

import java.util.Date;

/**
 * Model to store details of the client created when the application is installed on a new mobile device.
 * These details are later used to obtain OAuth2.0 tokens to access laudhoot web services.
 * <p/>
 * Created by apurve on 19/9/15.
 */
public class ClientDetails extends BaseModel {

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "additional_information")
    private String additionalInformation;

    @Column(name = "token_type")
    private String tokenType;

    @Column(name = "expires_on")
    private Date expiresOn;

    @Column(name = "scope")
    private String scope;

    @Column(name = "last_visited")
    private Geofence lastVisited;

    @Column(name = "visiting")
    private Geofence visiting;

    @Column(name = "favourite")
    private Geofence favourite;

    public ClientDetails() {
    }

    public ClientDetails(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Date getExpiresOn() {
        return expiresOn;
    }

    public void setExpiresOn(Date expiresOn) {
        this.expiresOn = expiresOn;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Geofence getLastVisited() {
        return lastVisited;
    }

    public void setLastVisited(Geofence lastVisited) {
        this.lastVisited = lastVisited;
    }

    public Geofence getVisiting() {
        return visiting;
    }

    public void setVisiting(Geofence visiting) {
        this.visiting = visiting;
    }

    public Geofence getFavourite() {
        return favourite;
    }

    public void setFavourite(Geofence favourite) {
        this.favourite = favourite;
    }
}
