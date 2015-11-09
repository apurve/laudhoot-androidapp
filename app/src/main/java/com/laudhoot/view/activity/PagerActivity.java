package com.laudhoot.view.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.laudhoot.Laudhoot;
import com.laudhoot.R;
import com.laudhoot.util.Toaster;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Self rendering page view activity with action bar. Fragments populating the pages are mapped in an XML file and
 * the activity renders the view based on meta data provided in the XML mapping.
 *
 * TODO - self rendering code uses reflection which should be implemented using DI.
 * Created by apurve on 1/3/15.
 */

public abstract class PagerActivity extends ActionBarActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Inject
    Toaster toaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.laudhoot_theme_color)));
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.laudhoot_theme_color_faded)));
        actionBar.setIcon(R.drawable.ic_launcher);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
                if (mSectionsPagerAdapter.getPageIcon(0) != null) {
                    setTitle(mSectionsPagerAdapter.getPageTitle(position));
                }
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            if (mSectionsPagerAdapter.getPageIcon(i) == null) {
                actionBar.addTab(
                        actionBar.newTab()
                                .setText(mSectionsPagerAdapter.getPageTitle(i))
                                .setTabListener(this));
            } else {
                actionBar.addTab(
                        actionBar.newTab()
                                .setIcon(mSectionsPagerAdapter.getPageIcon(i))
                                .setTabListener(this));
            }
        }

        // If icons are available then update activity title with the title of first page
        if (mSectionsPagerAdapter.getPageIcon(0) != null) {
            setTitle(mSectionsPagerAdapter.getPageTitle(0));
        }
    }


    @Override
    public abstract boolean onCreateOptionsMenu(Menu menu);

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    };

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

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

        private final String METHOD_NAME = "newInstance";

        public SectionsPagerAdapter(FragmentManager fm) throws AndroidRuntimeException {
            super(fm);
            pagesClasses = getResources().getStringArray(R.array.pages_classes);
            pageTitles = getResources().getStringArray(R.array.pages_titles);
            pageIcons = getResources().getStringArray(R.array.pages_icons);
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

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            try {
                Class<?> fragmentClass = Class.forName(pagesClasses[position]);
                Method method = fragmentClass.getMethod("newInstance", Integer.class);
                return (Fragment) method.invoke(null, position);
            } catch (IllegalAccessException e) {
                Log.d(Laudhoot.LOG_TAG, "The specified fragment have higher visibility.");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                Log.d(Laudhoot.LOG_TAG, "Fully qualified class name of the specified fragment in string resources cannot be found.");
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                Log.d(Laudhoot.LOG_TAG, "Fragments must have a static " + METHOD_NAME + " method.");
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                Log.d(Laudhoot.LOG_TAG, "Not able to call the static " + METHOD_NAME + " method.");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public int getCount() {
            return pagesClasses.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            return pageTitles[position];
        }

        public Integer getPageIcon(int position) {
            if (pageIcons.length > 0) {
                try {
                    Field field = R.drawable.class.getDeclaredField(pageIcons[position]);
                    return (Integer) field.get(null);
                } catch (NoSuchFieldException e) {
                    Log.d(Laudhoot.LOG_TAG, "Icon " + pageIcons[position] + " not found in drawable resources.");
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    Log.d(Laudhoot.LOG_TAG, "Resources unavailable, try rebuilding the project.");
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    public Toaster getToaster() {
        return toaster;
    }

}
