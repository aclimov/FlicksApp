package com.codepath.flicksapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alex_ on 3/9/2017.
 */

public class Trailer {


    public String getYoutubeId() {
        return key;
    }
    public String getType()
    {
        return name;
    }

    String key;
    String name;
    public Trailer(JSONObject jsonObject) throws JSONException
    {
        this.key = jsonObject.getString("key");
        this.name = jsonObject.getString("name");
    }

    public static ArrayList<Trailer> fromJSONArray(JSONArray trailerJSONArray)
    {
        ArrayList<Trailer> results = new ArrayList<>();
        for(int x=0;x<trailerJSONArray.length();x++)
        {
            try {
                results.add(new Trailer(trailerJSONArray.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }


}
