package com.laudhoot.web.util;

import android.content.Context;
import android.widget.Toast;

import com.laudhoot.web.model.BaseTO;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Base implementation of {@link retrofit.Callback} which provides default error handling.
 * <p/>
 * Created by root on 29/9/15.
 */
public abstract class BaseCallback<T extends BaseTO> implements Callback<T> {

    private Context context;

    private boolean toast = false;

    public BaseCallback(Context context) {
        this.context = context;
    }

    public BaseCallback(Context context, boolean toast) {
        this.context = context;
        this.toast = toast;
    }

    /**
     * Successful HTTP response.
     */
    protected abstract void success(T t, Response response, Context context);

    /**
     * Unsuccessful HTTP response due to network failure, non-2XX status code, or unexpected
     * exception.
     */
    protected abstract void failure(RetrofitError error, Context context);


    @Override
    public void success(T t, Response response) {
        success(t, response, context);
    }

    @Override
    public void failure(RetrofitError error) {
        if (toast) {
            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT);
        }
        failure(error, context);
    }

    public void enableToast() {
        this.toast = true;
    }

    public void disableToast() {
        this.toast = false;
    }

    public boolean toast() {
        return toast;
    }

}
