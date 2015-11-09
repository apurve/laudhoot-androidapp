package com.laudhoot.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.laudhoot.Laudhoot;
import com.laudhoot.R;
import com.laudhoot.persistence.model.view.Reply;
import com.laudhoot.persistence.model.view.Shout;
import com.laudhoot.persistence.repository.ClientDetailsRepository;
import com.laudhoot.persistence.repository.ReplyRepository;
import com.laudhoot.persistence.repository.ShoutRepository;
import com.laudhoot.util.DateUtil;
import com.laudhoot.util.NetworkStateManager;
import com.laudhoot.util.Toaster;
import com.laudhoot.view.fragment.ShoutFeedFragment;
import com.laudhoot.web.model.ReplyTO;
import com.laudhoot.web.model.ShoutTO;
import com.laudhoot.web.services.LaudhootAPI;
import com.laudhoot.web.util.AuthorizationUtil;
import com.laudhoot.web.util.WebTask;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ViewShoutActivity extends ActionBarActivity {

    private String clientId;

    private String geofenceCode;

    private Long shoutId;

    @InjectView(R.id.message)
    TextView message;

    @InjectView(R.id.laudhoot_difference)
    TextView laudhootDifference;

    @InjectView(R.id.laud)
    Button laud;

    @InjectView(R.id.hoot)
    Button hoot;

    @InjectView(R.id.comments)
    TextView comments;

    @InjectView(R.id.laud_count)
    TextView laudCount;

    @InjectView(R.id.hoot_count)
    TextView hootCount;

    @InjectView(R.id.elapsed_time)
    TextView elapsedTime;

    @InjectView(R.id.reply_text)
    TextView reply;

    @InjectView(R.id.post_reply)
    Button postReply;

    @Inject
    ShoutRepository shoutRepository;

    @Inject
    ReplyRepository replyRepository;

    @Inject
    ClientDetailsRepository clientDetailsRepository;

    @Inject
    LaudhootAPI laudhootAPI;

    @Inject
    Toaster toaster;

    @Inject
    NetworkStateManager networkStateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shout);
        ButterKnife.inject(this);
        ((Laudhoot) (getApplication())).inject(this);
        shoutId = getIntent().getLongExtra(ShoutFeedFragment.SHOUT_ID, 0);
        clientId = getIntent().getStringExtra(InitializationActivity.CLIENT_ID);
        geofenceCode = getIntent().getStringExtra(InitializationActivity.GEOFENCE_CODE);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.laudhoot_theme_color)));
        actionBar.setIcon(R.drawable.ic_launcher);
        actionBar.setDisplayHomeAsUpEnabled(true);

        postReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String replyText = reply.getText().toString();
                if(replyText != null && replyText.length()>0) {
                    new PostReplyTask().execute(geofenceCode, replyText,
                            AuthorizationUtil.authorizationToken(clientDetailsRepository.findByClientId(clientId)));
                } else {
                    toaster.makeToast(getString(R.string.shout_text_empty));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Shout shout = shoutRepository.findCached(shoutId);
        message.setText(shout.getMessage());
        if(shout.getLaudCount() == null || shout.getHootCount() == null) {
            laudhootDifference.setText("0");
        } else {
            laudhootDifference.setText(String.valueOf(shout.getLaudCount() - shout.getHootCount()));
        }

        comments.setText(shout.getRepliesCount()+" comments");

        if(shout.getCreatedOn() == null) {
            elapsedTime.setText("Just Now");
        } else {
            elapsedTime.setText(DateUtil.getElapsedDuration(shout.getCreatedOn()));
        }

        if(shout.getLaudCount() == null)
            laudCount.setText("0");
        else
            laudCount.setText(String.valueOf(shout.getLaudCount()));

        if(shout.getHootCount() == null)
            hootCount.setText("0");
        else
            hootCount.setText(String.valueOf(shout.getHootCount()));

        laud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toaster.makeToast("laud : "+shout.getDomainId());
            }
        });
        hoot.setRotation(180);
        hoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toaster.makeToast("hoot : "+shout.getDomainId());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_shout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_shout : {
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

    private class PostReplyTask extends WebTask<String, Integer, Reply> {

        public PostReplyTask() {
            super(ViewShoutActivity.this, networkStateManager);
        }

        @Override
        protected Reply doInBackground(String... params) {
            ReplyTO replyTO = new ReplyTO(params[0], params[1], shoutId);
            try {
                replyTO = laudhootAPI.createReply(replyTO, params[2]);
                return shoutRepository.cacheReply(replyTO, shoutId);
            } catch (Exception exception) {
                if(Laudhoot.D)
                    Log.d(Laudhoot.LOG_TAG, exception.getMessage()
                            + " | "
                            + exception.getStackTrace());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Reply response) {
            super.onPostExecute(response);
            if(response != null) {
                //TODO - update reply list
                toaster.makeToast(response.getMessage());
            } else {
                toaster.makeToast(getString(R.string.error_network));
            }
        }
    }

    private void abortResult() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}
