package com.laudhoot.view.fragment;

/**
 * Created by apurve on 2/3/15.
 */

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.laudhoot.Laudhoot;
import com.laudhoot.R;
import com.laudhoot.view.activity.LocationAwareActivity;
import com.laudhoot.service.GeofenceTransitionsIntentService;
import com.laudhoot.util.NetworkStateManager;
import com.laudhoot.web.util.WebTask;
import com.laudhoot.web.services.TestAPI;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import javax.inject.Inject;


/**
 * A fragment containing a simple view for testing purpose.
 *
 * Created by apurve on 12/12/14.
 */
public class PlaceholderFragment extends Fragment implements ResultCallback<Status> {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private LocationAwareActivity activity = null;

    private Button button1;
    private Button button2;
    private Button button3;

    @InjectView(R.id.section_label)
    public TextView textView;

    @InjectView(R.id.radius)
    public EditText radiusView;

    @InjectView(R.id.life)
    public EditText lifeView;

    @Inject
    TestAPI webTestApi;

    @Inject
    NetworkStateManager networkStateManager;

    List<Geofence> geoFences = new ArrayList<>();

    GeofencingRequest geofencingRequest;

    PendingIntent geoFePendingIntent;
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (LocationAwareActivity) activity;
        if (Laudhoot.D) {
            Log.e(Laudhoot.LOG_TAG, "+++ ON FRAGMENT ATTACH +++");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, rootView);
        ((Laudhoot) (getActivity().getApplication())).inject(this);
        button1 = (Button) rootView.findViewById(R.id.button_1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SampleWebTask().execute();
            }
        });

        button2 = (Button) rootView.findViewById(R.id.button_2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String radius = radiusView.getText().toString();
                String life = lifeView.getText().toString();
                if (radius.isEmpty() || life.isEmpty()) {
                    activity.makeToast("Radius or life cannot be blank.");
                } else if (activity.getLastLocation() == null) {
                    activity.makeToast("Turn on your GPS.");
                } else {
                    createGeoFence(activity.getLastLocation().getLatitude(),
                            activity.getLastLocation().getLongitude(),
                            Float.valueOf(radius),
                            Long.valueOf(life));
                    geoFePendingIntent = getGeofencePendingIntent();
                    LocationServices.GeofencingApi.addGeofences(
                            activity.getLocationApiClient(),
                            getGeofencingRequest(),
                            geoFePendingIntent
                    ).setResultCallback(PlaceholderFragment.this);
                }
            }
        });

        return rootView;
    }

    private void createGeoFence(double lat, double lan, float radius, long lifeInHrs){
        geoFences.add(new Geofence.Builder()
                .setRequestId(String.valueOf(geoFences.size()))
                .setCircularRegion(lat, lan, radius)
                .setExpirationDuration(lifeInHrs*60*60*1000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geoFences);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (geoFePendingIntent != null) {
            return geoFePendingIntent;
        }
        Intent intent = new Intent(activity, GeofenceTransitionsIntentService.class);
        /*We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        calling addGeofences() and removeGeofences().*/
        return PendingIntent.getService(activity, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onResult(Status status) {
        if(status.hasResolution()){
            activity.makeToast("Cannot create GeoFence.");
        }else{
            activity.makeToast("Successfully created GeoFence.");
        }

    }

    class SampleWebTask extends WebTask {

        public SampleWebTask(){
            super(getActivity(), networkStateManager);
        }

        @Override
        protected String doInBackground(String... params) {
            super.doInBackground(params);
            return webTestApi.testController1();
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            Toast.makeText(
                    getActivity().getApplicationContext(),
                    "Executing /test/1... Result:" + response,
                    Toast.LENGTH_LONG
            ).show();
        }
    }

}