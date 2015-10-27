package com.laudhoot.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Simple provider for toast messages.
 *
 * Created by root on 7/10/15.
 */
public class Toaster {
    private Context context;

    public Toaster(Context context) {
        this.context = context;
    }

    public void makeToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void makeShortToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
