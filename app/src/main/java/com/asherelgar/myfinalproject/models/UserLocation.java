package com.asherelgar.myfinalproject.models;

/**
 * Created by asherelgar on 29.6.2017.
 */

public class UserLocation {
    private double lat;
    private double lon;
    private String key;

    public UserLocation(double lat, double lon, String key) {
        this.lat = lat;
        this.lon = lon;
        this.key = key;
    }

    public UserLocation() {
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "UserLocation{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", key='" + key + '\'' +
                '}';
    }
}
