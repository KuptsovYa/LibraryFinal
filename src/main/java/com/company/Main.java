package com.company;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        System.out.print("Input a number of Producers: ");
        int prodCount = in.nextInt();
        System.out.print("Input a number of Consumers: ");
        int consCount = in.nextInt();
        if(prodCount < 3 && consCount < 3){
            prodCount = 3;
            consCount = 3;
        }
        Starter starter = new Starter(prodCount, consCount);
        starter.start();
    }
}
