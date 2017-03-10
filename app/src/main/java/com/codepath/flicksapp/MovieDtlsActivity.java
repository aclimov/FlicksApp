package com.codepath.flicksapp;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.flicksapp.models.Movie;
import com.codepath.flicksapp.models.Trailer;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static android.R.transition.move;
import static com.codepath.flicksapp.R.id.ratingBar;
import static com.codepath.flicksapp.models.Movie.MovieType.POPULAR;
import static java.security.AccessController.getContext;

/**
 * Created by alex_ on 3/9/2017.
 */

public class MovieDtlsActivity extends YouTubeBaseActivity implements  YouTubePlayer.OnInitializedListener {

    public static final String API_KEY="flicksapp-161019";
    Movie movie;
    ArrayList<Trailer> trailers ;
    RatingBar rbRating;
    TextView tvTitle;
    TextView tvReleaseDate;
    TextView tvOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_dtls);
        movie = (Movie) getIntent().getSerializableExtra("movie");

        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvReleaseDate = (TextView)findViewById(R.id.tvReleaseDate);
        tvOverview = (TextView)findViewById(R.id.tvOverview);
        rbRating=(RatingBar)findViewById(ratingBar);
        rbRating.setIsIndicator(true);
        rbRating.setRating(movie.getRating());

        LayerDrawable stars = (LayerDrawable) rbRating.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.rgb(218, 165, 32), PorterDuff.Mode.SRC_ATOP);

        tvTitle.setText(movie.getOriginalTitle());;
        tvOverview.setText(movie.getOverview());
        String releaseDateStr = movie.getReleaseDate();
        if(!releaseDateStr.isEmpty()) {
            tvReleaseDate.setText(releaseDateStr);
        }

        trailers=new ArrayList<>();

        String url=String.format("https://api.themoviedb.org/3/movie/%s/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed", movie.getMovieId());
        AsyncHttpClient client=new AsyncHttpClient();
        client.get(url,new JsonHttpResponseHandler(){
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray trailerJSONResults = null;
                try {
                    trailerJSONResults= response.getJSONArray("results");
                    trailers.addAll(Trailer.fromJSONArray(trailerJSONResults));
                    initYoutubePlayer();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initYoutubePlayer()
    {
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.player);
        youTubePlayerView.initialize(API_KEY,this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        if (trailers.size() == 0)
        {
            //youTubePlayer.cueVideo("1gMcRz2OoDg");
        }
        Trailer trailer = trailers.get(0);

        Configuration config = getResources().getConfiguration();
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE || movie.getMovieType() == POPULAR) {
            youTubePlayer.setFullscreen(true);
            youTubePlayer.loadVideo(trailer.getYoutubeId());
            // youTubePlayer.play();
        } else {
            youTubePlayer.cueVideo(trailer.getYoutubeId());
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
