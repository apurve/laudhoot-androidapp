package com.laudhoot.web.model;

import java.util.List;

/**
 * Transfer object for a shout.
 *
 * Created by root on 2/6/15.
 */
public class ShoutTO extends BaseTO {

    private String message;

    private Long laudCount;

    private Long hootCount;

    private String geoFenceCode;

    private List<ReplyTO> replies;

    public ShoutTO() {
    }

    public ShoutTO(String message, String geoFenceCode) {
        this.message = message;
        this.geoFenceCode = geoFenceCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getLaudCount() {
        return laudCount;
    }

    public void setLaudCount(Long laudCount) {
        this.laudCount = laudCount;
    }

    public Long getHootCount() {
        return hootCount;
    }

    public void setHootCount(Long hootCount) {
        this.hootCount = hootCount;
    }

    public String getGeoFenceCode() {
        return geoFenceCode;
    }

    public void setGeoFenceCode(String geoFenceCode) {
        this.geoFenceCode = geoFenceCode;
    }

    public List<ReplyTO> getReplies() {
        return replies;
    }

    public void setReplies(List<ReplyTO> replies) {
        this.replies = replies;
    }

}
