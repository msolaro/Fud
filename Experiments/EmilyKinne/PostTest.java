package com.example.fud;

import com.example.fud.Fragments.PantryFragment;
import com.example.fud.Models.Pantry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void postResponse_True() throws JSONException {

        PantryFragment test = mock(PantryFragment.class);
        PostRequest success = new PostRequest();

        JSONArray result = new JSONArray();
        result.put(new Pantry("ham", "meat", 1, "3/17/2020"));
        result.put(new Pantry("cucumber", "vegetable", 3, "3/20/2020"));

        String sessionKey = "9a0b84q3bq38f7basdyg2gvh384b551"; // user's correct session key

        //We are expecting a JSONObject response from the server
        JSONObject response = new JSONObject();

        //Simulated response from the server with a valid session key
        response.put("status", true);
        response.put("food", result);

        //requestItems method from PantryFragment that has not been tested
        when(test.requestItems(sessionKey)).thenReturn(response);

        Assert.assertEquals(success.tryPost(sessionKey), response.getBoolean("status"));
        Assert.assertEquals(success.getPost(sessionKey), response.getJSONArray("food"));
    }
    @Test
    public void postResponse_False() throws JSONException {

        PantryFragment test = mock(PantryFragment.class);
        PostRequest failure = new PostRequest();

        JSONArray result = new JSONArray();
        result.put(null);

        String sessionKey = "null";

        //We are expecting a JSONObject response from the server
        JSONObject response = new JSONObject();

        //Simulated response from the server is a JSONObject with an invalid sessionKey
        response.put("status", new Boolean (false));
        response.put("food", result);

        //requestItems method from PantryFragment that has not been tested
        when(test.requestItems(sessionKey)).thenReturn(response);

        Assert.assertEquals(failure.tryPost(sessionKey), response.getBoolean("status"));
        Assert.assertEquals(failure.getPost(sessionKey), response.getJSONArray("food"));
    }


}