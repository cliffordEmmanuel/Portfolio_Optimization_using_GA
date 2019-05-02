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
        this.mutationRate = 0.05;
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
        //displaying the entire population
        for(DNA x: this.population){
            System.out.println(x.toString());
        }
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

        return (totalFitness/this.population.length)  ;
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
            if(!pop[i].nonZeroNegative()) continue;
            if(!pop[i].sumTo100()) continue;
            int n = pop[i].getRank() ;
            for (int j = 0; j < n; j++) {
                matingPool.add(pop[i]);
            }
        }
        //performing the crossover and the mutation
        reproduce();
    }

    void tournamentSelection(){
        //What is the size of the mating pool........
        //i'm assuming a tournament round is made of 4 competitors
        DNA [] round = new DNA[4];
        while(matingPool.size() != 100){
            for (int i =0; i < round.length; i++){
                round[i] = this.population[random(0,this.population.length-1)];//randomly select a number of portfolios
                while( !(round[i].nonZeroNegative()) && (!round[i].sumTo100()) )
                    round[i] = this.population[random(0,this.population.length-1)];
                round[i].returnObjectiveFnValue();//calculate their fitness
            }
            //determine the fittest among them and add to a new population
            Arrays.sort(round, new ObjectiveValComparator()); //sort the DNA in the tournament
            matingPool.add(round[0]); // add the fitest into the mating pool
            //repeat this several times till the new population is full.
        }

        reproduce();
    }

    void randomPoolSelection(){
        calcFitness();

        //finding the total sum
        double sum = 0.0;
        for(DNA x: this.population) sum += x.objectiveValue;

        //normalizing the fitness value for the population
        for(DNA x: this.population) x.objectiveValue = x.objectiveValue/sum;

        for(int i = 0; i<this.population.length;i++){
            DNA n = pickOne(this.population);
            while( (n.nonZeroNegative()) && (n.sumTo100()) ){
                matingPool.add(n);
                break;
            }

        }
        reproduce();

    }

    private DNA pickOne(DNA [] arr){
        int i = 0;
        double r = random(1);
        while (r > 0){
            r -= arr[i].objectiveValue;
            i++;
        } i--;
        return arr[i];
    }


    private void reproduce() throws ArrayIndexOutOfBoundsException{
        for (int i = 0; i < population.length; i++) {
            //two random numbers to randomly select 2 parents from the mating pool...
            int a =  random(0,matingPool.size()-1);
            int b =  random(0,matingPool.size()-1);
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
            //this.reproduce();
        }
    }

    //ok so now i need to write a test for convergence for the entire population..
    //by convergence we mean all the elements in the population starts to look similar.
    //ok so look through the population and count the number of similar DNA take the one with the highest count divide by the
    //length of the population and multiply by 100.
    //and return the result
    //if result is greater or equal to the required convergence method
    //stop the entire evolution process

    //display computing time
    //number of generations
    //accuracy????
    //the highest performing portfolio to string as well...

}
