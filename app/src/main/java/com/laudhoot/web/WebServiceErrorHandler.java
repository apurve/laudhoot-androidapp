package com.laudhoot.web;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

import android.accounts.NetworkErrorException;

/**
 * Created by apurve on 1/3/15.
 */

public class WebServiceErrorHandler implements ErrorHandler {

    @SuppressWarnings("deprecation")
    @Override
    public Throwable handleError(RetrofitError cause) {
        if (cause.isNetworkError()) {
            return new NetworkErrorException();
        }
        return new Exception();
    }
}
