package com.laudhoot.persistence.repository;

/**
 * Contract for persistence services provided by the application framework.
 *
 * Created by apurve on 1/3/15.
 */
import android.database.Cursor;

import java.io.Serializable;
import java.util.List;

public interface CRUDRepository<T extends Serializable> {

    /**
     * Finds all excluding the soft deleted persistentObjects.
     * @return
     */
    List<T> findAllActive();

    /**
     * Finds all the persistentObjects.
     * @return
     */
    List<T> findAll();

    /**
     * Finds all the persistentObjects with the foreign key.
     * @return
     */
    List<T> findAllOnFK(String fk, Long fkValue);

    /**
     * Finds all the soft deleted persistentObjects.
     * @return
     */
    List<T> findArchived();

    /**
     * Finds the persistentObject based on its id.
     * @param id of the persistentObject
     * @return
     */
    T findById(Long id);

    /**
     * Finds the archive status of persistentObject by its id.
     * @param id of the persistentObject
     * @return
     */
    int findArchiveStatus(Long id);

    /**
     * Performs insertion or updation operation as required.
     * @param persistentObject the object which needs to be persisted
     * @return id of the inserted or updated record.
     */
    Long saveOrUpdate(T persistentObject);

    /**
     * Performs soft deletion operation on the persisted object.
     * @param id of the object which needs to be soft deleted
     */
    void delete(Long id);

    /**
     * Performs soft deletion operation on the persisted object.
     * @param persistentObject the object which needs to be soft deleted
     */
    void delete(T persistentObject);

    /**
     * Performs hard deletion operation on the persisted object.
     * @param id of the object which needs to be soft deleted
     */
    void hardDelete(Long id);

    /**
     * Performs hard deletion operation on the persisted object.
     * @param persistentObject the object which needs to be soft deleted
     */
    void hardDelete(T persistentObject);

    /**
     * Performs hard deletion operation on the repository.
     */
    void purgeRepository();

    /**
     * Performs restoration from archives operation on the persisted object.
     * @param persistentObject the object which needs to be soft deleted
     */
    void restore(T persistentObject);

    /**
     * Marks for deletion deletion operation on the persisted object.
     * @param persistentObject the object which needs to be soft deleted
     */
    void markForArchive(T persistentObject);

    /**
     * Fetch a cursor for result set of all records.
     * @return Cursor for the result records
     * */
    Cursor fetchResultCursor();

    public static class ArchiveStatus {
        public static final int NOT_ARCHIVED = 0;
        public static final int ARCHIVED = 1;
        public static final int MARKED_FOR_ARCHIVES = 2;
    }

}