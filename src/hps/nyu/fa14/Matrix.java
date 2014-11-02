package hps.nyu.fa14;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class Matrix {

    public final int rows;
    public final int cols;

    public final boolean[][] values;

    public Matrix(int rCount, int cCount) {
        rows = rCount;
        cols = cCount;
        values = new boolean[rows][cols];
    }

    public boolean satisfies(TableSum tableSum) {

        if (rows != tableSum.rows || cols != tableSum.cols) {
            throw new IndexOutOfBoundsException("Unexpected matrix size");
        }

        // TODO: Could make this more efficient by
        // only iterating over the matrix once
        // Check all row sums
        for (int r = 0; r < rows; r++) {
            int rSum = 0;
            for (int i = 0; i < cols; i++) {
                rSum += (values[r][i] ? 1 : 0);
            }
            if (rSum != tableSum.rowSums[r]) {
                return false;
            }
        }

        // Check all column sums
        for (int c = 0; c < cols; c++) {
            int cSum = 0;
            for (int i = 0; i < rows; i++) {
                cSum += (values[i][c] ? 1 : 0);
            }
            if (cSum != tableSum.colSums[c]) {
                return false;
            }
        }

        return true;
    }

    public void write(OutputStream output) throws IOException {
        // TODO: Fix so that id doesn't write the very last space
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(output));
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                bw.write(String.format("%d ", values[r][c] ? 1 : 0));
            }
        }
        bw.newLine();
    }
    
    public static void write(OutputStream output, List<Matrix> matrices) throws IOException {
        for(Matrix m : matrices){
            m.write(output);
        }
        output.close(); // TODO: Is this an acceptable convention?
    }
}
