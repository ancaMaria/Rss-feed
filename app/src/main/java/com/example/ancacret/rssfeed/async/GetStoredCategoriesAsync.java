package com.example.ancacret.rssfeed.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.ancacret.rssfeed.db.DBHandler;
import com.example.ancacret.rssfeed.pojo.CategoryItem;

import java.util.List;


public class GetStoredCategoriesAsync extends AsyncTask<Void, Void, List<CategoryItem>> {

    private Context mContext;

    public GetStoredCategoriesAsync(Context context) {
        mContext = context;
    }

    @Override
    protected List<CategoryItem> doInBackground(Void... params) {
        DBHandler dbHandler = new DBHandler(mContext);
        return dbHandler.getAllCategories();
    }
}
