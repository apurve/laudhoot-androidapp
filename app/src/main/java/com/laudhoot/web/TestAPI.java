package com.laudhoot.web;

import retrofit.http.GET;

/**
 * Created by apurve on 29/12/14.
 */
public interface TestAPI {

    public static final String TEST_CONTROLLER = "/test";

    public static final String TEST_URL1 = TEST_CONTROLLER + "/1";
    public static final String TEST_URL2 = TEST_CONTROLLER + "/2";

    @GET(TEST_URL1)
    public String testController1();

    @GET(TEST_URL2)
    public String testController2();


}
