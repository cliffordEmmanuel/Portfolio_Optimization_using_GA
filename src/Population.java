import java.util.ArrayList;

public class Population {

    DNA [] population;
    ArrayList<DNA> matingPool = new ArrayList<>();
    double mutationRate;


    int generations = 0;


    Population (){}
    Population(double mutationRate, int maxPop) {
        this.mutationRate = mutationRate;
        this.population = new DNA[maxPop];
        for (int i = 0; i < population.length; i++) population[i] = new DNA();
    }

    void display(){

        for(DNA x: this.population) {
            System.out.println(x.toString());
            x.fitness();
            System.out.println(x.score);
        }
    }

    void selection() {
        for (int i = 0; i < population.length; i++) {    //calculates the fitness for all members of the population
            population[i].fitness();
        }
        //finding the maximum fitness to perform normalization...
        double totalFitness = 0.0;
        for (int i = 0; i < population.length; i++){
            totalFitness += population[i].score;
        }

        System.out.println("totalfitness: " + totalFitness);
        //build the mating pool...
        for (int i = 0; i < population.length; i++) {

            int n = (int)( (population[i].score ) * 100);     //performing normalization
            //System.out.println("n = " + n);
            for (int j = 0; j < n; j++) {
                matingPool.add(population[i]);
            }
        }

        System.out.println("size of mating pool: " + this.matingPool.size());
    }

//    void reproduce() {
//        for (int i = 0; i < population.length; i++) {
//            //two random numbers to randomly select 2 parents from the mating pool...
//            int a = (int) (Math.random() * matingPool.size());
//            int b = (int) (Math.random() * matingPool.size());
//            DNA partnerA = matingPool.get(a);
//            DNA partnerB = matingPool.get(b);
//            DNA child = partnerA.crossover(partnerB);           //crossover
//            child.mutate(mutationRate);
//            population[i] = child;
//        }
//        generations++;
//        matingPool.clear();
//    }
}
