package com.mfundOptimizer;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Setup {

    public static void main(String[] args) throws FileNotFoundException {
        //When i'm asked to show the entire population
//__________________________________________________________________
        File file = new File("results.txt");
        PrintWriter out = new PrintWriter(file);

        Population pop = new Population(10000000);
        long startTime = System.nanoTime();
        while(pop.generations != 500 ){
            pop.tournamentSelection();
           // out.println("At generation:" + pop.generations + " Average Fitness: "+ pop.averageFitness());
            //pop.reproduce();
           // pop.averageFitness();
        }
        out.close();
        System.out.println("File successfully created");
        long endTime = System.nanoTime();

        long elaspedTime = endTime - startTime;
        long seconds = elaspedTime / 1000;
        seconds %= 60;
        System.out.println("Time elapsed: "+ seconds + "s" );
        System.out.println(pop.display());
//____________________________________________________________________

//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                App myapp = new App();
//                myapp.setVisible(true); //
//            }
//        });
    }
}
