package com.laudhoot.web.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * {
 *  access_token: "c7a4cdc3-528c-417b-92c6-da31bb8e9bb2"
 *  token_type: "bearer"
 *  expires_in: 599
 *  scope: "read write"
 * }
 * <p/>
 * Created by root on 29/9/15.
 */
public class TokenResponse extends BaseTO {

    @SerializedName("access_token") private String accessToken;
    @SerializedName("token_type") private String tokenType;
    @SerializedName("expires_in") private String expiresIn;
    @SerializedName("scope") private String scope;

    public TokenResponse() {
        super();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
