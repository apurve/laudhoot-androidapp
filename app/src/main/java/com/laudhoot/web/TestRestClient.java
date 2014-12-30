package com.laudhoot.web;

import retrofit.RestAdapter;

/**
 * Created by root on 29/12/14.
 */
public class TestRestClient {

    TestAPI testAPI;

    public TestRestClient() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(WebConstants.SERVER).build();
        testAPI = restAdapter.create(TestAPI.class);
    }

    public TestAPI getTestAPI() {
        return testAPI;
    }
}
