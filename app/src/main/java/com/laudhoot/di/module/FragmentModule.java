package com.laudhoot.di.module;

import android.util.AndroidRuntimeException;
import android.util.Log;

import com.laudhoot.Laudhoot;
import com.laudhoot.R;

import dagger.Module;

/**
 * Created by apurve on 12/4/15.
 */
@Module(
        complete = false,
        library = true,
        injects = {
                Laudhoot.class,
        }
)
public class FragmentModule {

    private final Laudhoot application;

    /**
     * Stores the list of pages' full class names
     */
    private String[] pagesClasses;

    /**
     * Stores the list pages' tile
     */
    private String[] pageTitles;

    /**
     * Stores the list pages' icons
     */
    private String[] pageIcons;

    public FragmentModule(Laudhoot application) {
        this.application = application;
        pagesClasses = application.getResources().getStringArray(R.array.pages_classes);
        pageTitles = application.getResources().getStringArray(R.array.pages_titles);
        pageIcons = application.getResources().getStringArray(R.array.pages_icons);
        if (pageTitles.length < 1 || pagesClasses.length < 1) {
            Log.d(Laudhoot.LOG_TAG, "R.array.pages_classes and R.array.pages_titles should have at least one item each.");
            throw new AndroidRuntimeException("No page(s) found in configuration.");
        } else if (pagesClasses.length != pageTitles.length) {
            Log.d(Laudhoot.LOG_TAG, "R.array.pages_classes and R.array.pages_titles should be equal in length.");
            throw new AndroidRuntimeException("Pages of the activity are not configured properly.");
        } else if (pageIcons.length > 0 && pagesClasses.length != pageTitles.length) {
            Log.d(Laudhoot.LOG_TAG, "R.array.pages_classes and R.array.pages_icons should be equal in length.");
            throw new AndroidRuntimeException("Pages of the activity are not configured properly.");
        }
    }
}
