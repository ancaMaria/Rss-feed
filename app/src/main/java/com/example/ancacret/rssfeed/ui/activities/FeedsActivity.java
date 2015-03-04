package com.example.ancacret.rssfeed.ui.activities;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ancacret.rssfeed.R;
import com.example.ancacret.rssfeed.adapters.CategoriesAdapter;
import com.example.ancacret.rssfeed.adapters.DrawerCategoriesAdapter;
import com.example.ancacret.rssfeed.adapters.DrawerProvidersAdapter;
import com.example.ancacret.rssfeed.adapters.RVCategoriesAdapter;
import com.example.ancacret.rssfeed.adapters.RVProvidersAdapter;
import com.example.ancacret.rssfeed.application.SavedProviderApplication;
import com.example.ancacret.rssfeed.async.DeleteRowsAsync;
import com.example.ancacret.rssfeed.async.GetProvidersFeedsAsync;
import com.example.ancacret.rssfeed.async.GetStoredCategoriesAsync;
import com.example.ancacret.rssfeed.async.GetStoredFeeds;
import com.example.ancacret.rssfeed.async.GetStoredProviders;
import com.example.ancacret.rssfeed.async.RssService;
import com.example.ancacret.rssfeed.async.StoreCategoriesToDbAsync;
import com.example.ancacret.rssfeed.async.StoreFeedsAsync;
import com.example.ancacret.rssfeed.async.StoreProvidersAsync;
import com.example.ancacret.rssfeed.communication.EventNoStoredData;
import com.example.ancacret.rssfeed.communication.StoredCategoriesEvent;
import com.example.ancacret.rssfeed.interfaces.DrawerListItem;
import com.example.ancacret.rssfeed.interfaces.IOnTaskCompleted;
import com.example.ancacret.rssfeed.pojo.CategoryItem;
import com.example.ancacret.rssfeed.pojo.CategoryListItem;
import com.example.ancacret.rssfeed.pojo.DrawerCategoryListItem;
import com.example.ancacret.rssfeed.pojo.DrawerSectionHeader;
import com.example.ancacret.rssfeed.pojo.HeaderItem;
import com.example.ancacret.rssfeed.pojo.ListViewItem;
import com.example.ancacret.rssfeed.pojo.NewsProvider;
import com.example.ancacret.rssfeed.pojo.RSSItem;
import com.example.ancacret.rssfeed.ui.SwipeRefreshLayoutWithLV;
import com.example.ancacret.rssfeed.ui.fragments.AllFeedsFragment;
import com.example.ancacret.rssfeed.ui.fragments.CategoryFragForDrawer;
import com.example.ancacret.rssfeed.utils.MfConstants;
import com.example.ancacret.rssfeed.utils.ProviderInit;
import com.example.ancacret.rssfeed.utils.TvrProviderConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;

public class FeedsActivity extends ActionBarActivity implements IOnTaskCompleted, AbsListView.OnScrollListener, SwipeRefreshLayoutWithLV.OnRefreshListener {

    private ListView mMainListView;
    private CategoriesAdapter mMainAdapter;
    private Map<String, HeaderItem> mMediafaxMap;
    private Map<String, HeaderItem> mTvrMap;
    private Map<String, HeaderItem> mStiri100Map;
    private List<CategoryItem> mCategoryItems;
    private List<CategoryItem> mCategoryItemsToBeSaved;
    List<Integer> colorList;
    private List<ListViewItem> mMainContentLVItems;
    private List<ListViewItem> mMediafaxLVItems;
    private List<ListViewItem> mTvrLVItems;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerLinear;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerListView;
    private ListView mDrawerListViewProviders;
    private RecyclerView mDrawerRecyclerView;
    private RVProvidersAdapter mRVProvidersAdapter;
    private RVCategoriesAdapter mRVCategoriesAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mProgressBar;
    private Spinner mProvidersSpinner;
    private ImageView mDownArrow;
    private DrawerProvidersAdapter mProvidersAdapter;
    private List<DrawerSectionHeader> mProvidersList;
    private List<DrawerCategoryListItem> mCategoriesList;
    private DrawerCategoriesAdapter mDrawerCategoriesAdapter;
    private List<DrawerCategoryListItem> mDrawerCategoryListItems;
    private List<DrawerListItem> mDrawerListItems;

    private List<RSSItem> mReturnedItemsFromDrawer;
    private List<NewsProvider> mNewsProviders;
    private NewsProvider mCurrentProvider;

    private EventBus mBus = EventBus.getDefault();
    private Map<RssService, String> mRequestQueue;
    private List<RssService> mResolvedRequests;
    private TextView mDrawerHeaderText;

