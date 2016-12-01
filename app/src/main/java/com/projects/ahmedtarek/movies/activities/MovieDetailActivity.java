package com.projects.ahmedtarek.movies.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.projects.ahmedtarek.movies.R;
import com.projects.ahmedtarek.movies.fragments.DetailFragment;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
*/
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(getIntent().getExtras());

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.details_container, detailFragment, DetailFragment.DETAIL_FRAGMENT_TAG)
                    .commit();
        }
    }

}
