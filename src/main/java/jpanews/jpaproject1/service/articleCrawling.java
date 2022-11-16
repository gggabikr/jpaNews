package jpanews.jpaproject1.service;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class articleCrawling {

//    String URL = "Please enter article's URL";
    Document doc;

    public boolean crawl(String url) {
        boolean result = false;

        if (url.contains("abcnews")) {
            result = abcNews(url);
        } else if(url.contains("bbc.com")){
            result = bbcNews(url);
        } else if(url.contains("nypost.com")){
            return nyPost(url);
        } else if(url.contains("msn.com") && url.contains("news")){
            return msnNews(url);
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
                //ul, li 같은 리스트들을 어떻게 처리할지 고민해보자.
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
                //ul, li 같은 리스트들을 어떻게 처리할지 고민해보자.
            }
        }
        return true;
    }
    public boolean nyPost(String url){
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
            Elements title = doc.getElementsByClass("headline--single");
            Elements body = doc.select("#main>article>div>div>div>div>div>p");

            System.out.println(title.size() > 0 ? title.text() : null);
            for (Element element : body) {
                System.out.println(element.text());
                //ul, li 같은 리스트들을 어떻게 처리할지 고민해보자.
            }
        }
        return true;
        //#main>article>div>div>div>div>div>header>h1
        //#main>article>div>div>div>div>div>p
    }
    public boolean msnNews(String url){
        
    }
//    public boolean washingtonPost(String url){}
//    public boolean cnnNews(String url){}
//    public boolean googleNews(String url){}
//    public boolean foxNews(String url){}
//    public boolean huffingtonPost(String url){}
//    public boolean nbcNews(String url){}




    public static void main(String[] args) {
        String abcUrl = "https://abcnews.go.com/US/bus-carrying-18-students-driver-crashes-kentucky-multiple/story?id=93283274";
        String bbcUrl = "https://www.bbc.com/news/world-middle-east-63636783";
        String nyPostUrl = "https://nypost.com/2022/11/15/jennifer-siebel-newsom-wife-to-california-gov-asked-to-fake-an-orgasm-in-court-during-harvey-weinstein-trial/";
        String NotExistUrl = "https://www.asded.com";

        articleCrawling articleCrawling = new articleCrawling();
//        articleCrawling.crawl(abcUrl);
//        articleCrawling.crawl(bbcUrl);
//        articleCrawling.crawl(URL3);
        articleCrawling.crawl(nyPostUrl);
//        articleCrawling.crawl(NotExistUrl);


    }
}

