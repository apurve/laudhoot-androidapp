package com.laudhoot.web.model;

/**
 * Created by root on 7/11/15.
 */
public class PostTO extends BaseTO {

    private String message;

    private String geoFenceCode;

    private Long laudCount;

    private Long hootCount;

    public PostTO() {
        super();
    }

    public PostTO(String geoFenceCode, String message) {
        super();
        this.geoFenceCode = geoFenceCode;
        this.message = message;
    }

    public PostTO(String geoFenceCode, String message, Long laudCount, Long hootCount) {
        super();
        this.geoFenceCode = geoFenceCode;
        this.message = message;
        this.laudCount = laudCount;
        this.hootCount = hootCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGeoFenceCode() {
        return geoFenceCode;
    }

    public void setGeoFenceCode(String geoFenceCode) {
        this.geoFenceCode = geoFenceCode;
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
