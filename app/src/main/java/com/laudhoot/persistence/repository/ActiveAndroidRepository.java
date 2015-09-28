package com.laudhoot.persistence.repository;

/**
 * ActiveAndroid implementation of the persistence services contract provided by the application.
 *
 * Created by apurve on 1/3/15.
 */

import android.database.Cursor;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.TableInfo;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.laudhoot.persistence.model.BaseModel;

import java.util.Date;
import java.util.List;

public class ActiveAndroidRepository<T extends BaseModel> implements CRUDRepository<T> {

    private Class<T> persistentObjectClass;

    public ActiveAndroidRepository(Class<T> persistentObjectClass) {
        this.persistentObjectClass = persistentObjectClass;
    }

    @Override
    public List<T> findAllActive() {
        return new Select().from(persistentObjectClass).where("archive_status = ?", ArchiveStatus.NOT_ARCHIVED).execute();
    }

    @Override
    public List<T> findAll() {
        return new Select().from(persistentObjectClass).execute();
    }

    @Override
    public List<T> findAllOnFK(String fk, Long fkValue) {
        return new Select().from(persistentObjectClass).where(fk + " = ?", fkValue).execute();
    }

    @Override
    public List<T> findArchived() {
        return new Select().from(persistentObjectClass).where("archive_status = ?", ArchiveStatus.ARCHIVED).execute();
    }

    @Override
    public T findById(Long id) {
        return (T) new Select()
                .from(persistentObjectClass)
                .where("Id = ?", id)
                .executeSingle();
    }

    @Override
    public int findArchiveStatus(Long id) {
        //TODO - optimize the query
        return ((T) new Select()
                .from(persistentObjectClass)
                .where("Id = ?", id)
                .executeSingle()).getArchiveStatus();
    }

    @Override
    public Long saveOrUpdate(T persistentObject) {
        if (persistentObject.getId() == null) {
            persistentObject.setCreatedOn(new Date());
        } else {
            persistentObject.setUpdatedOn(new Date());
        }
        // The save method works for both inserting and updating records.
        persistentObject.save();
        return persistentObject.getId();
    }

    @Override
    public void delete(T persistentObject) {
        persistentObject.setArchiveStatus(ArchiveStatus.ARCHIVED);
        saveOrUpdate(persistentObject);
    }

    @Override
    public void delete(Long id) {
        T temp = findById(id);
        delete(temp);
    }

    @Override
    public void hardDelete(Long id) {
        new Delete().from(persistentObjectClass).where("Id = ?", id).execute();
    }

    @Override
    public void hardDelete(T persistentObject) {
        hardDelete(persistentObject.getId());
    }

    @Override
    public void purgeRepository() {
        TableInfo tableInfo = Cache.getTableInfo(persistentObjectClass);
        ActiveAndroid.execSQL("delete from " + tableInfo.getTableName() + ";");
        ActiveAndroid.execSQL("delete from sqlite_sequence where name='" + tableInfo.getTableName() + "';");
    }

    @Override
    public void restore(T persistentObject) {
        persistentObject.setArchiveStatus(ArchiveStatus.NOT_ARCHIVED);
        saveOrUpdate(persistentObject);
    }

    @Override
    public void markForArchive(T persistentObject) {
        persistentObject.setArchiveStatus(ArchiveStatus.MARKED_FOR_ARCHIVES);
        saveOrUpdate(persistentObject);
    }

    @Override
    public Cursor fetchResultCursor() {
        String tableName = Cache.getTableInfo(persistentObjectClass).getTableName();
        // Query all items without any conditions
        String resultRecords = new Select(tableName + ".*, " + tableName + ".Id as _id").
                from(persistentObjectClass).toSql();
        // Execute query on the underlying ActiveAndroid SQLite database
        return Cache.openDatabase().rawQuery(resultRecords, null);
    }

}