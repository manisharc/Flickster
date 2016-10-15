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

/**
 * Created by chmanish on 10/12/16.
 */
public class MovieDetailsActivity extends YouTubeBaseActivity {

    private String apiKey;
    String youtubeKey;
    Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // based on whether popular or not, set different views
        movie = (Movie) getIntent().getSerializableExtra("movieDetails");
        youtubeKey = getIntent().getStringExtra("youtubeKey");
        apiKey = getString(R.string.youtube_api_key);
        if (movie.getVoteAverage() < 5){
            setContentView(R.layout.activity_movie_details);
            TextView title = (TextView)findViewById(R.id.tvMovieName);
            TextView releaseDate = (TextView)findViewById(R.id.tvReleaseDate);
            RatingBar ratingBar = (RatingBar)findViewById(R.id.ratingBar);
            TextView overview = (TextView) findViewById(R.id.tvOverview);
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

        }
        //Find the youtube key for that movie ID
        YouTubePlayerView youTubePlayerView =
                (YouTubePlayerView) findViewById(R.id.player);

        youTubePlayerView.initialize(apiKey,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        // do any work here to cue video, play video, etc.
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
