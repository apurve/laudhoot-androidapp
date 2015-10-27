package com.laudhoot.web;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

import android.content.Context;
import android.util.Log;

import com.laudhoot.Laudhoot;
import com.laudhoot.R;
import com.laudhoot.web.model.BaseTO;

import org.apache.http.HttpStatus;

import java.util.Map;

/**
 * Error handler implementation of Retrofit's {@link retrofit.ErrorHandler}. The handler simply tries to resolve
 * HTTP errors and if a response is available then tries to resolve to error messages present in
 * {@link com.laudhoot.web.model.BaseTO}.
 *
 * Created by apurve on 1/3/15.
 */

public class WebServiceErrorHandler implements ErrorHandler {

    private final Context context;

    public WebServiceErrorHandler(Context context) {
        super();
        this.context = context;
    }

    @SuppressWarnings("deprecation")
    @Override
    public Throwable handleError(RetrofitError cause) {
        String errorDescription = null;
        switch(cause.getKind()) {
            case NETWORK: {
                errorDescription = context.getString(R.string.error_network);
                if(Laudhoot.D)
                    Log.d(Laudhoot.LOG_TAG, "Network Error | "+cause.getMessage()+" | "+cause.getStackTrace());
                break;
            }
            case CONVERSION: {
                errorDescription = context.getString(R.string.error_conversion);
                if(Laudhoot.D)
                    Log.d(Laudhoot.LOG_TAG, "Conversion Error | "+cause.getMessage()+" | "+cause.getStackTrace());
                break;
            }
            case HTTP: {
                if(cause.getResponse().getStatus() == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                    errorDescription = context.getString(R.string.error_web_service_unexpected);
                    if(Laudhoot.D) {
                        Log.d(Laudhoot.LOG_TAG, "Unexpected Web Service Error | " + cause.getMessage() + " | " + cause.getStackTrace());
                        Log.d(Laudhoot.LOG_TAG, "HTTP Response | " + cause.getResponse().getBody());
                    }
                } if(cause.getResponse().getStatus() == HttpStatus.SC_UNAUTHORIZED) {
                    errorDescription = context.getString(R.string.error_client_unauthorized);
                    if(Laudhoot.D) {
                        Log.d(Laudhoot.LOG_TAG, "Client Unauthorized | " + cause.getMessage() + " | " + cause.getStackTrace());
                        Log.d(Laudhoot.LOG_TAG, "HTTP Response | " + cause.getResponse().getBody());
                    }
                } else {
                    try {
                        BaseTO errorResponse = (BaseTO) cause.getBodyAs(BaseTO.class);
                        if (errorResponse.hasError()) {
                            errorDescription = errorResponse.getError();
                            if (errorDescription != null && errorDescription.length() < 1) {
                                Map<String, String> errorMessages = errorResponse.getErrorMessages();
                                if (errorMessages != null && !errorMessages.isEmpty()) {
                                    errorDescription = context.getString(R.string.error_web_service_validation)+" ";
                                    for (Map.Entry entry : errorMessages.entrySet()) {
                                        errorDescription = errorDescription + entry.getKey() + ",";
                                    }
                                    errorDescription = errorDescription.substring(0,errorDescription.length()-1);
                                } else {
                                    errorDescription = context.getString(R.string.error_web_service_unexpected);
                                }
                            }
                        }
                    } catch (Exception exception) {
                        errorDescription = context.getString(R.string.error_web_service_unexpected);
                        if(Laudhoot.D) {
                            Log.d(Laudhoot.LOG_TAG, "Unexpected Web Service Error | " + cause.getMessage() + " | " + cause.getStackTrace());
                            Log.d(Laudhoot.LOG_TAG, "HTTP Response | " + cause.getResponse().getBody());
                        }
                    }
                }
                break;
            }
            case UNEXPECTED: {
                if (cause.getResponse() == null) {
                    errorDescription = context.getString(R.string.error_no_response);
                    if(Laudhoot.D)
                        Log.d(Laudhoot.LOG_TAG, "No Response | "+cause.getMessage()+" | "+cause.getStackTrace());
                } else {
                    errorDescription = context.getString(R.string.error_unexpected);
                    if(Laudhoot.D) {
                        Log.d(Laudhoot.LOG_TAG, "Unexpected Error | "+cause.getMessage()+" | "+cause.getStackTrace());
                        Log.d(Laudhoot.LOG_TAG, "Unexpected Error | "+cause.getResponse()+" | "+cause.getStackTrace());
                    }
                }
                break;
            }
            default: {
                errorDescription = context.getString(R.string.error_unexpected);
                if(Laudhoot.D)
                    Log.d(Laudhoot.LOG_TAG, "Unexpected Error | "+cause.getMessage()+" | "+cause.getStackTrace());
                break;
            }
        }
        if(errorDescription == null){
            errorDescription = context.getString(R.string.error_unexpected);
        }
        return new Exception(errorDescription, cause);
    }
}
