package com.example.ancacret.rssfeed.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ancacret.rssfeed.db.DBHandler;
import com.example.ancacret.rssfeed.pojo.CategoryItem;
import com.example.ancacret.rssfeed.pojo.RSSItem;
import com.example.ancacret.rssfeed.utils.MfConstants;

import java.util.ArrayList;
import java.util.List;


public class GetStoredFeeds extends AsyncTask<Void, Void, List<RSSItem>> {

    private Context mContext;

    public GetStoredFeeds(Context context) {
        mContext = context;
    }

    @Override
    protected List<RSSItem> doInBackground(Void... params) {
        DBHandler dbHandler = new DBHandler(mContext);
        List<RSSItem> retrievedItems = dbHandler.getAllRssItems();
        return retrievedItems;
    }

}
