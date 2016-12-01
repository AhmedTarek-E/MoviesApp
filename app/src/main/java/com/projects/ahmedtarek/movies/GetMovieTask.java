package com.projects.ahmedtarek.movies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.projects.ahmedtarek.movies.interfaces.OnMovieParsedListener;
import com.projects.ahmedtarek.movies.models.Movie;
import com.projects.ahmedtarek.movies.models.Review;
import com.projects.ahmedtarek.movies.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed Tarek on 10/19/2016.
 *
 * this AsyncTask has two purposes, only one to do at a time
 * 1 - to get all movies when no ID param is passed
 * 2 - to get reviews and trailers for a specific movie
 */
public class GetMovieTask extends AsyncTask<String, Void, List<Movie>> {
    private OnMovieParsedListener listener;
    private final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private final String API_QUERY = "api_key";
    private Context context;
    private final String TAG = "GetMovieTask";
    private final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?";
    private final String KEY_QUERY = "v";

    // Movie Attributes
    private final String POSTER_PATH = "poster_path";
    private final String PLOT_SYNOPSIS = "overview";
    private final String RELEASE_DATE = "release_date";
    private final String MOVIE_ID = "id";
    private final String ORIGINAL_TITLE = "original_title";
    private final String RATE = "vote_average";
    private boolean isConnectedToInternet = true;
    private Movie movie;

    // Trailer Attributes
    private final String TRAILER_KEY = "key";
    private final String TRAILER_NAME = "name";

    // Reviews Attributes
    private final String REVIEW_AUTHOR = "author";
    private final String REVIEW_URL = "url";
    private final String REVIEW_CONTENT = "content";

    public GetMovieTask(Context context, OnMovieParsedListener listener) {
        this.listener = listener;
        this.context = context;
    }

    public GetMovieTask(Context context, OnMovieParsedListener listener, Movie movie) {
        this.listener = listener;
        this.context = context;
        this.movie = movie;
    }

    @Override
    protected void onPreExecute() {
        if (!isOnline()) {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
            isConnectedToInternet = false;
        }
    }

    @Override
    protected List<Movie> doInBackground(String... params) {
        if (!isConnectedToInternet)
            return null;

        if (params.length != 0) {
            // this means a movieId has been passed

            String movieId = params[0];

            String jsonString = getJsonString(getTrailersUrl(movieId));
            if (jsonString != null)
                getMovieTrailers(movie, jsonString);

            jsonString = getJsonString(getReviewsUrl(movieId));
            if (jsonString != null)
                getMovieReviews(movie, jsonString);

            List<Movie> result = new ArrayList<>();
            result.add(movie);
            return result;
        }
        String sortingMethod = Utility.getPreferredSortOrder(context);

        Uri urlBuilder = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(sortingMethod)
                .appendQueryParameter(API_QUERY, BuildConfig.MY_MOVIE_DB_API_KEY)
                .build();
        String jsonString = getJsonString(urlBuilder);

        if (jsonString != null) {
            return getMoviesList(jsonString);
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Movie> movieList) {
        if (listener != null && movieList != null) {
            listener.onParsedItem(movieList);
        }
    }

    private void getMovieTrailers(Movie movie, String jsonString) {
        try {
            JSONArray trailers = new JSONObject(jsonString).getJSONArray("results");
            for(int i = 0 ; i < trailers.length() ; i++) {
                JSONObject object = trailers.getJSONObject(i);
                Trailer trailer = new Trailer();
                trailer.setName(object.getString(TRAILER_NAME));
                Uri uri = Uri.parse(BASE_YOUTUBE_URL).buildUpon()
                        .appendQueryParameter(KEY_QUERY, object.getString(TRAILER_KEY))
                        .build();
                trailer.setUrl(uri.toString());
                movie.addTrailer(trailer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getMovieReviews(Movie movie, String jsonString) {
        try {
            JSONArray reviews = new JSONObject(jsonString).getJSONArray("results");
            for(int i = 0 ; i < reviews.length() ; i++) {
                JSONObject object = reviews.getJSONObject(i);
                Review review = new Review();
                review.setAuthor(object.getString(REVIEW_AUTHOR));
                review.setUrl(object.getString(REVIEW_URL));
                review.setContent(object.getString(REVIEW_CONTENT));
                movie.addReview(review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<Movie> getMoviesList(String jsonString) {
        List<Movie> movies = new ArrayList<>();
        try {
            JSONArray allMovies = new JSONObject(jsonString).getJSONArray("results");
            for (int i = 0 ; i < allMovies.length() ; i++) {
                JSONObject object = allMovies.getJSONObject(i);
                Movie movie = new Movie(object.getString(MOVIE_ID));
                movie.setOriginalTitle(object.getString(ORIGINAL_TITLE));
                movie.setOverview(object.getString(PLOT_SYNOPSIS));
                movie.setReleaseDate(object.getString(RELEASE_DATE));
                movie.setVoteAverage(object.getDouble(RATE));
                movie.setMoviePoster(getImagePath(object.getString(POSTER_PATH)));

                movies.add(i, movie);
            }
            return movies;
        } catch (JSONException e) {
            Log.v(TAG, "error getting movies");
        }
        return null;
    }

    private String getImagePath(String shortPath) {
        Uri imageUrl = Uri.parse(BASE_IMAGE_URL).buildUpon()
                .appendEncodedPath("w185")
                .appendEncodedPath(shortPath)
                .build();
        Log.v(TAG, imageUrl.toString());
        return imageUrl.toString();
    }

    private String getJsonString(Uri urlBuilder) {
        HttpURLConnection httpConnection = null;
        BufferedReader bufferedReader = null;

        if (urlBuilder == null) {
            Log.v(TAG, "urlBuilder = null !!!");
            return null;
        }
        Log.v(TAG, urlBuilder.toString());

        try {
            URL url = new URL(urlBuilder.toString());
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            InputStream inputStream = httpConnection.getInputStream();
            if (inputStream == null)
                return null;

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0)
                return null;

            return buffer.toString();
        } catch (MalformedURLException e) {
            Log.v(TAG, "problem in connection", e);
            return null;
        } catch (IOException e) {
            Log.v(TAG, "problem in connection", e);
            return null;
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Uri getTrailersUrl(String movieId) {
      //  /movie/ {movie_id}/videos
        Uri uriBuilder = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(movieId)
                .appendEncodedPath("videos")
                .appendQueryParameter(API_QUERY, BuildConfig.MY_MOVIE_DB_API_KEY)
                .build();
        return uriBuilder;
    }

    private Uri getReviewsUrl(String movieId) {
        //  /movie/ {movie_id}/videos
        Uri uriBuilder = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(movieId)
                .appendEncodedPath("reviews")
                .appendQueryParameter(API_QUERY, BuildConfig.MY_MOVIE_DB_API_KEY)
                .build();
        return uriBuilder;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
