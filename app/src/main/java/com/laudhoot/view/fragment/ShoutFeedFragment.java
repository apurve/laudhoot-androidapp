package com.laudhoot.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.laudhoot.Laudhoot;
import com.laudhoot.R;
import com.laudhoot.view.EndlessListView;
import com.laudhoot.view.activity.InitializationActivity;
import com.laudhoot.view.activity.MainActivity;
import com.laudhoot.view.activity.PostShoutActivity;
import com.laudhoot.view.adapter.ShoutAdapter;
import com.laudhoot.web.model.ShoutTO;
import com.laudhoot.web.util.AuthorizationUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private static final int REQUEST_CODE_POST_SHOUT = 11;

    private MainActivity activity = null;

    private EndlessListView listView;

    private ShoutAdapter shoutFeedAdapter;

    private boolean shoutsUnavailable;

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
        setHasOptionsMenu(true);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.post_shout_button : {
                Intent intent = new Intent(activity, PostShoutActivity.class);
                intent.putExtra(InitializationActivity.CLIENT_ID, activity.getClientId());
                intent.putExtra(InitializationActivity.GEOFENCE_CODE, activity.getGeofenceCode());
                startActivityForResult(intent, REQUEST_CODE_POST_SHOUT);
                break;
            }
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_POST_SHOUT: {
                if(resultCode == Activity.RESULT_OK) {
                    ShoutTO shoutTO = new ShoutTO(
                            data.getExtras().getString(PostShoutActivity.SHOUT_MESSAGE), activity.getGeofenceCode());
                    shoutFeedAdapter.insert(shoutTO, 0);
                }
                break;
            }
            default: break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        if (!shoutsUnavailable)
            new ShoutsLoader().execute(activity.getGeofenceCode(), String.valueOf(shoutFeedAdapter.getCount()),
                    AuthorizationUtil.authorizationToken(activity.getClientDetailsRepository().findByClientId(activity.getClientId())));
        else
            listView.noDataAvailable();
    }

    private class ShoutsLoader extends AsyncTask<String, Void, List<ShoutTO>> {

        @Override
        protected List<ShoutTO> doInBackground(String... params) {
            List<ShoutTO> shoutTOs;
            try {
                shoutTOs = activity.getLaudhootApiClient().listShoutsOfGeoFence(params[0], params[1], params[2]);
            } catch (Exception exception) {
                if (Laudhoot.D)
                    Log.d(Laudhoot.LOG_TAG, exception.getMessage()
                            + " | "
                            + Arrays.toString(exception.getStackTrace()));
                return null;
            }
            return shoutTOs;
        }

        @Override
        protected void onPostExecute(List<ShoutTO> result) {
            super.onPostExecute(result);
            if(result == null || result.size() < 1) {
                activity.getToaster().makeToast(getString(R.string.error_loading_shouts));
                listView.noDataAvailable();
                makeShoutsUnavailableTemporarily();
            }
            else {
                listView.addNewData(result);
            }
        }

    }

    private void makeShoutsUnavailableTemporarily() {
        shoutsUnavailable = true;
        new MakeShoutsAvailable().execute();
    }

    private class MakeShoutsAvailable extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(300000); // sleep for 5 minutes
                shoutsUnavailable = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}