package com.laudhoot.web.services;

import com.laudhoot.web.util.BaseCallback;
import com.laudhoot.web.model.ClientDetailTO;
import com.laudhoot.web.model.CoordinateTO;
import com.laudhoot.web.model.GeoFenceTO;
import com.laudhoot.web.model.ReplyTO;
import com.laudhoot.web.model.ShoutTO;
import com.laudhoot.web.model.TokenResponse;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 *
 *
 * Created by apurve on 29/12/14.
 */
public interface LaudhootAPI {

    public static final String GEOFENCE = "/geofence";

    public static final String REST = "/rest";
    public static final String SHOUT = "/shout";
    public static final String CREATE = "/create";
    public static final String LAUD = "/laud";
    public static final String HOOT = "/hoot";
    public static final String REPLY = "/reply";

    @GET(REST+GEOFENCE)
    public void findGeoFence(@Query("latitude") Double latitude, @Query("longitude") Double longitude,
                             @Header("Authorization") String authorization, BaseCallback<GeoFenceTO> callback);

    @GET(GEOFENCE)
    public GeoFenceTO findGeoFence(@Query("geoFenceCode") String geoFenceCode);

    @GET(REST+SHOUT)
    public List<ShoutTO> listShoutsOfGeoFence(@Query("geoFenceCode") String geoFenceCode,
                                              @Query("shoutsAvailable") String shoutsAvailable,
                                              @Header("Authorization") String authorization);

    @POST(REST+SHOUT)
    public ShoutTO createShout(@Body ShoutTO shoutTO, @Header("Authorization") String authorization);

    @POST(REST+SHOUT+REPLY)
    public ReplyTO createReply(@Body ReplyTO replyTO);

    @POST(REST+SHOUT+LAUD)
    public Long laudShout(@Body Long shoutId);

    @POST(REST+SHOUT+HOOT)
    public Long hootShout(@Body Long shoutId);

    @POST(REST+SHOUT+REPLY+LAUD)
    public Long laudReply(@Body Long replyId);

    @POST(REST+SHOUT+REPLY+HOOT)
    public Long hootReply(@Body Long replyId);

}
