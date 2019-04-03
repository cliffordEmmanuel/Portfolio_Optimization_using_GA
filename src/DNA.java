import java.util.Arrays;

public class DNA {
    //Divide by 100 its in percentage!!!!

    //from a sample data studying 3 mutual funds for 5 years...
    protected  double mean_return [] = {0.2762,0.2012,0.2588};  //average expected return...
   // protected double expected_return [] = {0.1381,0.1006,0.1294}; // that is each mean return * 0.5 (for 5 years study period)
    protected double asset_weight []; //should be randomly generated....
    protected double expected_portfolio_return;
    protected double variance [] = {0.127832,0.081691,0.13235};
    //protected double correlation [] = {0.842878816,0.929113305,0.920421591};  we might never need it...
    protected double covariance [] = {0.086133442,0.120851001,0.095705228};

    protected double score = 0.0;

    DNA (){
        this.asset_weight = random_weights();
        this.expected_portfolio_return = this.portfolio_return_calc();
    }

    @Override
    public String toString() {
        return "DNA{" +
                "Asset weight = " + Arrays.toString(asset_weight) +
                " Expected portfolio return = " + expected_portfolio_return +
                '}';
    }

    private double portfolio_return_calc(){
        double sum = 0.0;
        for (int i =0; i < this.asset_weight.length; i++){
            sum+= (this.mean_return[i] * this.asset_weight[i]);  //expected return * asset weight...
        }
        return (sum/100);  //since we're dealing with percentage...
    }

    //random number generators...
    private int random(int min, int max) {
        int rn = (int) ((Math.random() * ((max - min) + 1)) + min);
        if (rn == 100) rn = 1;
        return rn;
    }

    private double random(int num) {
        return (Math.random() * num);
    }

    private double [] random_weights(){
        double rand_weights [] = new double[3];
        rand_weights[0] = random(0, 100);  //we'll always be varying this...
        rand_weights[1] = random(0, (100 - (int)rand_weights[0]));
        rand_weights[2] = 100 - (rand_weights[1] + rand_weights[0]);

        return rand_weights;
    }

    //not needed anymore...
//    private double [] calc_covariance(){
//        double cov [] = new double[3];
//        //this is to calculate the covariance for the asset pairs...
//        cov[0] = ( (Math.sqrt(variance[0])) * (Math.sqrt(variance[1])) * this.correlation[0] )/100;
//        cov[1] = ( (Math.sqrt(variance[0])) * (Math.sqrt(variance[2])) * this.correlation[1] )/100;
//        cov[2] = ( (Math.sqrt(variance[1])) * (Math.sqrt(variance[2])) * this.correlation[2] )/100;
//
//        return cov;
//    }


    //for crossover we'll be exchanging weights...
    //i think we need a new crossover algorithm
    //God we need to be fine now...


    //ok so now its time for the fitness function ...
    //variance.. oh God if there were a math function that will be great...
    protected void fitness(){
        //this time the fittest is the one with lowest variance...
        //so i need to find variance for each weight and covariance for each pairs of assets...

        //portfolio variance = t1 + t2
        //t1
        double t1 = 0.0;
        for (int i = 0; i < this.asset_weight.length; i++) t1 = (Math.pow(this.asset_weight[i],2) * this.variance[i]);
        //t2
        double t2 =( (2 * this.asset_weight[0] * this.asset_weight[1] * this.covariance[1])
                  +  (2 * this.asset_weight[0] * this.asset_weight[2] * this.covariance[2])
                  +  (2 * this.asset_weight[1] * this.asset_weight[2] * this.covariance[2]) );

        this.score = t1 + t2;
    }

    void mutate(double mutationRate) {
        double [] randomWeights = random_weights();

        for (int i = 0; i < asset_weight.length; i++) {
            if (random(1) < mutationRate) asset_weight[i] = randomWeights[i];
        }
    }
}
