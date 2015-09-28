package com.laudhoot.web.model;

import java.util.Map;

/**
 * Transfer objects are being used to transfer data on web.
 * The base class for all transfer objects.
 *
 * Created by root on 2/6/15.
 */
public class BaseTO {

    Long id;

    boolean error;

    Map<String, String> errorMessages;

    public final static String ERROR_KEY = "error";

    public BaseTO() {
    }

    public BaseTO(Long id, boolean error, Map<String, String> errorMessages) {
        this.id = id;
        this.error = error;
        this.errorMessages = errorMessages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean hasError() {
        return error;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Map<String, String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(Map<String, String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public String getError() {
        if (error && errorMessages != null) {
            return errorMessages.get(ERROR_KEY);
        }
        return null;
    }

}
