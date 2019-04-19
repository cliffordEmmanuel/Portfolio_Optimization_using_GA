package com.mfundOptimizer;

import javax.swing.*;

public class Setup {

    public static void main(String[] args) {
        //When i'm asked to show the entire population
//__________________________________________________________________
//
//        Population pop = new Population(10000000);
//
//        while(pop.generations != 500 ){
//            pop.rouletteWheelSelection();
//            pop.reproduce();
//           // pop.averageFitness();
//
//        }
//        System.out.println(pop.display());
//____________________________________________________________________

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                App myapp = new App();
                myapp.setVisible(true); //
            }
        });
    }
}
