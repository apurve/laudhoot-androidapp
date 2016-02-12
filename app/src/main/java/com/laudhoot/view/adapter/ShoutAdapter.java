package com.laudhoot.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.laudhoot.Laudhoot;
import com.laudhoot.R;
import com.laudhoot.persistence.model.view.Shout;
import com.laudhoot.persistence.repository.ClientDetailsRepository;
import com.laudhoot.persistence.repository.ShoutRepository;
import com.laudhoot.util.DateUtil;
import com.laudhoot.view.fragment.ShoutFeedFragment;
import com.laudhoot.web.model.VoteTO;
import com.laudhoot.web.services.LaudhootAPI;
import com.laudhoot.web.util.AuthorizationUtil;
import com.laudhoot.web.util.BaseCallback;
import com.rockerhieu.emojicon.EmojiconTextView;

import java.util.List;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Adapter to populate shout feed.
 *
 * Created by apurve on 9/5/15.
 */
public class ShoutAdapter extends WebFeedAdapter<Shout, ShoutHolder> {

    private Activity activity;

    private String clientId;

    @Inject
    protected LaudhootAPI laudhootAPI;

    @Inject
    protected ClientDetailsRepository clientDetailsRepository;

    @Inject
    public ShoutRepository shoutRepository;

    public ShoutAdapter(Activity activity, String clientId, List<Shout> shouts) {
        super(activity.getApplicationContext(), shouts, R.layout.shout_feed_item, R.layout.endless_feed_empty);
        this.activity = activity;
        this.clientId = clientId;
        ((Laudhoot) (activity.getApplication())).inject(this);
    }

    @Override
    public ShoutHolder createViewHolder(View convertView, final Shout shout) {
        final ShoutHolder viewHolder = new ShoutHolder();
        viewHolder.message = (EmojiconTextView) convertView.findViewById(R.id.message);
        viewHolder.laudhootDifference = (TextView) convertView.findViewById(R.id.laudhoot_difference);
        viewHolder.comments = (TextView) convertView.findViewById(R.id.comments);
        viewHolder.elapsedTime = (TextView) convertView.findViewById(R.id.elapsed_time);
        viewHolder.laudCount = (TextView) convertView.findViewById(R.id.laud_count);
        viewHolder.hootCount = (TextView) convertView.findViewById(R.id.hoot_count);
        viewHolder.laud = (Button) convertView.findViewById(R.id.laud);
        viewHolder.hoot = (Button) convertView.findViewById(R.id.hoot);
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

        viewHolder.hoot.setRotation(180);
        if(item.isVoted() && item.getIsLaudVote() != null) {
            if(item.getIsLaudVote()) {
                viewHolder.laud.setBackgroundResource(R.drawable.arrow_active);
                viewHolder.laud.setEnabled(false);
                setHootOnClickListener(viewHolder, item.getDomainId());
            } else {
                viewHolder.hoot.setBackgroundResource(R.drawable.arrow_active);
                viewHolder.hoot.setEnabled(false);
                setLaudOnClickListener(viewHolder, item.getDomainId());
            }
        } else {
            setLaudOnClickListener(viewHolder, item.getDomainId());
            setHootOnClickListener(viewHolder, item.getDomainId());
        }
    }

    private void setLaudOnClickListener(final ShoutHolder viewHolder, final long shoutId) {
        viewHolder.laud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voteShout(viewHolder, shoutId, true);
                }
        });
    }

    private void setHootOnClickListener(final ShoutHolder viewHolder, final long shoutId) {
        viewHolder.hoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voteShout(viewHolder, shoutId, false);
                }
        });
    }

    @Override
    public void updateEmptyViewHolder(EmptyViewHolder viewHolder) {
        viewHolder.title.setText(R.string.shout_feed_empty_title);
        viewHolder.message.setText(R.string.shout_feed_empty_message);
    }

    private void voteShout(final ShoutHolder viewHolder, final long shoutId, final Boolean isLaud) {
        viewHolder.laud.setEnabled(false);
        viewHolder.hoot.setEnabled(false);
        VoteTO voteTO = new VoteTO(shoutId, isLaud);
        laudhootAPI.votePost(voteTO,
                AuthorizationUtil.authorizationToken(clientDetailsRepository.findByClientId(clientId)),
                new BaseCallback<VoteTO>(activity.getApplicationContext()) {
                    @Override
                    protected void success(VoteTO voteTO, Response response, Context context) {
                        shoutRepository.vote(voteTO.getPostId(), voteTO.getIsLaud());
                        viewHolder.updateViewFromVote(voteTO.getIsLaud());
                    }

                    @Override
                    protected void failure(RetrofitError error, Context context) {
                        viewHolder.laud.setEnabled(true);
                        viewHolder.hoot.setEnabled(true);
                    }
                });
    }

}

class ShoutHolder extends WebFeedAdapter.ViewHolder {
    EmojiconTextView message;
    TextView laudhootDifference;
    Button laud;
    Button hoot;
    TextView comments;
    TextView laudCount;
    TextView hootCount;
    TextView elapsedTime;

    public void updateViewFromVote(boolean isLaud) {
        if (isLaud) {
            this.laud.setBackgroundResource(R.drawable.arrow_active);
            this.laudhootDifference.setText(String.valueOf(Integer.valueOf(this.laudhootDifference.getText().toString())+1));
            this.laudCount.setText(String.valueOf(Integer.valueOf(this.laudCount.getText().toString())+1));
            this.laud.setEnabled(false);
            this.hoot.setBackgroundResource(R.drawable.arrow_inactive);
            this.hoot.setEnabled(true);
        } else {
            this.hoot.setBackgroundResource(R.drawable.arrow_active);
            this.laudhootDifference.setText(String.valueOf(Integer.valueOf(this.laudhootDifference.getText().toString())-1));
            this.hootCount.setText(String.valueOf(Integer.valueOf(this.hootCount.getText().toString())+1));
            this.hoot.setEnabled(false);
            this.laud.setBackgroundResource(R.drawable.arrow_inactive);
            this.laud.setEnabled(true);
        }
    }
}