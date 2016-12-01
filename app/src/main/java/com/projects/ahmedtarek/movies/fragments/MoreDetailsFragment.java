package com.projects.ahmedtarek.movies.fragments;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.ahmedtarek.movies.R;
import com.projects.ahmedtarek.movies.adapters.DetailsAdapter;
import com.projects.ahmedtarek.movies.models.Review;
import com.projects.ahmedtarek.movies.models.Trailer;

import java.util.List;

/**
 * Created by Ahmed Tarek on 11/27/2016.
 */
public class MoreDetailsFragment extends Fragment {
    public static final int MOVIE_TRAILERS_MODE = R.layout.trailer_card;
    public static final int MOVIE_REVIEWS_MODE = R.layout.review_card;

    private int mode = -1;
    private RecyclerView recyclerView;
    private DetailsAdapter detailsAdapter;

    public static final String DETAILS_LIST_KEY = "movieDetailsKey";

    public MoreDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_more_details, container, false);

        Bundle args = getArguments();
        if (args == null) {
            return null;
        }
        List<?> moreDetailsList = (List<?>) args.getSerializable(DETAILS_LIST_KEY);
        if (moreDetailsList == null || moreDetailsList.size() == 0) {
            return null;
        }

        if (moreDetailsList.get(0) instanceof Trailer) {
            mode = MOVIE_TRAILERS_MODE;
        } else if (moreDetailsList.get(0) instanceof Review) {
            mode = MOVIE_REVIEWS_MODE;
        }

        recyclerView = (RecyclerView) rootView.findViewById(R.id.detailsRecyclerView);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false)
        );
        detailsAdapter = new DetailsAdapter(getActivity(), moreDetailsList, mode);
        recyclerView.setAdapter(detailsAdapter);

        return rootView;
    }
}
