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
public class TermsOfServicesFragment extends Fragment {

    public TermsOfServicesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.terms_of_services, container, false);
        getActivity().setTitle(getString(R.string.title_terms_of_service));
        return rootView;
    }
}
