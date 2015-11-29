package com.laudhoot.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.laudhoot.R;
import com.laudhoot.persistence.model.view.Reply;
import com.laudhoot.util.DateUtil;
import com.laudhoot.util.Toaster;
import com.laudhoot.view.activity.ViewShoutActivity;
import com.laudhoot.web.model.VoteTO;
import com.laudhoot.web.util.AuthorizationUtil;
import com.laudhoot.web.util.BaseCallback;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by root on 14/11/15.
 */
public class ReplyFeedAdapter extends WebFeedAdapter<Reply, ReplyHolder> {

    private static LayoutInflater inflater = null;

    private ViewShoutActivity activity;

    public ReplyFeedAdapter(ViewShoutActivity activity, List<Reply> shouts) {
        super(activity.getApplicationContext(), shouts, R.layout.reply_feed_item, R.layout.endless_feed_empty);
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity = activity;
    }

    @Override
    public ReplyHolder createViewHolder(View convertView, final Reply reply) {
        final ReplyHolder viewHolder = new ReplyHolder();
        viewHolder.message = (TextView) convertView.findViewById(R.id.reply_message);
        viewHolder.laudhootDifference = (TextView) convertView.findViewById(R.id.reply_laudhoot_difference);
        viewHolder.elapsedTime = (TextView) convertView.findViewById(R.id.reply_elapsed_time);
        viewHolder.laudCount = (TextView) convertView.findViewById(R.id.reply_laud_count);
        viewHolder.hootCount = (TextView) convertView.findViewById(R.id.reply_hoot_count);
        viewHolder.laud = (Button) convertView.findViewById(R.id.reply_laud);
        viewHolder.hoot = (Button) convertView.findViewById(R.id.reply_hoot);
        return viewHolder;
    }

    @Override
    public void updateViewHolder(ReplyHolder viewHolder, Reply item) {
        viewHolder.message.setText(item.getMessage());
        if(item.getLaudCount() == null || item.getHootCount() == null) {
            viewHolder.laudhootDifference.setText("0");
        } else {
            viewHolder.laudhootDifference.setText(String.valueOf(item.getLaudCount() - item.getHootCount()));
        }

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

    private void setLaudOnClickListener(final ReplyHolder viewHolder, final long replyId) {
        viewHolder.laud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voteReply(viewHolder, replyId, true);
                }
        });
    }

    private void setHootOnClickListener(final ReplyHolder viewHolder, final long replyId) {
        viewHolder.hoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voteReply(viewHolder, replyId, false);
            }
        });
    }

    @Override
    public void updateEmptyViewHolder(EmptyViewHolder viewHolder) {
        viewHolder.title.setText(R.string.reply_feed_empty_title);
        viewHolder.message.setText(R.string.reply_feed_empty_message);
    }

    private void voteReply(final ReplyHolder viewHolder, final long replyId, final Boolean isLaud) {
        viewHolder.laud.setEnabled(false);
        viewHolder.hoot.setEnabled(false);
        VoteTO voteTO = new VoteTO(replyId, isLaud);
        activity.getLaudhootApiClient().votePost(voteTO,
                AuthorizationUtil.authorizationToken(activity.getClientDetailsRepository().findByClientId(activity.getClientId())),
                new BaseCallback<VoteTO>(activity.getApplicationContext()) {
                    @Override
                    protected void success(VoteTO voteTO, Response response, Context context) {
                        activity.replyRepository.vote(voteTO.getPostId(), voteTO.getIsLaud());
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

class ReplyHolder extends WebFeedAdapter.ViewHolder {
    TextView message;
    TextView laudhootDifference;
    Button laud;
    Button hoot;
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