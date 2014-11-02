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

    // TODO: Could make this more efficient by
    // only iterating over the matrix once
    public boolean satisfies(TableSum tableSum) {

        if (rows != tableSum.rows || cols != tableSum.cols) {
            throw new IndexOutOfBoundsException("Unexpected matrix size");
        }

        // Check all rows
        for (int r = 0; r < rows; r++) {
            if (rowSum(r) != tableSum.rowSums[r]) {
                System.out.println(String.format("Row %d not satisfied", r));
                return false;
            }
        }

        // Check all columns
        for (int c = 0; c < cols; c++) {
            if (columnSum(c) != tableSum.colSums[c]) {
                System.out.println(String.format("Column %d not satisfied", c));
                return false;
            }
        }

        return true;
    }

    public int columnSum(int col) {
        int sum = 0;
        for (int i = 0; i < rows; i++) {
            sum += (values[i][col] ? 1 : 0);
        }
        return sum;
    }

    public int rowSum(int row) {
        int sum = 0;
        for (int i = 0; i < cols; i++) {
            sum += (values[row][i] ? 1 : 0);
        }
        return sum;
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

    public static void write(OutputStream output, List<Matrix> matrices)
            throws IOException {
        for (Matrix m : matrices) {
            m.write(output);
        }
        output.close(); // TODO: Is this an acceptable convention?
    }
}
