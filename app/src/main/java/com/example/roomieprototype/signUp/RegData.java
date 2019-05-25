package com.example.roomieprototype.signUp;

public class RegData {

    private String fullname, username, email;

    public RegData(String fullname, String email) {
        this.fullname = fullname;
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }
}
