package com.projects.ahmedtarek.movies.models;

import java.io.Serializable;

/**
 * Created by Ahmed Tarek on 11/25/2016.
 */
public class Trailer implements Serializable {
    private String url;
    private String name;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
