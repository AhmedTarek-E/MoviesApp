package com.projects.ahmedtarek.movies.database;

import android.provider.BaseColumns;

/**
 * Created by Ahmed Tarek on 12/1/2016.
 */
public class MovieContract {

    public class MovieEntry implements BaseColumns {
        public final static String TABLE_NAME = "favorite_movies";

        public final static String COLUMN_MOVIE_POSTER = "movie_poster";

        public final static String COLUMN_ORIGINAL_TITLE = "original_title";

        public final static String COLUMN_OVERVIEW = "overview";

        public final static String COLUMN_VOTE_AVERAGE = "vote_average";

        public final static String COLUMN_RELEASE_DATE = "release_date";

        public final static String COLUMN_MOVIE_ID = "movie_id";
    }
}
