package com.laudhoot.web.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * {@link android.app.ProgressDialog} extension for a web operations.
 *
 * Created by root on 5/4/15.
 */
public class WebProgressDialog extends ProgressDialog {

    public WebProgressDialog(Activity activity) {
        super(activity);
        this.setIndeterminate(true);
        this.setCancelable(false);
        this.setMessage("Please wait...");
    }

}
