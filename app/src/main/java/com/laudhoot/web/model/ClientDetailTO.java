package com.laudhoot.web.model;

import com.activeandroid.annotation.Column;

/**
 * Transfer object for client details.
 *
 * Created by root on 19/9/15.
 */
public class ClientDetailTO extends BaseTO {

    private String clientId;

    private String clientSecret;

    public ClientDetailTO(String clientId) {
        this.clientId = clientId;
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

}
