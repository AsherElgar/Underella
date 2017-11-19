package com.asherelgar.myfinalproject.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asherelgar on 19.6.2017.
 */

public class ShoppingList implements Parcelable {
    public static final Creator<ShoppingList> CREATOR = new Creator<ShoppingList>() {
        @Override
        public ShoppingList createFromParcel(Parcel source) {
            return new ShoppingList(source);
        }

        @Override
        public ShoppingList[] newArray(int size) {
            return new ShoppingList[size];
        }
    };
    //    private String title;
//    private String owner;
//    private String updaeTime;
//    private String profileImage;
    private String listID;

    public ShoppingList(String listID) {
//        this.title = title;
//        this.owner = owner;
//        this.updaeTime = updaeTime;
//        this.profileImage = profileImage;
        this.listID = listID;
    }

    public ShoppingList() {
    }

//    public String getProfileImage() {
//        return profileImage;
//    }

//    public void setProfileImage(String profileImage) {
//        this.profileImage = profileImage;
//    }

    protected ShoppingList(Parcel in) {
//        this.title = in.readString();
//        this.owner = in.readString();
//        this.updaeTime = in.readString();
//        this.profileImage = in.readString();
        this.listID = in.readString();
    }

//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getOwner() {
//        return owner;
//    }
//
//    public void setOwner(String owner) {
//        this.owner = owner;
//    }
//
//    public String getUpdaeTime() {
//        return updaeTime;
//    }
//
//    public void setUpdaeTime(String updaeTime) {
//        this.updaeTime = updaeTime;
//    }

    public String getListID() {
        return listID;
    }

    public void setListID(String listID) {
        this.listID = listID;
    }

    @Override
    public String toString() {
        return "ShoppingList{" +
//                "title='" + title + '\'' +
//                ", owner='" + owner + '\'' +
//                ", updaeTime='" + updaeTime + '\'' +
//                ", profileImage='" + profileImage + '\'' +
                ", listID='" + listID + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(this.title);
//        dest.writeString(this.owner);
//        dest.writeString(this.updaeTime);
//        dest.writeString(this.profileImage);
        dest.writeString(this.listID);
    }
}
