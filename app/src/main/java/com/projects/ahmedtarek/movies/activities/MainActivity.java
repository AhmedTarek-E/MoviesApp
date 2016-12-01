package com.projects.ahmedtarek.movies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.projects.ahmedtarek.movies.models.Movie;
import com.projects.ahmedtarek.movies.interfaces.OnMovieSelectedListener;
import com.projects.ahmedtarek.movies.interfaces.OnSortChangeListener;
import com.projects.ahmedtarek.movies.R;
import com.projects.ahmedtarek.movies.Utility;
import com.projects.ahmedtarek.movies.fragments.DetailFragment;
import com.projects.ahmedtarek.movies.fragments.MoviesFragment;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener,
        OnMovieSelectedListener {
    private Button sortButton;
    private String currentSortingMethod;
    private static boolean mTwoPane; // false = phone , true = tablet

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // sort Button in the toolbar
        sortButton = (Button) getLayoutInflater().inflate(R.layout.sort_button, toolbar, false);
        sortButton.setText(Utility.formatSortString(Utility.getPreferredSortOrder(this)));
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
                view.setBackgroundResource(R.drawable.sort_arrow_selected);
            }
        });
        toolbar.addView(sortButton);
        currentSortingMethod = Utility.getPreferredSortOrder(this);

        // check if tablet
        if (findViewById(R.id.details_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                DetailFragment detailFragment = new DetailFragment();
                detailFragment.setArguments(null);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.details_container, detailFragment, DetailFragment.DETAIL_FRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    public void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.sort_menu, popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                sortButton.setBackgroundResource(R.drawable.sort_arrow);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:

                return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    // for popup menu
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        String newSortMethod = null;
        switch (id) {
            case R.id.action_top_popular:
                newSortMethod = getString(R.string.popular_sorted);
                break;
            case R.id.action_top_rated:
                newSortMethod = getString(R.string.top_rated_sorted);
                break;
            case R.id.action_favorites:
                newSortMethod = getString(R.string.favorite_sorted);
        }
        if (newSortMethod != null && !currentSortingMethod.equals(newSortMethod)) {
            sortButton.setText(Utility.formatSortString(newSortMethod));
            OnSortChangeListener mf = (MoviesFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_movies);
            mf.onSortChange(newSortMethod);
            currentSortingMethod = newSortMethod;
            return true;
        }
        return false;
    }

    @Override
    public void onMovieSelected(Movie movie) {
        Bundle args = new Bundle();
        args.putSerializable(DetailFragment.MOVIE_KEY, movie);

        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(args);

        if (mTwoPane) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.details_container, detailFragment, DetailFragment.DETAIL_FRAGMENT_TAG)
                    .commit();

        } else {
            Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class).putExtras(args);
            startActivity(intent);
        }
    }

    public static boolean isTwoPane() {
        return mTwoPane;
    }
}
