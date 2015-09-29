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
public class WebTask extends AsyncTask<String, String, String> {

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
        super.onPreExecute();
        if(networkStateManager.isNotConnected()){
            Toast.makeText(activity, "Your internet is disabled, turn it on and then try again.", Toast.LENGTH_SHORT).show();
            cancel(true);
        }
        pDialog.show();
    }

    /**
     * Perform web operations in background thread
     * */
    @Override
    protected String doInBackground(String... params) {
        return null;
    }

    /**
     * Updating progress bar
     * */
    protected void onProgressUpdate(String... progress) {

    }

    /**
     * After completing background task
     * Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(String response) {
        pDialog.setIndeterminate(false);
        pDialog.dismiss();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        pDialog.setIndeterminate(false);
        pDialog.dismiss();
    }
}