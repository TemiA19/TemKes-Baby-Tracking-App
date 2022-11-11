package com.mobileapp.temkesbabytrackingapp;

public class Users {
    String uid;
    String name;
    String email;
    String imageUri;
    String password;

    public Users() {

    }

    public Users(String uid, String name, String email, String imageUri, String password) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.imageUri = imageUri;
        this.password = password;
      }

    public Users(String nameUpdate, String emailUpdate, String passwordUpdate) {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
