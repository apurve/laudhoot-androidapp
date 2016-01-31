package com.laudhoot.view.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.laudhoot.Laudhoot;
import com.laudhoot.R;
import com.laudhoot.persistence.model.view.Shout;
import com.laudhoot.persistence.repository.ClientDetailsRepository;
import com.laudhoot.persistence.repository.ShoutRepository;
import com.laudhoot.util.Toaster;
import com.laudhoot.view.EndlessListView;
import com.laudhoot.view.adapter.ShoutAdapter;
import com.laudhoot.view.fragment.BaseFragment;
import com.laudhoot.web.model.ShoutTO;
import com.laudhoot.web.services.LaudhootAPI;
import com.laudhoot.web.util.AuthorizationUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * Created by root on 14/1/16.
 */
public class UserShoutsActivity extends ActionBarActivity implements EndlessListView.EndlessListener {

    private static final int REQUEST_CODE_VIEW_SHOUT = 121;

    private String clientId;

    private String geofenceCode;

    @Bind(R.id.endless_feed_view)
    EndlessListView<Shout> listView;

    private ShoutAdapter shoutFeedAdapter;

    @Bind(R.id.endless_feed_swipe_container)
    SwipeRefreshLayout swipeContainer;

    @Inject
    LaudhootAPI laudhootAPI;

    @Inject
    ClientDetailsRepository clientDetailsRepository;

    @Inject
    ShoutRepository shoutRepository;

    @Inject
    Toaster toaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.endless_feed);
        ButterKnife.bind(this);
        ((Laudhoot) (getApplication())).inject(this);

        clientId = getIntent().getStringExtra(InitializationActivity.CLIENT_ID);
        geofenceCode = getIntent().getStringExtra(InitializationActivity.GEOFENCE_CODE);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.laudhoot_theme_color)));
        actionBar.setIcon(R.drawable.ic_launcher);
        actionBar.setDisplayHomeAsUpEnabled(true);

        shoutFeedAdapter = new ShoutAdapter(UserShoutsActivity.this, clientId, new ArrayList<Shout>());
        listView.setLoadingView(R.layout.loading_layout);
        listView.setAdapter(shoutFeedAdapter);
        listView.setEndlessListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UserShoutsActivity.this, ViewShoutActivity.class);
                intent.putExtra(BaseFragment.SHOUT_ID, shoutFeedAdapter.getItem(position).getDomainId());
                intent.putExtra(InitializationActivity.CLIENT_ID, clientId);
                intent.putExtra(InitializationActivity.GEOFENCE_CODE, geofenceCode);
                startActivityForResult(intent, REQUEST_CODE_VIEW_SHOUT);
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listView.refresh();
            }
        });
        swipeContainer.setColorSchemeColors(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
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
        new ShoutsLoader().execute(clientId, String.valueOf(count),
                AuthorizationUtil.authorizationToken(clientDetailsRepository.findByClientId(clientId)));
    }

    private class ShoutsLoader extends AsyncTask<String, Void, List<Shout>> {

        @Override
        protected List<Shout> doInBackground(String... params) {
            List<ShoutTO> shoutTOs;
            List<Shout> shouts = new ArrayList<Shout>();
            try {
                shoutTOs = laudhootAPI.listShoutsOfClient(params[0], params[1], params[2]);
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
                    toaster.makeToast(getString(R.string.error_loading_shouts));
                listView.noDataAvailable();
            } else {
                listView.addNewData(result);
            }
            swipeContainer.setRefreshing(false);
        }
    }
}
