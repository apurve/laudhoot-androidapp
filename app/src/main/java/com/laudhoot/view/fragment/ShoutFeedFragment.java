package com.laudhoot.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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
public class ShoutFeedFragment extends BaseFragment implements EndlessListView.EndlessListener {

    private static final int REQUEST_CODE_SHOUT = 11;

    private static final int REQUEST_CODE_VIEW_SHOUT = 12;

    private EndlessListView<Shout> listView;

    private ShoutAdapter shoutFeedAdapter;

    private SwipeRefreshLayout swipeContainer;

    @Inject
    public ShoutRepository shoutRepository;

    public static Fragment newInstance(Integer sectionNumber) {
        return initFragment(new ShoutFeedFragment(), sectionNumber);
    }

    public ShoutFeedFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.endless_feed, container, false);
        listView = (EndlessListView) rootView.findViewById(R.id.endless_shouts_feed);
        shoutFeedAdapter = new ShoutAdapter(getMainActivity(), getMainActivity().getClientId(), new ArrayList<Shout>());
        listView.setLoadingView(R.layout.loading_layout);
        listView.setAdapter(shoutFeedAdapter);
        listView.setEndlessListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, ViewShoutActivity.class);
                intent.putExtra(SHOUT_ID, shoutFeedAdapter.getItem(position).getDomainId());
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
        swipeContainer.setColorSchemeColors(R.color.laudhoot_theme_color);
        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.post_shout_button).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.post_shout_button: {
                Intent intent = new Intent(activity, PostShoutActivity.class);
                intent.putExtra(InitializationActivity.CLIENT_ID, activity.getClientId());
                intent.putExtra(InitializationActivity.GEOFENCE_CODE, activity.getGeofenceCode());
                startActivityForResult(intent, REQUEST_CODE_SHOUT);
                return true;
            }
            default:
                break;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SHOUT: {
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