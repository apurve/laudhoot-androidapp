package com.laudhoot.web;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

import android.accounts.NetworkErrorException;

/**
 * Error handler implementation of Retrofit's {@link retrofit.ErrorHandler}. The handler simply tries to resolve to
 * an HTTP error and if a response is available then tries to resolve to error messages present in
 * {@link com.laudhoot.web.model.BaseTO}.
 *
 * Note : TODO - should BaseTO be abstract?
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
