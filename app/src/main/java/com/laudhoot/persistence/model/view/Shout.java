package com.laudhoot.persistence.model.view;

import com.activeandroid.annotation.Column;
import com.laudhoot.persistence.model.BaseModel;
import com.laudhoot.web.model.BaseTO;
import com.laudhoot.web.model.ShoutTO;

import java.util.List;

/**
 * Created by root on 7/11/15.
 */
public class Shout extends Post<ShoutTO> {

    @Column(name = "replies_count")
    private int repliesCount;

    public Shout() {
        super();
    }

    public Shout(ShoutTO shoutTO) {
        super(shoutTO);
        this.repliesCount = shoutTO.getRepliesCount();
    }

    @Override
    public void update(ShoutTO transferObject) {
        this.setLaudCount(transferObject.getLaudCount());
        this.setHootCount(transferObject.getHootCount());
        this.setRepliesCount(transferObject.getRepliesCount());
        this.setVoted(transferObject.isVoted());
        this.setIsLaudVote(transferObject.getIsLaudVote());
    }

    public List<Reply> getReplies() {
        return getMany(Reply.class, "reply");
    }

    public int getRepliesCount() {
        return repliesCount;
    }

    public void setRepliesCount(int repliesCount) {
        this.repliesCount = repliesCount;
    }
}
