package com.laudhoot.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.laudhoot.Laudhoot;
import com.laudhoot.R;
import com.laudhoot.persistence.model.view.Shout;
import com.laudhoot.persistence.repository.ClientDetailsRepository;
import com.laudhoot.persistence.repository.ShoutRepository;
import com.laudhoot.util.NetworkStateManager;
import com.laudhoot.util.Toaster;
import com.laudhoot.view.fragment.ShoutFeedFragment;
import com.laudhoot.web.model.ShoutTO;
import com.laudhoot.web.services.LaudhootAPI;
import com.laudhoot.web.util.AuthorizationUtil;
import com.laudhoot.web.util.WebTask;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PostShoutActivity extends ActionBarActivity {

    @InjectView(R.id.shout_text)
    EditText shout;

    @Inject
    LaudhootAPI laudhootAPI;

    @Inject
    ClientDetailsRepository clientDetailsRepository;

    @Inject
    ShoutRepository shoutRepository;

    @Inject
    NetworkStateManager networkStateManager;

    @Inject
    Toaster toaster;

    private String clientId;

    private String geofenceCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_shout);
        ButterKnife.inject(this);
        ((Laudhoot) (getApplication())).inject(this);
        clientId = getIntent().getStringExtra(InitializationActivity.CLIENT_ID);
        geofenceCode = getIntent().getStringExtra(InitializationActivity.GEOFENCE_CODE);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.laudhoot_theme_color)));
        actionBar.setIcon(R.drawable.ic_launcher);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post_shout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.post_shout : {
                String shoutText = shout.getText().toString();
                if(shoutText != null && shoutText.length()>0) {
                    new PostShoutTask().execute(geofenceCode, shoutText,
                            AuthorizationUtil.authorizationToken(clientDetailsRepository.findByClientId(clientId)));
                } else {
                    toaster.makeToast(getString(R.string.shout_text_empty));
                }
                break;
            }
            case R.id.home : {
                abortResult();
                break;
            }
            default: return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private class PostShoutTask extends WebTask<String, Integer, Shout> {

        public PostShoutTask() {
            super(PostShoutActivity.this, networkStateManager);
        }

        @Override
        protected Shout doInBackground(String... params) {
            ShoutTO shoutTO = new ShoutTO(params[0], params[1]);
            try {
                shoutTO = laudhootAPI.createShout(shoutTO, params[2]);
                return shoutRepository.cache(shoutTO);
            } catch (Exception exception) {
                if(Laudhoot.D)
                    Log.d(Laudhoot.LOG_TAG, exception.getMessage()
                            + " | "
                            + exception.getStackTrace());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Shout response) {
            super.onPostExecute(response);
            if(response != null)
                returnResult(response);
            else
                toaster.makeToast(getString(R.string.error_network));
        }
    }

    private void returnResult(Shout response) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(ShoutFeedFragment.SHOUT_ID, response.getDomainId());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void abortResult() {
        NavUtils.navigateUpFromSameTask(this);
    }
}