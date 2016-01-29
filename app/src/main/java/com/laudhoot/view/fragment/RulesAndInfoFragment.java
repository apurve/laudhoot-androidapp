package com.laudhoot.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laudhoot.R;

/**
 * Created by root on 29/1/16.
 */
public class RulesAndInfoFragment extends Fragment {

    public RulesAndInfoFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.rules_and_info, container, false);
        getActivity().setTitle(getString(R.string.title_rules_and_info));
        return rootView;
    }
}
