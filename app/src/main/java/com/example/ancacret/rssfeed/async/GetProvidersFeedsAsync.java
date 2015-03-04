package com.example.ancacret.rssfeed.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.ancacret.rssfeed.db.DBHandler;
import com.example.ancacret.rssfeed.pojo.RSSItem;

import java.util.List;

/**
 * Created by anca.cret on 2/23/2015.
 */
public class GetProvidersFeedsAsync extends AsyncTask<Integer, Void, List<RSSItem>>{

    private Context mContext;
    private Integer mId;

    public GetProvidersFeedsAsync(Context context, int id) {
        mContext = context;
        mId = id;
    }

    @Override
    protected List<RSSItem> doInBackground(Integer... params) {
        DBHandler dbHandler = new DBHandler(mContext);
        List<RSSItem> retrievedItems = dbHandler.getFeedsFromProvider(mId);
        return retrievedItems;
    }
}
