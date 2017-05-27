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

    static BlockingQueue<WebPage> processQueue = new LinkedBlockingDeque<>();
    static BlockingQueue<WebPage> processQueue2 = new LinkedBlockingDeque<>();

    static ArrayList<String> webAddresses = new ArrayList<String>();
    static int flag = 0;

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

    //Section 1
    public static void crawl2(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        processQueue2.add(new WebPage(link,doc));
        System.out.println("Completed adding 2 : " + link);
        long endTime = System.nanoTime();
        System.out.println("Took "+(endTime - startTime) / 1000000 + " ms");
        flag = 1;
    }

    //Section 2
    public static void crawl3 () throws InterruptedException, IOException {
        WebPage initial = processQueue.take();
        WebPage modified = processQueue2.take();
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
        ExecutorService executorService2 = Executors.newFixedThreadPool(15);
        int N = 3; //Number of threads for crawling
        int M = 2; //Number of threads for processing

        startTime = System.nanoTime();
        System.out.println("Entered the first for loop");
        for(int i=0; i<N ; i++){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        try {
                            while (!bQueue.isEmpty()){
                                crawl(bQueue.take());
                            }
                            System.out.println("Sleeping");
                            Thread.sleep(25000);
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

        System.out.println("Exit the first for loop");

        System.out.println("Waiting till finished");
        while(flag!=1){
            System.out.print(" ");
        } //it will loop here until flag=1
        System.out.println("Flag passed");

        System.out.println("Enter the 2nd for loop");
        for(int j=0; j<M ; j++){
            executorService2.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        try {
                            while (!bQueue2.isEmpty()){
                                crawl3();
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
        executorService2.shutdown();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        processQueue = new LinkedBlockingDeque<>();
//        withoutThreads();
        withThreads();
    }
}
