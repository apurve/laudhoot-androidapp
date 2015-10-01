package com.laudhoot.web.services;

import com.laudhoot.web.model.ClientDetailTO;
import com.laudhoot.web.model.TokenResponse;
import com.laudhoot.web.util.BaseCallback;

import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 * Web api for client registration and authorization operations.
 *
 * Created by root on 30/9/15.
 */
public interface ClientAPI {

    public static final String CLIENT = "/rest/client";
    public static final String REGISTRATION = "/registration";
    public static final String ACCESS_TOKEN_ENDPOINT = "/oauth/token";

    @FormUrlEncoded
    @POST(ACCESS_TOKEN_ENDPOINT)
    public void requestClientToken(@Header("Authorization") String authorization,
                                   @Field("grant_type") String grantType, @Field("response_type") String responseType,
                                   BaseCallback<TokenResponse> callback);

    @POST(CLIENT+REGISTRATION) //TODO - make client registration secure
    public void clientRegistration(@Body ClientDetailTO clientDetailTO, BaseCallback<ClientDetailTO> callback);

}
