package com.example.ancacret.rssfeed.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.ancacret.rssfeed.db.DBHandler;
import com.example.ancacret.rssfeed.pojo.NewsProvider;

import java.util.List;

/**
 * Created by anca.cret on 2/17/2015.
 */
public class GetStoredProviders extends AsyncTask<Void, Void, List<NewsProvider>> {

    private Context mContext;

    public GetStoredProviders(Context context) {
        mContext = context;
    }

    @Override
    protected List<NewsProvider> doInBackground(Void... params) {
        DBHandler dbHandler = new DBHandler(mContext);
        return dbHandler.getProvidersFromDB();
    }
}
