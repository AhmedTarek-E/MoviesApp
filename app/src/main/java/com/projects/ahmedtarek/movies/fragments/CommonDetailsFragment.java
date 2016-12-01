package com.projects.ahmedtarek.movies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.projects.ahmedtarek.movies.R;
import com.projects.ahmedtarek.movies.Utility;
import com.projects.ahmedtarek.movies.models.Movie;

/**
 * Created by Ahmed Tarek on 11/30/2016.
 */
public class CommonDetailsFragment extends Fragment {

    TextView plotView;
    TextView releaseDateView;
    ImageView posterView;
    TextView rateTextView;
    TextView yearTextView;
    TextView durationTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frament_common_details, container, false);

        Bundle args = getArguments();
        if (args == null) {
            return null;
        }
        Movie movie = (Movie) args.getSerializable(DetailFragment.MOVIE_KEY);
        if (movie == null)
            return null;

        yearTextView = (TextView) rootView.findViewById(R.id.yearTextView);
        durationTextView = (TextView) rootView.findViewById(R.id.durationTextView);
        plotView = (TextView) rootView.findViewById(R.id.plotView);
        releaseDateView = (TextView) rootView.findViewById(R.id.releaseDateTextView);
        posterView = (ImageView) rootView.findViewById(R.id.imageViewDetail);
        rateTextView = (TextView) rootView.findViewById(R.id.rateTextView);

        plotView.setText(movie.getOverview());
        String date = getString(R.string.release_date) + movie.getReleaseDate();
        releaseDateView.setText(date);
        Glide.with(this).load(movie.getMoviePoster()).into(posterView);
        rateTextView.setText(String.valueOf(movie.getVoteAverage()));
        yearTextView.setText(Utility.getTheYearOfMovie(movie));

        return rootView;
    }
}
