package com.laudhoot.web.util;

import android.util.Base64;

/**
 * Utility class to provide authorization of different encoding protocols.
 *
 * Created by root on 29/9/15.
 */
public class AuthorizationUtil {

    /**
     * Basic authorization header value encoded to Base64.
     *
     * @param username username part of encoded authorization
     * @param password password part of encoded authorization
     *
     * @return "Basic " + username+":"+password
     */
    public static String basicAuthorization(String username, String password) {
        String credentials = username + ":" + password;
        return "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
    }

}
