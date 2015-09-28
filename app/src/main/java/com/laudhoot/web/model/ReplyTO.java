package com.laudhoot.web.model;

/**
 * Transfer object for a reply to post.
 *
 * Created by apurve on 2/6/15.
 */
public class ReplyTO extends BaseTO {

    private Long shoutId;

    private String message;

    private Long laudCount;

    private Long hootCount;

    public ReplyTO() {
    }

    public ReplyTO(String message, Long shoutId) {
        this.shoutId = shoutId;
        this.message = message;
    }

    public Long getShoutId() {
        return shoutId;
    }

    public void setShoutId(Long shoutId) {
        this.shoutId = shoutId;
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

}
