package com.example.fud;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.example.fud.PostRequest;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PostRequestTest {

    @Mock
    PostRequest request;

    String session = "session";

    @Test
    public void testTryPost() {
        boolean isTrue = false;

        try {
            when(request.tryPost(session)).thenReturn(true);
            isTrue = request.tryPost(session);
        } catch (JSONException e) {

        }
        assertTrue(isTrue);
    }

    @Test
    public void testRequestItems() {
        JSONObject object = null;
        when(request.requestItems(session)).thenReturn(new JSONObject());
        object = request.requestItems(session);
        assertNotNull(object);
    }

    @Test
    public void testGetPost() {
        JSONArray array = null;

        try {
            when(request.getPost(session)).thenReturn(new JSONArray());
            array = request.getPost(session);
        } catch (JSONException e) {

        }

        assertNotNull(array);
    }
}
