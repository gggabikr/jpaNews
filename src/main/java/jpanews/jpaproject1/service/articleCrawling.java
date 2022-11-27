package jpanews.jpaproject1.service;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class articleCrawling {

//    String URL = "Please enter article's URL";
//    String title;
//    String subTitle;
//    ArrayList<String> ArticleBody = new ArrayList<>();
    Document doc;

    public crawlingDto crawl(String url) {
        crawlingDto result;

        if (url.contains("abcnews")) {
            result = abcNews(url);
        } else if(url.contains("bbc.com")){
            result = bbcNews(url);
        } else if(url.contains("nypost.com")){
            return nyPost(url);
        } else if(url.contains("msn.com") && url.contains("news")){
            return msnNews(url);
        } else {
            System.out.println("Please check the URL again.");
            return new crawlingDto();
        }

        return result;
    }

    public crawlingDto abcNews(String url){
        crawlingDto dto = new crawlingDto();
        try {
            Connection conn = Jsoup.connect(url);
            doc = conn.get();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Something went wrong.");
            System.out.println("Please check the URL again.");
            return dto;
        } finally {
            Elements title = doc.select("div > span > div > div > span > div > h1");
            Elements subTitle = doc.select("div>span>div>p");
            Elements body = doc.select("div > span > div > div > span > article > p");

            for (Element element : title) {
                System.out.println(element.text());
                dto.title = element.text();
            }
            System.out.println(subTitle.get(1).text());
            dto.subTitle = subTitle.get(1).text();


            for (Element element : body) {
                System.out.println(element.text());
                dto.ArticleBody.add(element.text());
                //ul, li 같은 리스트들을 어떻게 처리할지 고민해보자.
            }
        }
        return dto;
    }
    public boolean nyTimes(String url){
        //because it requires paying to see the articles,
        //this method will not be built.
        return false;
    }
    public crawlingDto bbcNews(String url){
        crawlingDto dto = new crawlingDto();
        try {
            Connection conn = Jsoup.connect(url);
            doc = conn.get();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Something went wrong.");
            System.out.println("Please check the URL again.");
            return dto;
        } finally {
            Element title = doc.getElementById("main-heading");
            Elements body = doc.select("#main-content>div>div>div>article>div>div>p");

            dto.title = title.text();
//            System.out.println(title != null ? title.text() : null);
            for (Element element : body) {
//                System.out.println(element.text());
                dto.ArticleBody.add(element.text());
                //ul, li 같은 리스트들을 어떻게 처리할지 고민해보자.
            }
        }
        return dto;
    }
    public crawlingDto nyPost(String url){
        crawlingDto dto = new crawlingDto();
        try {
            Connection conn = Jsoup.connect(url);
            doc = conn.get();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Something went wrong.");
            System.out.println("Please check the URL again.");
            return dto;
        } finally {
            Elements title = doc.getElementsByClass("headline--single");
            Elements body = doc.select("#main>article>div>div>div>div>div>p");

            System.out.println(title.size() > 0 ? title.text() : null);
            dto.title = title.text();
            for (Element element : body) {
                System.out.println(element.text());
                dto.getArticleBody().add(element.text());
                //ul, li 같은 리스트들을 어떻게 처리할지 고민해보자.
            }
        }
        return dto;
    }
    public crawlingDto msnNews(String url){
        crawlingDto dto = new crawlingDto();
        try {
            Connection conn = Jsoup.connect(url);
            doc = conn.get();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Something went wrong.");
            System.out.println("Please check the URL again.");
            return dto;
        } finally {
            Elements title = doc.getElementsByTag("h1");
            Element body = doc.getElementsByAttributeValue("role", "main").select("section").get(0);
            System.out.println(title.size() > 0 ? title.text() : "could not find title");
            dto.title = title.text();

            String text = body.text();
            int j = text.indexOf("Related Articles");
            text = text.substring(0,j);

            System.out.println(text);
            dto.getArticleBody().add(text);
            //ul, li 같은 리스트들을 어떻게 처리할지 고민해보자.
        }
        return dto;
    }
//    will be updated later.
//    public boolean washingtonPost(String url){}
//    public boolean cnnNews(String url){}
//    public boolean googleNews(String url){}
//    public boolean foxNews(String url){}
//    public boolean huffingtonPost(String url){}
//    public boolean nbcNews(String url){}




//    public static void main(String[] args) {
//        String abcUrl = "https://abcnews.go.com/US/bus-carrying-18-students-driver-crashes-kentucky-multiple/story?id=93283274";
//        String bbcUrl = "https://www.bbc.com/news/world-middle-east-63636783";
//        String nyPostUrl = "https://nypost.com/2022/11/15/jennifer-siebel-newsom-wife-to-california-gov-asked-to-fake-an-orgasm-in-court-during-harvey-weinstein-trial/";
//        String msnUrl = "https://www.msn.com/en-us/news/technology/stranded-without-food-edible-drone-has-snackable-wings/ar-AA14991Z?ocid=EMMX&cvid=ec757b745bec4026aead2bea7d76f899";
//        String msnUrl2 = "https://www.msn.com/en-us/news/politics/mike-pence-said-7-words-that-disqualify-him-from-holding-office-kirschner/ar-AA14ww60?ocid=EMMX&cvid=75f7f1ddba254f59b60466e8849a3c9c";
//
//        String NotExistUrl = "https://www.asded.com";
//
//        articleCrawling articleCrawling = new articleCrawling();
////        articleCrawling.crawl(abcUrl);
////        articleCrawling.crawl(bbcUrl);
////        articleCrawling.crawl(nyPostUrl);
////        articleCrawling.crawl(msnUrl);
////        articleCrawling.crawl(msnUrl2);
//        articleCrawling.crawl(NotExistUrl);
//    }
}

