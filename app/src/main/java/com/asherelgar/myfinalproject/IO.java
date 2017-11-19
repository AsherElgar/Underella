package com.asherelgar.myfinalproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;


public class IO {

    public static String getWebsite(String url) throws IOException {

        return getWebsite(url, "utf-8");
    }

    public static String getWebsite(String url, String charset) throws IOException {
        URL address = new URL(url);
        URLConnection con = address.openConnection();
        InputStream in = con.getInputStream();

        return IO.getString(in, charset);
    }

    public static String getString(InputStream in) throws IOException {

        return getString(in, "utf-8");
    }

    public static String getString(InputStream in, String charset) throws IOException {
        StringBuilder builder = new StringBuilder();
        String line = null;//Empty line for the loop.

        BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } finally {
            reader.close();
        }

        return builder.toString();
    }

    public static String getString2(String in) throws IOException {
        StringBuilder builder = new StringBuilder();
        String line = null;//Empty line for the loop.

        BufferedReader reader = new BufferedReader(new StringReader(in));
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } finally {
            reader.close();
        }

        return builder.toString();
    }
}
