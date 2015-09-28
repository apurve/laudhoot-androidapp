package com.laudhoot.persistence.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by apurve on 9/5/15.
 */
public class GeoFenceTransition extends BaseModel {

    @Column(name="transition_string")
    private String            transitionString;

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
