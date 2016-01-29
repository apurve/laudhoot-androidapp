package com.laudhoot.web.model;

import com.activeandroid.annotation.Column;

import java.util.Map;

/**
 * Created by root on 17/1/16.
 */
public class NotificationTO extends BaseTO {

    private String message;

    private int actionCode;

    private String data;

    public NotificationTO() {
        super();
    }

    public NotificationTO(String message, int actionCode, String data) {
        super();
        this.message = message;
        this.actionCode = actionCode;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getActionCode() {
        return actionCode;
    }

    public void setActionCode(int actionCode) {
        this.actionCode = actionCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}