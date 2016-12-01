package com.projects.ahmedtarek.movies.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.projects.ahmedtarek.movies.R;
import com.projects.ahmedtarek.movies.Utility;
import com.projects.ahmedtarek.movies.database.MovieDbHelper;
import com.projects.ahmedtarek.movies.models.Movie;

import com.projects.ahmedtarek.movies.database.MovieContract.MovieEntry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

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
    Button favButton;
    MovieDbHelper openHelper;
    private boolean favoriteMovie;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frament_common_details, container, false);

        Bundle args = getArguments();
        if (args == null) {
            return null;
        }
        final Movie movie = (Movie) args.getSerializable(DetailFragment.MOVIE_KEY);
        if (movie == null)
            return null;

        openHelper = new MovieDbHelper(getActivity());

        yearTextView = (TextView) rootView.findViewById(R.id.yearTextView);
        durationTextView = (TextView) rootView.findViewById(R.id.durationTextView);
        plotView = (TextView) rootView.findViewById(R.id.plotView);
        releaseDateView = (TextView) rootView.findViewById(R.id.releaseDateTextView);
        posterView = (ImageView) rootView.findViewById(R.id.imageViewDetail);
        rateTextView = (TextView) rootView.findViewById(R.id.rateTextView);
        favButton = (Button) rootView.findViewById(R.id.favButton);

        favoriteMovie = isFavoriteMovie(movie);
        setFavButtonDesign();

        plotView.setText(movie.getOverview());
        String date = getString(R.string.release_date) + movie.getReleaseDate();
        releaseDateView.setText(date);
        Glide.with(this).load(movie.getMoviePoster()).into(posterView);
        rateTextView.setText(String.valueOf(movie.getVoteAverage()));
        yearTextView.setText(Utility.getTheYearOfMovie(movie));

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favoriteMovie) {
                    deleteMovieFromFavorites(movie.getMovieID());
                    favoriteMovie = false;
                } else {
                    addMovieToFavorites(movie);
                    favoriteMovie = true;
                }
                setFavButtonDesign();
            }
        });

        return rootView;
    }

    private void setFavButtonDesign() {
        if (favoriteMovie) {
            favButton.setText(getString(R.string.marked_as_fav));
            favButton.setBackgroundResource(R.drawable.fav_button_marked);
        } else {
            favButton.setText(getString(R.string.not_marked_as_fav));
            favButton.setBackgroundResource(R.drawable.fav_button_unmarked);
        }
    }

    private boolean isFavoriteMovie(Movie movie) {
        boolean isFavorite = false;
        String[] args = {movie.getMovieID()};
        Cursor cursor = openHelper.getReadableDatabase().query(
                MovieEntry.TABLE_NAME,
                null,
                MovieEntry.COLUMN_MOVIE_ID + "=?",
                args,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            isFavorite = true;
        }
        cursor.close();
        return isFavorite;
    }

    private void deleteMovieFromFavorites(String movieID) {
        String[] args = {movieID};
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.delete(
                MovieEntry.TABLE_NAME,
                MovieEntry.COLUMN_MOVIE_ID + "=?",
                args
        );
    }

    private void addMovieToFavorites(final Movie movie) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri uriPoster = saveMoviePoster(movie);

                SQLiteDatabase db = openHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(MovieEntry.COLUMN_MOVIE_ID, movie.getMovieID());
                if (uriPoster != null) {
                    values.put(MovieEntry.COLUMN_MOVIE_POSTER, uriPoster.toString());
                }
                values.put(MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
                values.put(MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
                values.put(MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
                values.put(MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());

                db.insert(MovieEntry.TABLE_NAME, null, values);
            }
        }).start();
    }

    private Uri saveMoviePoster(Movie movie) {
        Bitmap poster = null;
        try {
            poster = Glide.with(getActivity())
                    .load(movie.getMoviePoster())
                    .asBitmap()
                    .into(185, 277)
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            File img = createImageFile(movie.getOriginalTitle());
            FileOutputStream fos = new FileOutputStream(img);

            if (poster != null) {
                poster.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
                return Uri.fromFile(img);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File createImageFile(String title) {
        return new File(getActivity().getFilesDir(), title + ".png");
    }
}
