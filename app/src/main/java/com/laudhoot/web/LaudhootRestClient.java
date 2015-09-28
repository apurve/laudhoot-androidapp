package com.laudhoot.web;


import android.app.Application;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.laudhoot.Laudhoot;
import com.laudhoot.web.services.LaudhootAPI;
import com.laudhoot.web.services.TestAPI;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by apurve on 1/3/15.
 */

public class LaudhootRestClient {

    private final RestAdapter restAdapter;

    public LaudhootRestClient(Application application) {
        Gson customGson = new GsonBuilder().registerTypeHierarchyAdapter(byte[].class, new ByteArrayToBase64TypeAdapter()).create();

        restAdapter = new RestAdapter.Builder()
                .setEndpoint(WebConstants.BASE_URL)
                .setClient(getClient(application))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter((customGson)))
                        //.setErrorHandler(new BankServiceErrorHandler())
                .build();
    }

    private OkClient getClient(Application application) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(WebConstants.HTTP_TIMEOUT, TimeUnit.MINUTES);
        client.setReadTimeout(WebConstants.HTTP_TIMEOUT, TimeUnit.MINUTES);

        try {
            File cacheDir = new File(application.getCacheDir(), "http-cache");
            Cache cache = new Cache(cacheDir, 1024 * 1024 * 5l); // 5 MB HTTP Cache
            client.setCache(cache);
        } catch (IOException e) {
            Log.e(Laudhoot.LOG_TAG, "Could not create cache directory for HTTP client: " + e.getMessage(), e);
        }

        return new OkClient(client);
    }

    private static class ByteArrayToBase64TypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {

        @Override
        public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Base64.decode(json.getAsString(), Base64.NO_WRAP);
        }

        @Override
        public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(Base64.encodeToString(src, Base64.NO_WRAP));
        }
    }

    public LaudhootAPI getShoutWebService() {
        return restAdapter.create(LaudhootAPI.class);
    }

    public TestAPI getTestWebService() {
        return restAdapter.create(TestAPI.class);
    }
}
