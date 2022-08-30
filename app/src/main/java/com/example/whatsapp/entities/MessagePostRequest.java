package com.example.whatsapp.entities;

public class MessagePostRequest {
    private String content;

    public MessagePostRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
