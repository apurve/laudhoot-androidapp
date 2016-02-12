package com.laudhoot.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

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
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostShoutActivity extends ActionBarActivity implements
        EmojiconsFragment.OnEmojiconBackspaceClickedListener, EmojiconGridFragment.OnEmojiconClickedListener {

    @Bind(R.id.shout_text)
    EmojiconEditText shout;

    @Bind(R.id.showEmojicons)
    ImageView emojiButton;

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

    EmojiconsFragment emojiconsFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_shout);
        ButterKnife.bind(this);
        ((Laudhoot) (getApplication())).inject(this);
        clientId = getIntent().getStringExtra(InitializationActivity.CLIENT_ID);
        geofenceCode = getIntent().getStringExtra(InitializationActivity.GEOFENCE_CODE);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.laudhoot_theme_color)));
        actionBar.setIcon(R.drawable.ic_launcher);
        actionBar.setDisplayHomeAsUpEnabled(true);

        shout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideEmojiconFragment();
                return false;
            }
        });
    }

    @OnClick(R.id.showEmojicons)
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
        emojiButton.setBackgroundColor(getResources().getColor(R.color.laudhoot_theme_color));
    }

    private void hideEmojiconFragment() {
        if(emojiconsFragment != null && (!emojiconsFragment.isDetached()) && (!emojiconsFragment.isRemoving())) {
            getSupportFragmentManager().beginTransaction()
                    .remove(emojiconsFragment).commit();
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

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(shout, emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View view) {
        EmojiconsFragment.backspace(shout);
    }
}