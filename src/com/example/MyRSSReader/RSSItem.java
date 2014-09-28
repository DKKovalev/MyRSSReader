package com.example.MyRSSReader;

/**
 * Created by PsichO on 22.09.2014.
 */
public class RSSItem {
    String title = null;
    String description = null;
    String url = null;

    public RSSItem(String title, String description, String url) {
        this.title = title;
        this.description = description;
        this.url = url;
    }
}
