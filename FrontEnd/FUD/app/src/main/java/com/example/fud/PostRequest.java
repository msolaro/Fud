package com.example.fud;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is responsible for managing post requests
 */
public class PostRequest {

    JSONObject jObj;

    /**
     * Gets a user's pantry when they first login
     */
    public JSONObject requestItems(String session){
        return jObj;
    }

    /**
     * This function attempts to make a post request. Upon success, the function returns true, and upon failure, the function returns false.
     *
     * @param session
     * @return Post request success
     * @throws JSONException
     */
    public boolean tryPost(String session) throws JSONException {
        if (requestItems(session).getBoolean("status")) {
            return true;
        }
        return false;
    }

    /**
     * This function returns the
     *
     * @param session
     * @return pantry.requestItems(session).getJSONArray("food")
     * @throws JSONException
     */
    public JSONArray getPost(String session) throws JSONException {
        if (requestItems(session).getBoolean("status")) {
            return requestItems(session).getJSONArray("food");
        }
        return requestItems(session).getJSONArray("food");
    }

}
