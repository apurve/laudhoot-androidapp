package com.laudhoot.persistence.model.view;

import com.activeandroid.annotation.Column;
import com.laudhoot.web.model.NotificationTO;

import java.util.Map;

/**
 * Created by root on 17/1/16.
 */
public class Notification extends DomainCache<NotificationTO> {

    @Column(name = "message")
    private String message;

    @Column(name = "action_code")
    private int actionCode;

    @Column(name = "data")
    private String data;

    public Notification() {

    }

    public Notification(NotificationTO notificationTO) {
        super(notificationTO.getId());
        this.message = notificationTO.getMessage();
        this.actionCode = notificationTO.getActionCode();
        this.data = notificationTO.getData();
    }

    @Override
    public void update(NotificationTO transferObject) {

    }

    public int getActionCode() {
        return actionCode;
    }

    public void setActionCode(int actionCode) {
        this.actionCode = actionCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static class ActionCode {
        public static final int DO_NOTHING = 0;
        public static final int SHOW_SHOUT = 1;
    }
}
