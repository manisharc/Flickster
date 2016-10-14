package com.example.chmanish.flickster;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chmanish.flickster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by chmanish on 10/12/16.
 */
public class MovieDetailsActivity extends YouTubeBaseActivity {

    private String apiKey;
    private String trailerUrl1 = "https://api.themoviedb.org/3/movie/";
    private String trailerUrl2 = "/trailers?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    String youtubeKey;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // based on whether popular or not, set different views
        Movie movie = (Movie) getIntent().getSerializableExtra("movieDetails");
        if (movie.getVoteAverage() < 5){
            setContentView(R.layout.activity_movie_details);
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
            ratingBar.setRating(voteAverage);
            overview.setText(movie.getOverview());
        }
        else {
            apiKey = getString(R.string.youtube_api_key);
            setContentView(R.layout.item_popular_video);
            //Find the youtube key for that movie ID
            url = String.format("%s%s%s",trailerUrl1,movie.getId(),trailerUrl2);

            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONArray trailerJsonResults = null;
                    try {
                        trailerJsonResults = response.getJSONArray("youtube");
                        for(int i =0 ; i < trailerJsonResults.length(); i++){
                            if (trailerJsonResults.getJSONObject(i).getString("type").equals("Trailer")){
                                youtubeKey = trailerJsonResults.getJSONObject(i).getString("source");
                                YouTubePlayerView youTubePlayerView =
                                        (YouTubePlayerView) findViewById(R.id.player);

                                youTubePlayerView.initialize(apiKey,
                                        new YouTubePlayer.OnInitializedListener() {
                                            @Override
                                            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                                                YouTubePlayer youTubePlayer, boolean b) {

                                                // do any work here to cue video, play video, etc.
                                                //youTubePlayer.setFullscreen(true);
                                                youTubePlayer.loadVideo(youtubeKey);

                                            }
                                            @Override
                                            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                                                YouTubeInitializationResult youTubeInitializationResult) {

                                                Toast.makeText(MovieDetailsActivity.this, "Youtube Failed!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                break;

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);

                }
            });


        }
    }

}
