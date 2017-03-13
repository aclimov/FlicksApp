package com.codepath.flicksapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by alex_ on 3/12/2017.
 */

public class Actor {

    String character;
    String name;
    String profilePath;

    public Actor(JSONObject jsonObject) throws JSONException
    {
        character = jsonObject.getString("character");
        name= jsonObject.getString("name");
        profilePath= jsonObject.getString("profile_path");
    }

    public static ArrayList<Actor> fromJSONArray(JSONArray actorJSONArray)
    {
        ArrayList<Actor> results = new ArrayList<>();
        for(int x=0;x<actorJSONArray.length();x++)
        {
            try {
                results.add(new Actor(actorJSONArray.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
