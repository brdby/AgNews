package com.haskellish.agrinews.rss;

import java.io.Serializable;

public class News implements Serializable {

    private String title;
    private String description;
    private String link;
    private String image_url;

    public News(String title, String description, String link, String image_url) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.image_url = image_url;
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
}
