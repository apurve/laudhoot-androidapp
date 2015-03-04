package com.laudhoot.activity;

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

import com.laudhoot.fragment.PlaceholderFragment;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

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

    private final static String LOG_TAG = "LAUDHOOT-LOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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
                if(mSectionsPagerAdapter.getPageIcon(0) != null) {
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
            if(mSectionsPagerAdapter.getPageIcon(i) == null){
                actionBar.addTab(
                        actionBar.newTab()
                                .setText(mSectionsPagerAdapter.getPageTitle(i))
                                .setTabListener(this));
            }else{
                actionBar.addTab(
                        actionBar.newTab()
                                .setIcon(mSectionsPagerAdapter.getPageIcon(i))
                                .setTabListener(this));
            }
        }

        // If icons are available then update activity title with the title of first page
        if(mSectionsPagerAdapter.getPageIcon(0) != null){
            setTitle(mSectionsPagerAdapter.getPageTitle(0));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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
        private String [] pageTitles;

        /**
         * Stores the list pages' icons
         */
        private String [] pageIcons;

        private final String METHOD_NAME = "newInstance";

        public SectionsPagerAdapter(FragmentManager fm) throws AndroidRuntimeException {
            super(fm);
            pagesClasses = getResources().getStringArray(R.array.pages_classes);
            pageTitles = getResources().getStringArray(R.array.pages_titles);
            pageIcons = getResources().getStringArray(R.array.pages_icons);
            if(pageTitles.length < 1 || pagesClasses.length < 1){
                Log.d(LOG_TAG, "R.array.pages_classes and R.array.pages_titles should have at least one item each.");
                throw new AndroidRuntimeException("No page(s) found in configuration.");
            }else if(pagesClasses.length != pageTitles.length){
                Log.d(LOG_TAG,"R.array.pages_classes and R.array.pages_titles should be equal in length.");
                throw new AndroidRuntimeException("Pages of the activity are not configured properly.");
            }else if(pageIcons.length > 0 && pagesClasses.length != pageTitles.length){
                Log.d(LOG_TAG,"R.array.pages_classes and R.array.pages_icons should be equal in length.");
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
                Log.d(LOG_TAG, "The specified fragment have higher visibility.");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                Log.d(LOG_TAG, "Fully qualified class name of the specified fragment in string resources cannot be found.");
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                Log.d(LOG_TAG, "Fragments must have a static "+METHOD_NAME+" method.");
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                Log.d(LOG_TAG, "Not able to call the static "+METHOD_NAME+" method.");
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
            if(pageIcons.length > 0){
                try {
                    Field field = R.drawable.class.getDeclaredField(pageIcons[position]);
                    return (Integer) field.get(null);
                } catch (NoSuchFieldException e) {
                    Log.d(LOG_TAG, "Icon "+pageIcons[position]+" not found in drawable resources.");
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    Log.d(LOG_TAG, "Resources unavailable, try rebuilding the project.");
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

}
