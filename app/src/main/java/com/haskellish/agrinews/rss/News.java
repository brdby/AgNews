package com.haskellish.agrinews.rss;

import java.io.Serializable;

public class News implements Serializable {

    private String title;
    private String description;
    private String link;

    public News(String title, String description, String link) {
        this.title = title;
        this.description = description;
        this.link = link;
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
}
