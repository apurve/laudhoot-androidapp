package com.laudhoot.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.laudhoot.R;
import com.laudhoot.view.EndlessListView;

import java.util.List;

/**
 * Custom extension of array adapter for web feed, as availability of web feed is not guaranteed the adapter works
 * with an empty list view which is displayed in preceding scenario.
 * It also implements {@link com.laudhoot.view.EndlessListView.EndlessAdapter} out of the box.
 * <p/>
 * Created by root on 1/10/15.
 */
public abstract class WebFeedAdapter<I, H extends WebFeedAdapter.ViewHolder> extends ArrayAdapter<I> implements EndlessListView.EndlessAdapter<I> {

    protected LayoutInflater inflater = null;

    protected int feedItemId, emptyItemId;

    public WebFeedAdapter(Context context, List<I> items, int feedItemId, int emptyItemId) {
        super(context, feedItemId, items);
        this.feedItemId = feedItemId;
        this.emptyItemId = emptyItemId;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public WebFeedAdapter(Context context, List<I> items, int feedItemId) {
        super(context, feedItemId, items);
        this.feedItemId = feedItemId;
        this.emptyItemId = R.layout.endless_feed_empty;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (getCount() < 1) {
            EmptyViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(emptyItemId, null);
                holder = new EmptyViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.title_empty);
                holder.message = (TextView) convertView.findViewById(R.id.message_empty);
                convertView.setTag(holder);
            } else {
                holder = (EmptyViewHolder) convertView.getTag();
            }
            holder.position = position;
            updateEmptyViewHolder(holder);
        } else {
            H holder;
            final I item = getItem(position);
            if (convertView == null) {
                convertView = inflater.inflate(feedItemId, null);
                holder = createViewHolder(convertView, item);
                convertView.setTag(holder);
            } else {
                holder = (H) convertView.getTag();
            }
            holder.position = position;
            updateViewHolder(holder, item);
        }
        return convertView;
    }

    public abstract H createViewHolder(View convertView, final I item);

    public abstract void updateViewHolder(H viewHolder, I item);

    public abstract void updateEmptyViewHolder(EmptyViewHolder viewHolder);

    public static class EmptyViewHolder {
        TextView title;
        TextView message;
        int position;
    }

    public static class ViewHolder {
        int position;
    }

}
