package com.laudhoot.web;


import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by apurve on 1/3/15.
 */

public class RestClient {

    private TestAPI testWebService;
    RestAdapter restAdapter;
    private final Gson customGson;

    public RestClient() {
        customGson = new GsonBuilder().registerTypeHierarchyAdapter(byte[].class, new ByteArrayToBase64TypeAdapter()).create();
        restAdapter = new RestAdapter.Builder()
                .setEndpoint(WebConstants.BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                        //.setConverter(new GsonConverter((customGson)))
                        //.setErrorHandler(new BankServiceErrorHandler())
                .build();
        testWebService = restAdapter.create(TestAPI.class);
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

    public TestAPI getTestWebService() {
        return testWebService;
    }
}
