/*
package com.example.ancacret.rssfeed.ui.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ancacret.rssfeed.R;
import com.example.ancacret.rssfeed.adapters.DrawerAdapter;
import com.example.ancacret.rssfeed.ui.fragments.CategoryFragForDrawer;
import com.example.ancacret.rssfeed.utils.Constants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BaseActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerListView;
    private Toolbar mToolbar;
    private List<String> mDrawerValuesList;
    private Map<String, String> mCategoryMap;
    private DrawerAdapter mDrawerAdapter;
    private int mIcons[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayout());
        setupViews();
        createDrawerToogle();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        LayoutInflater inflater = getLayoutInflater();
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.header, mDrawerListView, false);
        mDrawerListView.addHeaderView(viewGroup);
        mDrawerListView.setOnItemClickListener(new DrawerItemClickListener());
        */
/* using LinkedHashMap to always return keys in same order *//*

        mCategoryMap = new LinkedHashMap<String, String>();
        mCategoryMap.put(Constants.URL_ECONOMIC, Constants.ECONOMIC);
        mCategoryMap.put(Constants.URL_CULTURA, Constants.CULTURA);
        mCategoryMap.put(Constants.URL_EXTERNE, Constants.EXTERNE);
        mCategoryMap.put(Constants.URL_POLITIC, Constants.POLITIC);
        mCategoryMap.put(Constants.URL_SOCIAL, Constants.SOCIAL);
        mCategoryMap.put(Constants.URL_STIINTA_SANATATE, Constants.STIINTA_SANATATE);
        mCategoryMap.put(Constants.URL_LIFE_INEDIT, Constants.LIFE_INEDIT);
        mDrawerValuesList = new ArrayList<String>();
        for (String key : mCategoryMap.keySet()) {
            mDrawerValuesList.add(mCategoryMap.get(key));
        }
        // mDrawerListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categoriesList));
        //mDrawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mDrawerValuesList));
        mIcons = new int[]{R.drawable.cat, R.drawable.ic_rabbit, R.drawable.fish, R.drawable.sheep};
        mDrawerAdapter = new DrawerAdapter(this, mIcons);
        mDrawerListView.setAdapter(mDrawerAdapter);
        if (mDrawerValuesList != null) {
            mDrawerAdapter.updateList(mDrawerValuesList);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public int getActivityLayout() {
        return R.layout.activity_base;
    }

    private void setupViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerListView = (ListView) findViewById(R.id.left_drawer_list);
    }

    private void createDrawerToogle() {

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.opened_drawer, R.string.closed_drawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(getString(R.string.app_name));
                */
/* rebuild menu to reflect any changes *//*

                invalidateOptionsMenu(); // calls onPrepareOptionsMenu
                syncState();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(getString(R.string.drawer_categories));
                invalidateOptionsMenu();
                syncState();
            }
        };

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerListView);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      //boolean indicator = mDrawerToggle.isDrawerIndicatorEnabled();
        if (!mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

*/
/*
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(mDrawerListView)) {
                    mDrawerLayout.closeDrawer(mDrawerListView);
                } else {
                    mDrawerLayout.openDrawer(mDrawerListView);
                }
                return true;
        }*//*

       return super.onOptionsItemSelected(item);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

        */
/* create new fragment and make request for the specified URL *//*

        private void selectItem(int position) {
            String url = "";
            // List<String> keys = new ArrayList(mCategoryMap.keySet());
            for (String key : mCategoryMap.keySet()) {
                if (mCategoryMap.get(key).equalsIgnoreCase(mDrawerAdapter.getItem(position-1))) {
                    url = key;
                    break;
                }
            }
            Fragment frag = CategoryFragForDrawer.newInstance(url);
            FragmentManager fm = getSupportFragmentManager();
            fm.popBackStack();
            fm.beginTransaction().replace(R.id.content_frame, frag).commit();

            mDrawerListView.setItemChecked(position, true);
            setTitle("Titttle");
            mDrawerLayout.closeDrawer(mDrawerListView);

        }
    }


}
*/
