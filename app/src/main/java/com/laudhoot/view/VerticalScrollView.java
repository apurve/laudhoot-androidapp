package com.laudhoot.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by root on 26/1/16.
 */
public class VerticalScrollView extends ScrollView {

    private float startY;

    public VerticalScrollView(Context context) {
        super(context);
    }

    public VerticalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        onTouchEvent(ev);
        if(MotionEvent.ACTION_DOWN == ev.getAction())
            startY = ev.getY();
        return (ev.getAction() == MotionEvent.ACTION_MOVE)
                && (Math.abs(startY - ev.getY()) > 50);
    }

}
