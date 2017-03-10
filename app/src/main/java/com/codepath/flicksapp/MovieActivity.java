package com.codepath.flicksapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.flicksapp.adaptres.MovieArrayAdapter;
import com.codepath.flicksapp.models.Movie;
import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.codepath.flicksapp.models.Movie.fromJSONArray;

public class MovieActivity extends AppCompatActivity {

    ArrayList<Movie> movies;
    MovieArrayAdapter adapter;
    ListView lvMovies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        lvMovies = (ListView) findViewById(R.id.lvMovies);
        movies=new ArrayList<>();
        adapter = new MovieArrayAdapter(this,movies);
        lvMovies.setAdapter(adapter);
        String url="https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
        AsyncHttpClient client=new AsyncHttpClient();
        client.get(url,new JsonHttpResponseHandler(){
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJSONResults = null;

                try {
                    movieJSONResults= response.getJSONArray("results");
                    movies.addAll(Movie.fromJSONArray(movieJSONResults));
                    adapter.notifyDataSetChanged();
                    Log.d("DEBUG",movies.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        setupListViewListener();
    }

    private void setupListViewListener(){

        lvMovies.setOnItemClickListener( new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Movie movie= movies.get(position);
                Intent i = new Intent(MovieActivity.this, MovieDtlsActivity.class);

                i.putExtra("movie",movie);
                //i.putExtra("position",position);*/
                startActivity(i);
            }
        });
    }
}
