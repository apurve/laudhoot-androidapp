package com.laudhoot.persistence.repository;

import com.laudhoot.persistence.model.GeoFenceTransition;

import java.util.List;

/**
 * Persistence operations repository for geo fence transition model.
 *
 * Created by apurve on 9/5/15.
 */
public class GeoFenceRepository extends ActiveAndroidRepository {

    public GeoFenceRepository() {
        super(GeoFenceTransition.class);
    }

    public GeoFenceTransition create(String transitionString){
        GeoFenceTransition transition = new GeoFenceTransition(transitionString);
        saveOrUpdate(transition);
        return transition;
    }

    public List<GeoFenceTransition> getTransitions(){
        return findAll();
    }
    
}
