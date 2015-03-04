package com.laudhoot.fragment;

/**
 * Created by root on 2/3/15.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.laudhoot.activity.R;
import com.laudhoot.util.WebTask;
import com.laudhoot.web.RestClient;
import com.laudhoot.web.TestAPI;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public Button button1;
    public Button button2;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(Integer sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        button1 = (Button) rootView.findViewById(R.id.button_1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new WebTaskOne(getActivity()).execute();
            }
        });

        button2 = (Button) rootView.findViewById(R.id.button_2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new WebTaskTwo(getActivity()).execute();
            }
        });
        return rootView;
    }

}

class WebTaskOne extends WebTask {

    public WebTaskOne(Activity activity){
        super(activity);
    }

    @Override
    protected String doInBackground(String... params) {
        super.doInBackground(params);
        TestAPI webTestApi = new RestClient().getTestWebService();
        return webTestApi.testController1();
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        Toast.makeText(
                activity.getApplicationContext(), "Executing /test/1... Result:" + response,
                Toast.LENGTH_LONG
        ).show();
    }
}

class WebTaskTwo extends WebTask {

    public WebTaskTwo(Activity activity){
        super(activity);
    }

    @Override
    protected String doInBackground(String... params) {
        super.doInBackground(params);
        TestAPI webTestApi = new RestClient().getTestWebService();
        return webTestApi.testController2();
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        Toast.makeText(
                activity.getApplicationContext(), "Executing /test/2... Result:" + response,
                Toast.LENGTH_LONG
        ).show();
    }
}