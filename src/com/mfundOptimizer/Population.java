package com.mfundOptimizer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import static com.mfundOptimizer.DNA.random;

public class Population {

    DNA[] population;
    ArrayList<DNA> matingPool = new ArrayList<>();
    double mutationRate;
    long investmentAmt;
    int generations = 0;


    Population( long investmentAmt) {
        this.mutationRate = 0.01;
        this.investmentAmt = investmentAmt;
        this.population = new DNA[200];
        for (int i = 0; i < population.length; i++) population[i] = new DNA();
    }

    String display(){
        DecimalFormat df1 = new DecimalFormat(".##");
        for(DNA x: this.population) {
            x.calcPortfolioVariance();
            x.returnObjectiveFnValue();
        }
        DNA[] list = sortByObjectiveVal();
//        ________________________________________________
//        //displaying the entire population
//        for(DNA x: this.population){
//            System.out.println(x.toString());
//        }
//        ________________________________________________
//
        return list[0].toString(investmentAmt);
    }


    DNA[] sortByObjectiveVal(){
        Arrays.sort(population, new ObjectiveValComparator());
        return population;
    }

    double averageFitness(){
        calcFitness();
        double totalFitness = 0.0;
        for (DNA x: this.population) totalFitness += x.objectiveValue;

        return (totalFitness/this.population.length) ;
    }

    void calcFitness(){
        for (int i = 0; i < population.length; i++) {    //calculates the portfolio_variance for all members of the population
            population[i].returnObjectiveFnValue();
        }
    }

    //ranking the elements in a reverse order
    // the smaller the portfolio_variance
    // the higher the rank hence the higher the probability of being picked
    //if it fails the constraints test of contains zero and sum to 100 do not add to the mating pool;

    void rouletteWheelSelection() {
        calcFitness();
        DNA[] pop = sortByObjectiveVal();
        int rk = this.population.length;
        for(int i = 0; i <this.population.length; i++) {
            pop[i].setRank(rk);
            rk--;
        }
        for (int i = 0; i < this.population.length; i++) {
            if(pop[i].nonZeroNegative() == false) continue;
            if(pop[i].sumTo100() == false) continue;
            int n = pop[i].getRank() ;
            for (int j = 0; j < n; j++) {
                matingPool.add(pop[i]);
            }
        }
    }

    void tournamentSelection(){
        //What is the size of the mating pool........
        //randomly select a number of portfolios
        //i'm assuming a tournament round is made of 4 competitors
        DNA [] round = new DNA[4];
        for (int i =0; i < round.length; i++){
            round[i] = this.population[random(0,this.population.length)];
            round[i].returnObjectiveFnValue();
        }
        //determine the fittest among them and add to a new population
        Arrays.sort(round, new ObjectiveValComparator());

        //repeat this several times till the new population is full.
    }

    void acceptReject(){ }
    
    void reproduce() throws ArrayIndexOutOfBoundsException{
        for (int i = 0; i < population.length; i++) {
            //two random numbers to randomly select 2 parents from the mating pool...
            int a = (int) (Math.random() * matingPool.size()-1);
            int b = (int) (Math.random() * matingPool.size()-1);
            DNA partnerA = matingPool.get(a);
            DNA partnerB = matingPool.get(b);
            DNA child = partnerA.crossover(partnerB);
            child.mutate(mutationRate);
            population[i] = child;
        }
        generations++;
        matingPool.clear();
    }

    void run(){
        while(this.generations != 500 ){
            this.rouletteWheelSelection();
            this.reproduce();
        }
    }

}
