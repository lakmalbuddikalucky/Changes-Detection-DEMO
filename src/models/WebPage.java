package models;

import io.FileHandler;
import io.WebCrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Roshan on 2/14/2017.
 */
public class WebPage {
    private String url;
    private Document doc;

    public WebPage(){
        this.url = null;
        this.doc = null;
    }

    public WebPage(String url) throws IOException {
        this.url = url;
        this.doc = WebCrawler.getWebDocument(url);
    }

    public WebPage(String url, String htmlString){
        this.url = url;
        this.doc = Jsoup.parse(htmlString);
    }

    public WebPage(String url, Document doc){
        this.url = url;
        this.doc = doc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    public void setDoc(String htmlString){
        this.doc = Jsoup.parse(htmlString);
    }

    public void save() throws IOException {
        URL urlA = new URL(url);
        String host = urlA.getHost();
        String [] res = host.split("(\\.|//)+(?=\\w)");
        String fileName = res[1] + ".html";

        if(url == null){
            return;
        }

        if(doc == null){
            return;
        }

        new FileHandler().write(doc.html(), fileName);
    }

}
