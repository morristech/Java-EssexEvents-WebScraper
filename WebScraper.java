package com.matthew.whatson;

/**
 * Created by matthew on 12/10/15.
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class WebScraper {

    //Creating a global variable
    private static int x;

    //Creating ArrayLists
    private static ArrayList<String> eventLinks = new ArrayList<String>();
    private static ArrayList<String> eventTitles = new ArrayList<String>();
    private static ArrayList<String> eventInfos = new ArrayList<String>();
    private static ArrayList<String> eventImages = new ArrayList<String>();
    private static ArrayList<String> eventDescs = new ArrayList<String>();

    //Attempting Initial Connection
    private int connect(){
        try {
            Document connection = Jsoup.connect("http://www.essexstudent.com/whatson/").get();
            System.out.println(">> Connection Successful");
            return 1;
        } catch (IOException ex) {
            System.out.println(">> Connection Failed");
            return 0;
        }
    }

    //Retrieving initial links from http://www.essexstudent.com/whatson/ of all events
    private void getLinks() {
        System.out.println(">> Retrieving Links At: http://www.essexstudent.com/whatson/");
        try {
            Document allEvents = Jsoup.connect("http://www.essexstudent.com/whatson/").get();

            Elements rawLink = allEvents.select("a.msl_event_name");

            for(Element e: rawLink) {
                String link = "http://www.essexstudent.com"+e.attr("href");
                eventLinks.add(link);
            }

            System.out.println(">> Links Successfully Received");

        } catch (IOException ex) {
            System.out.println(">> Connection Error: WebPage may not exist");
        }
    }

    //Connecting to individual events
    private Document getEvent() {
        try {
            Document Event = Jsoup.connect(eventLinks.get(x)).get();

            return Event;

        } catch (Exception ex) {
            if (ex instanceof NullPointerException) {
                x=x-1;
                return null;
            } else {
                System.out.println(">> Connection Error: WebPage may not exist");
                return null;
            }
        }
    }

    //For loop causes scraper to run infinitely
    //Retrieve Images For Each Link
    private void getImages(Document connection) {
        try {
            //Element correctImage = connection.getElementById("ctl00_ctl22_imgBanner");
            Element correctImage = connection.getElementById("msl_event");
            String image = correctImage.attr("src");
            eventImages.add(image);
        } catch (Exception ex) {
            if (ex instanceof NullPointerException) {
                x--;
            } else {
                System.out.println(">> Error Retrieving Image");
            }
        }
    }

    //Retrieve the Titles for each event
    private void getTitles(Document connection) {
        Elements rawTitle = connection.select("h1.header");

        for(Element e: rawTitle) {
            String title = e.text();
            eventTitles.add(title);
        }
    }

    //Retrieve the informatio nabout each event - date, time and location
    private void getInfo(Document connection) {
        Elements rawInfo = connection.select("h2");

        for(Element e: rawInfo) {
            String info = e.text();
            eventInfos.add(info);
        }
    }

    //Retrieve the description of each event
    private void getDesc(Document connection) {
        Elements rawDesc = connection.select("p");
        String desc = "";

        for(Element e: rawDesc) {
            desc += e.text();
        }
        eventDescs.add(desc);
    }

    //Display the eventLinks array
    private void displayLinks() {
        for(int i=0;i<eventLinks.size();i++) {
            System.out.println("Event Link: "+eventLinks.get(i));
        }
    }

    //Display the eventTitles array
    private void displayTitles() {
        for(int i=0;i<eventTitles.size();i++) {
            System.out.println("Event Title: "+eventTitles.get(i));
        }
    }

    //Display the eventImages array
    private void displayImages() {
        for(int i=0;i<eventImages.size();i++) {
            System.out.println("Event Image: "+eventImages.get(i));
        }
    }

    //Display the eventInfos array
    private void displayInfos() {
        for(int i=0;i<eventInfos.size();i++) {
            System.out.println("Event Info: "+eventInfos.get(i));
        }
    }

    //Display the eventDescs array
    private void displayDescs() {
        for(int i=0;i<eventDescs.size();i++) {
            System.out.println("Event Desc: "+eventDescs.get(i));
        }
    }

    //Test main to fill arrays and check their contents
    public static void main(String[] args) {
        WebScraper whatson = new WebScraper();

        System.out.println("stage one");
        whatson.connect();
        whatson.getLinks();

        System.out.println("stage two");
        for(x=1;x<eventLinks.size();x++) {
            whatson.getTitles(whatson.getEvent());
            whatson.getImages(whatson.getEvent());
            whatson.getInfo(whatson.getEvent());
            whatson.getDesc(whatson.getEvent());
            System.out.println("loop complete, starting next loop");
        }

        System.out.println("stage three");
        whatson.displayLinks();
        whatson.displayImages();
        whatson.displayTitles();
        whatson.displayDescs();
        whatson.displayInfos();

        System.out.println("stage four");
        System.out.println("Events: "+eventLinks.size());
        System.out.println("Images: "+eventImages.size());
        System.out.println("Descriptions: "+eventDescs.size());
        System.out.println("Titles: "+eventTitles.size());
        System.out.println("Infos: "+eventInfos.size());
    }
}
