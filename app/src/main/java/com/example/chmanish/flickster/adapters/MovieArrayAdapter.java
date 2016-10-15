package com.example.chmanish.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chmanish.flickster.R;
import com.example.chmanish.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by chmanish on 10/11/16.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    private static int NUM_OF_VIEWS = 2;
    private static int POPULAR_VIEW = 0;
    private static int NOT_SO_POPULAR_VIEW = 1;

    // View lookup cache
    private static class ViewHolder {
        TextView title;
        TextView overview;
        ImageView image;
        ImageView play;
    }

    private static class PopViewHolder {
        ImageView image;
    }

    public MovieArrayAdapter(Context context, List<Movie>movies){
        super (context, android.R.layout.simple_list_item_1, movies);
    }

    // Returns the number of types of Views that will be created by getView(int, View, ViewGroup)
    @Override
    public int getViewTypeCount() {
        // Returns the number of types of Views that will be created by this adapter
        return NUM_OF_VIEWS;
    }

    // Get the type of View that will be created by getView(int, View, ViewGroup)
    // for the specified item.
    @Override
    public int getItemViewType(int position) {
        Movie movie = getItem(position);
        if(movie.getVoteAverage() >= 5){
            return POPULAR_VIEW;
        }
        else {
            return NOT_SO_POPULAR_VIEW;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        int viewType = this.getItemViewType(position);
        int orientation = getContext().getResources().getConfiguration().orientation;

        if (viewType == POPULAR_VIEW &&
                orientation == Configuration.ORIENTATION_PORTRAIT){
            PopViewHolder popViewHolder;
            //check the existing view being reused
            if (convertView == null){
                popViewHolder = new PopViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_popular_movie, parent, false);
                popViewHolder.image = (ImageView) convertView.findViewById(R.id.ivBackdropImage);
                // Cache the viewHolder object inside the fresh view
                convertView.setTag(popViewHolder);
            }
            else {
                // View is being recycled, retrieve the viewHolder object from tag
                popViewHolder = (PopViewHolder) convertView.getTag();

            }

            // clear out the image from convertView
            popViewHolder.image.setImageResource(0);
            Picasso.with(getContext()).load(movie.getBackdropPath())
                        .transform(new RoundedCornersTransformation(10, 10))
                        .placeholder(R.drawable.placeholder).into(popViewHolder.image);
        }
        else {
            // view lookup cache stored in tag
            ViewHolder viewHolder;

            //check the existing view being reused
            if (convertView == null){
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_movie, parent, false);
                viewHolder.title = (TextView) convertView.findViewById(R.id.tvTitle);
                viewHolder.overview = (TextView) convertView.findViewById(R.id.tvOverview);
                viewHolder.play = (ImageView) convertView.findViewById(R.id.ivYoutubePlay);
                viewHolder.image = (ImageView) convertView.findViewById(R.id.ivMovieImage);
                // Cache the viewHolder object inside the fresh view
                convertView.setTag(viewHolder);
            }
            else {
                // View is being recycled, retrieve the viewHolder object from tag
                viewHolder = (ViewHolder) convertView.getTag();

            }

            // clear out the image from convertView
            viewHolder.image.setImageResource(0);

            //populate data
            viewHolder.title.setText(movie.getOriginalTitle());
            viewHolder.overview.setText(movie.getOverview());

            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (viewType == POPULAR_VIEW){
                    viewHolder.play.setVisibility(View.VISIBLE);

                }
                Picasso.with(getContext()).load(movie.getBackdropPath())
                        .transform(new RoundedCornersTransformation(10, 10))
                        .placeholder(R.drawable.placeholder)
                        .into(viewHolder.image);
            }
            else {
                Picasso.with(getContext()).load(movie.getPosterPath())
                        .transform(new RoundedCornersTransformation(10, 10))
                        .placeholder(R.drawable.placeholder).into(viewHolder.image);
            }

        }

        //return the view
        return convertView;
    }
}
