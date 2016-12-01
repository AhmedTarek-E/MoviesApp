package com.projects.ahmedtarek.movies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.projects.ahmedtarek.movies.models.Movie;
import com.projects.ahmedtarek.movies.interfaces.OnMovieSelectedListener;
import com.projects.ahmedtarek.movies.R;

import java.util.List;

/**
 * Created by Ahmed Tarek on 10/15/2016.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    List<Movie> moviesList;
    Context context;

    public MoviesAdapter(Context context, List<Movie> moviesList) {
        this.moviesList = moviesList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context)
                .load(moviesList.get(position).getMoviePoster())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(context.getResources().getDrawable(R.drawable.placeholder))
                .into(holder.imageView);
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(moviesList.get(position).getMovieID());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public Movie getItem(int position) {
        return moviesList.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.posterImage);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view instanceof ImageView) {
                int position = getLayoutPosition();
                //Utility.setSelectedMovie(moviesList.get(position));
                OnMovieSelectedListener listener = (OnMovieSelectedListener) context;
                listener.onMovieSelectedListener(getItem(position));
            }
        }
    }
}
