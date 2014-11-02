package hps.nyu.fa14.solve;

import java.util.List;

import hps.nyu.fa14.IGenerator;
import hps.nyu.fa14.Matrix;
import hps.nyu.fa14.TableSum;

/**
 * Generates as many distinct Matrices as possible (upto a specified cutoff)
 * 
 * @author ck1456@nyu.edu
 */
public class MultiGenerator implements IGenerator {

    private final int MAX;

    public MultiGenerator(){
        this(Integer.MAX_VALUE); // Essentially unbounded
    }
    
    public MultiGenerator(int k) {
        MAX = k;
    }

    @Override
    public List<Matrix> generate(TableSum tableSum) {

        
        
        return null;
    }

}
