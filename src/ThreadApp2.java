import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * Created by Roshan on 5/19/2017.
 */
public class ThreadApp2 extends Thread{
    static long startTime;
    static BlockingQueue<String> bQueue;
    static BlockingQueue<Document> processQueue;
    static ExecutorService executorService;

    public static void populateLinks(){
        bQueue = new LinkedBlockingDeque<>();
        bQueue.add("http://www.divaina.com/");
        bQueue.add("http://www.bbc.co.uk/");
        bQueue.add("https://news.google.com/");
        bQueue.add("https://www.yahoo.com/news/");
        bQueue.add("http://www.lankadeepa.lk/");
        bQueue.add("http://www.bbc.co.uk/");
        bQueue.add("http://www.divaina.com/");
        bQueue.add("https://news.google.com/");
        bQueue.add("https://www.yahoo.com/news/");
        bQueue.add("http://www.lankadeepa.lk/");
        bQueue.add("http://www.divaina.com/");
        bQueue.add("http://www.bbc.co.uk/");
        bQueue.add("https://news.google.com/");
        bQueue.add("https://www.yahoo.com/news/");
        bQueue.add("http://www.lankadeepa.lk/");
        bQueue.add("http://www.divaina.com/");
        bQueue.add("http://www.bbc.co.uk/");
        bQueue.add("https://news.google.com/");
        bQueue.add("https://www.yahoo.com/news/");
        bQueue.add("http://www.lankadeepa.lk/");
    }

    public static void crawl(String link) throws IOException {
        Document document = Jsoup.connect(link).get();
        System.out.println("Complete : " + link);
        processQueue.add(document);
        long endTime = System.nanoTime();
        System.out.println("Took "+(endTime - startTime) / 1000000 + " ms");
    }

    public static void process(Document doc){
//        doc.
    }

    public static void withoutThreads() throws InterruptedException {
        populateLinks();
        System.out.println("Without threads");

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
        populateLinks();
        System.out.println("With threads");
        executorService = Executors.newFixedThreadPool(11);

        startTime = System.nanoTime();
        for(int i=0; i<9; i++){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        try {
                            while (!bQueue.isEmpty()){
                                crawl(bQueue.take());
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

    public static void processWithThreads(){
        ExecutorService processExecutorService = Executors.newFixedThreadPool(11);
        for(int i=0; i<9; i++){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true){
                            if(!processQueue.isEmpty()){
                                process(processQueue.take());
                            } else {
                                wait();
                            }
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
        processWithThreads();
    }
}
