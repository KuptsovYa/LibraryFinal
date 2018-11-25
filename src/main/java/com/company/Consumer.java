package com.company;

import com.company.literature.Book;
import com.company.literature.Catalog;
import com.company.literature.Literature;
import com.company.literature.Magazine;

import java.io.*;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Consumer implements Runnable{

    private final LinkedBlockingQueue<Literature> queue;
    private final AtomicBoolean stop;
    private final String typeOfLib;
    private String PATH = System.getProperty("user.dir");

    public Consumer(final LinkedBlockingQueue<Literature> queue, final AtomicBoolean stop, String typeOfLib) {
        this.queue = queue;
        this.stop = stop;
        this.typeOfLib = typeOfLib;
    }

    public void run() {
        while (!stop.get()){
            try {
                StringBuilder sb = new StringBuilder(getTypeOfLib());
                sb.deleteCharAt(sb.length() - 1);
                if(queue.size() > 0 && queue.peek().getTypeOfLit().equals(sb.toString())){
                    Literature res = queue.take();
                    System.out.println(res.getAuthor() + " " + res.getTitle() + " " + res.getTypeOfLit());
                    writeToFile(res);
                }
            } catch (Exception e){
            }
        }
    }

    private void writeToFile(Literature literature) throws IOException {

        String s = PATH +"\\Libraries\\" + literature.getAuthor();
        String sExt =  "\\" +literature.getAuthor() + " " + literature.getTitle() + ".txt";
        File dir = new File(s);
        dir.mkdir();
        File file = new File(s+sExt);
        file.createNewFile();
        BufferedWriter bw =  new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));

        try {
            bw.write("\t\t\t"+ literature.getAuthor() + "\r\n\t\t\t" + literature.getTitle() + "\r\n");
            writeSpecial(bw, literature);
            bw.write("\t" + literature.getContent() + "\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            bw.flush();
            bw.close();
        }
    }

    private void writeSpecial(BufferedWriter bw, Literature literature) throws IOException {
        if (literature.getTypeOfLit().equals("Book")){
            Book b = (Book) literature;
            List<String> lstOfContent = b.getTableOfContent();
            for (String s : lstOfContent) {
                bw.write(s + "\r\n");
            }
        }else if(literature.getTypeOfLit().equals("Catalog")){
            Catalog c = (Catalog) literature;
            bw.write("\r\n" + c.getType() + "\r\n" + c.getPointers() + "\r\n\r\n");
        }else {
            Magazine m = (Magazine) literature;
            bw.write("\r\n" + m.getClassification() + "\r\n\r\n" );
        }
    }


    private String getTypeOfLib() {
        return typeOfLib;
    }
}
