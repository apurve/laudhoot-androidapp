package com.laudhoot.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Collection;
import java.util.List;

/**
 *
 *
 * Created by root on 30/9/15.
 */
public class EndlessListView<T> extends ListView implements AbsListView.OnScrollListener {

    private View footer;
    private EndlessListener listener;
    private EndlessAdapter<T> adapter;
    private boolean isLoading;

    public EndlessListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setOnScrollListener(this);
    }

    public EndlessListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnScrollListener(this);
    }

    public EndlessListView(Context context) {
        super(context);
        this.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (getAdapter() != null && getAdapter().getCount() > 1 && !isLoading
                && visibleItemCount + firstVisibleItem >= totalItemCount) {
            addFooterView(footer);
            isLoading = true;
            listener.loadData();
        }
    }

    public void setListener(EndlessListener listener) {
        this.listener = listener;
    }

    public void setLoadingView(int resId) {
        LayoutInflater inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footer = inflater.inflate(resId, null);
        this.addFooterView(footer);

    }

    public void setAdapter(EndlessAdapter<T> adapter) {
        super.setAdapter(adapter);
        this.adapter = adapter;
        this.removeFooterView(footer);
    }

    public void addNewData(List<T> data) {
        this.removeFooterView(footer);
        adapter.addAll(data);
        adapter.notifyDataSetChanged();
        isLoading = false;
    }

    public static interface EndlessListener {
        public void loadData();
    }

    public static interface EndlessAdapter<T> extends ListAdapter {
        public void notifyDataSetChanged();
        public void addAll(Collection<? extends T> collection);
    }

}