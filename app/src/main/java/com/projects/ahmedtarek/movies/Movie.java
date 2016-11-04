package com.projects.ahmedtarek.movies;

/**
 * Created by Ahmed Tarek on 10/15/2016.
 */
public class Movie {
    private String moviePoster;
    private String originalTitle;
    private String overview;
    private double voteAverage;
    private String releaseDate;
    private String movieID;
    private static Movie selectedMovie = null;

    public Movie(String moviePoster, String movieID) {
        this.moviePoster = moviePoster;
        this.movieID = movieID;
    }

    public static Movie getSelectedMovie() {
        return selectedMovie;
    }

    public static void setSelectedMovie(Movie selectedMovie) {
        Movie.selectedMovie = selectedMovie;
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
}
