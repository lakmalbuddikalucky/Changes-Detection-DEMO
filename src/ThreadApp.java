import algorithms.ChangeDetector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;

import io.FileHandler;
import io.WebCrawler;
import models.WebPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
//import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Roshan on 5/2/2017.
 */
public class ThreadApp extends Thread{
    static long startTime;
    static BlockingQueue<String> bQueue;
    static BlockingQueue<String> bQueue2;

    static BlockingQueue<WebPage> processQueue;
    static BlockingQueue<WebPage> processQueue2;

    static ArrayList<String> webAddresses = new ArrayList<String>();

    public static void populateLinks(){
        bQueue = new LinkedBlockingDeque<>();
        bQueue2 = new LinkedBlockingDeque<>();


        for(int x=0;x<2;x++){
            bQueue.add("http://www.divaina.com/");
            bQueue.add("http://www.bbc.co.uk/");
            bQueue.add("https://news.google.com/");
            bQueue.add("https://www.yahoo.com/news/");
            bQueue.add("http://www.lankadeepa.lk/");

            bQueue2.add("http://www.divaina.com/");
            bQueue2.add("http://www.bbc.co.uk/");
            bQueue2.add("https://news.google.com/");
            bQueue2.add("https://www.yahoo.com/news/");
            bQueue2.add("http://www.lankadeepa.lk/");
        }
    }

    public static void crawl(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        processQueue.add(new WebPage(link, doc));
        System.out.println("Completed adding : " + link);
        long endTime = System.nanoTime();
        System.out.println("Took "+(endTime - startTime) / 1000000 + " ms");
    }

    public static void crawl2(String link) throws IOException, InterruptedException {
        Document doc = Jsoup.connect(link).get();
        WebPage modified = new WebPage(link,doc);
        WebPage initial = processQueue.take();
        HashMap<String, ArrayList<String>> result = ChangeDetector.compare(WebCrawler.crawl(initial.getDoc()),
                WebCrawler.crawl(modified.getDoc()));

        System.out.println("Changes detected: "+result );

    }

    public static void process(Document doc){
//        doc.
    }

    public static void withoutThreads() throws InterruptedException {
        System.out.println("Without threads");
        populateLinks();

        startTime = System.nanoTime();
        while (!bQueue.isEmpty()){
            try {
                crawl(bQueue.take());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void withThreads(){
        System.out.println("With threads");
        populateLinks();
        ExecutorService executorService = Executors.newFixedThreadPool(15);

        startTime = System.nanoTime();
        for(int i=0; i<1 ; i++){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        try {
                            while (!bQueue.isEmpty()){
                                crawl(bQueue.take());
                            }
                            System.out.println("Sleeping");
                            Thread.sleep(100000);
                            if(bQueue.isEmpty()){
                                while(!bQueue2.isEmpty()){
                                    crawl2(bQueue2.take());
                                }
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        executorService.shutdown();
    }



    public static void main(String[] args) throws ExecutionException, InterruptedException {
        processQueue = new LinkedBlockingDeque<>();
//        withoutThreads();
        withThreads();
    }
}
