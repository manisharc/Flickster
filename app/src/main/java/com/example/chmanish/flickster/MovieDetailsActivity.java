package com.example.chmanish.flickster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.chmanish.flickster.models.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by chmanish on 10/12/16.
 */
public class MovieDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Movie movie = (Movie) getIntent().getSerializableExtra("movieDetails");

        ImageView imgView = (ImageView)findViewById(R.id.ivBackdropImage);
        TextView title = (TextView)findViewById(R.id.tvMovieName);
        TextView releaseDate = (TextView)findViewById(R.id.tvReleaseDate);
        RatingBar ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        TextView overview = (TextView) findViewById(R.id.tvOverview);

        imgView.setImageResource(0);
        Picasso.with(this).load(movie.getBackdropPath())
                .placeholder(R.drawable.placeholder).into(imgView);
        title.setText(movie.getOriginalTitle());
        releaseDate.setText(String.format("Release Date: %s",movie.getReleaseDate()));

        float voteAverage = movie.getVoteAverage();
        voteAverage = voteAverage/2;
        Log.d("voteAverage", String.valueOf(voteAverage));
        ratingBar.setRating((float)voteAverage);

        overview.setText(movie.getOverview());

    }

}
