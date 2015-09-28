package com.laudhoot.persistence.repository;

import com.activeandroid.query.Select;
import com.laudhoot.persistence.model.ClientDetails;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Persistence operations repository for client details model.
 *
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

    public ClientDetails updateTokens(String clientId, String accessToken, String refreshToken) {
        ClientDetails clientDetails = findByClientId(clientId);
        if(clientDetails != null) {
            clientDetails.setAccessToken(accessToken);
            clientDetails.setRefreshToken(refreshToken);
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

}
