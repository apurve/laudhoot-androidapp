package com.laudhoot.model;

/**
 * Created by apurve on 1/3/15.
 */

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.TableInfo;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BaseModel extends Model implements Serializable {

    @Column(name = "created_on")
    private Date createdOn;

    @Column(name = "updated_on")
    private Date updatedOn;

    @Column(name = "archive_status")
    private byte archiveStatus;

    protected <T extends Model, U extends Model> List<T> getManyThrough(Class<T> targetClass, Class<U> joinClass, String targetForeignKeyInJoin, String foreignKeyInJoin) {
        return new Select()
                .from(targetClass)
                .as("target_model")
                .join(joinClass)
                .as("join_model")
                .on("join_model." + targetForeignKeyInJoin + " = " + "target_model.id")
                .where(foreignKeyInJoin + " = ?", this.getId())
                .execute();
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
                .innerJoin(this.getClass()).on("mapping_model." + this.getClass().getSimpleName().toLowerCase() + " = ?", getId()).execute();

    }

/*    protected <T extends BaseModel, U extends BaseModel> List<T> getManyThroughMapping2(Class<T> targetModel, Class<U> mappingModel) {
        List<U> mappings = getMany(mappingModel, this.getClass().getSimpleName().toLowerCase());
        List<T> results = new ArrayList<T>();
        for (U mapping : mappings) {
            TableInfo targetTableInfo = Cache.getTableInfo(targetModel);
            List<T> result = new Select().from(targetModel)
                    .innerJoin(mappingModel).as("mapping_model").on("mapping_model." + targetModel.getSimpleName().toLowerCase() + "=" + targetTableInfo.getTableName() + "." + targetTableInfo.getIdName()).execute();
        }
        return results;
    }*/

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
