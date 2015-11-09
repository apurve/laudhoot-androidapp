package com.laudhoot.persistence.model.view;

import com.activeandroid.annotation.Column;
import com.laudhoot.persistence.model.BaseModel;
import com.laudhoot.web.model.BaseTO;

/**
 * Created by root on 8/11/15.
 */
public abstract class DomainCache<T extends BaseTO> extends BaseModel {

    @Column(name = "domain_id")
    private Long domainId;

    protected DomainCache() {
        super();
    }

    protected DomainCache(Long domainId) {
        super();
        this.domainId = domainId;
    }

    public abstract void update(T transferObject);

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

}
