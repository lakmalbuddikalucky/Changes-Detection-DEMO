package io;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Roshan on 2/10/2017.
 */
public class WebCrawler {
    public static String TAG_SEPARATOR = "-";

    public static HashMap<String, String> crawl(String url) throws IOException {
        /*
        Crawl through a given URL to cache the data from a website

        url : web address that needs to crawl

        return : ArrayList of tag address with content
         */
        Document doc = Jsoup.connect(url).get();
        HashMap<String, String> labels = new HashMap<String, String>();
        traverse(doc.children(), labels, doc.nodeName());
        return labels;
    }

    public static HashMap<String, String> crawl(Document doc) throws IOException {
        /*
        Crawl through a given URL to cache the data from a website

        doc : web

        return : ArrayList of tag address with content
         */
        HashMap<String, String> labels = new HashMap<String, String>();
        traverse(doc.children(), labels, doc.nodeName());
        return labels;
    }

    private static void traverse(Elements elements, HashMap<String, String> labels, String parent){
        /*
        Recursively traverse through the tree and create an address -> content array for each html tag
        Base method : crawl

        elements : tags inside a particular tag
        labels   : list of address and content mapping
        parent   : maintain the address for each recursive call

        return : null
         */
        int id = 0;
        String label;
        for(Element element: elements){
            id += 1;
            if((element.children().size() == 0) && !element.ownText().equals("")){
                label = parent + TAG_SEPARATOR + element.nodeName() + String.valueOf(id);
                labels.put(label, element.ownText());
            }else{
                parent = parent + TAG_SEPARATOR + element.nodeName() + String.valueOf(id);
                traverse(element.children(), labels, parent);
            }
        }
    }

    public static Document getWebDocument(String url) throws IOException {
        return Jsoup.connect(url).timeout(5000).get();
    }
}
