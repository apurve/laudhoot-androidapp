package com.laudhoot.util;

import android.app.Activity;
import android.app.DialogFragment;

/**
 * Contract specification as name suggests.
 * Created by root on 7/10/15.
 */
public interface LocationApiErrorHandler {
    void handleError(int errorCode);

    boolean resolvingError();
}
