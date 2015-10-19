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

    //Creating ArrayLists
    private static ArrayList<String> eventLinks = new ArrayList<String>();
    private static ArrayList<String> eventTitles = new ArrayList<String>();
    private static ArrayList<String> eventInfos = new ArrayList<String>();
    private static ArrayList<String> eventImages = new ArrayList<String>();
    private static ArrayList<String> eventDescs = new ArrayList<String>();
    private static ArrayList<Boolean> imageChecks = new ArrayList<Boolean>();
    private static ArrayList<String> tempImages = new ArrayList<String>();


    //Attempting Initial Connection
    public int connect(){
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
    public void getLinks() {
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
    public Document getEvent(int x) {
        try {
            Document Event = Jsoup.connect(eventLinks.get(x)).get();

            return Event;

        } catch (Exception ex) {
            if (ex instanceof NullPointerException) {
                //do nothing
                return null;
            } else {
                System.out.println(">> Connection Error: WebPage may not exist");
                return null;
            }
        }
    }

    //Replace 50 with a variable all for loops can work off of,
    //might not always be 50 events on.
    //Retrieve Images For Each Link
    public void getImages(int itemNumber) {
        try {

            Elements span;
            Document connection = Jsoup.connect("http://www.essexstudent.com/whatson/").get();
            Elements imageFinder = connection.select("span.msl_event_image");
            int tempInt = 0;

            for (int n=0;n<50;n++) {
                itemNumber++;

                if ((itemNumber % 2) == 0) {
                    span = connection.select("div.event_item.item" + itemNumber + ".itemEven > dl > dt > a > span.msl_event_image");
                } else {
                    span = connection.select("div.event_item.item" + itemNumber + ".itemOdd > dl > dt > a > span.msl_event_image");
                }

                String check = span.toString();

                if (check.matches(".*\\w.*")) {
                    imageChecks.add(true);
                } else {
                    imageChecks.add(false);
                }
            }

            for (Element e: imageFinder) {
                Elements imageTag = e.select("img");
                String allImages = imageTag.attr("src");
                tempImages.add(allImages);
            }

            for (int n=0;n<imageChecks.size();n++) {
                if (imageChecks.get(n)) {
                    eventImages.add(tempImages.get(tempInt));
                    tempInt++;
                } else {
                    Document eventPage = Jsoup.connect(eventLinks.get(n)).get();
                    Elements defaultImage = eventPage.select("#ctl00_ctl22_imgBanner");
                    String imageURL = defaultImage.attr("src");
                    eventImages.add(imageURL);
                }
            }

        } catch (Exception ex) {
            if (ex instanceof NullPointerException) {
                //do nothing
            } else {
                System.out.println(">> Error Retrieving Image");
            }
        }
    }

    //Delete 'X' in argument and commment
    //Retrieve the Titles for each event
    public void getTitles(Document connection,int x) {
        Elements rawTitle = connection.select("h1.header");

        for(Element e: rawTitle) {
            String title = e.text();
            eventTitles.add(title);
        }

        System.out.println("title: "+x+"//////////");
    }

    //Delete 'X' in argument and commment
    //Retrieve the information about each event - date, time and location
    public void getInfo(Document connection,int x) {
        Elements rawInfo = connection.select("h2");
        String info = "";

        for(Element e: rawInfo) {
            System.out.println("info x inside: "+x+"::::::::::::::::::::::::");
            System.out.println("Link: "+eventLinks.get(x));
            info += e.text()+" ";
            eventInfos.add(info);
        }
        System.out.println("info x outside: "+x+"]]]]]]]]]]]]]]]]]]");
    }

    //Delete 'X' in argument and commment
    //Retrieve the description of each event
    public void getDesc(Document connection,int x) {
        Elements rawDesc = connection.select("h3,p");
        String desc = "";

        for(Element e: rawDesc) {
            String check = e.text();
            if (check.equals("Tickets")) {
                //Do nothing
            } else if (check.matches(".*\\w.*")) {
                desc += e.text()+" ";
            }
        }
        System.out.println("desc int: "+x+" ....................");

        desc = desc.replace("Powered by MSL","");

        //Removing whitespaces - checking for ASCII characters
        if (desc.matches(".*\\w.*")) {
            eventDescs.add(desc);
        } else {
            desc = "More details for this event coming soon!, Watch this space!";
            eventDescs.add(desc);
        }
    }

    public ArrayList<String> returnLinks() {
        return eventLinks;
    }

    public ArrayList<String> returnImages() {
        return eventImages;
    }

    public ArrayList<String> returnTitles() {
        return eventTitles;
    }

    public ArrayList<String> returnInfos() {
        return eventInfos;
    }

    public ArrayList<String> returnDescs() {
        return eventDescs;
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
    /*public static void main(String[] args) {
        WebScraper whatson = new WebScraper();

        System.out.println("stage one");
        whatson.connect();
        whatson.getLinks();
        for(int o=0;o<eventLinks.size();o++) {
            System.out.println(eventLinks.get(o));
        }

        System.out.println("stage two");
        for(x=0;x<eventLinks.size();x++) {
            System.out.println("--------------------------------------------------------------");
            whatson.getTitles(whatson.getEvent());
            whatson.getInfo(whatson.getEvent());
            whatson.getDesc(whatson.getEvent());
            System.out.println("loop complete, starting next loop");
            System.out.println("x in main loop: "+x+" ---------------------");
            System.out.println("--------------------------------------------------------------");
        }
        whatson.getImages();

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
    }*/
}
