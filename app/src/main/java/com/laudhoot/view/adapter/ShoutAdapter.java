package com.laudhoot.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.laudhoot.R;
import com.laudhoot.view.fragment.ShoutFeedFragment;
import com.laudhoot.web.model.ShoutTO;

import java.util.List;

/**
 * Adapter to populate shout feed.
 *
 * Created by apurve on 9/5/15.
 */
public class ShoutAdapter extends WebFeedAdapter<ShoutTO, ShoutHolder> {

    private ShoutFeedFragment fragment;

    private static LayoutInflater inflater = null;

    public ShoutAdapter(ShoutFeedFragment fragment, List<ShoutTO> shouts) {
        super(fragment.getActivity().getApplicationContext(), shouts, R.layout.geofence_feed_item, R.layout.geofence_feed_empty);
        this.fragment = fragment;
        inflater = (LayoutInflater) fragment.getActivity().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ShoutHolder createViewHolder(View convertView, final ShoutTO shoutTO) {
        ShoutHolder viewHolder = new ShoutHolder();
        viewHolder.message = (TextView) convertView.findViewById(R.id.message);
        viewHolder.laudhootCount = (TextView) convertView.findViewById(R.id.laudhoot_count);
        viewHolder.laud = (Button) convertView.findViewById(R.id.laud);
        viewHolder.hoot = (Button) convertView.findViewById(R.id.hoot);
        viewHolder.laud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.laudShout(v, shoutTO.getId());
            }
        });
        viewHolder.hoot.setRotation(180);
        viewHolder.hoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.hootShout(v, shoutTO.getId());
            }
        });
        return viewHolder;
    }

    @Override
    public void updateViewHolder(ShoutHolder viewHolder, ShoutTO item) {
        viewHolder.message.setText(item.getMessage());
        if(item.getLaudCount() == null || item.getHootCount() == null) {
            viewHolder.laudhootCount.setText("1");
        } else {
            viewHolder.laudhootCount.setText(String.valueOf(item.getLaudCount() - item.getHootCount()));
        }
    }

    @Override
    public void updateEmptyViewHolder(EmptyViewHolder viewHolder) {
        viewHolder.title.setText(R.string.geofence_feed_empty_title);
        viewHolder.message.setText(R.string.geofence_feed_empty_message);
    }

}

class ShoutHolder extends WebFeedAdapter.ViewHolder {
    TextView message;
    TextView laudhootCount;
    Button laud;
    Button hoot;
}