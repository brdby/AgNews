package com.haskellish.agnews.rss;

import java.io.Serializable;
import java.util.ArrayList;

public class News implements Serializable {

    private String title;
    private String description;
    private String link;
    private String image_url;

    private ArrayList<String> categories;

    public News(String title, String description, String link, String image_url, ArrayList<String> categories) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.image_url = image_url;
        this.categories = categories;
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

    public String getImage_url() {
        return image_url;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }
}
