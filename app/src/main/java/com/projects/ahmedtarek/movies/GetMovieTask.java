package com.projects.ahmedtarek.movies;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

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
 */
public class GetMovieTask extends AsyncTask<Void, Void, List<Movie>> {
    private OnParsedItemListener listener;
    private final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private final String API_QUERY = "api_key";
    private Context context;
    private final String TAG = "GetMovieTask";
    private final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";

    // Attributes
    private final String POSTER_PATH = "poster_path";
    private final String PLOT_SYNOPSIS = "overview";
    private final String RELEASE_DATE = "release_date";
    private final String MOVIE_ID = "id";
    private final String ORIGINAL_TITLE = "original_title";
    private final String RATE = "vote_average";
    private boolean isConnectedToInternet = true;

    public GetMovieTask(Context context, OnParsedItemListener listener) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        if (!isOnline()) {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
            isConnectedToInternet = false;
        }
    }

    @Override
    protected List<Movie> doInBackground(Void... params) {
        if (!isConnectedToInternet)
            return null;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String sortingMethod = prefs.getString(context.getString(R.string.sorting_key), context.getString(R.string.popular_sorted));

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

    private List<Movie> getMoviesList(String jsonString) {
        List<Movie> movies = new ArrayList<>();
        try {
            JSONArray allMovies = new JSONObject(jsonString).getJSONArray("results");
            for (int i = 0 ; i < allMovies.length() ; i++) {
                JSONObject object = allMovies.getJSONObject(i);
                Movie movie = new Movie(getImagePath(object.getString(POSTER_PATH)),
                        object.getString(MOVIE_ID));
                movie.setOriginalTitle(object.getString(ORIGINAL_TITLE));
                movie.setOverview(object.getString(PLOT_SYNOPSIS));
                movie.setReleaseDate(object.getString(RELEASE_DATE));
                movie.setVoteAverage(object.getDouble(RATE));

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

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
