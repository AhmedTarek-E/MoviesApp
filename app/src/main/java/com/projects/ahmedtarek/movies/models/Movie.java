package com.projects.ahmedtarek.movies.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed Tarek on 10/15/2016.
 */
public class Movie implements Serializable {
    private String moviePoster;
    private String originalTitle;
    private String overview;
    private double voteAverage;
    private String releaseDate;
    private String movieID;
    private String duration;
    private List<Trailer> trailers = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();

    public Movie(String movieID) {
        this.movieID = movieID;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getMovieID() {
        return movieID;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void addTrailer(Trailer trailer) {
        trailers.add(trailer);
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }
}
