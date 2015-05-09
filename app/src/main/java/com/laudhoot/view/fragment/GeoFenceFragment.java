package com.laudhoot.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;

import com.laudhoot.Laudhoot;
import com.laudhoot.persistence.repository.GeoFenceRepository;
import com.laudhoot.view.activity.LocationAwareActivity;
import com.laudhoot.view.adapter.TransitionAdapter;

import javax.inject.Inject;

/**
 * Created by apurve on 12/4/15.
 */
public class GeoFenceFragment extends ListFragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private LocationAwareActivity activity = null;

    @Inject
    GeoFenceRepository geoFenceRepository;

    public static GeoFenceFragment newInstance(Integer sectionNumber) {
        GeoFenceFragment fragment = new GeoFenceFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public GeoFenceFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (LocationAwareActivity) activity;
        if (Laudhoot.D) {
            Log.e(Laudhoot.LOG_TAG, "+++ ON FRAGMENT ATTACH +++");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Laudhoot) (getActivity().getApplication())).inject(this);
        setListAdapter(new TransitionAdapter(activity, geoFenceRepository.getTransitions()));
    }

}
