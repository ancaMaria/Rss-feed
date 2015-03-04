package com.example.ancacret.rssfeed.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ancacret.rssfeed.R;
import com.example.ancacret.rssfeed.adapters.MyPagerAdapter;
import com.example.ancacret.rssfeed.async.AsyncGetBitmap;
import com.example.ancacret.rssfeed.pojo.CategoryListItem;
import com.example.ancacret.rssfeed.pojo.HeaderItem;
import com.example.ancacret.rssfeed.pojo.RSSItem;
import com.example.ancacret.rssfeed.ui.fragments.CategoryFragForPager;
import com.example.ancacret.rssfeed.utils.MfConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/* get all items from clicked item's category */
public class DetailsPager extends ActionBarActivity {

    private ViewPager mViewPager;
    private MyPagerAdapter mPagerAdapter;
    private List<CategoryListItem> mCategoryListItems;
    private List<RSSItem> mRSSItems;
    private List<Fragment> mFragmentList;
    private List<Fragment> mFragList2;
    private String mClickedFeedUrl;
    private String mClickedFeedFromDrawerList;
    private Toolbar mToolbar;
    private HeaderItem mHeaderItem;

    private Bitmap mBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbarWithShare);
        setSupportActionBar(mToolbar);
        mViewPager = (ViewPager) findViewById(R.id.pager);
       // final FragmentManager fm = getSupportFragmentManager();
        final FragmentManager fm = getSupportFragmentManager();
        mPagerAdapter = new MyPagerAdapter(fm);
        mViewPager.setAdapter(mPagerAdapter);
        mHeaderItem = getIntent().getParcelableExtra(MfConstants.HEADER_ITEM_INTENT_KEY);

        /* verify from where the click event took place */
        if( (getIntent().getStringExtra(MfConstants.CLICKED_URL_ITEM) != null)  &&
                (getIntent().getParcelableArrayListExtra(MfConstants.FEED_ITEMS_INTENT_KEY) != null) ){
            receiveCategoryFeeds();
        }
        if (getIntent().getStringExtra(MfConstants.CLICKED_ITEM_FROM_DRAWER_LISTING) != null &&
                (getIntent().getParcelableArrayListExtra(MfConstants.RSSITEMS_INTENT_KEY) != null) ) {
            receiveRssItems();
        }

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_share) {
                    int fragmentNb = mViewPager.getCurrentItem();
                    Fragment frag = new CategoryFragForPager();
                    if(mFragmentList != null){
                        frag = mFragmentList.get(fragmentNb);
                    } else if (mFragList2 != null){
                        frag = mFragList2.get(fragmentNb);
                    }
                    RSSItem rssItem = frag.getArguments().getParcelable(MfConstants.FRAG_ARG);
                    if (rssItem != null) {
                        shareUrlAndText(rssItem);
                        // shareImage(rssItem);
                    }
                    return true;
                }
                return false;
            }
        });

    }

    /* get the clicked feed by it's link and set the current item for the pager*/
    private void receiveCategoryFeeds() {
         /* clickedFeedUrl should be replaced with guid */
        mCategoryListItems = new ArrayList<CategoryListItem>();
        mCategoryListItems = getIntent().getParcelableArrayListExtra(MfConstants.FEED_ITEMS_INTENT_KEY);
        mClickedFeedUrl = getIntent().getStringExtra(MfConstants.CLICKED_URL_ITEM);
        if(mCategoryListItems != null){
            mFragmentList = new ArrayList<Fragment>();
            mFragmentList = getFragments();
            setCurrentPage();
        }

    }

    private void receiveRssItems() {
        mRSSItems = new ArrayList<RSSItem>();
        mRSSItems = getIntent().getParcelableArrayListExtra(MfConstants.RSSITEMS_INTENT_KEY);
        mClickedFeedFromDrawerList = getIntent().getStringExtra(MfConstants.CLICKED_ITEM_FROM_DRAWER_LISTING);
        if (mRSSItems != null) {
            mFragList2 = new ArrayList<Fragment>();
            mFragList2 = getFragmentsFromDrawer();
            setCurrPageForFragmentsFromDrawerRequest();
        }
    }

    private void shareImage(RSSItem rssItem) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, rssItem.getTitle() + "\n\n" + rssItem.getLink());
        Bitmap bitmap = getBitmapFromUrl(rssItem.getEnclosureUrl());
        Uri imageUri;
        try {
            imageUri = getBitmapUri(bitmap);
            sharingIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            startActivity(Intent.createChooser(sharingIntent, MfConstants.COMPLETE_ACTION_USING));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void shareUrlAndText(RSSItem rssItem) {
        try {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, rssItem.getTitle() + "\n\n" + rssItem.getLink());
            //sharingIntent.putExtra(Intent.EXTRA_TEXT, rssItem.getLink()); --> only this works for facebook
            startActivity(Intent.createChooser(sharingIntent, MfConstants.COMPLETE_ACTION_USING));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setCurrentPage() {
        for (CategoryListItem listItem : mCategoryListItems) {
            if (listItem.getRSSItem().getLink().equals(mClickedFeedUrl)) {
                int position = mCategoryListItems.indexOf(listItem);
                mPagerAdapter.setFragments(mFragmentList);
                mPagerAdapter.notifyDataSetChanged();
                mViewPager.setCurrentItem(position);
            }
        }
    }

    private void setCurrPageForFragmentsFromDrawerRequest() {
        for (RSSItem listItem : mRSSItems) {
            if (listItem.getLink().equals(mClickedFeedFromDrawerList)) {
                int position = mRSSItems.indexOf(listItem);
                mPagerAdapter.setFragments(mFragList2);
                mPagerAdapter.notifyDataSetChanged();
                mViewPager.setCurrentItem(position);
            }
        }
    }

    /* make a list of fragments, each with a RssItem as argument */
    public List<Fragment> getFragments() {
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        for (CategoryListItem item : mCategoryListItems) {
            if(mHeaderItem != null){
                fragmentList.add(CategoryFragForPager.newInstance(item.getRSSItem(), mHeaderItem.getColor()));
            } else {
                fragmentList.add(CategoryFragForPager.newInstance(item.getRSSItem(), getResources().getColor(R.color.lime_500)));
            }
        }
        return fragmentList;
    }

    public List<Fragment> getFragmentsFromDrawer() {
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        for (RSSItem item : mRSSItems) {
            if(mHeaderItem != null){
                fragmentList.add(CategoryFragForPager.newInstance(item, mHeaderItem.getColor()));
            } else {
                fragmentList.add(CategoryFragForPager.newInstance(item, getResources().getColor(R.color.lime_500)));
            }
        }
        return fragmentList;
    }


    /* returns Uri path to the bitmap */
    private Uri getBitmapUri(Bitmap bm) throws FileNotFoundException {
        Uri bmpUri;
        /* store image to default external directory */
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "share_image" + System.currentTimeMillis() + ".png");
        file.getParentFile().mkdirs();
        FileOutputStream out = new FileOutputStream(file);
        bm.compress(Bitmap.CompressFormat.PNG, 90, out);
        try {
            out.close();
            bmpUri = Uri.fromFile(file);
            return bmpUri;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* download image from URL, decode and return a bitmap */
    private Bitmap getBitmapFromUrl(String picUrl) {
        new AsyncGetBitmap(picUrl) {
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                mBitmap = bitmap;
            }
        }.execute();

        return mBitmap;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail_frag_menu, menu);
        return true;
    }

    /* TODO override toolbar's back icon */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();

       /* List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        FragmentManager fm = getSupportFragmentManager();
        FragmentManager childFm;
        for(Fragment fragment: fragmentList){
            if(fragment.getArguments().getString(MfConstants.FRAG_TAG1).equals("tag1")){
                CategoryFragForPager frag = (CategoryFragForPager) fragment;
                childFm = frag.getChildFragmentManager();
                if(childFm.getBackStackEntryCount() == 0){
                    super.onBackPressed();
                } else {
                    childFm.popBackStack();
                }
            }

        }*/

    }



}
