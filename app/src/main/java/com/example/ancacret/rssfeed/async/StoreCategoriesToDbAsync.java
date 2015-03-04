package com.example.ancacret.rssfeed.async;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ancacret.rssfeed.db.DBHandler;
import com.example.ancacret.rssfeed.pojo.CategoryItem;
import com.example.ancacret.rssfeed.utils.MfConstants;

import java.util.List;

public class StoreCategoriesToDbAsync extends AsyncTask<List<CategoryItem>, Void, List<CategoryItem>>{

    private List<CategoryItem> categoryItems;
    private Context context;

    public StoreCategoriesToDbAsync(List<CategoryItem> categoryItems, Context context) {
        this.categoryItems = categoryItems;
        this.context = context;
    }


    @Override
    protected List<CategoryItem> doInBackground(List<CategoryItem>... params) {
        DBHandler dbHandler = new DBHandler(context);
        List<CategoryItem> categoryItems = this.categoryItems;
        Log.v(MfConstants.TAG_LOG, "INSERTING to DB.... ");
        dbHandler.addCategoriesList(categoryItems);
        return dbHandler.getAllCategories();
    }

}
