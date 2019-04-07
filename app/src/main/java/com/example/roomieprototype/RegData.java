package com.example.roomieprototype;

public class RegData {

    private String fullname, username, email;

    public RegData(String fullname, String username, String email) {
        this.fullname = fullname;
        this.username = username;
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
