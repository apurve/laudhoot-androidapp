package com.laudhoot.persistence.model;

/**
 * Base model to provide common functionality to all other models in the application.
 *
 * The models in application currently work on ActiveAndroid persistence framework.
 *
 * Created by apurve on 1/3/15.
 */

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public abstract class BaseModel extends Model implements Serializable {

    @Column(name = "created_on")
    private Date createdOn;

    @Column(name = "updated_on")
    private Date updatedOn;

    @Column(name = "archive_status")
    private int archiveStatus;

    protected BaseModel() {
        super();
    }

    /**
     * Method to fetch related results through the mapping entity.
     * Works on the strict conventions of using class name of models in loser case as name of foreign keys columns to both the models.
     *
     * @param targetClass  The model to which this model holds a many-to-many relationship.
     * @param mappingClass The mapping model which establishes the many-to-many relationship.
     * @return List<T extends BaseModel> The result list of related models.
     */
    protected <T extends BaseModel, U extends BaseModel> List<T> getManyThroughMapping(Class<T> targetClass, Class<U> mappingClass) {
        return new Select().from(targetClass).as("target_model")
                .innerJoin(mappingClass).as("mapping_model").on("target_model.id = mapping_model." + targetClass.getSimpleName().toLowerCase())
                .where("mapping_model."+this.getClass().getSimpleName().toLowerCase()+"= ?", this.getId())
                .execute();
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

    public int getArchiveStatus() {
        return archiveStatus;
    }

    public void setArchiveStatus(int archiveStatus) {
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