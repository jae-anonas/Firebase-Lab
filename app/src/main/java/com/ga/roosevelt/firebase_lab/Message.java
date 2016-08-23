package com.ga.roosevelt.firebase_lab;

/**
 * Created by roosevelt on 8/23/16.
 */
public class Message {
    String username;
    String message;
    String color;

    public Message() {
    }

    public Message(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public Message(String username, String message, String color) {
        this.username = username;
        this.message = message;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
