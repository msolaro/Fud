package com.example.fud;

import com.android.volley.RequestQueue;
import com.example.fud.Controller.AppController;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AppControllerTest {

    @Mock
    AppController controller;

    private RequestQueue mRequestQueue;

    @Test
    public void testRequestQueue() {
        assertNull(mRequestQueue);
        when(controller.getRequestQueue()).thenReturn(mock(RequestQueue.class));
        mRequestQueue = controller.getRequestQueue();
        assertNotNull(mRequestQueue);
    }
}
