package com.laudhoot.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.laudhoot.R;
import com.laudhoot.persistence.model.GeoFenceTransition;
import com.laudhoot.persistence.repository.CRUDRepository;

import java.util.List;

/**
 * Created by apurve on 9/5/15.
 */
public class TransitionAdapter extends ArrayAdapter<GeoFenceTransition>{

    private Context context;

    public TransitionAdapter(Context context, List<GeoFenceTransition> transitions) {
        super(context, android.R.layout.simple_list_item_1, transitions);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        GeoFenceTransition transition = getItem(position);
        if(transition.getTransitionString().contains(context.getString(R.string.geofence_transition_entered))){
            view.setBackgroundColor(context.getResources().getColor(android.R.color.background_light));
        }else{
            view.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
        }
        /*if(transition.getArchiveStatus() == CRUDRepository.ArchiveStatus.NOT_ARCHIVED){
            view.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        }
        if(transition.getArchiveStatus() == CRUDRepository.ArchiveStatus.ARCHIVED){
            view.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        }
        if(transition.getArchiveStatus() == CRUDRepository.ArchiveStatus.MARKED_FOR_ARCHIVES){
            view.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
        }*/
        return view;
    }
}
