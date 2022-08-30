package com.example.whatsapp.entities;


import androidx.room.PrimaryKey;

public class ApiMessage {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String content;
    private String created;
    private boolean sent;

    public ApiMessage(String content, String created, boolean sent) {
        this.content = content;
        this.created = created;
        this.sent = sent;
    }

    public int get_id() {
        return id;
    }

    public void set_id(int _id) {
        this.id = _id;
    }

    public String get_content() {
        return content;
    }

    public void set_content(String _content) {
        this.content = _content;
    }

    public String get_created() {
        return created;
    }

    public void set_created(String _created) {
        this.created = _created;
    }

    public boolean is_sent() {
        return sent;
    }

    public void set_sent(boolean _sent) {
        this.sent = _sent;
    }
}
