package com.laudhoot.web;

import retrofit.RequestInterceptor;

/**
 * Implementation of {@link retrofit.RequestInterceptor} to handle injection of common headers of all web requests.
 *
 * Created by root on 29/9/15.
 */
public class HttpCommonHeaderHandler implements RequestInterceptor {

    @Override
    public void intercept(RequestFacade request) {
        request.addHeader("User-Agent", WebConstants.CLIENT_NAME);
    }

}
