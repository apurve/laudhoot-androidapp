package com.laudhoot.persistence.repository;

import android.util.Log;

import com.activeandroid.query.Select;
import com.laudhoot.Laudhoot;
import com.laudhoot.persistence.model.ClientDetails;
import com.laudhoot.persistence.model.Geofence;
import com.laudhoot.web.model.GeoFenceTO;
import com.laudhoot.web.model.TokenResponse;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Persistence operations repository for client details model.
 * <p/>
 * Created by apurve on 19/9/15.
 */
public class ClientDetailsRepository extends ActiveAndroidRepository {

    public ClientDetailsRepository() {
        super(ClientDetails.class);
    }

    public ClientDetails create(String clientId, String clientSecret) {
        ClientDetails clientDetails = new ClientDetails(clientId, clientSecret);
        saveOrUpdate(clientDetails);
        return clientDetails;
    }

    public ClientDetails updateTokens(String clientId, TokenResponse tokenResponse) {
        ClientDetails clientDetails = findByClientId(clientId);
        if (clientDetails != null) {
            clientDetails.setAccessToken(tokenResponse.getAccessToken());
            Calendar timeout = Calendar.getInstance();
            clientDetails.setExpiresOn(
                    new Date(System.currentTimeMillis() + (1000 * (Long.valueOf(tokenResponse.getExpiresIn())) - 10))
            );
            Log.d(Laudhoot.LOG_TAG, tokenResponse.getExpiresIn() + ":"
            + (1000 * (Long.valueOf(tokenResponse.getExpiresIn())) - 10) + ":"
            + new Date(System.currentTimeMillis() + (1000 * (Long.valueOf(tokenResponse.getExpiresIn())) - 10)));
            clientDetails.setTokenType(tokenResponse.getTokenType());
            clientDetails.setScope(tokenResponse.getScope());
            saveOrUpdate(clientDetails);
            return clientDetails;
        }
        return null;
    }

    public ClientDetails fetchClientDetails() {
        List<ClientDetails> clientDetailsList = findAllActive();
        if (clientDetailsList != null && clientDetailsList.size() > 0) {
            Collections.sort(clientDetailsList, new Comparator<ClientDetails>() {
                @Override
                public int compare(ClientDetails lhs, ClientDetails rhs) {
                    return lhs.getCreatedOn().compareTo(rhs.getCreatedOn());
                }
            });
            return clientDetailsList.get(0);
        }
        return null;
    }

    public boolean isClientSetup() {
        List clientDetailsList = findAllActive();
        return clientDetailsList != null && clientDetailsList.size() > 0;
    }

    public boolean isClientSetup(String clientId) {
        return findByClientId(clientId) != null;
    }

    public ClientDetails findByClientId(String clientId) {
        return (ClientDetails) new Select()
                .from(ClientDetails.class)
                .where("client_id = ?", clientId)
                .executeSingle();
    }

    public boolean isAccessTokenValid(String clientId) {
        ClientDetails clientDetails = findByClientId(clientId);
        String accessToken = clientDetails.getAccessToken();
        if (accessToken != null && accessToken.length() > 0) {
            if(Laudhoot.D) {
                Log.d(Laudhoot.LOG_TAG, new Date().after(clientDetails.getExpiresOn()) + " | " + clientDetails.getExpiresOn());
            }
            if(new Date().before(clientDetails.getExpiresOn())) {
                return true;
            }
        }
        return false;
    }

    public Geofence getLastVisitedGeofence(String clientId) {
        return findByClientId(clientId).getLastVisited();
    }

    public ClientDetails updateVisitingGeofence(Geofence geofence, String clientId) {
        ClientDetails clientDetails = findByClientId(clientId);
        if(clientDetails.getVisiting() != null) {
            if(clientDetails.getVisiting().getCode().equals(geofence.getCode())) {
                return clientDetails;
            } else {
                clientDetails.setLastVisited(clientDetails.getVisiting());
                clientDetails.setVisiting(geofence);
            }
        } else {
            clientDetails.setLastVisited(geofence);
            clientDetails.setVisiting(geofence);
        }
        saveOrUpdate(clientDetails);
        return clientDetails;
    }
}
