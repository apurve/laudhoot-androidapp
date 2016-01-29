package com.laudhoot.persistence.repository;

import com.activeandroid.query.Select;
import com.laudhoot.persistence.model.view.Notification;
import com.laudhoot.persistence.model.view.Reply;
import com.laudhoot.persistence.model.view.Shout;
import com.laudhoot.web.model.NotificationTO;
import com.laudhoot.web.model.ReplyTO;

import java.util.List;

/**
 * Created by root on 17/1/16.
 */
public class NotificationRepository extends ActiveAndroidRepository<Notification> {

    public NotificationRepository() {
        super(Notification.class);
    }

    public List<Notification> getNotifications() {
        return findAllActive();
    }

    public Notification create(NotificationTO notificationTO) {
        Notification notification = new Notification(notificationTO);
        saveOrUpdate(notification);
        return notification;
    }

    public void markRead(Long domainId) {
        Notification notification = findCached(domainId);
        if(notification != null) {
            notification.setArchiveStatus(ArchiveStatus.ARCHIVED);
            saveOrUpdate(notification);
        }
    }

    public Notification findCached(Long id) {
        return (Notification) new Select()
                .from(Notification.class)
                .where("domain_id = ?", id)
                .executeSingle();
    }

}
