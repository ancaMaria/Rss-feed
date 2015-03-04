package com.example.ancacret.rssfeed.pojo;


import android.os.Parcel;
import android.os.Parcelable;

public class RSSItem implements Parcelable {

    private int id;
    private String pubDate;
    private String title;
    private String description;
    private String link;
    private String category;
    private String enclosure;
    private int categoryId;

    public RSSItem() {
    }

    public RSSItem(Parcel in){
       // id = in.readInt();
        title = in.readString();
        description = in.readString();
        link = in.readString();
        category = in.readString();
        pubDate = in.readString();
        enclosure = in.readString();

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getCategory() {
        return category;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getEnclosureUrl() {
        return enclosure;
    }

    public void setEnclosureUrl(String enclosure) {
        this.enclosure = enclosure;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(link);
        dest.writeString(category);
        dest.writeString(pubDate);
        dest.writeString(enclosure);
    }

    public static final Creator<RSSItem> CREATOR = new Creator<RSSItem>() {
        @Override
        public RSSItem createFromParcel(Parcel source) {
            RSSItem rssItem = new RSSItem(source);
            return rssItem;
        }

        @Override
        public RSSItem[] newArray(int size) {
            return new RSSItem[0];
        }
    };

}
