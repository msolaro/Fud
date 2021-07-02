package com.example.fud.Models;

/**
 * This class is a model for the user
 */
public class ChatModel {

    public boolean sent;
    public String owner;
    public String message;

    public ChatModel(String owner, String message, boolean sent) {
        super();
        this.owner = owner;
        this.message = message;
        this.sent = sent;
    }

    /**
     * Returns the chat's message
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message of the ChatModel
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the chat's message
     * @return owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets the message of the ChatModel
     * @param owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

}