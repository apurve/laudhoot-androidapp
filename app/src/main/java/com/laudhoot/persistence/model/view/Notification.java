package com.laudhoot.persistence.model.view;

import com.activeandroid.annotation.Column;
import com.laudhoot.web.model.NotificationTO;

/**
 * Created by root on 17/1/16.
 */
public class Notification extends DomainCache<NotificationTO> {

    @Column(name = "message")
    private String message;

    @Column(name = "action_code")
    private int actionCode;

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

}
