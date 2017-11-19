package com.asherelgar.myfinalproject.models;

import java.util.List;

/**
 * Created by asherelgar on 16.6.2017.
 */

public class YouTubePlaylistData {
    private String songName;
    private String url;
    private String img;

    public YouTubePlaylistData(String favorite, String url, String duration) {
        this.songName = favorite;
        this.url = url;
        this.img = duration;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getFavorite() {
        return songName;
    }

    public void setFavorite(String favorite) {
        this.songName = favorite;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "YouTubeData{" +
                "favorite='" + songName + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public interface OnPlaylistArrived {
        void onPlaylistArrived(List<YouTubePlaylistData> data, Exception e);
    }

}
