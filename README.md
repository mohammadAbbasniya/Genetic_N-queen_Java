# 🧬 Genetic Algorithm (GA)
A simple and powerful implementation of [AI Genetic Algorithm](https://en.wikipedia.org/wiki/Genetic_algorithm) in java. all source codes are fully documented.

note that this package contains  ***N-Queen problem*** ,solved using this algorithm.
  
## Classes
### Genetic class
  This class contains the algorithm as a whole. your problem class must be a derivation of this algorithm class for the problem to be soled using genetic.
  In my implementation, next generation is producted in this way:
  - 60% of next population is created by [Crossover](https://en.wikipedia.org/wiki/Crossover_(genetic_algorithm)) by choosing the chromosomes of previous population. 
  - 10% of best chromosomes directly moved to next generation.
  - 10% of worst chromosomes directly moved to next generation.
  - 10% of best chromosomes selected for [mutation](https://en.wikipedia.org/wiki/Mutation_(genetic_algorithm)).
  - 10% of worst chromosomes selected for [mutation](https://en.wikipedia.org/wiki/Mutation_(genetic_algorithm)).

  you can find and change this implementation in `Genetic::trace` at line 60 through 105.
  
  
### Chromosome
  this class represents a chromosome in our implementation and contains an array to store gnomes.


## Usages
As you know, Genetic algorithm is a metaheuristic, not a problem (i.e. something like Prime or TSP) and it needs a problem to be solved.
you should write a class for your problem and we have a class `Genetic.java` that you must extends it and implements it's methods acording to your problem. mathod you must implements them:
  - `fitness` : is a method to assign a number to each chromosome that shows quality of this chromosome, this integer could be negative or positive, this integer used to sort chromosomes.
  - `newChromosome` : is a method to initialize and return a new Chromosome.
  - `randomInit` : is a method to fill a chromosome with random genomes value.
  - `mutate` : is a method to performe mutation on a passed chromosome.

There is sample usage that implements this Genetic class for N-Queen problem and you can find `Sample_NQueens.java` file in main directory.


  
