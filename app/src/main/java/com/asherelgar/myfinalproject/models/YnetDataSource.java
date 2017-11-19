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
 * Created by asherelgar on 5.6.2017.
 */

public class YnetDataSource {

    public static void getYnet(final OnYnetArrivedListener listener) {
        //1) no networking on the UI Thread-->
        // Let's work on Thread
        ExecutorService service = Executors.newSingleThreadExecutor();

        service.execute(new Runnable() {
            @Override
            public void run() {
                //code that run in the background

                try {
                    String xml = IO.getWebsite("http://www.ynet.co.il/Integration/StoryRss2.xml", "Windows-1255");
                    List<Ynet> data = parse(xml);
                    //notify the listener
                    listener.onYnetArrived(data, null);
                } catch (Exception e) {
                    listener.onYnetArrived(null, e);
                }
            }
        });

        service.shutdown();
    }

    private static List<Ynet> parse(String xml) {
        List<Ynet> data = new ArrayList<>();
        Document document = Jsoup.parse(xml);
        Elements items = document.getElementsByTag("item");

        for (Element item : items) {
            String descriptionHTML = item.getElementsByTag("description").first().text();
            String title = item.getElementsByTag("title").first().text().replace("<![CDATA[", "").replace("]]>", "");

            Document descriptionElement = Jsoup.parse(descriptionHTML);
            String link = descriptionElement.getElementsByTag("a").attr("href");
            String src = descriptionElement.getElementsByTag("img").attr("src");
            String content = descriptionElement.text();
            Ynet ynet = new Ynet(title, link, src, content);
            data.add(ynet);
        }

        return data;
    }

    public interface OnYnetArrivedListener {
        void onYnetArrived(List<Ynet> data, Exception e);
    }

    public static class Ynet {
        private String title;
        private String url;
        private String thumbnail;
        private String content;

        public Ynet(String title, String url, String thumbnail, String content) {
            this.title = title;
            this.url = url;
            this.thumbnail = thumbnail;
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public String getContent() {
            return content;
        }

        @Override
        public String toString() {
            return "Alcohol{" +
                    "title='" + title + '\'' +
                    ", url='" + url + '\'' +
                    ", thumbnail='" + thumbnail + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }
}
