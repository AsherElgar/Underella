package com.asherelgar.myfinalproject.models;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by asherelgar on 4.7.2017.
 */

public class User2 {
    private String name;
    private String email;
    private String photoUrl;
    private String userID;

    public User2() {
    }

    public User2(FirebaseUser user) {
        this.name = user.getDisplayName();
        this.email = user.getEmail();
//        this.photoUrl = String.valueOf(user.getPhotoUrl());
        this.userID = user.getUid();
        Uri photoUrl = user.getPhotoUrl();
        if (photoUrl == null) {
            this.photoUrl = "https://firebasestorage.googleapis.com/v0/b/shopee-c946a.appspot.com/o/default_avatar.png?alt=media&token=1bb47198-9680-439a-a35e-f927c7c627ac";
        } else
            this.photoUrl = photoUrl.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", userID='" + userID + '\'' +
                '}';
    }
}
