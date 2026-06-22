package com.example.carelanka;

/**
 * Data model for chat messages in CareLanka AI.
 */
public class ChatMessage {
    private String text;
    private boolean isUser;
    private String timestamp;
    private boolean isLoading;

    public ChatMessage(String text, boolean isUser, String timestamp) {
        this.text = text;
        this.isUser = isUser;
        this.timestamp = timestamp;
        this.isLoading = false;
    }

    public ChatMessage(boolean isLoading) {
        this.isLoading = isLoading;
        this.isUser = false;
    }

    public String getText() { return text; }
    public boolean isUser() { return isUser; }
    public String getTimestamp() { return timestamp; }
    public boolean isLoading() { return isLoading; }
}
