package com.projects.ahmedtarek.movies;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.hardware.display.DisplayManager;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements OnParsedItemListener {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    List<Movie> movieList;
    CardsAdapter adapter;
    private final String TAG = "MainFragment";
    private String currentSortingMethod;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutManager = new GridLayoutManager(getActivity(), 2);
        } else {
            mLayoutManager = new GridLayoutManager(getActivity(), 4);
        }
        recyclerView.setLayoutManager(mLayoutManager);

        currentSortingMethod = PreferenceManager.getDefaultSharedPreferences(
                getActivity()).getString(getString(R.string.sorting_key),
                getString(R.string.popular_sorted));
        new GetMovieTask(getActivity(), this).execute();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        String newSortMethod = PreferenceManager.getDefaultSharedPreferences(
                getActivity()).getString(getString(R.string.sorting_key),
                getString(R.string.popular_sorted));

        if (!currentSortingMethod.equals(newSortMethod)) {
            currentSortingMethod = newSortMethod;
            new GetMovieTask(getActivity(), this).execute();
        }
    }

    @Override
    public void onParsedItem(List<Movie> movieList) {
        this.movieList = movieList;
        adapter = new CardsAdapter(getActivity(), movieList);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
    }
}