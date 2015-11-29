package com.laudhoot.persistence.model.view;

import com.activeandroid.annotation.Column;
import com.laudhoot.web.model.BaseTO;
import com.laudhoot.web.model.PostTO;
import com.laudhoot.web.model.ReplyTO;

/**
 * Created by root on 8/11/15.
 */
public class Reply extends Post<ReplyTO> {

    @Column(name = "shout")
    Shout shout;

    public Reply() {
        super();
    }

    public Reply(ReplyTO replyTO, Shout shout) {
        super(replyTO);
        this.shout = shout;
    }

    @Override
    public void update(ReplyTO transferObject) {
        this.setLaudCount(transferObject.getLaudCount());
        this.setHootCount(transferObject.getHootCount());
        this.setVoted(transferObject.isVoted());
        this.setIsLaudVote(transferObject.getIsLaudVote());
    }

    public Shout getShout() {
        return shout;
    }

    public void setShout(Shout shout) {
        this.shout = shout;
    }
}
