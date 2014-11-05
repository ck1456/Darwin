package hps.nyu.fa14.solve;

import hps.nyu.fa14.Matrix;
import hps.nyu.fa14.TableSum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Generates a single solution random that satisfies the constraints
 * 
 * @author ck1456@nyu.edu
 */
public class RandomGenerator extends AbstractGenerator {

    public int maxToGenerate = 1;

    @Override
    public List<Matrix> generate(TableSum tableSum) {

        // Keep a set of all of the Matrices
        Set<Matrix> matrices = new HashSet<Matrix>();
       while(matrices.size() < maxToGenerate){
           Matrix m = generateOneRandom(tableSum);
           if(m != null){
               matrices.add(m);
           }
       }

        // At this point, all of the column sums should be satisfied, too
        return new ArrayList<Matrix>(matrices);
    }
    
    private static Matrix generateOneRandom(TableSum tableSum) {
        
     // Create a new matrix
        Matrix m = tableSum.emptyMatrix();

        // For each row, create a random permutation and fill in to match row sum
        for (int r = 0; r < m.rows; r++) {
            // create permutation
            Integer[] oList = orderedList(m.cols);
            permute(oList);
            for (int c = 0; c < tableSum.rowSums[r]; c++) {
                m.values[r][oList[c]] = true;
            }
        } // At this point all of the row sums should be satisfied
        
        // keep track of where else you can put bits
        repairColumns(m, tableSum);
        if(m.satisfies(tableSum)){
            return m; // We win
        }
        return null; // Otherwise, return null
    }
    
    // Add or delete bits in each row to make sure the row constraints are met    
    public static void repairRows(Matrix m, TableSum tableSum){
    
        for(int r = 0; r < m.rows; r++){
            while(m.rowSum(r) < tableSum.rowSums[r]){
                // add an empty bit
                List<Integer> bits = getIndexOfBitsInRow(m, r, false);
                m.values[r][bits.get(rand.nextInt(bits.size()))] = true;
            }
            while(m.rowSum(r) > tableSum.rowSums[r]){
                // remove a full bit
                List<Integer> bits = getIndexOfBitsInRow(m, r, true);
                m.values[r][bits.get(rand.nextInt(bits.size()))] = false;
            }
        }
    }
    
    private static List<Integer> getIndexOfBitsInRow(Matrix m, int row, boolean value){
        List<Integer> bitIndexes = new ArrayList<Integer>();
        for(int i = 0; i < m.cols; i++){
            if(m.values[row][i] == value){
                bitIndexes.add(i);
            }
        }
        return bitIndexes;
    }
    
    private static List<Integer> getIndexOfBitsInColumn(Matrix m, int col, boolean value){
        List<Integer> bitIndexes = new ArrayList<Integer>();
        for(int i = 0; i < m.rows; i++){
            if(m.values[i][col] == value){
                bitIndexes.add(i);
            }
        }
        return bitIndexes;
    }
    
    // Assuming the rowSums are all correct, shift the bits to repair the column sums 
    public static void repairColumns(Matrix m, TableSum tableSum){
        for(int c = 0; c < m.cols; c++){
            while(m.columnSum(c) < tableSum.colSums[c]){
                // Find a random row in this column that is not set,
                // and has a bit available in a later column
                List<Integer> possibleRows = getIndexOfBitsInColumn(m, c, false);
                int[] pos = getPositionOfBitInRowWithValueAfterColumn(m, possibleRows, c, true);
                if(pos[0] < 0){
                    return; // abort - this matrix won't work
                }
                m.values[pos[0]][c] = true;
                m.values[pos[0]][pos[1]] = false;
            }
            while(m.columnSum(c) > tableSum.colSums[c]){
                // Find a random row in this column that is set,
                // and has a space available in a later column
                List<Integer> possibleRows = getIndexOfBitsInColumn(m, c, true);
                int[] pos = getPositionOfBitInRowWithValueAfterColumn(m, possibleRows, c, false);
                if(pos[0] < 0){
                    return; // abort - this matrix won't work
                }
                m.values[pos[0]][c] = false;
                m.values[pos[0]][pos[1]] = true;
            }
        }
    }

    private static int[] getPositionOfBitInRowWithValueAfterColumn(Matrix m, List<Integer> rows, int afterColumn, boolean value){
        // return row, col to swap
        // get all rows after afterColumn that have a bit of the 
        int maxBitCount = 0;
        int bestRow = -1;
        List<Integer> bestColumns = null;
        permute(rows);
        for(int r : rows){
            List<Integer> possibleColumns = new ArrayList<Integer>();
            for(int c = afterColumn + 1; c < m.cols; c++){
                if(m.values[r][c] == value){
                    possibleColumns.add(c);
                }
            }
            if(possibleColumns.size() >= maxBitCount){
                bestRow = r; // Always pull from the row with the most to give
                maxBitCount = possibleColumns.size();
                bestColumns = possibleColumns;
            }
        }
        if(bestRow > 0 && bestColumns.size() > 0){
            return new int[]{ bestRow, bestColumns.get(0)};
        }
        else {
            return new int[]{ -1, -1 }; // Sentinel value
        }
    }
    
    private static Integer[] orderedList(int len){
        Integer[] list = new Integer[len];
        for(int i = 0; i < len; i++){
            list[i] = i;
        }
        return list;
    }
    
    private static final Random rand = new Random();
    // Implements Fisher-Yates:
    // http://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle
    private static <T> void permute(T[] input) {
        for (int i = input.length - 1; i > 1; i--) {
            int swapIndex = rand.nextInt(i + 1);
            T swapValue = input[swapIndex];
            input[swapIndex] = input[i];
            input[i] = swapValue;
        }
    }
    
    private static <T> void permute(List<T> input) {
        for (int i = input.size() - 1; i > 1; i--) {
            int swapIndex = rand.nextInt(i + 1);
            T swapValue = input.get(swapIndex);
            input.set(swapIndex, input.get(i));
            input.set(i, swapValue);
        }
    }
}
