package com.laudhoot.web.model;

/**
 * Transfer object for a reply to post.
 *
 * Created by apurve on 2/6/15.
 */
public class ReplyTO extends PostTO {

    private Long shoutId;

    public ReplyTO() {

    }

    public ReplyTO(String geoFenceCode, String message, Long shoutId) {
        super(geoFenceCode, message);
        this.shoutId = shoutId;
    }

    public Long getShoutId() {
        return shoutId;
    }

    public void setShoutId(Long shoutId) {
        this.shoutId = shoutId;
    }

}
