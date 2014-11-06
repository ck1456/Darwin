package hps.nyu.fa14.solve;

import hps.nyu.fa14.Matrix;
import hps.nyu.fa14.SwapPosition;
import hps.nyu.fa14.TableSum;

import java.util.Arrays;
import java.util.List;

/**
 * Generates a single solution that satisfies the constraints
 * and maximizes correlation
 * 
 * @author ck1456@nyu.edu
 */
public class MaxGenerator extends AbstractGenerator {

    public int maxToGenerate = 1;

    @Override
    public List<Matrix> generate(TableSum tableSum) {

        // Generate a random solution, then locally optimize and keep track of the best
        int maxTest = 1000; // TODO: Make this termination condition better
        while(--maxTest > 0){
            RandomGenerator g = new RandomGenerator();
            g.maxToGenerate = 1;
            Matrix m = g.generate(tableSum).get(0);
            m = steepestSearch(m);
            if(updateIfBest(m)){
                bestMatrix = m;
            }
        }

        // At this point, all of the column sums should be satisfied, too
        return Arrays.asList(bestMatrix);
    }

    private Matrix bestMatrix = null;
    
    // Optimize a Matrix by searching swaps and taking them if they improve correlation
    public static Matrix localSearch(Matrix m){
        Matrix best = m;
        int bestCorrelation = m.correlation();
                
        boolean improve = true;
        while(improve){
            improve = false;
            for(SwapPosition swapPos : SwapGenerator.getSwapPositions(best)){
                Matrix newM = best.clone();
                swapPos.swap(newM);
                int corr = newM.correlation();
                if(corr > bestCorrelation){
                    //System.out.println("Local Search improvement: " + corr);
                    improve = true;
                    bestCorrelation = corr;
                    best = newM;
                    break; // Start climbing again from this position
                }
            }
        }
        return best;
    }
    
    public static Matrix steepestSearch(Matrix m){
        Matrix best = m;
        int bestCorrelation = m.correlation();
                
        boolean improve = true;
        while(improve){
            improve = false;
            Matrix newBest = best;
            for(SwapPosition swapPos : SwapGenerator.getSwapPositions(best)){
                Matrix newM = best.clone();
                swapPos.swap(newM);
                int corr = newM.correlation();
                if(corr > bestCorrelation){
                    improve = true;
                    bestCorrelation = corr;
                    newBest = newM;
                }
            }
            best = newBest;
        }
        return best;
    }
}
