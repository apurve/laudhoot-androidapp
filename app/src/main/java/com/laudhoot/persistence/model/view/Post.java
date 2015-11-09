package com.laudhoot.persistence.model.view;

import com.activeandroid.annotation.Column;
import com.laudhoot.persistence.model.BaseModel;
import com.laudhoot.web.model.PostTO;

/**
 * Created by root on 7/11/15.
 */
public abstract class Post<T extends PostTO> extends DomainCache<T> {

    @Column(name = "message")
    private String message;

    @Column(name = "geofence_code")
    private String geoFenceCode;

    @Column(name = "laud_count")
    private Long laudCount;

    @Column(name = "hoot_count")
    private Long hootCount;

    public Post() {
        super();
    }

    public Post(Long domainId) {
        super(domainId);
    }

    public Post(PostTO postTO) {
        super(postTO.getId());
        this.geoFenceCode = postTO.getGeoFenceCode();
        this.message = postTO.getMessage();
        this.laudCount = postTO.getLaudCount();
        this.hootCount = postTO.getHootCount();
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
