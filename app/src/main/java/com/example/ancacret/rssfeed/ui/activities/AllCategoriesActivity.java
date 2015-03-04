/*
package com.example.ancacret.rssfeed.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ancacret.rssfeed.R;
import com.example.ancacret.rssfeed.adapters.CategoriesAdapter;
import com.example.ancacret.rssfeed.async.GetStoredData;
import com.example.ancacret.rssfeed.async.RssService;
import com.example.ancacret.rssfeed.async.StoreToDbAsync;
import com.example.ancacret.rssfeed.pojo.CategoryItem;
import com.example.ancacret.rssfeed.pojo.CategoryListItem;
import com.example.ancacret.rssfeed.pojo.HeaderItem;
import com.example.ancacret.rssfeed.pojo.ListViewItem;
import com.example.ancacret.rssfeed.pojo.RSSItem;
import com.example.ancacret.rssfeed.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


*/
/* TODO list all feeds from each category *//*

public class AllCategoriesActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private CategoriesAdapter mAdapter;
    private String[] urls;
    private List<String> categoryStrings;
    private List<CategoryItem> mCategoryItems;
    List<Integer> colorList;
    private List<ListViewItem> mListViewItems;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feeds_activity);
        setupViews();
        setSupportActionBar(mToolbar);

        mListView.setOnItemClickListener(this);

        urls = new String[]{
                // Constants.URL_ALL_TOPICS,
               // Constants.OTHER_URL
                Constants.URL_ECONOMIC,
                Constants.URL_CULTURA,
                Constants.URL_POLITIC,
                Constants.URL_EXTERNE,
                Constants.URL_LIFE_INEDIT,
                Constants.URL_SOCIAL,
                Constants.URL_SPORT,
                Constants.URL_STIINTA_SANATATE
        };
        colorList = new ArrayList<Integer>() {
            {
                add(getApplicationContext().getResources().getColor(R.color.blue_500));
                add(getApplicationContext().getResources().getColor(R.color.pink_700));
                add(getApplicationContext().getResources().getColor(R.color.green_500));
            }
        };
        categoryStrings = new ArrayList<String>() {
            {
                add(Constants.TOATE);
                add(Constants.ECONOMIC);
                add(Constants.CULTURA);
                add(Constants.POLITIC);
                add(Constants.EXTERNE);
                add(Constants.LIFE_INEDIT);
                add(Constants.URL_SOCIAL);
                add(Constants.URL_SPORT);
                add(Constants.URL_STIINTA_SANATATE);
            }
        };

        mListViewItems = new ArrayList<ListViewItem>();
        mAdapter = new CategoriesAdapter(this, mListViewItems);
        mListView.setAdapter(mAdapter);
        fillItemList();

    }

    @Override
    public int getActivityLayout() {
        return R.layout.all_categories;

    }

    private void setupViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mListView = (ListView) findViewById(R.id.mainContentListView);
    }

    private void fillItemList() {
        */
/*  if there isn't an internet connection, fill data with data from DB
        *//*

        if (checkConnection()) {
            getStoredData();
        } else {
            */
/* get feeds from each category *//*

            for (String url : urls) {
                new RssService(url) {
                    @Override
                    protected void onPostExecute(List<RSSItem> rssItems) {
                        super.onPostExecute(rssItems);
                        */
/* put feeds below specific category *//*

                        if ((rssItems != null) && (rssItems.size() > 0)) {
                            String category = rssItems.get(0).getCategory();
                            mListViewItems.add(new HeaderItem(category));
                            for (RSSItem rssItem : rssItems) {
                                mListViewItems.add(new CategoryListItem(rssItem, getApplicationContext()));
                            }
                        }
                        mAdapter.notifyDataSetChanged();

                    }
                }.execute();
            }


         */
/* async task to take the first item from each feed category
        * the result will contain a list of RessItems*//*

           */
