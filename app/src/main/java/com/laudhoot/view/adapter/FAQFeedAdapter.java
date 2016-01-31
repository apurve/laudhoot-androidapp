package com.laudhoot.view.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.laudhoot.Laudhoot;
import com.laudhoot.R;
import com.laudhoot.persistence.model.view.Shout;
import com.laudhoot.web.model.FAQTO;

import java.util.List;

/**
 * Created by root on 31/1/16.
 */
public class FAQFeedAdapter extends WebFeedAdapter<FAQTO, FAQHolder> {

    public FAQFeedAdapter(Activity activity, String clientId, List<FAQTO> faqs) {
        super(activity.getApplicationContext(), faqs, R.layout.faq_feed_item);
    }

    @Override
    public FAQHolder createViewHolder(View convertView, FAQTO item) {
        final FAQHolder viewHolder = new FAQHolder();
        viewHolder.question = (TextView) convertView.findViewById(R.id.faq_question);
        viewHolder.answer = (TextView) convertView.findViewById(R.id.faq_answer);
        return viewHolder;
    }

    @Override
    public void updateViewHolder(FAQHolder viewHolder, FAQTO item) {
        viewHolder.question.setText(item.getQuestion());
        viewHolder.answer.setText(item.getAnswer());
    }

    @Override
    public void updateEmptyViewHolder(EmptyViewHolder viewHolder) {
        viewHolder.title.setText(R.string.shout_feed_empty_title);
        viewHolder.message.setText(R.string.shout_feed_empty_message);
    }

}


class FAQHolder extends WebFeedAdapter.ViewHolder {
    TextView question;
    TextView answer;
}