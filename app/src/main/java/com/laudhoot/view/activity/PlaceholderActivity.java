package com.laudhoot.view.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.laudhoot.R;
import com.laudhoot.view.fragment.PrivacyPolicyFragment;
import com.laudhoot.view.fragment.RulesAndInfoFragment;
import com.laudhoot.view.fragment.TermsOfServicesFragment;

/**
 * Created by root on 29/1/16.
 */
public class PlaceholderActivity extends ActionBarActivity {

    public static final String PLACEHOLDER_KEY = "placeholder";

    private static final int CONTENT_VIEW_ID = 10101010;
    public static final int PC_RULES_AND_INFO = 0;
    public static final int PC_TERMS_OF_SERVICE = 1;
    public static final int PC_PRIVACY_POLICY = 2;

    private int placeholderCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        placeholderCode = getIntent().getIntExtra(PLACEHOLDER_KEY, 0);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.laudhoot_theme_color)));
        actionBar.setIcon(R.drawable.ic_launcher);
        actionBar.setDisplayHomeAsUpEnabled(true);

        FrameLayout frame = new FrameLayout(this);
        frame.setId(CONTENT_VIEW_ID);
        setContentView(frame, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // If we're being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.
        if (savedInstanceState != null) {
            return;
        }

        Fragment fragment = null;
        switch(placeholderCode) {
            case PC_RULES_AND_INFO : {
                fragment = new RulesAndInfoFragment();break;
            }
            case PC_TERMS_OF_SERVICE : {
                fragment = new TermsOfServicesFragment();break;
            }
            case PC_PRIVACY_POLICY : {
                fragment = new PrivacyPolicyFragment();break;
            }
        }

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        fragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(CONTENT_VIEW_ID, fragment).commit();
    }
}
