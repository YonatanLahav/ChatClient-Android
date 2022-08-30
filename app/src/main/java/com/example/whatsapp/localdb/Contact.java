package com.example.whatsapp.localdb;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Contact {

    @PrimaryKey(autoGenerate =  true)
    private int id;

    private String connectedUser;
    private String contactId;
    private String contactName;
    private String contactServer;
    private String last;
    private String lastDate;

    public Contact(String connectedUser, String contactId, String contactName, String contactServer, String last, String lastDate) {
        this.connectedUser = connectedUser;
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactServer = contactServer;
        this.last = last;
        this.lastDate = lastDate;
    }

    public int getId() {
        return id;
    }

    public String getConnectedUser() {
        return connectedUser;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setConnectedUser(String connectedUser) {
        this.connectedUser = connectedUser;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactServer() {
        return contactServer;
    }

    public void setContactServer(String contactServer) {
        this.contactServer = contactServer;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }
}
