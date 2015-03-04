package com.example.ancacret.rssfeed.pojo;

import com.example.ancacret.rssfeed.interfaces.ProviderInterface;

import java.util.Map;


public class NewsProvider implements ProviderInterface {

    /* map with url as key and object with category name and color as value */
    private String mName;
    private String mSlogan;
    private Map<String, HeaderItem> mCategoryObjects;

    private int id;

    public NewsProvider() {
    }

    public NewsProvider(String name, Map<String, HeaderItem> categoryLinks) {
        mName = name;
        mCategoryObjects = categoryLinks;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        mName = name;
    }


    public void setSlogan(String slogan) {
        mSlogan = slogan;
    }

    public String getSlogan() {
        return mSlogan;
    }

    @Override
    public Map<String, HeaderItem> getCategoryObjects() {
        return mCategoryObjects;
    }

    public String getName() {
        return mName;
    }
}
