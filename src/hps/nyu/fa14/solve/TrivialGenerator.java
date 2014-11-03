package hps.nyu.fa14.solve;

import hps.nyu.fa14.IGenerator;
import hps.nyu.fa14.Matrix;
import hps.nyu.fa14.TableSum;

import java.util.Arrays;
import java.util.List;

/**
 * Generates a single solution that satisfies the constraints
 * 
 * @author ck1456@nyu.edu
 */
public class TrivialGenerator implements IGenerator {

    @Override
    public List<Matrix> generate(TableSum tableSum) {

        // Create a new matrix
        Matrix m = tableSum.emptyMatrix();

        // For each row sum go through and populate columns starting at top
        for (int r = 0; r < m.rows; r++) {
            for (int c = 0; c < tableSum.rowSums[r]; c++) {
                m.values[r][c] = true;
            }
        } // At this point all of the row sums should be satisfied
        
        // lastBits keeps the position of the last set bit in each row
        int[] lastBits = new int[m.rows];
        for (int r = 0; r < m.rows; r++) {
            lastBits[r] = tableSum.rowSums[r] - 1;
        }

        // For each column sum, find col that isn't satisfied
        // ==> percolate down the row
        for (int c = 0; c < m.cols; c++) {
            while (m.columnSum(c) > tableSum.colSums[c]) {
                // find the row with the smallest last bit, and shift that
                boolean[] invalid = new boolean[lastBits.length];
                int rowToShift = minIndex(lastBits, invalid);
                boolean isValid = false;
                while (!isValid) {
                    if (!m.values[rowToShift][c]) {
                        invalid[rowToShift] = true;
                        rowToShift = minIndex(lastBits, invalid);
                    } else {
                        isValid = true;
                    }
                }
                int r = rowToShift;
                // ==> pop this bit to the next open spot in the row
                m.values[r][c] = false;
                lastBits[r] = lastBits[r] + 1;
                m.values[r][lastBits[r]] = true;
            }
        }
        // At this point, all of the column sums should be satisfied, too
        return Arrays.asList(m);
    }

    private static int minIndex(int[] values, boolean[] invalid) {
        int minIndex = values.length;
        int minValue = Integer.MAX_VALUE;
        for (int i = 0; i < values.length; i++) {
            if (!invalid[i] && values[i] < minValue) {
                minValue = values[i];
                minIndex = i;
            }
        }
        return minIndex;
    }
}
