package src;

import java.util.*;

abstract public class Genetic<GEN> {
    protected final int fitnessThreshold;
    protected final int generationThreshold;
    protected List<Chromosome<GEN>> population;
    protected final int crowd;
    protected Random random = new Random(System.currentTimeMillis());

    /**
     * constructor of genetic algorithm
     * @param fitnessThreshold    : a threshold to stop tracing when receive a certain fitness
     * @param generationThreshold : a threshold to stop tracing when we generated a certain number of generations
     * @param crowd               : number of initial population, it must be an integer dividable by 10 (i.e. 10,20,50,100,...)
     */
    protected Genetic(int fitnessThreshold, int generationThreshold, int crowd) {
        if (crowd % 10 != 0)
            throw new IllegalArgumentException("crowd must be an integer dividable by 10");

        this.fitnessThreshold = fitnessThreshold;
        this.generationThreshold = generationThreshold;
        this.population = new ArrayList<>(crowd);
        this.crowd = crowd;
    }

    /**
     * trace function, an iterative method
     * 1- generate random population
     * 2- check for stop tracing (thresholds)
     * 3- generate next generation
     * 4- goto 2
     * @return a tracing result object
     */
    public TraceRes trace() {
        long startT = System.currentTimeMillis();

        //generate random initial population
        for (int i = 0; i < crowd; i++) {
            Chromosome<GEN> chromosome  = newChromosome();
            randomInit(chromosome);
            population.add(chromosome);
        }

        sortPopulation();
        int generation = 1;
        int bestFitness = fitness(population.get(crowd - 1));

        while (generation < generationThreshold && bestFitness < fitnessThreshold) {
            List<Chromosome<GEN>> newPopulation = new ArrayList<>();
            int _10_PercentIndex = (int) (0.1 * (float) crowd);
            int _40_PercentIndex = (int) (0.4 * (float) crowd);
            int _90_PercentIndex = (int) (0.9 * (float) crowd);

            //60% of better chromosomes selected for Cross-Over
            for (int i = _40_PercentIndex; i < crowd; i += 2) {
                Chromosome<GEN> c1 = newChromosome();
                Chromosome<GEN> c2 = newChromosome();
                crossOver(population.get(i), population.get(i + 1), c1, c2);
                newPopulation.add(c1);
                newPopulation.add(c2);
            }

            //10% of best chromosomes directly moved to next generation
            for (int i = _90_PercentIndex; i < crowd; i++) {
                newPopulation.add(population.get(i));
            }

            //10% of worst chromosomes directly moved to next generation
            for (int i = 0; i < _10_PercentIndex; i++) {
                newPopulation.add(population.get(i));
            }

            //10% of best chromosomes selected for mutation
            for (int i = _90_PercentIndex; i < crowd; i++) {
                GEN[] src = population.get(i).asArray();
                Chromosome<GEN> c = newChromosome();

                for (int j = 0; j < src.length; j++) {
                    c.set(j , src[j]);
                }

                mutate(c);
                newPopulation.add(c);
            }

            //10% of worst chromosomes selected for mutation
            for (int i = 0; i < _10_PercentIndex; i++) {
                Chromosome<GEN> c = newChromosome();
                GEN[] src = population.get(i).asArray();

                for (int j = 0; j < src.length; j++) {
                    c.set(j , src[j]);
                }

                mutate(c);
                newPopulation.add(c);
            }

            population = newPopulation;

            sortPopulation();
            generation++;
            bestFitness = fitness(population.get(crowd - 1));
        }

        long endT = System.currentTimeMillis();
        Chromosome<GEN> best = population.get(crowd - 1);
        return new TraceRes(best, fitness(best), generation, endT-startT, crowd);
    }

    /**
     * a method to sort population using fitness method
     */
    protected void sortPopulation() {
        population.sort(Comparator.comparingInt(this::fitness));
    }

    /**
     * a method to do crossOver, this method takes 2 parents
     * and fill Genomes of 2 child using cross over.
     *
     * @param parent1 : first parent of cross over
     * @param parent2 : second parent of cross over
     * @param child1  : first generated child
     * @param child2  : second generated child
     */
    protected void crossOver(Chromosome<GEN> parent1, Chromosome<GEN> parent2,
                             Chromosome<GEN> child1, Chromosome<GEN> child2) {
        int n = parent1.asArray().length;
        int cutPoint = random.nextInt(n - 2) + 1;
        int i;
        for (i = 0; i < cutPoint; i++) {
            child1.set(i, parent1.get(i));
            child2.set(i, parent2.get(i));
        }
        for (; i < n; i++) {
            child1.set(i, parent2.get(i));
            child2.set(i, parent1.get(i));
        }
    }

    /**
     * fitness method, Implemented by sub-class.
     * higher fitness => better chromosome
     * @param chromosome : chromosome to check it's fitness
     * @return fitness of chromosome represented by an integer
     */
    protected abstract int fitness(Chromosome<GEN> chromosome);

    /**
     * a method to get an instance of a empty chromosome.
     * Implemented by sub-class.
     * @return empty instance of a new chromosome
     */
    protected abstract Chromosome<GEN> newChromosome();

    /**
     * a method to fill a chromosome with random genomes.
     * implemented by sub-class.
     * @param chromosome chromosome to be filled
     */
    protected abstract void randomInit(Chromosome<GEN> chromosome);

    /**
     * a method to mutate a passed chromosome.
     * Implemented by sub-class.
     * @param chromosome
     */
    protected abstract void mutate(Chromosome<GEN> chromosome);


    /**
     * an inner class to represent output (result) of tracing
     */
    public class TraceRes {
        public final Chromosome<GEN> bestChromosome;
        public final int fitness;
        public final int passedGenerations;
        public final long duration;
        public final int crowd;

        /**
         * constructor of trace result
         * @param bestChromosome    : best chromosome that tracing created
         * @param fitness           : fitness of chromosome
         * @param passedGenerations : count of generations passed to receive this result
         * @param duration          : duration of algorithm
         * @param crowd             : number if initial population
         */
        public TraceRes(Chromosome<GEN> bestChromosome, int fitness, int passedGenerations, long duration, int crowd) {
            this.bestChromosome = bestChromosome;
            this.fitness = fitness;
            this.passedGenerations = passedGenerations;
            this.duration = duration;
            this.crowd = crowd;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("Trace Result:");
            sb.append("\n\tbestChromosome    = ").append(Arrays.toString(bestChromosome.asArray()));
            sb.append("\n\tfitness           = ").append(fitness);
            sb.append("\n\tpassedGenerations = ").append(passedGenerations);
            sb.append("\n\tduration          = ").append(duration).append(" ms");
            sb.append("\n\tcrowd             = ").append(crowd);
            return sb.toString();
        }
    }
}
