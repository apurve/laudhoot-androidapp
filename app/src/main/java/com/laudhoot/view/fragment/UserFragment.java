package com.laudhoot.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Fragment for user notifications and user settings.
 *
 * Created by root on 4/1/16.
 */
public class UserFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static UserFragment newInstance(Integer sectionNumber) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public UserFragment() {

    }
}
