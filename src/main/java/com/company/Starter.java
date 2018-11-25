package com.company;

import com.company.literature.Literature;

import java.io.File;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Starter {

    private AtomicBoolean stop;
    private LinkedBlockingQueue<Literature> queue;
    private LinkedBlockingQueue<String> rawLiteratureQueue;
    private Producer prodArr[];
    private Consumer consArr[];
    private ExecutorService maxProducers;
    private ExecutorService maxConsumers;
    private String[] typesArr = {"Books", "Catalogs", "Magazines"};
    private String PATH = System.getProperty("user.dir");

    public Starter (int prodCount, int consCount){
        this.stop = new AtomicBoolean(false);
        this.queue = new LinkedBlockingQueue<>();
        this.rawLiteratureQueue = new LinkedBlockingQueue<>();
        this.prodArr = new Producer[prodCount];
        this.consArr = new Consumer[consCount];
        this.maxProducers = Executors.newFixedThreadPool(prodCount);
        this.maxConsumers = Executors.newFixedThreadPool(consCount);
    }

    private void readRaw(){
        File literatureDir = new File(PATH + "\\Literature");
        File[] literatureListing = literatureDir.listFiles();

        for (File f : literatureListing){
            rawLiteratureQueue.add(f.getAbsolutePath());
        }

    }

    public void start(){
        Random r = new Random();
        readRaw();
        for (int i = 0; i < prodArr.length; i++){
            maxProducers.submit(new Producer(queue, stop, typesArr[r.nextInt(typesArr.length)], rawLiteratureQueue));
        }
        for (int i = 0; i < consArr.length; i++){
            maxConsumers.submit(new Consumer(queue, stop, typesArr[r.nextInt(typesArr.length)]));
        }
        Timer timer = new Timer();
        timer.schedule(new Stopper(), 10000);
    }

    private class Stopper extends TimerTask{
        @Override
        public void run(){
            stop.set(true);
        }
    }
}
