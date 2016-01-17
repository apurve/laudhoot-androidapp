package com.laudhoot.web.services;

import com.laudhoot.web.model.VoteTO;
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
    public static final String REPLY = "/reply";
    public static final String VOTE = "/vote";
    public static final String CLIENT = "/client";

    @GET(REST+GEOFENCE)
    public void findGeoFence(@Query("latitude") Double latitude, @Query("longitude") Double longitude,
                             @Header("Authorization") String authorization, BaseCallback<GeoFenceTO> callback);

    @GET(GEOFENCE)
    public GeoFenceTO findGeoFence(@Query("geoFenceCode") String geoFenceCode);

    @GET(REST+SHOUT)
    public List<ShoutTO> listShoutsOfGeoFence(@Query("geoFenceCode") String geoFenceCode,
                                              @Query("shoutsAvailable") String shoutsAvailable,
                                              @Header("Authorization") String authorization);

    @GET(REST+SHOUT+CLIENT)
    public List<ShoutTO> listShoutsOfClient(@Query("clientId") String clientId,
                                              @Query("shoutsAvailable") String shoutsAvailable,
                                              @Header("Authorization") String authorization);

    @GET(REST+SHOUT+REPLY)
    public List<ReplyTO> listRepliesOfShout(@Query("shoutId") String shoutId,
                                              @Query("repliesAvailable") String repliesAvailable,
                                              @Header("Authorization") String authorization);

    @POST(REST+SHOUT)
    public ShoutTO createShout(@Body ShoutTO shoutTO, @Header("Authorization") String authorization);

    @POST(REST+SHOUT+REPLY)
    public ReplyTO createReply(@Body ReplyTO replyTO, @Header("Authorization") String authorization);

    @POST(REST+SHOUT+VOTE)
    public void votePost(@Body VoteTO voteTO, @Header("Authorization") String authorization, BaseCallback<VoteTO> callback);

}
