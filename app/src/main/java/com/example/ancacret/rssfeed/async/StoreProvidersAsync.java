package com.example.ancacret.rssfeed.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.ancacret.rssfeed.db.DBHandler;
import com.example.ancacret.rssfeed.pojo.NewsProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anca.cret on 2/17/2015.
 */
public class StoreProvidersAsync extends AsyncTask<List<NewsProvider>, Void, List<NewsProvider>> {

    private List<NewsProvider> mProviders;
    private Context mContext;

    public StoreProvidersAsync(List<NewsProvider> providers, Context context) {
        mProviders = providers;
        mContext = context;
    }

    @Override
    protected List<NewsProvider> doInBackground(List<NewsProvider>... params) {
        DBHandler dbHandler = new DBHandler(mContext);
        List<NewsProvider> newsProviders = new ArrayList<NewsProvider>();
        for(NewsProvider provider : mProviders){
            dbHandler.insertProvider(provider);
        }
        newsProviders = dbHandler.getProvidersFromDB();
        return newsProviders;
    }


    /*@Override
    protected Long doInBackground(NewsProvider... params) {
        DBHandler dbHandler = new DBHandler(mContext);
        return dbHandler.insertProvider(mProvider);
    }*/


}
