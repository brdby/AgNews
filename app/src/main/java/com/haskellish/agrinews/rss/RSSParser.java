package com.haskellish.agrinews.rss;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RSSParser {

    private List<News> news;

    public RSSParser(String urlLink){
        try {
            URL url = new URL(urlLink);
            InputStream inputStream = url.openConnection().getInputStream();
            news = parse(inputStream);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private ArrayList<News> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            while (parser.nextTag() !=  XmlPullParser.END_TAG && !parser.getName().equals("channel"));
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private ArrayList<News> readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {
        ArrayList<News> entries = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, null, null);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("item")) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private News readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "item");

        //required fields
        String title = "";
        String description = "";
        String link = "";
        String image_url = "";
        ArrayList<String> categories = new ArrayList<>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readContent(parser);
            } else if (name.equals("description")) {
                description = readContent(parser);
            } else if (name.equals("link")) {
                link = readContent(parser);
            } else if (name.equals("category")) {
                categories.add(readContent(parser));
            } else if (name.equals("enclosure") && parser.getAttributeValue(null, "type").contains("image")) {
                image_url = parser.getAttributeValue(null, "url");
                readContent(parser);
            } else {
                skip(parser);
            }
        }
        return new News(title, description, link, image_url, categories);
    }

    private String readContent(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, null);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, null);
        return title;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }



    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    public List<News> getNews() {
        return news;
    }
}
