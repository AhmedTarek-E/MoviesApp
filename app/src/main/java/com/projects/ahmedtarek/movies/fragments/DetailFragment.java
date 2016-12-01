package com.projects.ahmedtarek.movies.fragments;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.graphics.Rect;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.projects.ahmedtarek.movies.GetMovieTask;
import com.projects.ahmedtarek.movies.adapters.DetailViewPagerAdapter;
import com.projects.ahmedtarek.movies.models.Movie;
import com.projects.ahmedtarek.movies.interfaces.OnMovieParsedListener;
import com.projects.ahmedtarek.movies.R;
import com.projects.ahmedtarek.movies.Utility;
import com.projects.ahmedtarek.movies.models.Review;
import com.projects.ahmedtarek.movies.models.Trailer;

import java.io.Serializable;
import java.util.List;

public class DetailFragment extends Fragment implements OnMovieParsedListener {
    TextView titleView;
    TabLayout tabLayout;
    ViewPager viewPager;

    public static final String MOVIE_KEY = "get_movie";
    public static final String DETAIL_FRAGMENT_TAG = "detailFragment";

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Bundle args = getArguments();
        if (args == null) {
            return null;
        }
        Movie movie = (Movie) args.getSerializable(MOVIE_KEY);
        if (movie == null)
            return null;

        new GetMovieTask(getActivity(), this, movie).execute(movie.getMovieID());

        titleView = (TextView) rootView.findViewById(R.id.titleView);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);

        titleView.setText(movie.getOriginalTitle());

        return rootView;
    }

    private void setupViewPager(ViewPager viewPager, Movie movie) {
        DetailViewPagerAdapter adapter = new DetailViewPagerAdapter(getFragmentManager());

        Bundle args = new Bundle();
        args.putSerializable(MOVIE_KEY, movie);

        CommonDetailsFragment commonDetailsFragment = new CommonDetailsFragment();
        commonDetailsFragment.setArguments(args);

        Bundle args2 = new Bundle();
        args2.putSerializable(MoreDetailsFragment.DETAILS_LIST_KEY, (Serializable) movie.getTrailers());

        MoreDetailsFragment trailersFragment = new MoreDetailsFragment();
        trailersFragment.setArguments(args2);

        Bundle args3 = new Bundle();
        args3.putSerializable(MoreDetailsFragment.DETAILS_LIST_KEY, (Serializable) movie.getReviews());

        MoreDetailsFragment reviewsFragment = new MoreDetailsFragment();
        reviewsFragment.setArguments(args3);

        adapter.addFragment(commonDetailsFragment, "Details");
        adapter.addFragment(trailersFragment, "Trailers");
        adapter.addFragment(reviewsFragment, "Reviews");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onParsedItem(List<Movie> movieList) {
        setupViewPager(viewPager, movieList.get(0));
        tabLayout.setupWithViewPager(viewPager);
    }
}
