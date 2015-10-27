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
import com.laudhoot.view.activity.MainActivity;
import com.laudhoot.view.adapter.ShoutAdapter;
import com.laudhoot.web.model.ShoutTO;
import com.laudhoot.web.services.LaudhootAPI;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Fragment hosting shout feed.
 * <p/>
 * Created by apurve on 12/4/15.
 */
public class ShoutFeedFragment extends Fragment implements EndlessListView.EndlessListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private MainActivity activity = null;

    private EndlessListView listView;

    private ShoutAdapter shoutFeedAdapter;

    int multiplier = 1;

    public static ShoutFeedFragment newInstance(Integer sectionNumber) {
        ShoutFeedFragment fragment = new ShoutFeedFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ShoutFeedFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
        if (Laudhoot.D) {
            Log.d(Laudhoot.LOG_TAG, "+++ ON FRAGMENT ATTACH +++");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Laudhoot) (getActivity().getApplication())).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.geofence_feed, container, false);
        listView = (EndlessListView) rootView.findViewById(R.id.geofence_feed);
        shoutFeedAdapter = new ShoutAdapter(ShoutFeedFragment.this, new ArrayList<ShoutTO>());
        listView.setLoadingView(R.layout.loading_layout);
        listView.setAdapter(shoutFeedAdapter);
        listView.setListener(this);
        //new FakeNetLoader().execute();
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
        activity.getToaster().makeToast("LAUD, Shout : " + shoutId);
    }

    public void hootShout(View v, long shoutId) {
        v.setBackgroundResource(R.drawable.arrow_active);
        //v.setRotation(180);
        activity.getToaster().makeToast("HOOT, Shout : " + shoutId);
    }

    @Override
    public void loadData() {
        List<ShoutTO> shouts = new ArrayList<>();
        new FakeNetLoader().execute();
    }

    private class FakeNetLoader extends AsyncTask<Void, Void, List<ShoutTO>> {

        @Override
        protected List<ShoutTO> doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
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