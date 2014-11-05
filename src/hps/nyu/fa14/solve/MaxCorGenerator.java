package hps.nyu.fa14.solve;

import hps.nyu.fa14.IGenerator;
import hps.nyu.fa14.Matrix;
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
public class MaxCorGenerator implements IGenerator {

    // Allows clients to set a useful cutoff for testing
    // Generates 10000 by default for problem A
    public int maxToGenerate = 10000;
    public int populationSize = 50;
    public int generations = 1;
    public double mutationProb = 0.9;
    private Random rand = new Random();
    private List<Integer> rowsNotAllowed = new ArrayList<Integer>();
    private List<Integer> colsNotAllowed = new ArrayList<Integer>();

    @Override
    public List<Matrix> generate(TableSum tableSum) {

        // Keep a set of all of the Matrices
        Set<Matrix> matrices = new HashSet<Matrix>();
        
        rowsNotAllowed = new ArrayList<Integer>();
        for(int i=0;i<tableSum.rows;i++) {
          if(tableSum.rowSums[i] == 0 || tableSum.rowSums[i] == tableSum.cols) {
            rowsNotAllowed.add(i);
          }
        }
        colsNotAllowed = new ArrayList<Integer>();
        for(int i=0;i<tableSum.cols;i++) {
          if(tableSum.colSums[i] == 0 || tableSum.colSums[i] == tableSum.rows) {
            colsNotAllowed.add(i);
          }
        }

        // Generate one solution to start
        int maxCorr = 0;
        Matrix m0 = new TrivialGenerator().generate(tableSum).get(0);
        matrices.add(m0);
        int corr = m0.correlation();
        maxCorr = corr;
        Matrix bestCorMatrix = new Matrix(m0.rows,m0.cols);
        List<List<Matrix>> populations = new ArrayList<List<Matrix>>();
        List<Matrix> population = new ArrayList<Matrix>();
        population.add(m0);
        for(int i=1;i<populationSize;i++) {
          Matrix newM = mutate(m0);
          corr = newM.correlation2();
          if(maxCorr < corr) {
            maxCorr = corr;
            bestCorMatrix = newM;
          }
          population.add(newM);
        }
        populations.add(population);
        
        int t = 0;
        while(t < generations) {
          m0 = bestCorMatrix;
          population = new ArrayList<Matrix>();
          population.add(m0);
          for(int i=1;i<populationSize;i++) {
            Matrix newM = mutate(m0);
            corr = newM.correlation2();
            if(maxCorr < corr) {
              maxCorr = corr;
              bestCorMatrix = newM;
            }
            population.add(newM);
          }
          populations.add(population);
          t++;
        }
        
        System.out.println("Best correlation " + maxCorr);
        
        /*List<Matrix> mQueue = new ArrayList<Matrix>();
        mQueue.add(m0);

        while (mQueue.size() > 0) {
            Matrix mCurr = mQueue.remove(0);
            for (SwapPosition swapPos : getSwapPositions(mCurr)) {
                Matrix newM = mCurr.clone();
                swapPos.swap(newM);
                boolean added = matrices.add(newM); // only adds if it is
                                                    // distinct
                corr = newM.correlation();
                if(maxCorr < corr) {
                  maxCorr = corr;
                  bestCorMatrix = newM;
                }
                if(added){
                    mQueue.add(newM); // explore this one later
                }

                if (matrices.size() >= maxToGenerate) {
                    mQueue.clear();
                    break;
                }
            }

        }
        System.out.println(String.format("Generated %d matrices",
                matrices.size()));
        System.out.println("Max corr "+maxCorr);*/
        return new ArrayList<Matrix>(matrices);
    }
    
    private Matrix mutate(Matrix m) {
      boolean flag = false;
      Matrix newM = m.clone();
      while(!flag) {
        boolean isRowAllowed = false;
        boolean isColAllowed = false;
        int rowToSwap = 0;
        int colToSwap = 0;
        while(!isRowAllowed) {
          rowToSwap = rand.nextInt(m.rows);
          if(!rowsNotAllowed.contains(rowToSwap)) {
            isRowAllowed = true;
          }
        }
        while(!isColAllowed) {
          colToSwap = rand.nextInt(m.cols);
          if(!colsNotAllowed.contains(colToSwap)) {
            isColAllowed = true;
          }
        }
        newM.values[rowToSwap][colToSwap] = !newM.values[rowToSwap][colToSwap];
        int rowSwap = rand.nextInt(m.rows);
        while(rowSwap != rowToSwap && newM.values[rowSwap][colToSwap] != newM.values[rowToSwap][colToSwap]) {
          rowSwap = rand.nextInt(m.rows);
        }
        newM.values[rowSwap][colToSwap] = !newM.values[rowSwap][colToSwap];
        int colSwap = rand.nextInt(m.cols);
        while(colSwap != colToSwap && newM.values[rowToSwap][colSwap] != newM.values[rowToSwap][colToSwap]) {
          colSwap = rand.nextInt(m.cols);
        }
        newM.values[rowToSwap][colSwap] = !newM.values[rowToSwap][colSwap];
        flag = true;
      }
      return newM;
    }

    private Iterable<SwapPosition> getSwapPositions(final Matrix m) {
        return new Iterable<SwapPosition>() {
            @Override
            public Iterator<SwapPosition> iterator() {
                return new SwapPositionIterator(m);
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

    // Defines corners of a sub matrix which can be swapped and still preserve
    // row and column sums:
    // x11,y11 x12,y12
    // x21,y21 x22,y22
    private static class SwapPosition {
        public int r11;
        public int c11;

        public int r12;
        public int c12;

        public int r21;
        public int c21;

        public int r22;
        public int c22;

        // Swap must be of the form
        // 1 0 or 0 1
        // 0 1 -- 1 0
        public boolean isValid(Matrix m) {
            boolean base = m.values[r11][c11];
            if ((base == m.values[r22][c22]) && (base != m.values[r12][c12])
                    && (base != m.values[r21][c21])) {
                return true;
            }

            return false;
        }

        // Modifies the matrix according to this swap
        public void swap(Matrix m) {
            m.values[r11][c11] = !m.values[r11][c11];
            m.values[r12][c12] = !m.values[r12][c12];
            m.values[r21][c21] = !m.values[r21][c21];
            m.values[r22][c22] = !m.values[r22][c22];
        }
    }
}
