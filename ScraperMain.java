/**
 * Created by matthew on 19/10/15.
 */
public class ScraperMain {
    private static int x;
    static int itemNumber = x;

    public static void main(String[] args) {
        WebScraper whatson = new WebScraper();

        System.out.println("stage one");
        if (whatson.connect()==1) {
            whatson.getLinks();
            for(int o=0;o<whatson.returnLinks().size();o++) {
                System.out.println(whatson.returnLinks().get(o));
            }

            System.out.println("stage two");
            for(x=0;x<whatson.returnLinks().size();x++) {
                System.out.println("--------------------------------------------------------------");
                whatson.getTitles(whatson.getEvent(x),x);
                whatson.getInfo(whatson.getEvent(x),x);
                whatson.getDesc(whatson.getEvent(x),x);
                System.out.println("loop complete, starting next loop");
                System.out.println("x in main loop: "+x+" ---------------------");
                System.out.println("--------------------------------------------------------------");
            }
            whatson.getImages(itemNumber);

            System.out.println("stage three");
            System.out.println("Events: "+whatson.returnLinks().size());
            System.out.println("Images: "+whatson.returnImages().size());
            System.out.println("Descriptions: "+whatson.returnDescs().size());
            System.out.println("Titles: "+whatson.returnTitles().size());
            System.out.println("Infos: "+whatson.returnInfos().size());
        }
    }
}
