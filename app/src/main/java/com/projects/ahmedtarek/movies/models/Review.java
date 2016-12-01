package com.projects.ahmedtarek.movies.models;

import java.io.Serializable;

/**
 * Created by Ahmed Tarek on 11/25/2016.
 */
public class Review implements Serializable {
    private String author;
    private String url;
    private String content;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
