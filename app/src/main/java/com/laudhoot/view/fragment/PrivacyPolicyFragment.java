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
public class PrivacyPolicyFragment extends Fragment {

    public PrivacyPolicyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.privacy_policy, container, false);
        getActivity().setTitle(getString(R.string.title_privacy_policy));
        return rootView;
    }
}
