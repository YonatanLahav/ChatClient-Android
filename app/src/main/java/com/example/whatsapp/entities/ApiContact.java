package com.example.whatsapp.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ApiContact {
    @PrimaryKey
    private String id;
    private String name;
    private String server;
    private String last;
    private String lastdate;

    public ApiContact(String id, String name, String server, String last, String lastdate) {
        this.id = id;
        this.name = name;
        this.server = server;
        this.last = last;
        this.lastdate = lastdate;
    }

    public ApiContact(ContactsPostRequest contactsPostRequest) {
        this.id = contactsPostRequest.getId();
        this.name = contactsPostRequest.getName();
        this.server = contactsPostRequest.getServer();
        this.last = "";
        this.lastdate = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getLastdate() {
        return lastdate;
    }

    public void setLastdate(String lastdate) {
        this.lastdate = lastdate;
    }
}
