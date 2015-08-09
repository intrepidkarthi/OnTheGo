/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.looksphere.goindia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.SupportMapFragment;
import com.looksphere.goindia.R;
import com.looksphere.goindia.customview.RoundedImageView;
import com.looksphere.goindia.fragment.AboutUsFragment;
import com.looksphere.goindia.fragment.CompletedFragment;
import com.looksphere.goindia.fragment.EmailFragment;
import com.looksphere.goindia.fragment.HomeFragment;
import com.looksphere.goindia.fragment.SettingsFragment;
import com.looksphere.goindia.fragment.ShareFragment;
import com.looksphere.goindia.fragment.VolunteeringFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final int DEFAULT_PAGES = 2;
    private DrawerLayout mDrawerLayout;
    int fragmentCounter = 0;
    RoundedImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        profileImage = (RoundedImageView) findViewById(R.id.profile_image);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }


        if (fragmentCounter == 0) {

            Fragment fragment = null;
            fragment = HomeFragment.newInstance();
            fragmentCounter = 0;
            // update the main content by replacing fragments
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                Intent i = new Intent(getApplicationContext(), SwachhPostActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.profile_image:
                Intent parallaxViewIntent = new Intent(this, ProfileActivity.class);
                startActivity(parallaxViewIntent);
                return true;

            case R.id.action_assigned:
                //mTitle = menuItems[2];
//                SpannableString s = new SpannableString(mTitle);
//                s.setSpan(SwachhApplication.roboticThin, 0, s.length(),
//                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                getActionBar().setTitle(s);

                Fragment assignedFragment = null;
                assignedFragment = VolunteeringFragment.newInstance();
                fragmentCounter = 0;
                // update the main content by replacing fragments
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, assignedFragment)
                        .commit();
                break;
            case R.id.action_completed:
                //mTitle = menuItems[3];
//                SpannableString ss = new SpannableString(mTitle);
//                ss.setSpan(SwachhApplication.roboticThin, 0, ss.length(),
//                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                getActionBar().setTitle(ss);

                try {

                    Fragment completedFragment = null;
                    // completedFragment = CompletedFragment.newInstance();
                    fragmentCounter = 0;
                    // update the main content by replacing fragments
                    FragmentManager completedFragmentManager = getSupportFragmentManager();
                    completedFragmentManager.beginTransaction()
                            .replace(R.id.container, completedFragment)
                            .commit();
                }
                catch(Exception e){}
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //when coming from other activities
        if (fragmentCounter == 0) {

            Fragment fragment = null;
            fragment = HomeFragment.newInstance();
            fragmentCounter = 0;
            // update the main content by replacing fragments
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "Home");
        adapter.addFragment(new SupportMapFragment(), "Maps");
        adapter.addFragment(new CompletedFragment(), "Done");
        //adapter.addFragment(new CheeseListFragment(), "Tab 4");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(DEFAULT_PAGES);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();



                            Fragment fragment = null;
                            switch (menuItem.getItemId()) {
                                case R.id.nav_home:
                                    fragment = HomeFragment.newInstance();
                                    fragmentCounter = 0;
                                    break;
//                                case R.id.nav_maps:
//                                    fragment = SwachhMapFragment.newInstance();
//                                    fragmentCounter = 1;
//                                    break;
                                case R.id.nav_volunteering:
                                    fragment = VolunteeringFragment.newInstance();
                                    fragmentCounter = 2;
                                    break;
                                case R.id.nav_completed:
                                    fragment = CompletedFragment.newInstance();
                                    fragmentCounter = 3;
                                    break;
                                case R.id.nav_aboutus:
                                    fragment = AboutUsFragment.newInstance();
                                    fragmentCounter = 4;
                                    break;
                                case R.id.nav_email:
                                    fragment = EmailFragment.newInstance();
                                    fragmentCounter = 5;
                                    break;
                                case R.id.nav_share:
                                    fragment = ShareFragment.newInstance();
                                    fragmentCounter = 6;
                                    break;
                                case R.id.nav_settings:
                                    fragment = SettingsFragment.newInstance();
                                    fragmentCounter = 7;
                                    break;

                            }


                            // update the main content by replacing fragments
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.container, fragment)
                                    .commit();






                        return true;
                    }
                });
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
