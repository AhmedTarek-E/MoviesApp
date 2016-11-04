package com.projects.ahmedtarek.movies;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {
    TextView titleView;
    TextView plotView;
    TextView releaseDateView;
    ImageView posterView;
    RatingBar ratingBar;
    TextView rateTextView;

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Movie movie = Movie.getSelectedMovie();
        titleView = (TextView) rootView.findViewById(R.id.titleView);
        plotView = (TextView) rootView.findViewById(R.id.plotView);
        releaseDateView = (TextView) rootView.findViewById(R.id.releaseDateView);
        posterView = (ImageView) rootView.findViewById(R.id.imageViewDetail);
        ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
        rateTextView = (TextView) rootView.findViewById(R.id.rateTextView);

        titleView.setText(movie.getOriginalTitle());
        plotView.setText(movie.getOverview());
        String date = getString(R.string.release_date) + movie.getReleaseDate();
        releaseDateView.setText(date);
        Glide.with(this).load(movie.getMoviePoster()).into(posterView);
        ratingBar.setRating((float) (movie.getVoteAverage()/2));
        rateTextView.setText(String.valueOf(movie.getVoteAverage()));
        return rootView;
    }
}
