package com.looksphere.goindia.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;

import com.looksphere.goindia.R;
import com.looksphere.goindia.application.SwachhApplication;
import com.looksphere.goindia.fragment.NavigationDrawerFragment;


public class LauncherActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle = "";
    private int fragmentCounter = 0;

    String[] menuItems;

    private DrawerLayout mDrawer;
    //  private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_launcher);
        //initUI();
    }


    private void initUI() {

//        mNavigationDrawerFragment = (NavigationDrawerFragment)
//                getFragmentManager().findFragmentById(R.id.navigation_drawer);
//        mTitle = getTitle();
//        // Set up the drawer.
//        mNavigationDrawerFragment.setUp(
//                R.id.navigation_drawer,
//                (DrawerLayout) findViewById(R.id.drawer_layout));
//
//
//        menuItems = getResources().getStringArray(R.array.menu_items);


    }


    @Override
    protected void onResume() {
        super.onResume();
        //when coming from other activities
//        if (fragmentCounter == 0) {
//
//            Fragment fragment = null;
//            fragment = HomeFragment.newInstance();
//            fragmentCounter = 0;
//            // update the main content by replacing fragments
//            FragmentManager fragmentManager = getFragmentManager();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.container, fragment)
//                    .commit();
//        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

//        Fragment fragment = null;
//        switch (position) {
////            case 0:
////                fragment = HomeFragment.newInstance();
////                fragmentCounter = 0;
////                break;
//            case 1:
//                fragment = SwachhMapFragment.newInstance();
//                fragmentCounter = 1;
//                break;
//            case 2:
//                fragment = VolunteeringFragment.newInstance();
//                fragmentCounter = 2;
//                break;
////            case 3:
////                fragment = CompletedFragment.newInstance();
////                fragmentCounter = 3;
////                break;
//            case 4:
//                fragment = AboutUsFragment.newInstance();
//                fragmentCounter = 4;
//                break;
//            case 5:
//                fragment = EmailFragment.newInstance();
//                fragmentCounter = 5;
//                break;
//            case 6:
//                fragment = ShareFragment.newInstance();
//                fragmentCounter = 6;
//                break;
//            case 7:
//                fragment = SettingsFragment.newInstance();
//                fragmentCounter = 7;
//                break;
//
//        }
//
//
//        // update the main content by replacing fragments
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.container, fragment)
//                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = menuItems[0];
                break;
            case 1:
                mTitle = menuItems[1];
                break;
            case 2:
                mTitle = menuItems[2];
                break;
            case 3:
                mTitle = menuItems[3];
                break;
            case 4:
                mTitle = menuItems[4];
                break;
            case 5:
                mTitle = menuItems[5];
                break;
            case 6:
                mTitle = menuItems[6];
                break;
            case 7:
                mTitle = menuItems[7];
                break;

        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        actionBar.setDisplayShowTitleEnabled(true);
        SpannableString s = new SpannableString(mTitle);
        s.setSpan(SwachhApplication.roboticThin, 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        actionBar.setTitle(s);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!mNavigationDrawerFragment.isDrawerOpen()) {
//            // Only show items in the action bar relevant to this screen
//            // if the drawer is not showing. Otherwise, let the drawer
//            // decide what to show in the action bar.
//            getMenuInflater().inflate(R.menu.launcher, menu);
//            restoreActionBar();
//            return true;
//        }
//        return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.launcher, menu);
        restoreActionBar();
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


//        switch(id)
//        {
//            case R.id.action_assigned:
//                mTitle = menuItems[2];
//                SpannableString s = new SpannableString(mTitle);
//                s.setSpan(SwachhApplication.roboticThin, 0, s.length(),
//                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                getActionBar().setTitle(s);
//
//                Fragment assignedFragment = null;
//                assignedFragment = VolunteeringFragment.newInstance();
//                fragmentCounter = 0;
//                // update the main content by replacing fragments
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, assignedFragment)
//                        .commit();
//                break;
//            case R.id.action_completed:
//                mTitle = menuItems[3];
//                SpannableString ss = new SpannableString(mTitle);
//                ss.setSpan(SwachhApplication.roboticThin, 0, ss.length(),
//                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                getActionBar().setTitle(ss);
//
//                Fragment completedFragment = null;
////               // completedFragment = CompletedFragment.newInstance();
////                fragmentCounter = 0;
////                // update the main content by replacing fragments
////                FragmentManager completedFragmentManager = getFragmentManager();
////                completedFragmentManager.beginTransaction()
////                        .replace(R.id.container, completedFragment)
////                        .commit();
//                break;
//        }

        return super.onOptionsItemSelected(item);
    }


}