    private List<RSSItem> mReturnedFeeds;
    private List<RSSItem> mAllReturnedFeeds;
    private String mCurrentCategoryLink;
    private List<RSSItem> mAllFeedsFromDB;
    private int mCurrentProviderId;
    private List<CategoryItem> mAllProvidersCategoryItems;
    private List<RSSItem> mSortedFeeds;
    private SwipeRefreshLayoutWithLV mSwipeContainer;

    private List<HeaderItem> mHeaderItems;

    private Handler mHandler = new Handler();
    private boolean mIsRefreshing = false;
    private final Runnable refreshing = new Runnable() {
        @Override
        public void run() {
            try {
                if (mIsRefreshing) {
                    mSwipeContainer.setRefreshing(true);
                    mHandler.postDelayed(this, 2000);
                } else {
                    /* stop animation when data is loaded
                    * update list with new data*/
                    mSwipeContainer.setRefreshing(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feeds_activity);
        setupViews();
        setSupportActionBar(mToolbar);
        createDrawerToogle();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mMainListView.setVisibility(View.GONE);
        mMainAdapter = new CategoriesAdapter(getApplicationContext(), Collections.<ListViewItem>emptyList());
        mMainListView.setAdapter(mMainAdapter);
        //mMainListView.setOnScrollListener(this);
        initLists();
        initialiazeMaps();
        defineProviders();
        mCurrentProvider = getProviderFromPrefs();
        /*instantiate fragments (for main content and for drawer requests)*/
        instantiate(mCurrentProvider, mMainContentLVItems);
        //instantiateOneCategoryFrag();
        mCategoriesList = new ArrayList<DrawerCategoryListItem>();
        prepareDrawerProvidersList();
        setDrawerCategoriesList();
        if (mCurrentProvider != null) {
            setDrawerCategoriesAdapter();
        } else {
            setDrawerProvidersAdapter();
        }
        initSwipeOptions();

    }

    public void initSwipeOptions() {
        mSwipeContainer = (SwipeRefreshLayoutWithLV) findViewById(R.id.swipe_refresh_layout);
        setAppearance();
        mSwipeContainer.setListView(mMainListView);
        mSwipeContainer.setOnRefreshListener(this);
        // mSwipeContainer.post(refreshing);
    }

    @Override
    public void onRefresh() {
        Log.v(MfConstants.TAG_LOG, "swipe to refresh gesture identified");
        /* show the refresh animation
         * request new data; stop animation on post execute
         */
        mSwipeContainer.setRefreshing(true);
        new RssService(MfConstants.URL_SPORT) {
            @Override
            protected void onPreExecute() {
                mIsRefreshing = true;
            }

            @Override
            protected void onPostExecute(List<RSSItem> rssItems) {
                Log.v(MfConstants.TAG_LOG, "request finished");
                mIsRefreshing = false;
                mSwipeContainer.setRefreshing(false);
                mMainContentLVItems.clear();
                HeaderItem header = new HeaderItem(rssItems.get(0).getCategory(), getResources().getColor(R.color.brown_300));
                mMainContentLVItems.add(header);
                for (RSSItem rssItem : rssItems) {
                    CategoryListItem item = new CategoryListItem(rssItem, getApplicationContext());
                    mMainContentLVItems.add(item);
                }
                updateMainContentList(mMainContentLVItems, getCategoryHeadersList(mCurrentProvider));
            }
        };
    }

    public void setAppearance() {
        mSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        /*  SwipeRefreshLayout indicator does not appear when the setRefreshing(true) is called before the SwipeRefreshLayout.onMeasure()...
         *  */
        TypedValue outValue = new TypedValue();
        /* returns true if attribute was found */
        getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, outValue, true);
        mSwipeContainer.setProgressViewOffset(false, 0, getResources().getDimensionPixelSize(outValue.resourceId));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mBus.isRegistered(this)) {
            mBus.register(this);
        }
    }

    /* activity becomes visible to user */
    @Override
    protected void onResume() {
        super.onResume();
        cleanRequestsMap();
        setProviders();
    }

    private void initLists() {
        mMainContentLVItems = new ArrayList<ListViewItem>();
        mHeaderItems = new ArrayList<HeaderItem>();
        mRequestQueue = new LinkedHashMap<RssService, String>();
        mResolvedRequests = new ArrayList<RssService>();
        mAllReturnedFeeds = new ArrayList<RSSItem>();
    }

    public void cleanRequestsMap() {
        removeResolvedRequests();
        cancelUnprocessedRequests();
        createRequestsQueue(mCurrentProvider);
    }

    /* method called when a click on a provider from Drawer was made
    * /* get feeds from DB if they exit for the current provider, else make new request*/
    public void onEvent(DrawerSectionHeader headerEvent) {
        Log.v(MfConstants.TAG_LOG, "clicked provider event identified");
        if (headerEvent.getSectionName().equals(MfConstants.NAME)) {
            mCurrentProvider = getProviderByName(MfConstants.NAME);
            /* save provider to preferences also */
            saveProviderToPrefs(mCurrentProvider.getName());

        } else if (headerEvent.getSectionName().equals(TvrProviderConstants.NAME)) {
            mCurrentProvider = getProviderByName(TvrProviderConstants.NAME);
            saveProviderToPrefs(mCurrentProvider.getName());
        }
        /* remove resolved requests from the list
            add this provider's requests to the map and cancel any unprocessed requests from requests queue*/
        cleanRequestsMap();
        mMainContentLVItems.clear();
        mDrawerLayout.closeDrawer(mDrawerLinear);
        getSupportActionBar().setTitle(mCurrentProvider.getName());
        /* get feeds from db */
        getCurrentProviderStoredFeeds();
    }

    /* find the provider ID so we can query db to find the feeds for this provider
    * if not found make new request*/
    private void getCurrentProviderStoredFeeds() {
        /* find the id of the current provider */
        new GetStoredProviders(getApplicationContext()) {
            @Override
            protected void onPostExecute(List<NewsProvider> providers) {
                super.onPostExecute(providers);
                for (NewsProvider dbProvider : providers) {
                    if (dbProvider.getName().equalsIgnoreCase(mCurrentProvider.getName())) {
                        /* async task for this ID */
                        new GetProvidersFeedsAsync(getApplicationContext(), dbProvider.getId()) {
                            @Override
                            protected void onPostExecute(List<RSSItem> rssItems) {
                                super.onPostExecute(rssItems);
                                /* make new request if no feeds were found */
                                if (rssItems.size() > 0) {
                                    Log.v(MfConstants.TAG_LOG, "FOUND -- " + rssItems.size() + " -- FEEDS");
                                    mSortedFeeds = sortFeedsByCategory(rssItems);
                                    createMainContentFromSortedList(mSortedFeeds);
                                    updateMainContentList(mMainContentLVItems, getCategoryHeadersList(mCurrentProvider));
                                } else {
                                    Log.v(MfConstants.TAG_LOG, "NO FEEDS FOUND");
                                    /*  make new request for this provider */
                                    getProviderFeedsRequest();
                                }
                            }
                        }.execute();
                        break;
                    }
                }
            }
        }.execute();
    }

    private void cancelUnprocessedRequests() {
        Set set = mRequestQueue.keySet();
        Iterator<RssService> itr = set.iterator();
        while (itr.hasNext()) {
            RssService service = itr.next();
            service.cancel(true);
            Log.v(MfConstants.TAG_LOG, "CANCELED? " + service.isCancelled());
        }
    }

    private void removeResolvedRequests() {
        Set set = mRequestQueue.keySet();
        Iterator<RssService> itr = set.iterator();
        for (RssService finishedService : mResolvedRequests) {
            while (itr.hasNext()) {
                RssService service = itr.next();
                if (service.equals(finishedService)) {
                    itr.remove();
                }
                break;
            }
        }
    }

    public void onEvent(StoredCategoriesEvent event) {
        if (event.getMessage().equals(MfConstants.RESULT_SUCCESS)) {
            getCurrentProviderStoredFeeds();
            //executeServices();
        }
    }

    private void executeServices() {
        for (RssService request : mRequestQueue.keySet()) {
            if (!request.isCancelled()) {
                request.execute();
            }
        }
    }

    /* method called when data is not found in DB */
    public void onEvent(EventNoStoredData dataIsStored) {
        if (!dataIsStored.isDataIsStored()) {
            getProviderFeedsRequest();
        }

    }

    public void onEvent(DrawerCategoryListItem categoryEvent) {
        String url = categoryEvent.getCategoryLink();
        getSupportActionBar().setTitle(mCurrentProvider.getCategoryObjects().get(url).getCategory());
        mMainContentLVItems.clear();
        mDrawerLayout.closeDrawer(mDrawerLinear);
        selectUrl(url);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBus.unregister(mBus);
    }

    private NewsProvider getProviderFromPrefs() {
        SavedProviderApplication savedProvider = (SavedProviderApplication) getApplicationContext();
        String providerName = savedProvider.getProviderName();
        if (!providerName.equals("")) {
            return getProviderByName(providerName);
        } else {
            return new NewsProvider(MfConstants.NAME, mMediafaxMap);
        }
    }

    private void saveProviderToPrefs(String providerName) {
        SavedProviderApplication savedProvider = (SavedProviderApplication) getApplicationContext();
        savedProvider.saveProvider(providerName);
    }

    /* create map with unprocessed requests; when finished executed remove them from this map */
    private void createRequestsQueue(NewsProvider provider) {
        for (String categoryUrl : provider.getCategoryObjects().keySet()) {
            //List<RSSItem> returnedFeeds = new ArrayList<RSSItem>();
            mRequestQueue.put(new RssService(categoryUrl, this), categoryUrl);
        }
    }

    private void setDrawerProvidersAdapter() {
        mDrawerRecyclerView.setHasFixedSize(true);
        mRVProvidersAdapter = new RVProvidersAdapter(this, mProvidersList, mNewsProviders);
        mDrawerRecyclerView.setAdapter(mRVProvidersAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mDrawerRecyclerView.setLayoutManager(mLayoutManager);
        mDownArrow = (ImageView) findViewById(R.id.downUpIcon);
        mDownArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerHeaderText.setText(mCurrentProvider.getName());
                setDrawerCategoriesAdapter();
                // mDrawerRecyclerView.swapAdapter(mRVCategoriesAdapter, true);
            }
        });
    }

    private void setDrawerCategoriesAdapter() {
        setDrawerCategoriesList();
        mDrawerRecyclerView.setHasFixedSize(true);
        mRVCategoriesAdapter = new RVCategoriesAdapter(this, mCategoriesList);
        mDrawerRecyclerView.setAdapter(mRVCategoriesAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mDrawerRecyclerView.setLayoutManager(mLayoutManager);
        mDownArrow = (ImageView) findViewById(R.id.downUpIcon);
        mDownArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerHeaderText.setText(getResources().getString(R.string.headerText));
                setDrawerProvidersAdapter();
                //mDrawerRecyclerView.swapAdapter(mRVProvidersAdapter, true);
            }
        });
    }

    private void prepareDrawerProvidersList() {
        mProvidersList = new ArrayList<DrawerSectionHeader>();
        DrawerSectionHeader header = new DrawerSectionHeader(MfConstants.NAME,
                getResources().getColor(R.color.brown_300));
        mProvidersList.add(header);
        mProvidersList.add(new DrawerSectionHeader(TvrProviderConstants.NAME, getResources().getColor(R.color.brown_300)));
    }

    private void setDrawerCategoriesList() {
        mCategoriesList.clear();
        for (String keyUrl : mCurrentProvider.getCategoryObjects().keySet()) {
            HeaderItem headerItem = mCurrentProvider.getCategoryObjects().get(keyUrl);
            DrawerCategoryListItem newItem = new DrawerCategoryListItem(keyUrl, headerItem);
            mCategoriesList.add(newItem);
        }
    }

    /*private void createProvidersList(ViewGroup viewGroup) {
        mProvidersList = new ArrayList<DrawerSectionHeader>();
        mProvidersAdapter = new DrawerProvidersAdapter(this);
        mDrawerListView.setAdapter(mProvidersAdapter);
        mProvidersList.add(new DrawerSectionHeader(MfConstants.NAME,
                getResources().getColor(R.color.brown_300)));
        mProvidersList.add(new DrawerSectionHeader(TvrProviderConstants.NAME, getResources().getColor(R.color.brown_300)));
        mProvidersAdapter.updateList(mProvidersList);
        mProvidersAdapter.notifyDataSetChanged();
    }*/

    public void initialiazeMaps() {
        //Resources resources = getApplicationContext().getResources();
        /* using LinkedHashMap to always return keys in same order */
        /* map contains url as key and object with category name and color as value */
        mMediafaxMap = new LinkedHashMap<String, HeaderItem>();
        mTvrMap = new LinkedHashMap<String, HeaderItem>();
        ProviderInit init = new ProviderInit(getApplicationContext(), mMediafaxMap, mTvrMap);
        init.addItems();

    }

    private void defineProviders() {
        mNewsProviders = new ArrayList<NewsProvider>();
        NewsProvider mediafaxProvider = new NewsProvider(MfConstants.NAME, mMediafaxMap);
        mediafaxProvider.setSlogan(MfConstants.SLOGAN);
        NewsProvider tvrProvider = new NewsProvider(TvrProviderConstants.NAME, mTvrMap);
        tvrProvider.setSlogan(TvrProviderConstants.SLOGAN);
        // NewsProvider stiri100Provider = new NewsProvider(MfConstants.PROVIDER_STIRI_100, mStiri100Map);
        mNewsProviders.add(mediafaxProvider);
        mNewsProviders.add(tvrProvider);
    }

    private void createDrawerToogle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.opened_drawer, R.string.closed_drawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //getSupportActionBar().setTitle(getString(R.string.app_name));
                /* rebuild menu to reflect any changes */
                invalidateOptionsMenu(); // calls onPrepareOptionsMenu
                syncState();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle(getString(R.string.drawer_categories));
                invalidateOptionsMenu();
                syncState();
            }
        };

    }

    private void setupViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mMainListView = (ListView) findViewById(R.id.mainContentListView);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLinear = (LinearLayout) findViewById(R.id.drawerLinear);
        // mDrawerListView = (ListView) findViewById(R.id.left_drawer_list);
        mProgressBar = (ProgressBar) findViewById(R.id.loading_progress);
        mDrawerRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mDrawerHeaderText = (TextView) findViewById(R.id.headerText);
    }

    private void selectUrl(String url) {
        mReturnedItemsFromDrawer = new ArrayList<RSSItem>();
        getRssItemsRequest(url);
    }

    private void getRssItemsRequest(String link) {
        new RssService(link) {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressBar.setVisibility(View.VISIBLE);
                //getListView().setVisibility(View.GONE);
            }

            @Override
            protected void onPostExecute(List<RSSItem> rssItems) {
                super.onPostExecute(rssItems);
                if (rssItems != null) {
                    mReturnedItemsFromDrawer = rssItems;
                    for (RSSItem rssItem : rssItems) {
                        CategoryListItem listItem = new CategoryListItem(rssItem, getApplicationContext());
                        mMainContentLVItems.add(listItem);
                    }
                    updateMainContentList(mMainContentLVItems, getCategoryHeadersList(mCurrentProvider));
                   /* Fragment frag = CategoryFragForDrawer.newInstance(mReturnedItemsFromDrawer);
                    FragmentManager fm = getSupportFragmentManager();
                    *//* removes the main fragment used for the main content list *//*
                    fm.beginTransaction().replace(R.id.main_container, frag).commitAllowingStateLoss();*/
                } else {
                    Toast.makeText(getApplicationContext(), "No feeds", Toast.LENGTH_SHORT).show();
                }
                mProgressBar.setVisibility(View.GONE);
                mMainListView.setVisibility(View.VISIBLE);
            }
        }.execute();
    }


    private List<HeaderItem> getCategoryHeadersList(NewsProvider provider) {
        List<HeaderItem> mapValueCategoriesList = new ArrayList<HeaderItem>();
        for (String keyUrl : provider.getCategoryObjects().keySet()) {
            mapValueCategoriesList.add(provider.getCategoryObjects().get(keyUrl));
        }
        return mapValueCategoriesList;
    }

    /* the child FragmentManager ends up with a broken internal state when it is detached from the activity */
    /* when you call FragmentTransaction#commit() after onSaveInstanceState() is called, the transaction
    won't be remembered because it was never recorded as part of the Activity's state in the first place */
    /* TODO called once  */
    private void instantiateFragment(NewsProvider provider, List<ListViewItem> feeds) {
        getSupportActionBar().setTitle(provider.getName());
        if (feeds != null) {
            Fragment frag = AllFeedsFragment.newInstance(feeds, getCategoryHeadersList(provider));
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main_container, frag, "").commitAllowingStateLoss();
        }
    }

    private void instantiate(NewsProvider provider, List<ListViewItem> feeds) {
        getSupportActionBar().setTitle(provider.getName());
        if (feeds != null) {
            Fragment frag = AllFeedsFragment.newInstance(feeds, mHeaderItems);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main_container, frag).commit();
        }
    }

    private void instantiateOneCategoryFrag(NewsProvider provider, List<RSSItem> feeds) {
        getSupportActionBar().setTitle(provider.getName());
        mReturnedItemsFromDrawer = feeds;
        if (feeds != null) {
            Fragment frag = CategoryFragForDrawer.newInstance(mReturnedItemsFromDrawer);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main_container, frag).commitAllowingStateLoss();
        }

    }

    /* TODO modify to update the fragment with new list of feeds */
    private void updateMainContentList(List<ListViewItem> feeds, List<HeaderItem> headers) {
        mMainAdapter.updateList(feeds);
        mProgressBar.setVisibility(View.GONE);
        mMainListView.setVisibility(View.VISIBLE);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        AllFeedsFragment feedsFragment;
        for (Fragment frag : fragments) {
            if (frag instanceof AllFeedsFragment) {
                feedsFragment = (AllFeedsFragment) frag;
                feedsFragment.updateFragLists(feeds, headers);
            }
        }
        //instantiateFragment(mCurrentProvider, feeds);
    }

    /* new rewuest for the feeds of this provider, save to DB */
    void getProviderFeedsRequest() {
        mAllReturnedFeeds = new ArrayList<RSSItem>();
        cleanRequestsMap();
        executeServices();
    }

    /*  update the main content list view with the returned items
     * save them to DB */
    @Override
    public void onRequestFinished(String categoryLink, List<RSSItem> rssItems) {
        if (rssItems != null) {
            //String category = rssItems.get(0).getCategory();
            String category = mCurrentProvider.getCategoryObjects().get(categoryLink).getCategory();
            /* prepare list of received items to remove from queue map of requests*/
            /* calling remove(service) rises Concurrent modification exception
            *  when changing the provider those items will be removed
            * */
            mIsRefreshing = false;
            for (RssService service : mRequestQueue.keySet()) {
                if (mRequestQueue.get(service).equals(categoryLink)) {
                    mResolvedRequests.add(service);
                }
            }
            for (String keyUrl : mCurrentProvider.getCategoryObjects().keySet()) {
                if (mCurrentProvider.getCategoryObjects().get(keyUrl).getCategory().equalsIgnoreCase(category)) {
                    HeaderItem headerItem = new HeaderItem(category, mCurrentProvider.getCategoryObjects().get(keyUrl).getColor());
                    mMainContentLVItems.add(headerItem);
                    break;
                }
            }
            for (RSSItem rssItem : rssItems) {
                mMainContentLVItems.add(new CategoryListItem(rssItem, getApplicationContext()));
            }
            updateMainContentList(mMainContentLVItems, getCategoryHeadersList(mCurrentProvider));
            mCurrentCategoryLink = categoryLink;
            mReturnedFeeds = rssItems;
            /* in order to save them to db I need to set the category id to each feed
             * save to DB when we have all feeds of the provider */
            String result = setCategoryIdForReturnedFeeds(mAllProvidersCategoryItems);
            /* when reached the last category of the provider save the feeds */
            if (isLastCategory()) {
                if (result.equals(MfConstants.RESULT_SUCCESS)) {
                    saveAllFeedsToDB();
                }
            }
        }
    }

    /* check if current category equals the last category of the provider */
    private boolean isLastCategory() {
        Set set = mCurrentProvider.getCategoryObjects().keySet();
        Iterator<String> itr = set.iterator();
        while (itr.hasNext()) {
            if (mCurrentCategoryLink.equals(itr.next())) {
                return true;
            }
            break;
        }
        return false;
    }

    /* check if we have the providers and categories in DB, else save them */
    private void setProviders() {
        new GetStoredProviders(getApplicationContext()) {
            @Override
            protected void onPostExecute(List<NewsProvider> providers) {
                super.onPostExecute(providers);
                if (providers.size() > 0) {
                    /* set ids */
                    for (NewsProvider provider : mNewsProviders) {
                        for (NewsProvider providerDB : providers) {
                            if (provider.getName().equals(providerDB.getName())) {
                                provider.setId(providerDB.getId());
                            }
                            if (mCurrentProvider.getName().equals(providerDB.getName())) {
                                mCurrentProvider.setId(providerDB.getId());
                            }
                        }
                    }
                    getAllCategoryItemsFromDB();
                } else {
                    new StoreProvidersAsync(mNewsProviders, getApplicationContext()) {
                        @Override
                        protected void onPostExecute(List<NewsProvider> providers) {
                            if (providers != null) {
                                for (NewsProvider provider : providers) {
                                    Log.v(MfConstants.TAG_LOG, " --- PROVIDERS SAVED WITH ID: --- " + provider.getId());
                                }
                                /* on callback we have the providers ids to set for the category items*/
                                saveCategories(providers);
                            }
                        }
                    }.execute();
                }
            }
        }.execute();
    }

    private void getAllCategoryItemsFromDB() {
        new GetStoredCategoriesAsync(getApplicationContext()) {
            @Override
            protected void onPostExecute(List<CategoryItem> categoryItems) {
                super.onPostExecute(categoryItems);
                if (categoryItems.size() > 0) {
                    mAllProvidersCategoryItems = new ArrayList<CategoryItem>();
                    for (CategoryItem categoryItem : categoryItems) {
                        mAllProvidersCategoryItems.add(categoryItem);
                        Log.v(MfConstants.TAG_LOG, "category url -- > " + categoryItem.getUrl());
                    }
                    /* send event that categories were saved so we can get the feeds.. */
                    StoredCategoriesEvent event = new StoredCategoriesEvent(MfConstants.RESULT_SUCCESS);
                    EventBus.getDefault().post(event);
                }
            }
        }.execute();
    }

    /* save categories of all providers */
    private void saveCategories(List<NewsProvider> providersFromDB) {
        List<CategoryItem> categoryItems = new ArrayList<CategoryItem>();
        /* search providersFromDB to match these providers (which have the category urls) */
        for (NewsProvider provider : mNewsProviders) {
            for (NewsProvider providerFromDB : providersFromDB) {
                if (provider.getName().equals(providerFromDB.getName())) {
                    Map<String, HeaderItem> categoryObjects = provider.getCategoryObjects();
                    for (String categoryUrl : categoryObjects.keySet()) {
                        CategoryItem newCategory = new CategoryItem();
                        newCategory.setUrl(categoryUrl);
                        newCategory.setName(categoryObjects.get(categoryUrl).getCategory());
                        /* setting the id for the provider from db.. */
                        newCategory.setIdProvider(providerFromDB.getId());
                        categoryItems.add(newCategory);
                    }
                }
            }
        }

        new StoreCategoriesToDbAsync(categoryItems, getApplicationContext()) {
            @Override
            protected void onPostExecute(List<CategoryItem> returnedCategoryItems) {
                if (returnedCategoryItems != null) {
                    mAllProvidersCategoryItems = returnedCategoryItems;
                    for (CategoryItem categoryItem : returnedCategoryItems) {
                        Log.v(MfConstants.TAG_LOG, " --- CATEGORY SAVED : " + categoryItem.getUrl());
                    }
                    /* send event that categories were saved so we can get the feeds.. */
                    StoredCategoriesEvent event = new StoredCategoriesEvent(MfConstants.RESULT_SUCCESS);
                    EventBus.getDefault().post(event);
                }
            }
        }.execute();
    }


    public void saveAllFeedsToDB() {
        /* dont delete the other provider's feeds, only those of the current provider if any! */
        new GetProvidersFeedsAsync(getApplicationContext(), mCurrentProvider.getId()) {
            @Override
            protected void onPostExecute(List<RSSItem> rssItems) {
                super.onPostExecute(rssItems);
                if (rssItems.size() > 0) {
                    Log.v(MfConstants.TAG_LOG, "FOUND OLD ITEMS");
                    /* delete old news of this provider*/
                    new DeleteRowsAsync(getApplicationContext(), mCurrentProvider.getId()) {
                        @Override
                        protected void onPostExecute(Integer rowsDeleted) {
                            super.onPostExecute(rowsDeleted);
                            Log.v(MfConstants.TAG_LOG, "items deleted!!" + rowsDeleted);
                            if (rowsDeleted > 0) {
                                /* put new values )*/
                                storeNewValues();
                            } else {
                                Log.v(MfConstants.TAG_LOG, "DELETING OLD ITEMS FAILED");
                            }
                        }
                    }.execute();
                } else {
                    storeNewValues();
                }
            }
        }.execute();
    }

    private void storeNewValues() {
        new StoreFeedsAsync(getApplicationContext(), mAllReturnedFeeds) {
            @Override
            protected void onPostExecute(List<RSSItem> rssItems) {
                super.onPostExecute(rssItems);
                Log.v(MfConstants.TAG_LOG, " --- " + rssItems.size() + "FEEDS SAVED AT CATEGORY  ---");
            }
        }.execute();
    }

    /* as parameters: the feeds from a category and the category items of a provider */
    private String setCategoryIdForReturnedFeeds(List<CategoryItem> categoryItems) {
        /* search for the item that has the specific category url */
        CategoryItem foundCategoryItem = new CategoryItem();
        for (CategoryItem categoryItem : categoryItems) {
            if (categoryItem.getUrl().equals(mCurrentCategoryLink)) {
                foundCategoryItem = categoryItem;
                break;
            }
        }
        Log.v(MfConstants.TAG_LOG, "found category url >>> " + foundCategoryItem.getUrl());
        if (foundCategoryItem != null) {
            for (RSSItem rssItem : mReturnedFeeds) {
                rssItem.setCategoryId(foundCategoryItem.getId());
                mAllReturnedFeeds.add(rssItem);
            }
            return MfConstants.RESULT_SUCCESS;
        }
        return MfConstants.RESULT_ERROR;
    }


    @Override
    public void onPreExecuting() {
        mProgressBar.setVisibility(View.VISIBLE);
        mIsRefreshing = true;
    }

    @Override
    public void onCanceling() {
    }


    /*private void getStoredFeeds() {
        mCategoryItems = new ArrayList<CategoryItem>();
        mAllFeedsFromDB = new ArrayList<RSSItem>();
        new GetStoredFeeds(getApplicationContext()) {
            @Override
            protected void onPostExecute(List<RSSItem> rssItemsFromDB) {
                *//* update main content list with the feeds from DB *//*
                *//* sort retrieved feeds by category *//*
                if (rssItemsFromDB.size() > 0) {
                    mSortedFeeds = sortFeedsByCategory(rssItemsFromDB);
                    searchProviderInDB(mSortedFeeds.get(0).getCategoryId());
                } else {
                    EventNoStoredData storedData = new EventNoStoredData(false);
                    EventBus.getDefault().post(storedData);
                }
            }
        }.execute();
    }*/

    private void createMainContentFromSortedList(List<RSSItem> sortedFeeds) {
        if (mMainContentLVItems.size() > 0) {
            mMainContentLVItems.clear();
        }
        if (sortedFeeds.size() > 0) {
                    /* add header item when category changes */
            ListViewItem headerItem = new HeaderItem(sortedFeeds.get(0).getCategory(), getResources().getColor(R.color.purple_100));
            mMainContentLVItems.add(headerItem);
            for (RSSItem rssItem : sortedFeeds) {
                int index = sortedFeeds.indexOf(rssItem);
                if ((index > 0) && (index < sortedFeeds.size()) && !(rssItem.getCategory().equals(sortedFeeds.get(index - 1).getCategory()))) {
                    ListViewItem newHeaderItem = new HeaderItem(sortedFeeds.get(index).getCategory(), getResources().getColor(R.color.purple_100));
                    mMainContentLVItems.add(newHeaderItem);
                }
                CategoryListItem newListItem = new CategoryListItem(rssItem, getApplicationContext());
                mMainContentLVItems.add(newListItem);
            }
        }
    }

    /* find the provider of these feeds
     * the insertion was made for a single provider
     * take the category id of an rssItem to see to which provider these feeds belong to
      */
    /*private void searchProviderInDB(final int categoryId) {
        new GetStoredCategoriesAsync(getApplicationContext()) {
            @Override
            protected void onPostExecute(List<CategoryItem> categoryItems) {
                if (categoryItems != null) {
                    for (CategoryItem categoryItem : categoryItems) {
                        if (categoryItem.getId() == categoryId) {
                            mCurrentProviderId = categoryItem.getIdProvider();
                            break;
                        }
                    }
                    new GetStoredProviders(getApplicationContext()) {
                        @Override
                        protected void onPostExecute(List<NewsProvider> providers) {
                            if (providers != null) {
                                for (NewsProvider provider : providers) {
                                    if (provider.getId() == mCurrentProviderId) {
                                        *//* found current provider *//*
                                        for (NewsProvider newsProvider : mNewsProviders) {
                                            if (newsProvider.getName().equals(provider.getName())) {
                                                mCurrentProvider = newsProvider;
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                }
                                createMainContentFromSortedList(mSortedFeeds);
                                //updateMainContentList();
                                mMainAdapter.updateList(mMainContentLVItems);
                                mProgressBar.setVisibility(View.GONE);
                                mMainListView.setVisibility(View.VISIBLE);
                                instantiateFragment(mCurrentProvider, mMainContentLVItems);
                            }
                        }
                    }.execute();
                }
            }
        }.execute();
    }*/

    private List<RSSItem> sortFeedsByCategory(List<RSSItem> rssItemsFromDB) {
        Collections.sort(rssItemsFromDB, new Comparator<RSSItem>() {
            @Override
            public int compare(RSSItem lhs, RSSItem rhs) {
                return lhs.getCategory().compareTo(rhs.getCategory());
            }
        });
        return rssItemsFromDB;
    }

    private NewsProvider getProviderByName(String name) {
        for (NewsProvider newsProvider : mNewsProviders) {
            if (name.equalsIgnoreCase(newsProvider.getName())) {
                return newsProvider;
            }
        }
        return null;
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
       /* Log.v(MfConstants.TAG_LOG, "on scroll changed event");
        if(!mSwipeContainer.canChildScrollUp()){
            mSwipeContainer.setEnabled(true);
        }*/
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

       /* if(firstVisibleItem == 0){
            mSwipeContainer.setEnabled(true);
        } else {
            mSwipeContainer.setEnabled(false);
        }*/
    }


    /* new pager activity which swipes between fragments that contains the feeds from each category */
    private class DrawerCategoryItemCL implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DrawerSectionHeader header = (DrawerSectionHeader) mDrawerCategoriesAdapter.getItem(0);
            String providerName = header.getSectionName();
            /* ignore clicking on header item action */
            if (position != 0) {
                selectItem(position, providerName);
            }
        }

        /* create new fragment and make request for the specified URL */
        private void selectItem(int position, String providerName) {
            DrawerCategoryListItem clickedCategoryLI = (DrawerCategoryListItem) mDrawerCategoriesAdapter.getItem(position);
            String url = null;
            /* search the clicked category in the keyset of the map  */
            NewsProvider currentProvider = getProviderByName(providerName);
            getSupportActionBar().setTitle(currentProvider.getName());
            for (String keyUrl : currentProvider.getCategoryObjects().keySet()) {
                if (currentProvider.getCategoryObjects().get(keyUrl).getCategory().equalsIgnoreCase(clickedCategoryLI.getItem().getCategory())) {
                    url = keyUrl;
                    getSupportActionBar().setTitle(mMediafaxMap.get(keyUrl).getCategory());
                    break;
                }
            }
            if (url != null) {
                selectUrl(url);
            }
            mDrawerListView.setItemChecked(position, true);
            //mDrawerLayout.closeDrawer(mDrawerListView);
            mDrawerLayout.closeDrawer(mDrawerLinear);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.feeds, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sync) {
            initiateRefresh();
            return true;
        }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* refreshes main content list */
    private void initiateRefresh() {
        getSupportActionBar().setTitle(mCurrentProvider.getName());
        Log.v(MfConstants.TAG_LOG, " --- on sync event ---");
        mMainContentLVItems.clear();
        /*delete Old Items and get new ones*/
        getProviderFeedsRequest();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
        }
        super.onBackPressed();
    }

    @Override
    public ActionBar getSupportActionBar() {
        return super.getSupportActionBar();
    }


}
