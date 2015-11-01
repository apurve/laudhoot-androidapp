package com.laudhoot.web.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.laudhoot.util.NetworkStateManager;

/**
 * Base {@link android.os.AsyncTask} to perform web operations.
 *
 * Created by apurve on 2/3/15.
 * */
public abstract class WebTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    private Activity activity;

    private NetworkStateManager networkStateManager;

    protected ProgressDialog pDialog;

    public WebTask(Activity activity, NetworkStateManager networkStateManager) {
        this.activity = activity;
        this.networkStateManager = networkStateManager;
        pDialog = new WebProgressDialog(activity);
    }

    /**
     * Before starting background thread
     * Show Progress Bar Dialog
     * */
    @Override
    protected void onPreExecute() {
        if(networkStateManager.isNotConnected()){
            Toast.makeText(activity, "Your internet is disabled, turn it on and then try again.", Toast.LENGTH_SHORT).show();
            cancel(true);
        }
        pDialog.show();
        super.onPreExecute();
    }

    /**
     * Perform web operations in background thread
     * */
    @Override
    protected abstract Result doInBackground(Params... params);

    /**
     * Updating progress bar
     * */
    protected void onProgressUpdate(Progress... progress) {

    }

    /**
     * After completing background task
     * Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(Result response) {
        if(pDialog != null && pDialog.isShowing()) {
            pDialog.setIndeterminate(false);
            pDialog.dismiss();
        }
        super.onPostExecute(response);
    }

    @Override
    protected void onCancelled() {
        if(pDialog != null && pDialog.isShowing()) {
            pDialog.setIndeterminate(false);
            pDialog.dismiss();
        }
        super.onCancelled();
    }
}