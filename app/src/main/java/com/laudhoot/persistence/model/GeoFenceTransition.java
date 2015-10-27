package com.laudhoot.persistence.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Model to use meta data of a transition from the boundary of a geo fence.
 * <p/>
 * Note : This is not being used currently but intended to optimize or provide new features.
 * <p/>
 * Created by apurve on 9/5/15.
 */
public class GeoFenceTransition extends BaseModel {

    @Column(name = "transition_string")
    private String transitionString;

    public GeoFenceTransition() {
        super();
    }

    public GeoFenceTransition(String transitionString) {
        super();
        this.transitionString = transitionString;
    }

    public String getTransitionString() {
        return transitionString;
    }

    public void setTransitionString(String transitionString) {
        this.transitionString = transitionString;
    }

    @Override
    public String toString() {
        return transitionString +
                "\n" + getCreatedOn();
    }

}
