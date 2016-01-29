package com.laudhoot.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laudhoot.R;
import com.laudhoot.util.Toaster;

import javax.inject.Inject;

/**
 * Created by root on 23/1/16.
 */
public class ExploreFragment extends BaseFragment {

    @Inject
    Toaster toaster;

    public static Fragment newInstance(Integer sectionNumber) {
        return initFragment(new ExploreFragment(), sectionNumber);
    }

    public ExploreFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.explore, container, false);
        return rootView;
    }
}
