/**
 * class to represent a chromosome
 * @param <GEN> data type of genomes that this chromosome will contains
 */
public class Chromosome<GEN> {
    private final GEN[] genomes;

    /**
     * constructor of chromosome
     * @param gens genomes that make up chromosome
     */
    public Chromosome(GEN...gens) {
        this.genomes = gens;
    }

    /**
     * @return genomes as an array
     */
    public GEN[] asArray() {
        return genomes;
    }

    /**
     * @param i : index of considered gen
     * @return get i-th gen in chromosome
     */
    public GEN get(int i){
        return genomes[i];
    }

    /**
     * set a new value to a gen of chromosome
     * @param i      : index of considered gen
     * @param newVal : new value for that gen
     */
    public void set(int i, GEN newVal){
        genomes[i] = newVal;
    }
}
