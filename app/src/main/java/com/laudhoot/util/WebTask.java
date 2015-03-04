package com.laudhoot.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by apurve on 2/3/15.
 * Background Async Task to download data.
 * */
public class WebTask extends AsyncTask<String, String, String> {

    protected Activity activity;
    protected ProgressDialog pDialog;

    protected WebTask(Activity activity) {
        this.activity = activity;
    }

    /**
     * Before starting background thread
     * Show Progress Bar Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if(!(activeNetworkInfo != null && activeNetworkInfo.isConnected())){
            Toast.makeText(activity.getApplicationContext(), "Your internet is disabled, turn in on and then try again.", Toast.LENGTH_SHORT).show();
            cancel(true);
        }

        pDialog = new ProgressDialog(activity);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please wait...");
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
}