package websocket;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 
 * @author msolaro
 * @author rlspick
 * 
 * Web Socket implementation for Server
 * Acts as a controller for FrontEnd 
 *
 */

@ServerEndpoint("/websocket/{username}")
@Component
public class WebSocketServer {
	
    private static Map<Session, String> sessionUsernameMap = new HashMap<>();
    private static Map<String, Session> usernameSessionMap = new HashMap<>();
    
    private final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    
    /**
     * Adds user to chat group/ Opens a new session
     * 
     * @param session Session
     * @param username To Add
     * @throws IOException
     */
    @OnOpen
    public void onOpen(
    	      Session session, 
    	      @PathParam("username") String username) throws IOException 
    {
        logger.info("Entered into Open");
        
        sessionUsernameMap.put(session, username);
        usernameSessionMap.put(username, session);
        
        String message="User:" + username + " has Joined the Chat";
        	broadcast(message);
		
    }
 
    /**
     * Send a new message
     * 
     * @param session Session
     * @param message To Send
     * @throws IOException
     */
    @OnMessage
    public void onMessage(Session session, String message) throws IOException 
    {
    	logger.info("Entered into Message: Got Message:"+message);
    	String username = sessionUsernameMap.get(session);
    	
    	if (message.startsWith("@"))
    	{
    		String destUsername = message.split(" ")[0].substring(1);
    		sendMessageToPArticularUser(destUsername, "[DM] " + username + ": " + message);
    		sendMessageToPArticularUser(username, "[DM] " + username + ": " + message);
    	}
    	else
    	{
	    	broadcast(username + ": " + message);
    	}
    }
 
    /**
     * Closes connection for user
     * 
     * @param session To Close
     * @throws IOException
     */
    @OnClose
    public void onClose(Session session) throws IOException
    {
    	logger.info("Entered into Close");
    	
    	String username = sessionUsernameMap.get(session);
    	sessionUsernameMap.remove(session);
    	usernameSessionMap.remove(username);
        
    	String message= username + " disconnected";
        broadcast(message);
    }
 
    /**
     * Error reporting
     * 
     * @param session Current Session
     * @param throwable
     */
    @OnError
    public void onError(Session session, Throwable throwable) 
    {
    	logger.info("Entered into Error");
    }
    
    /**
     * Send a message to a specific user
     * 
     * @param username Recipient 
     * @param message To Send
     */
	private void sendMessageToPArticularUser(String username, String message) 
    {	
    	try {
    		usernameSessionMap.get(username).getBasicRemote().sendText(message);
        } catch (IOException e) {
        	logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();
        }
    }
    
	/**
	 * Broadcast chat message to chat group 
	 * 
	 * @param message To Send
	 * @throws IOException
	 */
    private static void broadcast(String message) 
    	      throws IOException 
    {	  
    	sessionUsernameMap.forEach((session, username) -> {
    		synchronized (session) {
	            try {
	                session.getBasicRemote().sendText(message);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    });
	}
}

