package com.laudhoot.view.fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.laudhoot.Laudhoot;
import com.laudhoot.R;
import com.laudhoot.persistence.model.view.Shout;
import com.laudhoot.util.Toaster;
import com.laudhoot.view.activity.InitializationActivity;
import com.laudhoot.view.activity.MainActivity;
import com.laudhoot.view.activity.UserShoutsActivity;
import com.laudhoot.view.activity.ViewShoutActivity;

import javax.inject.Inject;

/**
 * Fragment for user notifications and user settings.
 *
 * Created by root on 4/1/16.
 */
public class UserFragment extends BaseFragment {

    private static final int REQUEST_CODE_USER_SHOUTS = 221;

    ListView myStuffList;

    ListView notifications;

    String[] myStuffItems = new String[] { "My Shouts",
            "Settings"
    };

    @Inject
    Toaster toaster;

    public static Fragment newInstance(Integer sectionNumber) {
        return initFragment(new UserFragment(), sectionNumber);
    }

    public UserFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_stuff, container, false);
        myStuffList = (ListView) rootView.findViewById(R.id.my_stuff_list);
        ArrayAdapter<String> myStuffAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, myStuffItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                return view;
            }
        };
        myStuffList.setAdapter(myStuffAdapter);
        myStuffList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 0 : {
                        Intent intent = new Intent(getActivity(), UserShoutsActivity.class);
                        intent.putExtra(InitializationActivity.CLIENT_ID, activity.getClientId());
                        intent.putExtra(InitializationActivity.GEOFENCE_CODE, activity.getGeofenceCode());
                        startActivityForResult(intent, REQUEST_CODE_USER_SHOUTS);
                        break;
                    }
                    case 1 : {
                        break;
                    }
                    default : {
                        break;
                    }
                }
            }
        });

        notifications = (ListView) rootView.findViewById(R.id.notifications_list);
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_USER_SHOUTS: {
                if (resultCode == Activity.RESULT_OK) {

                }
                break;
            }
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
