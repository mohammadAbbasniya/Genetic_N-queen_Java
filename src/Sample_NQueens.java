/**
 * a simple class that implements genetic class
 * you need to implement a class for your own problem as this class done
 */
public class Sample_NQueens extends Genetic<Integer> {
    private final int queens;

    /**
     * N_Queen constructor
     *
     * @param queens              : number of queens in board
     * @param generationThreshold : a threshold to stop tracing when we generated a certain number of generations
     * @param crowd               : number of initial population, it must be an integer dividable by 10 (i.e. 10,20,50,100,...)
     */
    public Sample_NQueens(int queens, int generationThreshold, int crowd) {
        super(queens * (queens - 1) / 2, generationThreshold, crowd);
        this.queens = queens;
    }

    @Override
    protected int fitness(Chromosome<Integer> chromosome) {
        int n = chromosome.asArray().length;
        int NO_ATTACK = n * (n - 1) / 2;
        int x1, y1, x2, y2;
        for (int i = 0; i < n; i++) {
            x1 = i;
            y1 = chromosome.get(i);

            for (int j = i + 1; j < n; j++) {
                x2 = j;
                y2 = chromosome.get(j);
                if (y1 == y2 || (x1 + y1) == (x2 + y2) || (x1 - y1) == (x2 - y2))
                    NO_ATTACK--;
            }
        }
        return NO_ATTACK;
    }

    @Override
    protected Chromosome<Integer> newChromosome() {
        return new Chromosome<>(new Integer[queens]);
    }

    @Override
    protected void randomInit(Chromosome<Integer> chromosome) {
        for (int i = 0; i < queens; i++) {
            chromosome.set(i, random.nextInt(queens));
        }
    }

    @Override
    protected void mutate(Chromosome<Integer> chromosome) {
        chromosome.set(random.nextInt(queens), random.nextInt(queens));
    }


    /**
     * derived method, just run it
     *
     * @param args command line arguments, ignore...
     */
    public static void main(String[] args) {
        System.out.println("run 8-queen genetic by initial crowd:10 and generations threshold:500...");
        Sample_NQueens problem = new Sample_NQueens(8, 500, 20);
        System.out.println(problem.trace());
    }
}
