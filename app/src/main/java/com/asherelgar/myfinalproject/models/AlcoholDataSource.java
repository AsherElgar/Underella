package com.asherelgar.myfinalproject.models;

import android.util.Log;

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

public class AlcoholDataSource {

    public static void getAlcohol(final OnAlcoholArrivedListener listener) {
        //1) no networking on the UI Thread-->
        // Let's work on Thread
        ExecutorService service = Executors.newSingleThreadExecutor();

        service.execute(new Runnable() {
            @Override
            public void run() {
                //code that run in the background

                try {
                    String xml = IO.getWebsite("http://www.ynet.co.il/Integration/StoryRss975.xml", "Windows-1255");
                    Log.d("Hackeru", xml);
//                    String url = "https://drinking.co.il/feed/";
//                    SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(url)));
//

                    List<Alcohol> data = parse(xml);

//                    List<SyndEntry> entries =  feed.getEntries();

//                    for (SyndEntry entry : entries) {
//                        String title = entry.getTitle();
//                        String description = entry.getDescription() + "";
//                        String fullDescrip = entry.getComments();
//                        String link = entry.getLink();
//
//
//                        Alcohol alcohol = new Alcohol(title, description, fullDescrip, link);
//                        data.add(alcohol);
//
//                    }

//                    String description = feed.getDescription();
//                    List<Alcohol> data = parse(url);
//                    Log.d("Hackeru", data.toString());
                    //notify the listener
                    listener.onAlcoholArrived(data, null);
                } catch (Exception e) {
                    listener.onAlcoholArrived(null, e);
                }
            }
        });

        service.shutdown();
    }

    private static List<Alcohol> parse(String xml) {
        List<Alcohol> data = new ArrayList<>();
        Document document = Jsoup.parse(xml);
        Elements items = document.getElementsByTag("item");

        for (Element item : items) {
            String title = item.getElementsByTag("title").first().text().replace("<![CDATA[", "").replace("]]>", "");

            String descriptionHTML = item.getElementsByTag("description").first().text();

            Document descriptionElement = Jsoup.parse(descriptionHTML);
            String link = descriptionElement.getElementsByTag("a").attr("href");
            String src = descriptionElement.getElementsByTag("img").attr("src");
            String content = descriptionElement.text().replace("<![CDATA[", "").replace(";]", "");

            if (src == null) {
                src = "https://www.eatrightontario.ca/EatRightOntario/media/Website-images-resized/Alcohol-v-2-resized.jpg";
            }
            Alcohol alcohol = new Alcohol(title, link, src, content);
            data.add(alcohol);
        }

        return data;
    }


    public interface OnAlcoholArrivedListener {
        void onAlcoholArrived(List<Alcohol> data, Exception e);
    }

    public static class Alcohol {
        private String title;
        private String url;
        private String thumbnail;
        private String content;

        public Alcohol(String title, String url, String thumbnail, String content) {
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
