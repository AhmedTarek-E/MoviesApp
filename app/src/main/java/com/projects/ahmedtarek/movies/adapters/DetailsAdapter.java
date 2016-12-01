package com.projects.ahmedtarek.movies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.projects.ahmedtarek.movies.R;
import com.projects.ahmedtarek.movies.fragments.MoreDetailsFragment;
import com.projects.ahmedtarek.movies.models.Review;
import com.projects.ahmedtarek.movies.models.Trailer;

import java.util.List;

/**
 * Created by Ahmed Tarek on 11/27/2016.
 */
public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder> {
    private Context mContext;
    private List<?> detailsList;
    private int mLayoutMode;

    public DetailsAdapter(Context context, List<?> detailsList, int mode) {
        mContext = context;
        this.detailsList = detailsList;
        mLayoutMode = mode;
    }

    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(mLayoutMode, parent, false);
        return new DetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {
        switch (mLayoutMode) {
            case MoreDetailsFragment.MOVIE_TRAILERS_MODE:
                Trailer trailer = (Trailer) detailsList.get(position);
                holder.nameTextView.setText(trailer.getName());
                break;
            case MoreDetailsFragment.MOVIE_REVIEWS_MODE:
                Review review = (Review) detailsList.get(position);
                String authorName = mContext.getString(R.string.author_text) + review.getAuthor();
                holder.nameTextView.setText(authorName);
                String content = getTrimmedText(review.getContent());
                holder.contentTextView.setText(content);
                break;
        }
    }

    private String getTrimmedText(String content) {
        int trimLength = 110;
        if (content.length() > trimLength) {
            return content.substring(0, trimLength) + " ..... see more!";
        }
        return content;
    }

    @Override
    public int getItemCount() {
        return detailsList.size();
    }

    class DetailsViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView contentTextView;

        public DetailsViewHolder(View itemView) {
            super(itemView);
            switch(mLayoutMode) {
                case MoreDetailsFragment.MOVIE_TRAILERS_MODE:
                    nameTextView = (TextView) itemView.findViewById(R.id.trailerTextView);
                    break;
                case MoreDetailsFragment.MOVIE_REVIEWS_MODE:
                    nameTextView = (TextView) itemView.findViewById(R.id.authorTextView);
                    contentTextView = (TextView) itemView.findViewById(R.id.reviewTextView);
                    break;
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (mLayoutMode) {
                        case MoreDetailsFragment.MOVIE_TRAILERS_MODE:
                            Trailer trailer = (Trailer) detailsList.get(getLayoutPosition());
                            mContext.startActivity(
                                    new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.getUrl()))
                            );
                            break;
                        case  MoreDetailsFragment.MOVIE_REVIEWS_MODE:
                            Review review = (Review) detailsList.get(getLayoutPosition());
                            mContext.startActivity(
                                    new Intent(Intent.ACTION_VIEW, Uri.parse(review.getUrl()))
                            );
                    }
                }
            });
        }
    }
}
