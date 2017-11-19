package com.asherelgar.myfinalproject.models;

import com.asherelgar.myfinalproject.IO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by asherelgar on 16.6.2017.
 */

public class YouTubeData {
    private String songName;
    private String url;
    private String img;

    public YouTubeData(String favorite, String url, String duration) {
        this.songName = favorite;
        this.url = url;
        this.img = duration;
    }

    public static void getYoutube(final OnYouTubeArrived listener, String channelLink) {

        final String linkSearch = "https://www.youtube.com/feeds/videos.xml?playlist_id=" + channelLink;
        //1) no networking on the UI Thread-->
        // Let's work on Thread
        final String link1 = "https://www.youtube.com/feeds/videos.xml?playlist_id=PL3oW2tjiIxvQ1BZS58qtot3-p-lD32oWT";
        ExecutorService service = Executors.newSingleThreadExecutor();


        service.execute(new Runnable() {
            @Override
            public void run() {
                //code that run in the background

                try {
                    String xml = IO.getWebsite(linkSearch);

                    List<YouTubeData> data = parse(xml);
                    //notify the listener
                    listener.onYoutubeArrived(data, null);
                } catch (Exception e) {
                    listener.onYoutubeArrived(null, e);
                }

            }
        });

        service.shutdown();
    }

    // PL3oW2tjiIxvQ1BZS58qtot3-p-lD32oWT

    private static List<YouTubeData> parse(String xml) {
        List<YouTubeData> data = new ArrayList<>();
        Document document = Jsoup.parse(xml);
        Elements items = document.getElementsByTag("entry");

        for (Element item : items) {
            String id = item.getElementsByTag("yt:videoId").first().text();
            String title = item.getElementsByTag("title").first().text();
            String image = item.getElementsByTag("media:thumbnail").attr("url");

            Document descriptionElement = Jsoup.parse(image);
            String link = descriptionElement.getElementsByTag("media:thumbnail").attr("url");

            String content = descriptionElement.text();
            YouTubeData tube = new YouTubeData(title, id, image);
            data.add(tube);
        }

        return data;
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

    public interface OnYouTubeArrived {
        void onYoutubeArrived(List<YouTubeData> data, Exception e);
    }

}
