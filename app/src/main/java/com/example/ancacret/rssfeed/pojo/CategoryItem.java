package com.example.ancacret.rssfeed.pojo;



public class CategoryItem extends RSSItem {

    private int id;
    private String name;
    private int color;
    private RSSItem mRSSItem;
    private String url;
    private int idProvider;


    public CategoryItem() {
    }

    public CategoryItem(String name, String url, RSSItem RSSItem) {
        this.name = name;
        this.url = url;
        mRSSItem = RSSItem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Integer getColor() {
        return color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public RSSItem getRSSItem() {
        return mRSSItem;
    }

    public String getUrl() {
        return url;
    }

    public void setRSSItem(RSSItem RSSItem) {
        mRSSItem = RSSItem;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIdProvider() {
        return idProvider;
    }

    public void setIdProvider(int idProvider) {
        this.idProvider = idProvider;
    }
}
