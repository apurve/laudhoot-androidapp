package com.laudhoot.persistence.repository;

import com.activeandroid.query.Select;
import com.laudhoot.persistence.model.view.Reply;
import com.laudhoot.persistence.model.view.Shout;
import com.laudhoot.web.model.ReplyTO;

/**
 * Created by root on 8/11/15.
 */
public class ReplyRepository extends ActiveAndroidRepository<Reply> {

    public ReplyRepository() {
        super(Reply.class);
    }

    public Reply cache(ReplyTO replyTO, Shout shout) {
        Reply reply = findCached(replyTO.getId());
        if(reply == null) {
            reply = new Reply(replyTO, shout);
        } else {
            reply.update(replyTO);
        }
        saveOrUpdate(reply);
        return reply;
    }

    public Reply findCached(Long id) {
        return (Reply) new Select()
                .from(Reply.class)
                .where("domain_id = ?", id)
                .executeSingle();
    }

    public void vote(Long replyId, boolean isLaud) {
        Reply reply = findCached(replyId);
        reply.setVoted(true);
        if(isLaud) {
            reply.setLaudCount(reply.getLaudCount()+1);
            reply.setIsLaudVote(true);
        } else {
            reply.setHootCount(reply.getHootCount()+1);
            reply.setIsLaudVote(false);
        }
    }

}
