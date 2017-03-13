package com.codepath.flicksapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.flicksapp.adaptres.MovieArrayAdapter;
import com.codepath.flicksapp.adaptres.ReviewArrayAdapter;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static android.R.attr.width;
import static com.codepath.flicksapp.R.id.lvMovies;
import static com.codepath.flicksapp.R.id.ratingBar;
import static com.codepath.flicksapp.models.Movie.MovieType.POPULAR;

/**
 * Created by alex_ on 3/9/2017.
 */

public class MovieDtlsActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    public static final String API_KEY = "flicksapp-161019";
    Movie movie;
    ArrayList<Trailer> trailers;
    //RatingBar rbRating;
    //  TextView tvTitle;
    //TextView tvReleaseDate;
    //TextView tvOverview;
    TextView tvGenres;
    ReviewArrayAdapter reviewAdapter;
    ListView lvReviews;


    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvOverview) TextView tvOverview;
    @BindView(R.id.tvReleaseDate) TextView tvReleaseDate;
    @BindView(R.id.ratingBar) RatingBar rbRating;
    @BindView(R.id.tvDirectedBy) TextView tvDirectedBy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_dtls);
        ButterKnife.bind(this);
        movie = (Movie) getIntent().getSerializableExtra("movie");

        //tvTitle = (TextView) findViewById(R.id.tvTitle);
      /*  tvReleaseDate = (TextView) findViewById(R.id.tvReleaseDate);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbRating = (RatingBar) findViewById(ratingBar);*/
        rbRating.setIsIndicator(true);
        rbRating.setRating(movie.getRating());

        LayerDrawable stars = (LayerDrawable) rbRating.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.rgb(218, 165, 32), PorterDuff.Mode.SRC_ATOP);

        tvTitle.setText(movie.getOriginalTitle());
        tvOverview.setText(movie.getOverview());
        String releaseDateStr = movie.getReleaseDate();
        if (!releaseDateStr.isEmpty()) {
            tvReleaseDate.setText(releaseDateStr);
        }

        trailers = new ArrayList<>();

        String url = String.format("https://api.themoviedb.org/3/movie/%s/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed", movie.getMovieId());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray trailerJSONResults = null;
                try {
                    trailerJSONResults = response.getJSONArray("results");
                    trailers.addAll(Trailer.fromJSONArray(trailerJSONResults));
                    initYoutubePlayer();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        //fill details
        url=String.format("https://api.themoviedb.org/3/movie/%s?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&append_to_response=credits,reviews", movie.getMovieId());
        AsyncHttpClient client1 = new AsyncHttpClient();
        client1.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {

                    setDetails(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setDetails(JSONObject jsonObject) throws JSONException
    {
        movie.getDetails(jsonObject);

        tvGenres = (TextView) findViewById(R.id.tvGenres);
        tvGenres.setText(movie.getGenres());

        ImageView img = (ImageView)findViewById(R.id.ivImdb);
        img.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(movie.getImdbUrl()));
                startActivity(intent);
            }
        });

        ArrayList reviews = movie.getReviews();
        if(reviews.size()>0) {
            lvReviews = (ListView) findViewById(R.id.lvReviews);
            reviewAdapter = new ReviewArrayAdapter(this, movie.getReviews());
            lvReviews.setAdapter(reviewAdapter);
        }else
        {
            TextView tvReview = (TextView)findViewById(R.id.tvReviews);
            tvReview.setText("No reviews found.");
        }


        tvDirectedBy.setText(String.format("Directed By: %s ",movie.getDirectedBy()));
    }

    private void initYoutubePlayer() {
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.player);
        youTubePlayerView.initialize(API_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        if (trailers.size() == 0) {
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
