
package com.example.applicationmanager.ui;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.applicationmanager.R;
import com.example.applicationmanager.vo.NavDrawerItem;

public class ApplicationListActivity extends FragmentActivity implements ActionBar.TabListener {

    private String[] mAppSectionsTitle;

    private DrawerLayout mDrawerLayout;

    private ListView mDrawerList;

    private NavDrawerListAdapter mNavDrawerListAdapter;

    private ArrayList<NavDrawerItem> navDrawerItems;

    private TypedArray navMenuIcons;

    private ActionBarDrawerToggle mDrawerToggle;

    private ViewPager viewPager;

    private TabsPagerAdapter mAdapter;

    private ActionBar actionBar;

    private String[] tabNames = {
            "Installed", "Update"
    };

    private final static String TAG = ApplicationListActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAppSectionsTitle = getResources().getStringArray(R.array.nav_drawer_items);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.left_drawer);

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        // adding nav drawer items to array.

        navDrawerItems.add(new NavDrawerItem(mAppSectionsTitle[0], navMenuIcons
                .getResourceId(0, -1)));
        navDrawerItems.add(new NavDrawerItem(mAppSectionsTitle[1], navMenuIcons
                .getResourceId(1, -1)));
        navDrawerItems.add(new NavDrawerItem(mAppSectionsTitle[2], navMenuIcons
                .getResourceId(2, -1)));
        navDrawerItems.add(new NavDrawerItem(mAppSectionsTitle[3], navMenuIcons
                .getResourceId(3, -1)));
        navDrawerItems.add(new NavDrawerItem(mAppSectionsTitle[4], navMenuIcons
                .getResourceId(4, -1)));
        navDrawerItems.add(new NavDrawerItem(mAppSectionsTitle[5], navMenuIcons
                .getResourceId(5, -1)));

        // recycle the typed array

        navMenuIcons.recycle();

        mNavDrawerListAdapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(mNavDrawerListAdapter);

        // enabling the action bar app icon and behaving it as toggle button

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, // nav
                                                                                             // menu
                                                                                             // toggle
                                                                                             // icon
                R.string.app_name, // nav drawer open - description for
                                   // accessibility
                R.string.app_name // nav drawer close - description for
                                  // accessibility
        ) {
            @Override
            public void onDrawerClosed(View view) {
                // getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Initilization
        viewPager = (ViewPager)findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        /**
         * on swiping the viewpager make respective tab selected
         */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        // Adding Tabs
        for (String tab_name : tabNames) {
            actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
        }

    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private class NavDrawerListAdapter extends BaseAdapter {

        ArrayList<NavDrawerItem> navDrawerItems;

        Context context;

        public NavDrawerListAdapter(Context applicationContext,
                ArrayList<NavDrawerItem> navDrawerItems) {
            this.navDrawerItems = navDrawerItems;
            this.context = applicationContext;
        }

        @Override
        public int getCount() {
            return navDrawerItems.size();
        }

        @Override
        public Object getItem(int position) {
            return navDrawerItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater)context
                        .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.drawer_list_item, null);
            }

            ImageView imgIcon = (ImageView)convertView.findViewById(R.id.icon);
            TextView txtTitle = (TextView)convertView.findViewById(R.id.title);
            TextView txtCount = (TextView)convertView.findViewById(R.id.counter);

            imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
            txtTitle.setText(navDrawerItems.get(position).getTitle());

            // displaying count
            // check whether it set visible or not
            if (navDrawerItems.get(position).getCounterVisibility()) {
                txtCount.setText(navDrawerItems.get(position).getCount());
            } else {
                // hide the counter view
                txtCount.setVisibility(View.GONE);
            }

            return convertView;
        }

    }

    private class TabsPagerAdapter extends FragmentPagerAdapter {

        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            switch (arg0) {
                case 0:
                    return new InstalledAppListFragment();
                case 1:
                    return new UpdateAppListFragment();
                default:
                    break;
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabNames.length;
        }

    }

    private void selectItem(int position) {

        switch (position) {
            case 0:

                Intent intent = new Intent(getApplicationContext(), VideoListActivity.class);
                startActivity(intent);

                break;
            case 1:
                Intent intent2 = new Intent(getApplicationContext(), PicturesListActivity.class);
                startActivity(intent2);

                break;

            case 2:
                Intent intent3 = new Intent(getApplicationContext(), DocumentListActivity.class);
                startActivity(intent3);

                break;

            case 3:
                Intent intent4 = new Intent(getApplicationContext(), MusicListActivity.class);
                startActivity(intent4);

                break;

        }

    }

}
