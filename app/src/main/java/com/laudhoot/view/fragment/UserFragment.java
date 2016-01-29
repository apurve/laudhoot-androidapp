package com.laudhoot.view.fragment;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.laudhoot.Laudhoot;
import com.laudhoot.R;
import com.laudhoot.persistence.model.view.Notification;
import com.laudhoot.persistence.model.view.Shout;
import com.laudhoot.persistence.repository.NotificationRepository;
import com.laudhoot.util.Toaster;
import com.laudhoot.view.activity.InitializationActivity;
import com.laudhoot.view.activity.MainActivity;
import com.laudhoot.view.activity.UserShoutsActivity;
import com.laudhoot.view.activity.ViewShoutActivity;
import com.laudhoot.web.WebConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Fragment for user notifications and user settings.
 * <p/>
 * Created by root on 4/1/16.
 */
public class UserFragment extends BaseFragment {

    private static final int REQUEST_CODE_USER_SHOUTS = 221;

    private static final int REQUEST_CODE_VIEW_SHOUT = 222;

    @Bind(R.id.my_stuff_list)
    ListView myStuffList;

    @Bind(R.id.love_on_us_list)
    ListView loveOnUsList;

    @Bind(R.id.other_stuff_list)
    ListView otherStuffList;

    @Bind(R.id.notifications_list)
    ListView notifications;

    String[] myStuffItems = new String[]{"My Shouts"
            // TODO - ,"My Comments"
            // TODO - ,"Manage Favourite Locations"
            // TODO - ,"Settings"
    };

    String[] loveOnUsItems = new String[]{"Share Laudhoot"
            , "Rate Laudhoot"
            , "Follow us on Facebook"
            , "Follow us on Twitter"
    };

    String[] otherStuffItems = new String[]{"Contact Us"
            , "Rules and Info"
            , "Terms of Service"
            , "Privacy Policy"
    };

    List<Notification> notificationItems = new ArrayList<Notification>();

    @Inject
    NotificationRepository notificationRepository;

    @Inject
    Toaster toaster;

    public static Fragment newInstance(Integer sectionNumber) {
        return initFragment(new UserFragment(), sectionNumber);
    }

    public UserFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_stuff, container, false);
        ButterKnife.bind(this, rootView);
        handleMyStuff(rootView);
        handleLoveOnUs(rootView);
        handleOtherStuff(rootView);
        handleNotifications(rootView);
        return rootView;
    }

    private void handleNotifications(View rootView) {
        updateNotifications();
        final ArrayAdapter<Notification> myNotificationAdapter = new ArrayAdapter<Notification>(
                getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, notificationItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setText(getItem(position).getMessage());
                text.setTextColor(Color.BLACK);
                if(Notification.ActionCode.DO_NOTHING == getItem(position).getActionCode()) {
                    text.setClickable(false);
                }
                return view;
            }
        };
        notifications.setAdapter(myNotificationAdapter);
        notifications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Notification notification = myNotificationAdapter.getItem(position);
                switch (notification.getActionCode()) {
                    case Notification.ActionCode.SHOW_SHOUT: {
                        Intent intent = new Intent(activity, ViewShoutActivity.class);
                        intent.putExtra(SHOUT_ID, notification.getData());
                        intent.putExtra(InitializationActivity.CLIENT_ID, activity.getClientId());
                        intent.putExtra(InitializationActivity.GEOFENCE_CODE, activity.getGeofenceCode());
                        startActivityForResult(intent, REQUEST_CODE_VIEW_SHOUT);
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        });
    }

    private void updateNotifications() {
        notificationItems = notificationRepository.getNotifications();
        if (notificationItems == null || notificationItems.size() < 1) {
            Notification notification = new Notification();
            notification.setMessage(getString(R.string.no_notifications));
            notification.setActionCode(Notification.ActionCode.DO_NOTHING);
            notificationItems.add(notification);
        }
    }

    private void handleMyStuff(View rootView) {
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
                switch (position) {
                    case 0: {
                        Intent intent = new Intent(getActivity(), UserShoutsActivity.class);
                        intent.putExtra(InitializationActivity.CLIENT_ID, activity.getClientId());
                        intent.putExtra(InitializationActivity.GEOFENCE_CODE, activity.getGeofenceCode());
                        startActivityForResult(intent, REQUEST_CODE_USER_SHOUTS);
                        break;
                    }
                    case 1: {
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        });
    }

    private void handleLoveOnUs(View rootView) {
        ArrayAdapter<String> loveOnUsAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, loveOnUsItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                return view;
            }
        };
        loveOnUsList.setAdapter(loveOnUsAdapter);
        loveOnUsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0 : {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, WebConstants.SHARE_SUBJECT);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, WebConstants.SHARE_TEXT);
                        startActivity(Intent.createChooser(shareIntent, "How do you want to share?"));
                        break;
                    }
                    case 1 : {
                        Uri uri = Uri.parse("market://details?id=" + getMainActivity().getApplicationContext().getPackageName());
                        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        myAppLinkToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(myAppLinkToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + getMainActivity().getApplicationContext().getPackageName())));
                        }
                        break;
                    }
                    case 2 : {
                        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                        facebookIntent.setData(Uri.parse(WebConstants.FACEBOOK_URL));
                        startActivity(facebookIntent);
                        break;
                    }
                    case 3 : {
                        Intent twitterIntent = new Intent(Intent.ACTION_VIEW);
                        twitterIntent.setData(Uri.parse(WebConstants.TWITTER_URL));
                        startActivity(twitterIntent);
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        });
    }

    private void handleOtherStuff(View rootView) {
        ArrayAdapter<String> otherStuffAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, otherStuffItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                return view;
            }
        };
        otherStuffList.setAdapter(otherStuffAdapter);
        otherStuffList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        break;
                    }
                    case 1: {
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        });
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
