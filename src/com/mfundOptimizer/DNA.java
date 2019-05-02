package com.mfundOptimizer;

import java.text.DecimalFormat;
import java.util.Comparator;



class ObjectiveValComparator implements Comparator<DNA>{
    @Override
    public  int compare(DNA dna1, DNA dna2){
        int result = dna1.objectiveValue < dna2.objectiveValue ? -1 : (dna1.objectiveValue == dna2.objectiveValue ? 0 : 1);
        return result;
    }
}

public class DNA{

    //from a sample data studying 3 mutual funds for 5 years...
    protected  double mean_return [] = {0.27622,0.20122,0.25878};  //average expected return...
    protected double asset_weight [];
    protected double expected_portfolio_return;
    protected double variance [] = {0.127831937,0.081690952,0.132349947};
    protected double covariance [] = {0.086133442,0.120851001,0.095705228};
    protected double objectiveValue;


    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    protected int rank = 0;
    protected double portfolio_variance = 0.0;



    DNA (){
        this.asset_weight = random_weights();
        this.expected_portfolio_return = this.portfolio_return_calc();
        this.objectiveValue = returnObjectiveFnValue();
    }

    @Override
    public String toString(){
        return "Asset weights: " + asset_weight[0] + "," + asset_weight[1]  + "," + asset_weight[2] + "\n"+
                "Objective function value: "+ this.objectiveValue + "\n" +
                "Portfolio return: "+ this.expected_portfolio_return + "\n" +
                "Portfolio variance: "+ this.portfolio_variance + "\n";
    }

    public String toString(long money) {
        DecimalFormat df1 = new DecimalFormat(".##");
        return "\nWith GHC"+ money +" you can invest:\n\n" +
                "  GHC" +((long)asset_weight[0] * money) + " into MFund 1\n"  +  //Databank Epack Fund
                "  GHC" +((long)asset_weight[1] * money) + " into MFund 2\n" +   //HFC Equity Trust Fund
                "  GHC" +((long)asset_weight[2] * money) + " into MFund 3\n" +   //SAS Fortune Fund
                "\nWith an Expected return of " + df1.format(expected_portfolio_return * 100) + "%" +
                " at a " + df1.format(this.portfolio_variance) + "% risk " + "\n";
    }

    private double portfolio_return_calc(){
        double sum = 0.0;
        for (int i =0; i < this.asset_weight.length; i++){
            sum+= (this.mean_return[i] * (this.asset_weight[i]));  //expected return * asset weight...
        }
        return sum;  //since we're dealing with percentage...
    }

    //random number generators...
    static int random(int min, int max) {
        int rn = (int) ((Math.random() * ((max - min) + 1)) + min);
        if (rn == 100) rn = 1;
        return rn;
    }

    static double random(int num) {
        return (Math.random() * num);
    }

    private double [] random_weights(){
        double rand_weights [] = new double[3];

        rand_weights[0] = random(0, 100);  //number one
        rand_weights[1] = random(0, 100); //number two
        if( (rand_weights[0]+rand_weights[1]) >= 100.0 ) random_weights();
        rand_weights[2] = 100 - (rand_weights[0]+rand_weights[1]);


        //trying to ensure the sum to 100 condition here...
//        if (sumTo100(rand_weights) == false) {
//            double sum = 0.0;
//            for(int i=0;i<rand_weights.length;i++){
//                sum+=rand_weights[i];
//            }
//            if (sum < 100) rand_weights[2] = rand_weights[2] + (100- sum);
//            else rand_weights[2] = 100- (rand_weights[0] +rand_weights[1]);
//            //random_weights();
//        }
        rand_weights[0] /=100;
        rand_weights[1] /=100;
        rand_weights[2] /=100;
        return rand_weights;


    }


    protected void calcPortfolioVariance(){
        //portfolio variance = t1 + t2
        //t1
        double t1 = 0.0;
        for (int i = 0; i < this.asset_weight.length; i++) t1 += ( Math.pow(this.asset_weight[i],2) * this.variance[i]);
        //t2
        double t2 =( (2 * this.asset_weight[0] * this.asset_weight[1] * this.covariance[0])
                  +  (2 * this.asset_weight[0] * this.asset_weight[2] * this.covariance[1])
                  +  (2 * this.asset_weight[1] * this.asset_weight[2] * this.covariance[2]) );

        this.portfolio_variance = (t1 + t2);
    }


    //objective function value...
    double returnObjectiveFnValue(){
        calcPortfolioVariance();
        return ( (this.expected_portfolio_return  ) - this.portfolio_variance);
    }
    //crossover operator
    DNA crossover(DNA partner) {
        // The child is a new instance of DNA.
        // Note that the DNA is generated randomly in the constructor,
        // but we will overwrite it below with DNA from parents.
        DNA child = new DNA();
        int midpoint = random(0,asset_weight.length);
        for (int i = 0; i < asset_weight.length; i++) {
            if (i > midpoint) child.asset_weight[i] = this.asset_weight[i];
            else child.asset_weight[i] = partner.asset_weight[i];
        }
        return child;
    }


    //mutation operator...
    void mutate(double mutationRate) {
        for (int i = 0; i < asset_weight.length; i++) {
            if (random(1) < mutationRate) asset_weight[random(0,2)]+=0.01;
        }
    }


    //Constraints
    boolean nonZeroNegative(){
        //non-zero constraints
        for(int i = 0;i<asset_weight.length;i++){
            if (asset_weight[i] >= 0.0) continue;
            else return false;
        }
        return true;
    }
    boolean sumTo100(){
        double sum = 0.0;
        for(int i = 0; i<this.asset_weight.length; i++){
            sum += this.asset_weight[i];
        }
        if (sum == 1.0)return true;
        else return false;
    }


}
