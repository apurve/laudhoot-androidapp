package com.laudhoot.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.laudhoot.R;
import com.laudhoot.persistence.model.view.Shout;
import com.laudhoot.util.DateUtil;
import com.laudhoot.view.fragment.ShoutFeedFragment;
import com.laudhoot.web.model.ShoutTO;

import java.util.List;

/**
 * Adapter to populate shout feed.
 *
 * Created by apurve on 9/5/15.
 */
public class ShoutAdapter extends WebFeedAdapter<Shout, ShoutHolder> {

    private ShoutFeedFragment fragment;

    private static LayoutInflater inflater = null;

    public ShoutAdapter(ShoutFeedFragment fragment, List<Shout> shouts) {
        super(fragment.getActivity().getApplicationContext(), shouts, R.layout.geofence_feed_item, R.layout.geofence_feed_empty);
        this.fragment = fragment;
        inflater = (LayoutInflater) fragment.getActivity().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ShoutHolder createViewHolder(View convertView, final Shout shout) {
        ShoutHolder viewHolder = new ShoutHolder();
        viewHolder.message = (TextView) convertView.findViewById(R.id.message);
        viewHolder.laudhootDifference = (TextView) convertView.findViewById(R.id.laudhoot_difference);
        viewHolder.comments = (TextView) convertView.findViewById(R.id.comments);
        viewHolder.elapsedTime = (TextView) convertView.findViewById(R.id.elapsed_time);
        viewHolder.laudCount = (TextView) convertView.findViewById(R.id.laud_count);
        viewHolder.hootCount = (TextView) convertView.findViewById(R.id.hoot_count);
        viewHolder.laud = (Button) convertView.findViewById(R.id.laud);
        viewHolder.hoot = (Button) convertView.findViewById(R.id.hoot);
        viewHolder.laud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.laudShout(v, shout.getDomainId());
            }
        });
        viewHolder.hoot.setRotation(180);
        viewHolder.hoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.hootShout(v, shout.getDomainId());
            }
        });
        return viewHolder;
    }

    @Override
    public void updateViewHolder(ShoutHolder viewHolder, Shout item) {
        viewHolder.message.setText(item.getMessage());
        if(item.getLaudCount() == null || item.getHootCount() == null) {
            viewHolder.laudhootDifference.setText("0");
        } else {
            viewHolder.laudhootDifference.setText(String.valueOf(item.getLaudCount() - item.getHootCount()));
        }

        viewHolder.comments.setText(item.getRepliesCount()+" comments");

        if(item.getCreatedOn() == null) {
            viewHolder.elapsedTime.setText("Just Now");
        } else {
            viewHolder.elapsedTime.setText(DateUtil.getElapsedDuration(item.getCreatedOn()));
        }

        if(item.getLaudCount() == null)
            viewHolder.laudCount.setText("0");
        else
            viewHolder.laudCount.setText(String.valueOf(item.getLaudCount()));

        if(item.getHootCount() == null)
            viewHolder.hootCount.setText("0");
        else
            viewHolder.hootCount.setText(String.valueOf(item.getHootCount()));
    }

    @Override
    public void updateEmptyViewHolder(EmptyViewHolder viewHolder) {
        viewHolder.title.setText(R.string.geofence_feed_empty_title);
        viewHolder.message.setText(R.string.geofence_feed_empty_message);
    }

}

class ShoutHolder extends WebFeedAdapter.ViewHolder {
    TextView message;
    TextView laudhootDifference;
    Button laud;
    Button hoot;
    TextView comments;
    TextView laudCount;
    TextView hootCount;
    TextView elapsedTime;
}