/* new AllCategoriesRssService(categoryStrings, urls) {
                @Override
                protected void onPostExecute(List<CategoryItem> categoryItems) {
                    super.onPostExecute(categoryItems);
                    Random random = new Random();
                    for (CategoryItem item : categoryItems) {
                        item.setColor(colorList.get(random.nextInt(colorList.size())));
                    }
                    mAdapter.updateItemList(categoryItems);
                *//*
*/
/* TODO store categoryItems to db on another thread*//*
*/
/*
                    storeResultsToDB(categoryItems, getApplicationContext());
                }
            }.execute();
        }*//*


        }

    }

    private boolean checkConnection() {
        return false;
    }

    private void storeResultsToDB(List<CategoryItem> categoryItems, Context applicationContext) {
        new StoreToDbAsync(categoryItems, getApplicationContext()) {
            @Override
            protected void onPostExecute(List<CategoryItem> categoryItems) {
                super.onPostExecute(categoryItems);
                if (categoryItems != null) {
                    for (CategoryItem item : categoryItems) {
                        Log.v(Constants.TAG_LOG, "FROM DB..." + item.getName());
                    }
                }
            }
        }.execute();
    }

    */
/*  *//*

    private void getStoredData() {
        mCategoryItems = new ArrayList<CategoryItem>();
        new GetStoredData(getApplicationContext()) {
            @Override
            protected void onPostExecute(List<CategoryItem> categoryItems) {
                super.onPostExecute(categoryItems);
                mCategoryItems = categoryItems;
                Random random = new Random();
                if (mCategoryItems != null) {
                    for (CategoryItem item : categoryItems) {
                        item.setColor(colorList.get(random.nextInt(colorList.size())));
                    }
                    //  mAdapter.updateItemList(mCategoryItems);
                }
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.all_categories, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    */
/*@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }*//*



    */
/* on click send the feed items from the category *//*

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(AllCategoriesActivity.this, DetailsPager.class);
        */
/* send url to get description of the feed and a unique id from the clicked item *//*

        String clickedUrl, categoryLink;
        if (mAdapter.getItemViewType(position) == CategoriesAdapter.RowType.LIST_ITEM.ordinal()) {
            CategoryListItem listItem = (CategoryListItem) mAdapter.getItem(position);
            */
/* send all feeds and the url from the clicked item
            *  we need the url or id to set the current page of the pager*//*

            clickedUrl = listItem.getRSSItem().getLink();
            intent.putExtra(Constants.CLICKED_URL_ITEM, clickedUrl);
           */
/* for (String link : urls) {
                if (link.contains(listItem.getRSSItem().getCategory())) {
                    *//*
*/
/* send link through intent *//*
*/
/*
                    categoryLink = link;
                    intent.putExtra(Constants.CATEGORY_URL_KEY, categoryLink);
                    break;
                }
            }*//*


            */
/* pass to pager only the feed items from this category *//*

            List<CategoryListItem> clickedCategoryItems = new ArrayList<CategoryListItem>();
            */
/* not really nice - go through whole list to search the items from this category *//*

            for (ListViewItem item : mListViewItems) {
                if (item.getViewType() == CategoriesAdapter.RowType.LIST_ITEM.ordinal()) {
                    if (((CategoryListItem) item).getRSSItem().getCategory().equals(listItem.getRSSItem().getCategory())) {
                        clickedCategoryItems.add((CategoryListItem) item);
                    }
                }
            }
            if(clickedCategoryItems != null){
                intent.putParcelableArrayListExtra(Constants.FEED_ITEMS_INTENT_KEY, (ArrayList<? extends android.os.Parcelable>) clickedCategoryItems);
            }
            startActivity(intent);


            */
/* on first click pass the whole list of feeds *//*

            */
/*List<CategoryListItem> categoryListItems = new ArrayList<CategoryListItem>();
            for (ListViewItem item : mListViewItems) {
                if (item.getViewType() == CategoriesAdapter.RowType.LIST_ITEM.ordinal()) {
                    categoryListItems.add((CategoryListItem) item);
                }
            }
            if (categoryListItems != null) {
                intent.putParcelableArrayListExtra(Constants.FEED_ITEMS_INTENT_KEY, (ArrayList<? extends android.os.Parcelable>) categoryListItems);
            }*//*



        }


    }




}
*/
