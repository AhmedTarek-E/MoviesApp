package com.projects.ahmedtarek.movies.interfaces;

import com.projects.ahmedtarek.movies.models.Movie;

import java.util.List;

/**
 * Created by Ahmed Tarek on 10/20/2016.
 */
public interface OnMovieParsedListener {
    void onParsedItem(List<Movie> movieList);
}
