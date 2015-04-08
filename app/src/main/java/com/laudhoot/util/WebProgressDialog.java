package com.laudhoot.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by root on 5/4/15.
 */
public class WebProgressDialog extends ProgressDialog {

    public WebProgressDialog(Activity activity) {
        super(activity);
        this.setIndeterminate(true);
        this.setCancelable(false);
        this.setMessage("Please wait downloading...");
    }

}
