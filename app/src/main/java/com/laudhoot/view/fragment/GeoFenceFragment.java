package com.laudhoot.view.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laudhoot.Laudhoot;
import com.laudhoot.R;
import com.laudhoot.view.EndlessListView;
import com.laudhoot.view.activity.LocationAwareActivity;
import com.laudhoot.view.adapter.ShoutAdapter;
import com.laudhoot.web.model.ShoutTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment hosting shout feed.
 * <p/>
 * Created by apurve on 12/4/15.
 */
public class GeoFenceFragment extends Fragment implements EndlessListView.EndlessListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private LocationAwareActivity activity = null;

    private EndlessListView listView;

    private ShoutAdapter shoutFeedAdapter;

    int multiplier = 1;

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
            Log.d(Laudhoot.LOG_TAG, "+++ ON FRAGMENT ATTACH +++");
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
        listView = (EndlessListView) rootView.findViewById(R.id.geofence_feed);
        shoutFeedAdapter = new ShoutAdapter(GeoFenceFragment.this, createDummyShouts(multiplier));
        listView.setLoadingView(R.layout.loading_layout);
        listView.setAdapter(shoutFeedAdapter);
        listView.setListener(this);
        return rootView;
    }

    private List<ShoutTO> createDummyShouts(int multiplier) {
        List<ShoutTO> shouts = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            ShoutTO shoutTO = new ShoutTO(i * multiplier + " lorem ispun, loretta with a toaata tataa", "HOME");
            shoutTO.setId((long) i * multiplier);
            shoutTO.setLaudCount(5l * multiplier * i);
            shoutTO.setHootCount(2l * multiplier * i);
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

    @Override
    public void loadData() {
        multiplier += 20;
        FakeNetLoader fl = new FakeNetLoader();
        fl.execute(new String[]{});
    }

    private class FakeNetLoader extends AsyncTask<String, Void, List<ShoutTO>> {

        @Override
        protected List<ShoutTO> doInBackground(String... params) {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return createDummyShouts(multiplier);
        }

        @Override
        protected void onPostExecute(List<ShoutTO> result) {
            super.onPostExecute(result);
            listView.addNewData(result);
        }

    }
}