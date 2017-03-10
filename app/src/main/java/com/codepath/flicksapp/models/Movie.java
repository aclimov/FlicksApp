package com.codepath.flicksapp.models;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alex_ on 3/6/2017.
 */

public class Movie implements Serializable   {

    public enum MovieType {
       POPULAR,NOPOPULAR
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }
    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w500%s", backdropPath);
    }
    public String getOriginalTitle() {
        return originalTitle;
    }
    public String getOverview() {
        return overview;
    }
    public String getMovieId()
    {
        return Integer.toString(movieId);
    }
    public String getReleaseDate()
    {
        if(isReleaseDateSpecified)
        {
            android.text.format.DateFormat df = new android.text.format.DateFormat();
            return String.format("Release date:\n    %s", df.format("MMM-dd", releaseDate).toString());
        }
        return"";
    }
    public float getRating()
    {
        double d=voteAverage/2;
        return (float) d;
    }


    String posterPath;
    String originalTitle;
    String overview;
    String backdropPath;
    int movieId;
    Double voteAverage;
    Date releaseDate;
    Boolean isReleaseDateSpecified;

    public Movie(JSONObject jsonObject) throws JSONException
    {
        this.posterPath = jsonObject.getString("poster_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.voteAverage = jsonObject.getDouble("vote_average");
        this.movieId = jsonObject.getInt("id");
        String sReleaseDate = jsonObject.getString("release_date");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            this.releaseDate = dateFormat.parse(sReleaseDate);
            this.isReleaseDateSpecified=true;
        } catch (ParseException e) {
            this.isReleaseDateSpecified=false;
        }
        //let's assume that mvies with vote_average>7 are popular

     /* if(this.voteAverage>7.0)
      {
          this.movieType=MovieType.POPULAR;
      }else
      {
          this.movieType=MovieType.NONPOPULAR;
      }*/

      //movieType=MovieType.NONPOPULAR;


    }

    public static ArrayList<Movie> fromJSONArray(JSONArray movieJSONArray)
    {
        ArrayList<Movie> results = new ArrayList<>();
        for(int x=0;x<movieJSONArray.length();x++)
        {
            try {
                results.add(new Movie(movieJSONArray.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    public MovieType getMovieType()
    {
        //return MovieType.POPULAR;
           return (this.voteAverage>=7)?MovieType.POPULAR:MovieType.NOPOPULAR;
    }
}
