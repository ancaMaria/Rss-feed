package com.example.ancacret.rssfeed.interfaces;

import com.example.ancacret.rssfeed.pojo.RSSItem;

import java.util.List;

/**
 * Created by anca.cret on 2/13/2015.
 */
public interface IOnTaskCompleted {

    public void onRequestFinished(String url, List<RSSItem> feeds);
    public void onPreExecuting();
    public void onCanceling();


}
