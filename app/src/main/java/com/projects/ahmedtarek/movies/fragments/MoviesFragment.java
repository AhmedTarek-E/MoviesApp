package com.projects.ahmedtarek.movies.fragments;

import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.ahmedtarek.movies.GetMovieTask;
import com.projects.ahmedtarek.movies.activities.MainActivity;
import com.projects.ahmedtarek.movies.database.MovieContract;
import com.projects.ahmedtarek.movies.database.MovieDbHelper;
import com.projects.ahmedtarek.movies.interfaces.OnMovieSelectedListener;
import com.projects.ahmedtarek.movies.models.Movie;
import com.projects.ahmedtarek.movies.adapters.MoviesAdapter;
import com.projects.ahmedtarek.movies.interfaces.OnMovieParsedListener;
import com.projects.ahmedtarek.movies.interfaces.OnSortChangeListener;
import com.projects.ahmedtarek.movies.R;
import com.projects.ahmedtarek.movies.Utility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment implements OnMovieParsedListener, OnSortChangeListener {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Movie> movieList = new ArrayList<>();
    private MoviesAdapter adapter;
    private final String MOVIES_LIST_KEY = "movies_list";
    private final static String SELECTED_MOVIE_KEY = "selected_movie";
    private final String TAG = "MoviesFragment";
    MovieDbHelper openHelper;

    private static final String[] QUERY_COLUMNS = {
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
            MovieContract.MovieEntry.COLUMN_MOVIE_POSTER,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE
    };

    private static final int MOVIE_ID_INDEX = 0;
    private static final int MOVIE_ORIGINAL_TITLE_INDEX = 1;
    private static final int MOVIE_POSTER_INDEX = 2;
    private static final int MOVIE_OVERVIEW_INDEX = 3;
    private static final int MOVIE_RELEASE_DATE_INDEX = 4;
    private static final int MOVIE_VOTE_AVERAGE_INDEX = 5;



    public MoviesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        openHelper = new MovieDbHelper(getActivity());
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutManager = new GridLayoutManager(getActivity(), 2);
        } else {
            int spanCount = 4;
            if (MainActivity.isTwoPane())
                spanCount = 3;
            mLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        }
        recyclerView.setLayoutManager(mLayoutManager);

        if (savedInstanceState == null) {
            if (Utility.getPreferredSortOrder(getActivity())
                    .equals(getString(R.string.favorite_sorted))) {

                movieList = getFavoriteMovies();
                onParsedItem(movieList);
            } else {
                new GetMovieTask(getActivity(), this).execute();
            }

        } else {
            if (savedInstanceState.getSerializable(MOVIES_LIST_KEY) instanceof ArrayList) {
                movieList = (List<Movie>) savedInstanceState.getSerializable(MOVIES_LIST_KEY);
                if (movieList != null) {
                    onParsedItem(movieList);
                    int selectedMoviePosition = savedInstanceState.getInt(SELECTED_MOVIE_KEY);
                    if (selectedMoviePosition != -1) {
                        recyclerView.smoothScrollToPosition(selectedMoviePosition);

                       /* if (MainActivity.isTwoPane()) {
                            OnMovieSelectedListener listener = (OnMovieSelectedListener) getActivity();
                            listener.onMovieSelected(adapter.getItem(selectedMoviePosition));
                        }*/
                    }
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(MOVIES_LIST_KEY, (Serializable) movieList);
        if (adapter != null) {
            int selectedMoviePosition = adapter.getmSelectedMoviePosition();
            outState.putInt(SELECTED_MOVIE_KEY, selectedMoviePosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onParsedItem(List<Movie> movieList) {
        this.movieList = movieList;
        adapter = new MoviesAdapter(getActivity(), movieList);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSortChange(String newSortMethod) {
        Utility.saveCurrentSorting(getActivity(), newSortMethod);
        if (newSortMethod.equals(getString(R.string.favorite_sorted))) {
            movieList = getFavoriteMovies();
            onParsedItem(movieList);
        } else {
            new GetMovieTask(getActivity(), this).execute();
        }
    }

    private List<Movie> getFavoriteMovies(){
        List<Movie> movieList = new ArrayList<>();
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.query(
                MovieContract.MovieEntry.TABLE_NAME,
                QUERY_COLUMNS,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie(cursor.getString(MOVIE_ID_INDEX));
                movie.setOriginalTitle(cursor.getString(MOVIE_ORIGINAL_TITLE_INDEX));
                movie.setMoviePoster(cursor.getString(MOVIE_POSTER_INDEX));
                movie.setOverview(cursor.getString(MOVIE_OVERVIEW_INDEX));
                movie.setReleaseDate(cursor.getString(MOVIE_RELEASE_DATE_INDEX));
                movie.setVoteAverage(cursor.getDouble(MOVIE_VOTE_AVERAGE_INDEX));

                movieList.add(movie);
            } while (cursor.moveToNext());
        }
        return movieList;
    }
}