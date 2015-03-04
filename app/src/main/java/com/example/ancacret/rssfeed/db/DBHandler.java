package com.example.ancacret.rssfeed.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ancacret.rssfeed.pojo.CategoryItem;
import com.example.ancacret.rssfeed.pojo.NewsProvider;
import com.example.ancacret.rssfeed.pojo.RSSItem;
import com.example.ancacret.rssfeed.utils.MfConstants;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DBHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mydb";
    // table names
    private static final String TABLE_CATEGORIES = "categories_table";
    private static final String TABLE_PROVIDERS = "providers_table";
    private static final String TABLE_RSS_ITEMS = "rrs_item_table";
    // categories table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_CATEGORY_NAME = "categoryName";
    // private static final String KEY_ICON_COLOR = "icon_color";
    private static final String KEY_URL = "url";
    private static final String KEY_RSSITEM = "rss_item_id";
    // rss items table Columns names
    private static final String KEY_TITLE = "rss_title";
    private static final String KEY_DESCRIPTION = "item_description";
    private static final String KEY_LINK = "item_link";
    private static final String KEY_ID_CATEGORY = "rss_item_category";
    private static final String KEY_PUB_DATE = "rss_item_pub_date";
    private static final String KEY_CATEGORY = "category_link";
    private static final String KEY_PIC_URL = "pic_url";

    private static final String KEY_ID_PROVIDER = "id_provider";
    private static final String KEY_PROVIDER_NAME = "provider_name";

    private final String joinQuery = "SELECT * FROM " + TABLE_RSS_ITEMS + " A INNER JOIN " + TABLE_CATEGORIES + " B ON A." +KEY_ID_CATEGORY+ " = B."
            +KEY_ID+  " WHERE B." +KEY_ID_PROVIDER+ " = ?;";

    private final String selectQuery = "SELECT * FROM " + TABLE_RSS_ITEMS + " WHERE "+ TABLE_RSS_ITEMS + "."+ KEY_ID_CATEGORY
            + " IN("+ "SELECT " +TABLE_CATEGORIES+ "."+ KEY_ID + " FROM "+ TABLE_CATEGORIES +  " WHERE "+ TABLE_CATEGORIES + "."+KEY_ID_PROVIDER + " = ?);";

    private final String deleteQuery =  "DELETE FROM " + TABLE_RSS_ITEMS + " WHERE "+ TABLE_RSS_ITEMS + "."+ KEY_ID_CATEGORY
            + " IN("+ "SELECT " +TABLE_CATEGORIES+ "."+ KEY_ID + " FROM "+ TABLE_CATEGORIES +  " WHERE "+ TABLE_CATEGORIES + "."+KEY_ID_PROVIDER + " = ?);";

    private final String deleteStmt = TABLE_RSS_ITEMS + "."+ KEY_ID_CATEGORY
            + " IN("+ "SELECT " +TABLE_CATEGORIES+ "."+ KEY_ID + " FROM "+ TABLE_CATEGORIES +  " WHERE "+ TABLE_CATEGORIES + "."+KEY_ID_PROVIDER + " = ?);";


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /* when modifying structure create a joinQuery for modifications to take effect */
        String CREATE_TABLE_RSS_ITEMS = "CREATE TABLE " + TABLE_RSS_ITEMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + KEY_TITLE + " TEXT, "
                + KEY_LINK + " TEXT, "
                + KEY_DESCRIPTION + " TEXT, "
                + KEY_ID_CATEGORY + " INTEGER, "
                + KEY_CATEGORY + " TEXT, "
                + KEY_PUB_DATE + " TEXT, "
                + KEY_PIC_URL + " TEXT"
                + ")";

        String CREATE_TABLE_CATEGORIES = "CREATE TABLE " + TABLE_CATEGORIES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + KEY_CATEGORY_NAME + " TEXT, "
                + KEY_URL + " TEXT, "
                + KEY_ID_PROVIDER + " INTEGER " + ")";

        String CREATE_TABLE_PROVIDER = "CREATE TABLE " + TABLE_PROVIDERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + KEY_PROVIDER_NAME + " TEXT" + ")";

        db.execSQL(CREATE_TABLE_RSS_ITEMS);
        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_PROVIDER);

        newInstall();
    }

    private void newInstall(){

    }

    /* gets called only when database version is changed */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v(MfConstants.TAG_LOG, "ON UPGRADE CALLED  ");
        switch (oldVersion){
            case 1:
                //do something
            case 2:
                break;
        }

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RSS_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROVIDERS);
        // Create tables again
        onCreate(db);
    }

   /* @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }*/

    public long insertProvider(NewsProvider provider){
        long newId;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PROVIDER_NAME, provider.getName());
        newId = db.insert(TABLE_PROVIDERS, null, values);
        return newId;
    }

    public List<NewsProvider> getProvidersFromDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<NewsProvider> providers = new ArrayList<NewsProvider>();
        String query = "SELECT * FROM " + TABLE_PROVIDERS;
        Cursor cursorToQuery = db.rawQuery(query, null);
        while(cursorToQuery.moveToNext()){
            NewsProvider provider = new NewsProvider();
            provider.setId(cursorToQuery.getInt(0));
            provider.setName(cursorToQuery.getString(1));
            providers.add(provider);
        }
        return providers;
    }

    public long insertCategoryIntoDB(CategoryItem categoryItem){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_NAME, categoryItem.getName());
        values.put(KEY_URL, categoryItem.getUrl());
        values.put(KEY_ID_PROVIDER, categoryItem.getIdProvider());
        return db.insert(TABLE_CATEGORIES, null, values);
    }

    public void addCategoriesList(List<CategoryItem> itemList){
        SQLiteDatabase db = this.getWritableDatabase();
        int insertedId;
        for(CategoryItem item : itemList){
            insertedId = (int) insertCategoryIntoDB(item);
        }
        db.close();
    }


    public List<CategoryItem> getAllCategories(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<CategoryItem> categoryItems = new ArrayList<CategoryItem>();
        String query = "SELECT * FROM " + TABLE_CATEGORIES;
        Cursor cursorToCategoryItems = db.rawQuery(query, null);
        while (cursorToCategoryItems.moveToNext()){
            CategoryItem item = new CategoryItem();
            item.setId(cursorToCategoryItems.getInt(0));
            item.setName(cursorToCategoryItems.getString(1));
            item.setUrl(cursorToCategoryItems.getString(2));
            item.setIdProvider(cursorToCategoryItems.getInt(3));
            categoryItems.add(item);
        }
        return categoryItems;
    }

    private long insertRssItem(RSSItem rssItem) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues rssItemValues = new ContentValues();
        rssItemValues.put(KEY_TITLE, rssItem.getTitle());
        rssItemValues.put(KEY_LINK, rssItem.getLink());
        rssItemValues.put(KEY_CATEGORY, rssItem.getCategory());
        /* id of the url category it belongs to */
        rssItemValues.put(KEY_ID_CATEGORY, rssItem.getCategoryId());
        rssItemValues.put(KEY_PUB_DATE, rssItem.getPubDate());
        rssItemValues.put(KEY_PIC_URL, rssItem.getEnclosureUrl());
        if(rssItem.getDescription() != null){
            rssItemValues.put(KEY_DESCRIPTION, rssItem.getDescription());
        } else {
            rssItemValues.put(KEY_DESCRIPTION, "description not found");
        }
        return db.insert(TABLE_RSS_ITEMS, null, rssItemValues);
    }

    public void addAllfeeds(List<RSSItem> rssItems){
        SQLiteDatabase db = this.getWritableDatabase();
        for(RSSItem rssItem : rssItems){
            insertRssItem(rssItem);
        }
        db.close();
    }

    public List<RSSItem> getAllRssItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<RSSItem> rssItems = new ArrayList<RSSItem>();
        String queryRssItemTable = "SELECT * FROM " + TABLE_RSS_ITEMS;
        Cursor cursorToRssItems = db.rawQuery(queryRssItemTable, null);
        while (cursorToRssItems.moveToNext()) {
            RSSItem rssItem = new RSSItem();
            setFeedAttributes(rssItem, cursorToRssItems);
            rssItems.add(rssItem);
        }
        return rssItems;
    }

    private void setFeedAttributes(RSSItem rssItem, Cursor cursorToRssItems) {
        rssItem.setId(cursorToRssItems.getInt(0));
        rssItem.setTitle(cursorToRssItems.getString(1));
        rssItem.setLink(cursorToRssItems.getString(2));
        rssItem.setDescription(cursorToRssItems.getString(3));
        rssItem.setCategoryId(cursorToRssItems.getInt(4));
        rssItem.setCategory(cursorToRssItems.getString(5));
        rssItem.setPubDate(cursorToRssItems.getString(6));
        rssItem.setEnclosureUrl(cursorToRssItems.getString(7));
    }


    public String recreateTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RSS_ITEMS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROVIDERS);
            // Create tables again
            onCreate(db);
            return MfConstants.RESULT_SUCCESS;
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return MfConstants.RESULT_ERROR;
    }

    public String deleteAllRows(){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
        db.delete(TABLE_RSS_ITEMS, null, null);
        db.delete(TABLE_CATEGORIES, null, null);
        db.delete(TABLE_CATEGORIES, null, null);
            return MfConstants.RESULT_SUCCESS;
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return MfConstants.RESULT_ERROR;
    }

    public List<RSSItem> getFeedsFromProvider(int providerId){
        List<RSSItem> rssItems = new ArrayList<RSSItem>();
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursorToRssItems = db.rawQuery(joinQuery, new String[]{String.valueOf(providerId)});
        Cursor cursorToRssItems = db.rawQuery(selectQuery, new String[]{String.valueOf(providerId)});
        while (cursorToRssItems.moveToNext()){
            RSSItem rssItem = new RSSItem();
            setFeedAttributes(rssItem, cursorToRssItems);
            rssItems.add(rssItem);
        }
        return rssItems;
    }


    public int deleteRows(int providerId){
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor cursor = db.rawQuery(deleteQuery, new String[]{String.valueOf(providerId)});
        return db.delete(TABLE_RSS_ITEMS, deleteStmt, new String[]{String.valueOf(providerId)});
    }


}
