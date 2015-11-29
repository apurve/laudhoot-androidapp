package com.laudhoot.persistence.repository;

import com.activeandroid.query.Select;
import com.laudhoot.persistence.model.view.Reply;
import com.laudhoot.persistence.model.view.Shout;
import com.laudhoot.web.model.ReplyTO;
import com.laudhoot.web.model.ShoutTO;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Persistence operations repository for shout model.
 * <p/>
 * Created by root on 8/11/15.
 */
public class ShoutRepository extends ActiveAndroidRepository {

    private ReplyRepository replyRepository;

    public ShoutRepository(ReplyRepository replyRepository) {
        super(Shout.class);
        this.replyRepository = replyRepository;
    }

    public Shout cache(ShoutTO shoutTO) {
        Shout shout = findCached(shoutTO.getId());
        if(shout == null) {
            shout = new Shout(shoutTO);
        } else {
            shout.update(shoutTO);
        }
        saveOrUpdate(shout);
        return shout;
    }

    public Shout findCached(Long id) {
        return (Shout) new Select()
                .from(Shout.class)
                .where("domain_id = ?", id)
                .executeSingle();
    }

    public Reply cacheReply(ReplyTO replyTO, Long shoutId) {
        Shout shout = findCached(shoutId);
        Reply reply = null;
        if(shout != null) {
            reply = replyRepository.cache(replyTO, shout);
        }
        return reply;
    }

    public void vote(Long shoutId, boolean isLaud) {
        Shout shout = findCached(shoutId);
        shout.setVoted(true);
        if(isLaud) {
            shout.setLaudCount(shout.getLaudCount()+1);
            shout.setIsLaudVote(true);
        } else {
            shout.setHootCount(shout.getHootCount()+1);
            shout.setIsLaudVote(false);
        }
    }
}
