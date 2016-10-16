package com.example.chmanish.flickster;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chmanish.flickster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chmanish on 10/12/16.
 */
public class MovieDetailsActivity extends YouTubeBaseActivity {

    private String apiKey;
    String youtubeKey;
    Movie movie;

    //BindViews
    @BindView(R.id.tvMovieName) TextView title;
    @BindView(R.id.tvReleaseDate) TextView releaseDate;
    @BindView(R.id.tvOverview) TextView overview;
    @BindView(R.id.ratingBar) RatingBar ratingBar;
    @BindView(R.id.player) YouTubePlayerView youTubePlayerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // based on whether popular or not, set different views
        movie = (Movie) getIntent().getSerializableExtra("movieDetails");
        youtubeKey = getIntent().getStringExtra("youtubeKey");
        apiKey = getString(R.string.youtube_api_key);
        if (movie.getVoteAverage() < 5){
            setContentView(R.layout.activity_movie_details);
            ButterKnife.bind(this);
            title.setText(movie.getOriginalTitle());
            releaseDate.setText(String.format("Release Date: %s",movie.getReleaseDate()));
            float voteAverage = movie.getVoteAverage();
            voteAverage = voteAverage/2;
            Log.d("voteAverage", String.valueOf(voteAverage));
            ratingBar.setRating(voteAverage);
            overview.setText(movie.getOverview());
        }
        else {

            setContentView(R.layout.item_popular_video);
            ButterKnife.bind(this);

        }

        youTubePlayerView.initialize(apiKey,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        // cue video if it is not a popular movie
                        if (movie.getVoteAverage() < 5)
                            youTubePlayer.cueVideo(youtubeKey);
                        else {
                            //youTubePlayer.setFullscreen(true);
                            youTubePlayer.loadVideo(youtubeKey);
                        }

                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                        Toast.makeText(MovieDetailsActivity.this, "Youtube Failed!", Toast.LENGTH_SHORT).show();
                    }
                });

    }

}
