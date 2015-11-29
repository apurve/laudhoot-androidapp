package com.laudhoot.web.model;

/**
 * Created by root on 21/11/15.
 */
public class VoteTO extends BaseTO {

    private Long postId;

    private Boolean isLaud;

    public VoteTO() {
        super();
    }

    public VoteTO(Long postId, Boolean isLaud) {
        this.postId = postId;
        this.isLaud = isLaud;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Boolean getIsLaud() {
        return isLaud;
    }

    public void setIsLaud(Boolean isLaud) {
        this.isLaud = isLaud;
    }

}
