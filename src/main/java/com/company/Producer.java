package com.company;
import com.company.literature.Book;
import com.company.literature.Catalog;
import com.company.literature.Literature;
import com.company.literature.Magazine;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sun.plugin.javascript.navig.Array;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Producer implements Runnable {

    private final LinkedBlockingQueue<Literature> queue;
    private final LinkedBlockingQueue<String> rawLiteratureQueue;
    private final AtomicBoolean stop;
    private final String typeOfWriter;

    public Producer(final LinkedBlockingQueue<Literature> queue, final AtomicBoolean stop, String typeOfWriter, final LinkedBlockingQueue<String> rawLiteratureQueue) {
        this.queue = queue;
        this.rawLiteratureQueue = rawLiteratureQueue;
        this.stop = stop;
        this.typeOfWriter = typeOfWriter;
    }

    public void run() {
        while (!stop.get()) {
            Random r = new Random();
            try {
                System.out.println("Read from file");
                Literature l = readFromFile();
                if(l != null) {
                    queue.add(l);
                    System.out.println("object add to queue");
                }else {
                    System.out.println("object missmatch");
                }
                Thread.sleep(r.nextInt(1000));
            } catch (Exception e) {

            }
        }
    }

    private Literature readFromFile() throws IOException, ParseException, InterruptedException {
        if (rawLiteratureQueue.size() == 0){
            stop.set(true);
        }
        String path = rawLiteratureQueue.take();
        JSONParser parser = new JSONParser();
        JSONObject jsonObject  = (JSONObject)parser.parse(
                new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));

        if (getTypeOfWriter().equals("Books")
                && jsonObject.containsKey("Table of content")){

            return createBook(jsonObject);
        } else if(getTypeOfWriter().equals("Catalogs")
                && jsonObject.containsKey("Pointers")){

            return createCatalog(jsonObject);
        } else if(getTypeOfWriter().equals("Magazines")
                && jsonObject.containsKey("Classification")){

            return createMagazine(jsonObject);
        }
        rawLiteratureQueue.add(path);
        return null;
    }

    private Book createBook(JSONObject jsonObject){
        String author = (String) jsonObject.get("Author");
        String title = (String) jsonObject.get("Title");
        String content = (String) jsonObject.get("Content");
        List<String> tableOfContent = new ArrayList<String>();
        try {
            JSONArray table = (JSONArray) jsonObject.get("Table of content");
            for (Object obj : table) {
                tableOfContent.add((String) obj);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return new Book("Book", author, title, content, tableOfContent);
    }

    private Catalog createCatalog(JSONObject jsonObject){
        String author = (String) jsonObject.get("Author");
        String title = (String) jsonObject.get("Title");
        String content = (String) jsonObject.get("Content");
        String type = (String) jsonObject.get("Type");
        String pointers = (String) jsonObject.get("Pointers");
        return new Catalog("Catalog", author, title, content, type, pointers);
    }

    private Magazine createMagazine(JSONObject jsonObject){
        String author = (String) jsonObject.get("Author");
        String title = (String) jsonObject.get("Title");
        String content = (String) jsonObject.get("Content");
        String classification = (String) jsonObject.get("Classification");
        return new Magazine("Magazine", author, title, content, classification);
    }

    private String getTypeOfWriter() {
        return typeOfWriter;
    }
}