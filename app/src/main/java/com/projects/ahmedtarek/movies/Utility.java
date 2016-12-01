package com.projects.ahmedtarek.movies;

import android.content.Context;
import android.preference.PreferenceManager;

import com.projects.ahmedtarek.movies.models.Movie;

/**
 * Created by Ahmed Tarek on 11/12/2016.
 */
public class Utility {

    public static String getPreferredSortOrder(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.sorting_key),
                context.getString(R.string.popular_sorted));
    }

    public static String formatSortString(String s) {
        switch (s) {
            case "popular":
                return "Popular";
            case "top_rated":
                return "Top Rated";
            case "favorites":
                return "Favorites";
            default:
                return null;
        }
    }

    public static void saveCurrentSorting(Context context, String currentMethod) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(context.getString(R.string.sorting_key), currentMethod)
                .apply();
    }

    public static String getTheYearOfMovie(Movie movie) {
        char[] year = new char[4];
        movie.getReleaseDate().getChars(0, 4, year, 0);
        return new String(year);
    }
}
