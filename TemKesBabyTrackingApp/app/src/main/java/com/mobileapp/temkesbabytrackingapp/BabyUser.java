package com.mobileapp.temkesbabytrackingapp;

public class BabyUser {

    String uid;
    String babyName;
    String babyWeight;
    String babyGender;
    String babyBirthday;
    String imageUri;

    public BabyUser(String uid, String babyName, String babyWeight, String babyGender, String babyBirthday, String imageUri) {
        this.uid = uid;
        this.babyName = babyName;
        this.babyWeight = babyWeight;
        this.babyGender = babyGender;
        this.babyBirthday = babyBirthday;
        this.imageUri = imageUri;
    }

    public BabyUser() {

    }

    public BabyUser(String uid, String babyName, String babyBirthday, String babyGender, String babyWeight) {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBabyName() {
        return babyName;
    }

    public void setBabyName(String babyName) {
        this.babyName = babyName;
    }

    public String getBabyWeight() {
        return babyWeight;
    }

    public void setBabyWeight(String babyWeight) {
        this.babyWeight = babyWeight;
    }

    public String getBabyGender() {
        return babyGender;
    }

    public void setBabyGender(String babyGender) {
        this.babyGender = babyGender;
    }

    public String getBabyBirthday() {
        return babyBirthday;
    }

    public void setBabyBirthday(String babyBirthday) {
        this.babyBirthday = babyBirthday;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
