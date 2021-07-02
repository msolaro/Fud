package com.example.fud;

import org.java_websocket.client.WebSocketClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WebsocketTest {

    @Test
    public void verifyCallToIsOpenConnection() {
        WebSocketClient session = mock(WebSocketClient.class);
        String textMsg = "Test Message";

        when(session.isOpen()).thenReturn(true);

        //verify(session, times(1));
    }
}
