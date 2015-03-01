package com.laudhoot.model;

/**
 * Created by root on 1/3/15.
 */

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

public class BaseModel extends Model implements Serializable{

    @Column(name = "created_on")
    private Date createdOn;

    @Column(name = "updated_on")
    private Date updatedOn;

    @Column(name = "archive_status")
    private byte archiveStatus;

    public BaseModel() {
        super();
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public byte getArchiveStatus() {
        return archiveStatus;
    }

    public void setArchiveStatus(byte archiveStatus) {
        this.archiveStatus = archiveStatus;
    }

    @Override
    public String toString() {
        return "BaseModel{" +
                "createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                ", archiveStatus=" + archiveStatus +
                '}';
    }
}
