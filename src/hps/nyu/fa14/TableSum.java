package hps.nyu.fa14;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TableSum {

    public final int rows;
    public final int cols;
    
    public final int[] rowSums;
    public final int[] colSums;
    
    public TableSum(int rCount, int cCount){
        rows = rCount;
        cols = cCount;
        rowSums = new int[rows];
        colSums = new int[cols];
    }
    
    public Matrix emptyMatrix(){
        return new Matrix(rows, cols);
    }
    
    public static TableSum parse(InputStream input) throws IOException {
        
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        
        // Read dimensions
        String[] dims = br.readLine().trim().split("\\s");
        int rows = Integer.parseInt(dims[0]);
        int cols = Integer.parseInt(dims[1]);
        TableSum tSum = new TableSum(rows, cols);

        // Read row sums
        String[] rowSums = br.readLine().trim().split("\\s");
        for(int i = 0; i < tSum.rows; i++){
            tSum.rowSums[i] = Integer.parseInt(rowSums[i]);
        }

        // Read col sums
        String[] colSums = br.readLine().trim().split("\\s");
        for(int i = 0; i < tSum.cols; i++){
            tSum.colSums[i] = Integer.parseInt(colSums[i]);
        }

        return tSum;
    }
    
    public static TableSum parseFile(String filePath) throws IOException {
        return parse(new FileInputStream(new File(filePath)));
    }
}
