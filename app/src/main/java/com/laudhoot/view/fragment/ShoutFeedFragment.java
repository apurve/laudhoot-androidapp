package com.laudhoot.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.laudhoot.Laudhoot;
import com.laudhoot.R;
import com.laudhoot.persistence.model.view.Shout;
import com.laudhoot.persistence.repository.ShoutRepository;
import com.laudhoot.view.EndlessListView;
import com.laudhoot.view.activity.InitializationActivity;
import com.laudhoot.view.activity.MainActivity;
import com.laudhoot.view.activity.PostShoutActivity;
import com.laudhoot.view.activity.ViewShoutActivity;
import com.laudhoot.view.adapter.ShoutAdapter;
import com.laudhoot.web.model.ShoutTO;
import com.laudhoot.web.util.AuthorizationUtil;

import java.util.ArrayList;
import java.util.Arrays;
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

    private static final int REQUEST_CODE_POST_SHOUT = 11;

    private static final int REQUEST_CODE_VIEW_SHOUT = 12;

    public static final String SHOUT_ID = "shout_id";

    private MainActivity activity = null;

    private EndlessListView<Shout> listView;

    private ShoutAdapter shoutFeedAdapter;

    private SwipeRefreshLayout swipeContainer;

    @Inject
    public ShoutRepository shoutRepository;

    public static ShoutFeedFragment newInstance(Integer sectionNumber) {
        ShoutFeedFragment fragment = new ShoutFeedFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ShoutFeedFragment() {

    }

    public MainActivity getMainActivity() {
        return activity;
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
        ((Laudhoot) (getMainActivity().getApplication())).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.endless_feed, container, false);
        listView = (EndlessListView) rootView.findViewById(R.id.endless_shouts_feed);
        shoutFeedAdapter = new ShoutAdapter(ShoutFeedFragment.this, new ArrayList<Shout>());
        listView.setLoadingView(R.layout.loading_layout);
        listView.setAdapter(shoutFeedAdapter);
        listView.setEndlessListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, ViewShoutActivity.class);
                intent.putExtra(ShoutFeedFragment.SHOUT_ID, shoutFeedAdapter.getItem(position).getDomainId());
                intent.putExtra(InitializationActivity.CLIENT_ID, activity.getClientId());
                intent.putExtra(InitializationActivity.GEOFENCE_CODE, activity.getGeofenceCode());
                startActivityForResult(intent, REQUEST_CODE_VIEW_SHOUT);
            }
        });

        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.endless_feed_swipe_container);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listView.refresh();
            }
        });
        /*swipeContainer.setColorSchemeColors(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);*/
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.post_shout_button: {
                Intent intent = new Intent(activity, PostShoutActivity.class);
                intent.putExtra(InitializationActivity.CLIENT_ID, activity.getClientId());
                intent.putExtra(InitializationActivity.GEOFENCE_CODE, activity.getGeofenceCode());
                startActivityForResult(intent, REQUEST_CODE_POST_SHOUT);
                break;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_POST_SHOUT: {
                if (resultCode == Activity.RESULT_OK) {
                    Shout shout = shoutRepository.findCached(data.getExtras().getLong(SHOUT_ID));
                    shoutFeedAdapter.insert(shout, 0);
                    listView.smoothScrollToPosition(0);
                }
                break;
            }
            case REQUEST_CODE_VIEW_SHOUT: {
                // TODO - handle proper view update
                listView.refresh();
                break;
            }
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void loadData(int count) {
        new ShoutsLoader().execute(activity.getGeofenceCode(), String.valueOf(count),
                AuthorizationUtil.authorizationToken(activity.getClientDetailsRepository().findByClientId(activity.getClientId())));
    }

    private class ShoutsLoader extends AsyncTask<String, Void, List<Shout>> {

        @Override
        protected List<Shout> doInBackground(String... params) {
            List<ShoutTO> shoutTOs;
            List<Shout> shouts = new ArrayList<Shout>();
            try {
                shoutTOs = activity.getLaudhootApiClient().listShoutsOfGeoFence(params[0], params[1], params[2]);
                if (shoutTOs != null) {
                    for (ShoutTO shoutTO : shoutTOs) {
                        shouts.add(shoutRepository.cache(shoutTO));
                    }
                }
            } catch (Exception exception) {
                if (Laudhoot.D)
                    Log.d(Laudhoot.LOG_TAG, exception.getMessage()
                            + " | "
                            + Arrays.toString(exception.getStackTrace()));
                return null;
            }
            return shouts;
        }

        @Override
        protected void onPostExecute(List<Shout> result) {
            super.onPostExecute(result);
            if (result == null || result.size() < 1) {
                if (Laudhoot.D)
                    activity.getToaster().makeToast(getString(R.string.error_loading_shouts));
                listView.noDataAvailable();
            } else {
                listView.addNewData(result);
            }
            swipeContainer.setRefreshing(false);
        }

    }

}