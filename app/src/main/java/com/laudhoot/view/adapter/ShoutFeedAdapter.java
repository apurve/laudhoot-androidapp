package com.laudhoot.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.laudhoot.R;
import com.laudhoot.persistence.model.GeoFenceTransition;
import com.laudhoot.persistence.repository.CRUDRepository;
import com.laudhoot.view.activity.LocationAwareActivity;
import com.laudhoot.view.fragment.GeoFenceFragment;
import com.laudhoot.web.model.ShoutTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter to populate shout feed.
 *
 * TODO - create a base web feed adapter which can be used a utility by other adapters.
 *
 * Created by apurve on 9/5/15.
 */
public class ShoutFeedAdapter extends BaseAdapter {

    private GeoFenceFragment fragment;

    private List<ShoutTO> shouts;

    private static LayoutInflater inflater = null;

    public ShoutFeedAdapter(GeoFenceFragment fragment, List<ShoutTO> shouts) {
        super();
        this.fragment = fragment;
        this.shouts = shouts;
        inflater = (LayoutInflater) fragment.getActivity().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (shouts == null || shouts.size() <= 0)
            return 1;
        return shouts.size();
    }

    @Override
    public Object getItem(int position) {
        if (shouts == null || shouts.size() <= 0)
            return null;
        return shouts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (shouts == null || shouts.size() <= 0) {
            EmptyViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.geofence_feed_empty, null);
                holder = new EmptyViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.title_empty);
                holder.message = (TextView) convertView.findViewById(R.id.message_empty);
                convertView.setTag(holder);
            } else {
                holder = (EmptyViewHolder) convertView.getTag();
            }
            holder.position = position;
            holder.title.setText(R.string.geofence_feed_empty_title);
            holder.message.setText(R.string.geofence_feed_empty_message);
        } else {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.geofence_feed_item, null);
                holder = new ViewHolder();
                holder.message = (TextView) convertView.findViewById(R.id.message);
                holder.laudhootCount = (TextView) convertView.findViewById(R.id.laudhoot_count);
                holder.laud = (Button) convertView.findViewById(R.id.laud);
                holder.hoot = (Button) convertView.findViewById(R.id.hoot);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.position = position;
            final ShoutTO shout = shouts.get(position);
            holder.message.setText(shout.getMessage());
            holder.laudhootCount.setText(String.valueOf(shout.getLaudCount() - shout.getHootCount()));
            holder.laud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.laudShout(v, shout.getId());
                }
            });
            holder.hoot.setRotation(180);
            holder.hoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.hootShout(v, shout.getId());
                }
            });
        }
        return convertView;
    }

    public static class ViewHolder {
        TextView message;
        TextView laudhootCount;
        Button laud;
        Button hoot;
        int position;
    }

    public static class EmptyViewHolder {
        TextView title;
        TextView message;
        int position;
    }
}
