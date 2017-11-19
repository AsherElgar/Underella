package com.asherelgar.myfinalproject.models;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by asherelgar on 19.6.2017.
 */

//Firebase Objects: POJO
//1)must have empty constructor
//2)Getter ans Setter for all properties

public class User {
    private String name;
    private String email;
    private String photoUrl;

    public User() {
    }

    public User(FirebaseUser user) {
        this.name = user.getDisplayName();
        this.email = user.getEmail();
//        this.photoUrl = String.valueOf(user.getPhotoUrl());

        Uri photoUrl = user.getPhotoUrl();
        if (photoUrl == null) {
            this.photoUrl = "https://firebasestorage.googleapis.com/v0/b/myfinalproject-56d11.appspot.com/o/default_avatar.png?alt=media&token=d9f1a6a8-2bbc-40ad-b43c-003986ab98bd";
        } else
            this.photoUrl = photoUrl.toString();
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
                '}';
    }
}
