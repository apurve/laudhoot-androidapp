package com.laudhoot.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;

import com.laudhoot.Laudhoot;
import com.laudhoot.R;
import com.laudhoot.view.activity.MainActivity;

/**
 * Created by root on 14/1/16.
 */
public abstract class BaseFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static final String SHOUT_ID = "shout_id";

    protected MainActivity activity = null;

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
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    protected static Fragment initFragment(Fragment fragment, Integer sectionNumber) {
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.post_shout_button).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

}
