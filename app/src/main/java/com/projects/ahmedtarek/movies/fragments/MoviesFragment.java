package com.projects.ahmedtarek.movies.fragments;

import android.content.res.Configuration;
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
import com.projects.ahmedtarek.movies.models.Movie;
import com.projects.ahmedtarek.movies.adapters.MoviesAdapter;
import com.projects.ahmedtarek.movies.interfaces.OnMovieParsedListener;
import com.projects.ahmedtarek.movies.interfaces.OnSortChangeListener;
import com.projects.ahmedtarek.movies.R;
import com.projects.ahmedtarek.movies.Utility;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment implements OnMovieParsedListener, OnSortChangeListener {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    List<Movie> movieList;
    MoviesAdapter adapter;
    private final String TAG = "MoviesFragment";

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

        new GetMovieTask(getActivity(), this).execute();
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
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }


    @Override
    public void onParsedItem(List<Movie> movieList) {
        this.movieList = movieList;
        adapter = new MoviesAdapter(getActivity(), movieList);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSortChangeListener(String newSortMethod) {
        Utility.saveCurrentSorting(getActivity(), newSortMethod);
        new GetMovieTask(getActivity(), this).execute();
    }
}