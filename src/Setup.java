public class Setup {

    public static void main(String[] args) {
        Population pop = new Population(0.01,50);

       // Population pop = new Population(0.01,100);


        pop.selection();
        pop.display();

    }

}
