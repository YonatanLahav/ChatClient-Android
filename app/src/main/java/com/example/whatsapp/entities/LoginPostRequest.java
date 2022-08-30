package com.example.whatsapp.entities;

public class LoginPostRequest {
    private String Id;
    private String Password;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public LoginPostRequest(String id, String password) {
        Id = id;
        Password = password;
    }
}
