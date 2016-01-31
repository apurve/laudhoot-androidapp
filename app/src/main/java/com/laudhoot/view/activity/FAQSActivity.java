package com.laudhoot.view.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.laudhoot.Laudhoot;
import com.laudhoot.R;
import com.laudhoot.persistence.model.view.Shout;
import com.laudhoot.persistence.repository.ClientDetailsRepository;
import com.laudhoot.util.Toaster;
import com.laudhoot.view.EndlessListView;
import com.laudhoot.view.adapter.FAQFeedAdapter;
import com.laudhoot.view.adapter.ShoutAdapter;
import com.laudhoot.web.model.FAQTO;
import com.laudhoot.web.model.ShoutTO;
import com.laudhoot.web.services.LaudhootAPI;
import com.laudhoot.web.util.AuthorizationUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by root on 31/1/16.
 */
public class FAQSActivity extends ActionBarActivity implements EndlessListView.EndlessListener {

    private String clientId;

    @Bind(R.id.endless_feed_view)
    protected EndlessListView<FAQTO> listView;

    @Inject
    protected LaudhootAPI laudhootAPI;

    @Inject
    protected ClientDetailsRepository clientDetailsRepository;

    @Inject
    protected Toaster toaster;

    private FAQFeedAdapter faqFeedAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.endless_feed);
        ButterKnife.bind(this);
        ((Laudhoot) (getApplication())).inject(this);
        clientId = getIntent().getStringExtra(InitializationActivity.CLIENT_ID);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.laudhoot_theme_color)));
        actionBar.setIcon(R.drawable.ic_launcher);
        actionBar.setDisplayHomeAsUpEnabled(true);

        faqFeedAdapter = new FAQFeedAdapter(FAQSActivity.this, clientId, new ArrayList<FAQTO>());
        listView.setLoadingView(R.layout.loading_layout);
        listView.setAdapter(faqFeedAdapter);
        listView.setEndlessListener(this);
    }

    @Override
    public void loadData(int count) {
        new FAQLoader().execute(String.valueOf(count),
                AuthorizationUtil.authorizationToken(clientDetailsRepository.findByClientId(clientId)));
    }

    private class FAQLoader extends AsyncTask<String, Void, List<FAQTO>> {

        @Override
        protected List<FAQTO> doInBackground(String... params) {
            List<FAQTO> faqTOs;
            try {
                faqTOs = laudhootAPI.listFAQs(params[0], params[1]);
            } catch (Exception exception) {
                if (Laudhoot.D)
                    Log.d(Laudhoot.LOG_TAG, exception.getMessage()
                            + " | "
                            + Arrays.toString(exception.getStackTrace()));
                return null;
            }
            return faqTOs;
        }

        @Override
        protected void onPostExecute(List<FAQTO> result) {
            super.onPostExecute(result);
            if (result == null || result.size() < 1) {
                if (Laudhoot.D)
                    toaster.makeToast(getString(R.string.error_loading_faqs));
                listView.noDataAvailable();
            } else {
                listView.addNewData(result);
            }
        }
    }
}
