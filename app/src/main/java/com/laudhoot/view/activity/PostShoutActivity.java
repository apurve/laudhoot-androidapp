package com.laudhoot.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.laudhoot.Laudhoot;
import com.laudhoot.R;
import com.laudhoot.persistence.repository.ClientDetailsRepository;
import com.laudhoot.util.NetworkStateManager;
import com.laudhoot.util.Toaster;
import com.laudhoot.web.model.ShoutTO;
import com.laudhoot.web.services.LaudhootAPI;
import com.laudhoot.web.util.AuthorizationUtil;
import com.laudhoot.web.util.WebTask;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PostShoutActivity extends ActionBarActivity {

    public static String SHOUT_MESSAGE = "shout_message";

    @InjectView(R.id.shout_text)
    EditText shout;

    @Inject
    LaudhootAPI laudhootAPI;

    @Inject
    ClientDetailsRepository clientDetailsRepository;

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
                    new PostShoutTask().execute(shoutText, geofenceCode,
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
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class PostShoutTask extends WebTask<String, Integer, ShoutTO> {

        public PostShoutTask() {
            super(PostShoutActivity.this, networkStateManager);
        }

        @Override
        protected ShoutTO doInBackground(String... params) {
            ShoutTO shoutTO = new ShoutTO(params[0], params[1]);
            try {
                shoutTO = laudhootAPI.createShout(shoutTO, params[2]);
            } catch (Exception exception) {
                if(Laudhoot.D)
                    Log.d(Laudhoot.LOG_TAG, exception.getMessage()
                            + " | "
                            + exception.getStackTrace());
                return null;
            }
            return shoutTO;
        }

        @Override
        protected void onPostExecute(ShoutTO response) {
            super.onPostExecute(response);
            if(response != null)
                returnResult(response);
            else
                toaster.makeToast(getString(R.string.error_network));
        }
    }

    private void returnResult(ShoutTO response) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(SHOUT_MESSAGE, response.getMessage());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void abortResult() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}