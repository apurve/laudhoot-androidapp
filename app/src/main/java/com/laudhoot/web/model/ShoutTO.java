package com.laudhoot.web.model;

import java.util.List;

/**
 * Transfer object for a shout.
 *
 * Created by root on 2/6/15.
 */
public class ShoutTO extends PostTO {

    private int repliesCount;

    public ShoutTO() {
        super();
    }

    public ShoutTO(String geoFenceCode, String message) {
        super(geoFenceCode, message);
    }

    public int getRepliesCount() {
        return repliesCount;
    }

    public void setRepliesCount(int repliesCount) {
        this.repliesCount = repliesCount;
    }

}
