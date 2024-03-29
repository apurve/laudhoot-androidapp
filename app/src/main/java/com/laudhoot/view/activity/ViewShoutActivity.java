package com.laudhoot.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
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
import com.laudhoot.view.EndlessListView;
import com.laudhoot.view.adapter.ReplyFeedAdapter;
import com.laudhoot.view.fragment.ShoutFeedFragment;
import com.laudhoot.web.model.BaseTO;
import com.laudhoot.web.model.ReplyTO;
import com.laudhoot.web.model.ShoutTO;
import com.laudhoot.web.model.VoteTO;
import com.laudhoot.web.services.LaudhootAPI;
import com.laudhoot.web.util.AuthorizationUtil;
import com.laudhoot.web.util.BaseCallback;
import com.laudhoot.web.util.WebTask;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ViewShoutActivity extends ActionBarActivity implements EndlessListView.EndlessListener,
        EmojiconsFragment.OnEmojiconBackspaceClickedListener, EmojiconGridFragment.OnEmojiconClickedListener {

    private String clientId;

    private String geofenceCode;

    private Long shoutId;

    @Bind(R.id.view_shout_content)
    View shoutContent;

    @Bind(R.id.message)
    EmojiconTextView message;

    @Bind(R.id.showReplyEmojicons)
    ImageView emojiButton;

    @Bind(R.id.laudhoot_difference)
    TextView laudhootDifference;

    @Bind(R.id.laud)
    Button laud;

    @Bind(R.id.hoot)
    Button hoot;

    @Bind(R.id.comments)
    TextView comments;

    @Bind(R.id.laud_count)
    TextView laudCount;

    @Bind(R.id.hoot_count)
    TextView hootCount;

    @Bind(R.id.elapsed_time)
    TextView elapsedTime;

    @Bind(R.id.reply_text)
    EmojiconEditText reply;

    @Bind(R.id.post_reply)
    Button postReply;

    @Bind(R.id.endless_replies_feed)
    EndlessListView repliesView;

    ReplyFeedAdapter repliesAdapter;

    @Inject
    ShoutRepository shoutRepository;

    @Inject
    public ReplyRepository replyRepository;

    @Inject
    ClientDetailsRepository clientDetailsRepository;

    @Inject
    LaudhootAPI laudhootAPI;

    @Inject
    Toaster toaster;

    @Inject
    NetworkStateManager networkStateManager;

    EmojiconsFragment emojiconsFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shout);
        ButterKnife.bind(this);
        ((Laudhoot) (getApplication())).inject(this);
        shoutId = getIntent().getLongExtra(ShoutFeedFragment.SHOUT_ID, 0);
        clientId = getIntent().getStringExtra(InitializationActivity.CLIENT_ID);
        geofenceCode = getIntent().getStringExtra(InitializationActivity.GEOFENCE_CODE);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.laudhoot_theme_color)));
        actionBar.setIcon(R.drawable.ic_launcher);
        actionBar.setDisplayHomeAsUpEnabled(true);

        repliesAdapter = new ReplyFeedAdapter(ViewShoutActivity.this, new ArrayList<Reply>());
        repliesView.setLoadingView(R.layout.loading_layout);
        repliesView.setAdapter(repliesAdapter);
        repliesView.setEndlessListener(this);

        postReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String replyText = reply.getText().toString();
                if (replyText != null && replyText.length() > 0) {
                    new PostReplyTask().execute(geofenceCode, replyText,
                            AuthorizationUtil.authorizationToken(clientDetailsRepository.findByClientId(clientId)));
                } else {
                    toaster.makeToast(getString(R.string.shout_text_empty));
                }
            }
        });

        reply.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideEmojiconFragment();
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView(shoutRepository.findCached(shoutId));
    }

    @OnClick(R.id.showReplyEmojicons)
    public void handleEmojiconFragment() {
        if(emojiconsFragment == null) {
            emojiconsFragment = EmojiconsFragment.newInstance(false);
        }
        if(emojiconsFragment.isVisible()) {
            hideEmojiconFragment();
        } else {
            showEmojiconFragment();
            hideKeyboard();
        }
    }

    private void showEmojiconFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.emojicons, emojiconsFragment).commit();
        shoutContent.animate().translationYBy(
                -1*getResources().getDimensionPixelSize(R.dimen.emojicons_height)).setDuration(500);
        emojiButton.setBackgroundColor(getResources().getColor(R.color.laudhoot_theme_color));
    }

    private void hideEmojiconFragment() {
        if(emojiconsFragment != null && (!emojiconsFragment.isDetached()) && (!emojiconsFragment.isRemoving())) {
            getSupportFragmentManager().beginTransaction()
                    .remove(emojiconsFragment).commit();
            shoutContent.animate().translationYBy(
                    getResources().getDimensionPixelSize(R.dimen.emojicons_height)).setDuration(500);
            emojiButton.setBackgroundColor(getResources().getColor(R.color.switch_thumb_normal_material_dark));
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void refreshView(final Shout shout) {
        message.setText(shout.getMessage());
        if (shout.getLaudCount() == null || shout.getHootCount() == null) {
            laudhootDifference.setText("0");
        } else {
            laudhootDifference.setText(String.valueOf(shout.getLaudCount() - shout.getHootCount()));
        }

        comments.setText(shout.getRepliesCount() + " comments");

        if (shout.getCreatedOn() == null) {
            elapsedTime.setText("Just Now");
        } else {
            elapsedTime.setText(DateUtil.getElapsedDuration(shout.getCreatedOn()));
        }

        if (shout.getLaudCount() == null)
            laudCount.setText("0");
        else
            laudCount.setText(String.valueOf(shout.getLaudCount()));

        if (shout.getHootCount() == null)
            hootCount.setText("0");
        else
            hootCount.setText(String.valueOf(shout.getHootCount()));

        hoot.setRotation(180);
        if(shout.isVoted() && shout.getIsLaudVote() != null) {
            if(shout.getIsLaudVote()) {
                laud.setBackgroundResource(R.drawable.arrow_active);
                laud.setEnabled(false);
                setHootOnClickListener(shout.getDomainId());
            } else {
                hoot.setBackgroundResource(R.drawable.arrow_active);
                hoot.setEnabled(false);
                setLaudOnClickListener(shout.getDomainId());
            }
        } else {
            setLaudOnClickListener(shout.getDomainId());
            setHootOnClickListener(shout.getDomainId());
        }
    }

    private void setHootOnClickListener(final long shoutId) {
        hoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voteShout(v, shoutId, false);
            }
        });
    }

    private void setLaudOnClickListener(final long shoutId) {
        laud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voteShout(v, shoutId, true);
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
            case R.id.refresh_shout: {
                laudhootAPI.refreshShout(shoutId,
                        AuthorizationUtil.authorizationToken(clientDetailsRepository.findByClientId(clientId)),
                        new BaseCallback<ShoutTO>(getApplicationContext()) {
                            @Override
                            protected void success(ShoutTO shoutTO, Response response, Context context) {
                                refreshView(shoutRepository.cache(shoutTO));
                                toaster.makeToast("Refreshed.");
                            }
                            @Override
                            protected void failure(RetrofitError error, Context context) {

                            }
                        });
                break;
            }
            case R.id.home: {
                abortResult();
                break;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void loadData(int count) {
        new RepliesLoader().execute(String.valueOf(shoutId), String.valueOf(count),
                AuthorizationUtil.authorizationToken(clientDetailsRepository.findByClientId(clientId)));
    }

    private void voteShout(final View v, final long postId, final Boolean isLaud) {
        laud.setEnabled(false);
        hoot.setEnabled(false);
        VoteTO voteTO = new VoteTO(postId, isLaud);
        laudhootAPI.votePost(voteTO,
                AuthorizationUtil.authorizationToken(clientDetailsRepository.findByClientId(clientId)),
                new BaseCallback<VoteTO>(getApplicationContext()) {
                    @Override
                    protected void success(VoteTO voteTO, Response response, Context context) {
                        shoutRepository.vote(voteTO.getPostId(), voteTO.getIsLaud());
                        updateViewFromVote(voteTO.getIsLaud());
                    }

                    @Override
                    protected void failure(RetrofitError error, Context context) {
                        laud.setEnabled(true);
                        hoot.setEnabled(true);
                    }
                });
    }

    private void updateViewFromVote(boolean isLaud) {
        if (isLaud) {
            laud.setBackgroundResource(R.drawable.arrow_active);
            laudhootDifference.setText(String.valueOf(Integer.valueOf(laudhootDifference.getText().toString()) + 1));
            laudCount.setText(String.valueOf(Integer.valueOf(laudCount.getText().toString()) + 1));
            laud.setEnabled(false);
            hoot.setBackgroundResource(R.drawable.arrow_inactive);
            hoot.setEnabled(true);
        } else {
            hoot.setBackgroundResource(R.drawable.arrow_active);
            laudhootDifference.setText(String.valueOf(Integer.valueOf(laudhootDifference.getText().toString()) - 1));
            hootCount.setText(String.valueOf(Integer.valueOf(hootCount.getText().toString()) + 1));
            hoot.setEnabled(false);
            laud.setBackgroundResource(R.drawable.arrow_inactive);
            laud.setEnabled(true);
        }
    }

    private class RepliesLoader extends AsyncTask<String, Void, List<Reply>> {

        @Override
        protected List<Reply> doInBackground(String... params) {
            List<ReplyTO> replyTOs;
            List<Reply> replies = new ArrayList<Reply>();
            try {
                replyTOs = laudhootAPI.listRepliesOfShout(params[0], params[1], params[2]);
                if (replyTOs != null) {
                    for (ReplyTO replyTO : replyTOs) {
                        replies.add(shoutRepository.cacheReply(replyTO, Long.valueOf(params[0])));
                    }
                }
            } catch (Exception exception) {
                if (Laudhoot.D)
                    Log.d(Laudhoot.LOG_TAG, exception.getMessage()
                            + " | "
                            + Arrays.toString(exception.getStackTrace()));
                return null;
            }
            return replies;
        }

        @Override
        protected void onPostExecute(List<Reply> result) {
            super.onPostExecute(result);
            if (result == null || result.size() < 1) {
                if (Laudhoot.D)
                    toaster.makeToast(getString(R.string.error_loading_replies));
                repliesView.noDataAvailable();
            } else {
                repliesView.addNewData(result);
            }
        }

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
                if (Laudhoot.D)
                    Log.d(Laudhoot.LOG_TAG, exception.getMessage()
                            + " | "
                            + exception.getStackTrace());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Reply response) {
            super.onPostExecute(response);
            if (response != null) {
                repliesAdapter.insert(response, repliesAdapter.getCount());
                reply.setText("");
                repliesView.smoothScrollToPosition(repliesAdapter.getCount()-1);
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

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(reply, emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View view) {
        EmojiconsFragment.backspace(reply);
    }

    public Toaster getToaster() {
        return toaster;
    }

    public String getClientId() {
        return clientId;
    }

    public String getGeofenceCode() {
        return geofenceCode;
    }

    public LaudhootAPI getLaudhootApiClient() {
        return laudhootAPI;
    }

    public ClientDetailsRepository getClientDetailsRepository() {
        return clientDetailsRepository;
    }
}
