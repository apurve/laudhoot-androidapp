package com.laudhoot.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.laudhoot.Laudhoot;
import com.laudhoot.R;
import com.laudhoot.persistence.repository.GeoFenceRepository;
import com.laudhoot.view.activity.LocationAwareActivity;
import com.laudhoot.view.adapter.ShoutFeedAdapter;
import com.laudhoot.web.model.ShoutTO;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by apurve on 12/4/15.
 */
public class GeoFenceFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private LocationAwareActivity activity = null;

    private ListView listView;

    private ShoutFeedAdapter shoutFeedAdapter;

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
        //((Laudhoot) (getActivity().getApplication())).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.geofence_feed, container, false);
        listView = (ListView) rootView.findViewById(R.id.geofence_feed);
        shoutFeedAdapter = new ShoutFeedAdapter(GeoFenceFragment.this, createDummyShouts());
        listView.setAdapter(shoutFeedAdapter);
        return rootView;
    }

    private List<ShoutTO> createDummyShouts() {
        List<ShoutTO> shouts = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ShoutTO shoutTO = new ShoutTO("Lorem ispun, loretta with a toaata tataa", "HOME");
            shoutTO.setId((long)i);
            shoutTO.setLaudCount(5l);
            shoutTO.setHootCount(2l);
            shouts.add(shoutTO);
        }
        return shouts;
    }

    public void laudShout(View v, long shoutId) {
        v.setBackgroundResource(R.drawable.arrow_active);
        activity.makeToast("LAUD, Shout : " + shoutId);
    }

    public void hootShout(View v, long shoutId) {
        v.setBackgroundResource(R.drawable.arrow_active);
        //v.setRotation(180);
        activity.makeToast("HOOT, Shout : " + shoutId);
    }
}