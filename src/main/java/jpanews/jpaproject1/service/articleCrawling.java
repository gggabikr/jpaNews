package jpanews.jpaproject1.service;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class articleCrawling {

    String URL = "Please enter article's URL";
    Document doc;

    public boolean crawl(String url) {
        boolean result = false;

        if (url.contains("abcnews")) {
            result = abcNews(url);
        }
        if(url.contains("bbc.com")){
            result = bbcNews(url);
        }


        return result;
    }

    public boolean abcNews(String url){
        try {
            Connection conn = Jsoup.connect(url);
            doc = conn.get();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Something went wrong.");
            System.out.println("Please check the URL again.");
            return false;
        } finally {
            Elements title = doc.select("div > span > div > div > span > div > h1");
            Elements subTitle = doc.select("div>span>div>p");
            Elements body = doc.select("div > span > div > div > span > article > p");

            for (Element element : title) {
                System.out.println(element.text());
            }
            System.out.println(subTitle.get(1).text());

            for (Element element : body) {
                System.out.println(element.text());
            }
        }
        return true;
    }

    public boolean nyTimes(String url){
        //because it requires paying to see the articles,
        //this method will not be built.
        return false;
    }
    public boolean bbcNews(String url){
        try {
            Connection conn = Jsoup.connect(url);
            doc = conn.get();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Something went wrong.");
            System.out.println("Please check the URL again.");
            return false;
        } finally {
            Element title = doc.getElementById("main-heading");
            Elements body = doc.select("#main-content>div>div>div>article>div>div>p");

            System.out.println(title != null ? title.text() : null);
            for (Element element : body) {
                System.out.println(element.text());
            }
        }
        return true;
    }
//    public boolean nyPost(String url){}
//    public boolean msnNews(String url){}
//    public boolean washingtonPost(String url){}
//    public boolean cnnNews(String url){}
//    public boolean googleNews(String url){}
//    public boolean foxNews(String url){}
//    public boolean huffingtonPost(String url){}
//    public boolean nbcNews(String url){}




    public static void main(String[] args) {
        String URL = "https://abcnews.go.com/US/bus-carrying-18-students-driver-crashes-kentucky-multiple/story?id=93283274";
        String URL2 = "https://www.bbc.com/news/world-middle-east-63636783";
        String URL3 = "https://www.asded.com";
        articleCrawling articleCrawling = new articleCrawling();
//        articleCrawling.crawl(URL);
        articleCrawling.crawl(URL2);
//        articleCrawling.crawl(URL3);
//        articleCrawling.crawl(URL3);


    }
}

