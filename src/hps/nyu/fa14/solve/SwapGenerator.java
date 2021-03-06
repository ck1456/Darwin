package hps.nyu.fa14.solve;

import hps.nyu.fa14.Matrix;
import hps.nyu.fa14.SwapPosition;
import hps.nyu.fa14.TableSum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Given a matrix, find a distinct matrix that satisfies the same constraints by
 * swapping two col positions in two different rows
 * 
 * @author ck1456@nyu.edu
 */
public class SwapGenerator extends AbstractGenerator {

    // Allows clients to set a useful cutoff for testing
    // Generates 10000 by default for problem A
    public int maxToGenerate = 10000;

    @Override
    public List<Matrix> generate(TableSum tableSum) {

        // Keep a set of all of the Matrices
        Set<Matrix> matrices = new HashSet<Matrix>();

        // Generate one solution to start
        Matrix m0 = new TrivialGenerator().generate(tableSum).get(0);
        matrices.add(m0);

        List<Matrix> mQueue = new ArrayList<Matrix>();
        mQueue.add(m0);

        while (mQueue.size() > 0) {
            Matrix mCurr = mQueue.remove(0);
            for (SwapPosition swapPos : getSwapPositions(mCurr)) {
                Matrix newM = mCurr.clone();
                swapPos.swap(newM);
                boolean added = matrices.add(newM); // only adds if it is
                                                    // distinct
                if(added){
                    mQueue.add(newM); // explore this one later
                    updateIfBest(newM);
                }

                if (matrices.size() >= maxToGenerate) {
                    mQueue.clear();
                    break;
                }
            }
        }
        //System.out.println(String.format("Generated %d matrices",
        //        matrices.size()));
        return new ArrayList<Matrix>(matrices);
    }

    public static SwapGenerator newInfiniteGenerator(){
        SwapGenerator g = new SwapGenerator();
        g.maxToGenerate = Integer.MAX_VALUE;
        return g;
    }
    
    public static Iterable<SwapPosition> getSwapPositions(final Matrix m) {
        return new Iterable<SwapPosition>() {
            @Override
            public Iterator<SwapPosition> iterator() {
                return new SwapPositionIterator(m);
            }
        };
    }
    
    public static Iterable<SwapPosition> getSwapPositionsRandom(final Matrix m) {
        return new Iterable<SwapPosition>() {
            @Override
            public Iterator<SwapPosition> iterator() {
                return SwapPositionIterator.randomStart(m);
            }
        };
    }

    /**
     * Enumerates possible locations that a swap could be made
     */
    private static class SwapPositionIterator implements Iterator<SwapPosition> {

        private final Matrix m;

        private SwapPosition next = null;

        private int baseR = 0;
        private int baseC = 0;
        private int spanR = 0;
        private int spanC = 1; // Important for initialization

        SwapPositionIterator(Matrix matrix) {
            m = matrix;
            setNext();
        }

        private static final Random RAND = new Random();
        public static SwapPositionIterator randomStart(Matrix m){
            SwapPositionIterator iter = new SwapPositionIterator(m);
            iter.baseR = RAND.nextInt(m.rows);
            iter.baseC = RAND.nextInt(m.cols);
            iter.setNext();
            return iter;
        }
        
        private void setNext() {

            while (next == null || !next.isValid(m)) {
                // increment row span
                spanR++;
                if (baseR + spanR >= m.rows) {
                    // increment column span
                    spanC++;
                    spanR = 1;
                    if (baseC + spanC >= m.cols) {
                        // increment base column
                        baseC++;
                        spanC = 1;
                        if (baseC + spanC >= m.cols) {
                            // increment base row
                            baseR++;
                            baseC = 0;
                        }
                    }
                }

                // if out of bounds, complete
                if ((baseR + spanR >= m.rows) || (baseC + spanC >= m.cols)) {
                    next = null;
                    return; // done iterating
                }

                SwapPosition nextPos = new SwapPosition();
                nextPos.r11 = baseR;
                nextPos.c11 = baseC;

                nextPos.r12 = baseR;
                nextPos.c12 = baseC + spanC;

                nextPos.r21 = baseR + spanR;
                nextPos.c21 = baseC;

                nextPos.r22 = baseR + spanR;
                nextPos.c22 = baseC + spanC;

                next = nextPos;
            }
            // System.out.println("Next set");
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public SwapPosition next() {
            if (next == null) {
                throw new ArrayIndexOutOfBoundsException();
            }
            SwapPosition toReturn = next;
            next = null;
            setNext();
            return toReturn;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
