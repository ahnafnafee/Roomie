package com.example.roomieprototype.messages.Model;

public class User {

    private String id;
    private String email;
    private String fullname;
    private String imageURL;
    private String status;


    public User(String id, String email, String fullname, String imageURL, String status) {
        this.id = id;
        this.email = email;
        this.fullname = fullname;
        this.imageURL = imageURL;
        this.status = status;
    }

    public User() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
