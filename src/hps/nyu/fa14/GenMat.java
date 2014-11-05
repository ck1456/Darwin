package hps.nyu.fa14;

import hps.nyu.fa14.solve.MaxGenerator;
import hps.nyu.fa14.solve.SwapGenerator;
import hps.nyu.fa14.solve.TimedGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GenMat {


    /**
     * PROBLEM A: Find many matrices that satisfy the table sums
     */
    public static List<Matrix> solveMulti(TableSum tableSum){
        
        // Generates 10000 by default, but can set this to more
        SwapGenerator g = new SwapGenerator();
        //g.maxToGenerate = 10000;
        return g.generate(tableSum);
    }
    
    /**
     * PROBLEM B: Find one matrix that satisfies the table sums
     * and maximizes correlation 
     */    
    public static List<Matrix> solveMax(TableSum tableSum){
        
        SwapGenerator g = SwapGenerator.newInfiniteGenerator();
        IGenerator tg = new TimedGenerator(g, 120);
        Matrix m = tg.generate(tableSum).get(0);
        System.out.println(String.format("Correlation: %d", m.correlation()));
        return Arrays.asList(m);
    }
    
    /**
     * Generates Matrices (GenMat)
     * 
     * @param args
     */
    public static void main(String[] args) throws FileNotFoundException,
            IOException {

        if (args.length != 3) {
            usage();
        }
        // first parameter is input
        String problemType = args[0].trim().toUpperCase();
        String inputFile = args[1];
        String outputFile = args[2];

        TableSum tableSum = TableSum.parseFile(inputFile);
        List<Matrix> solution = null;
        if(problemType.equals("A")){
            solution = solveMulti(tableSum);
        } else if(problemType.equals("B")){
            solution = solveMax(tableSum);
        } else {
            usage();
        }
        
        // Make directory for the output file if it does not exist
        File outFile = new File(outputFile);
        outFile.getAbsoluteFile().getParentFile().mkdirs();
        Matrix.write(new FileOutputStream(outFile), solution);
    }

    private static void usage() {
        // How to use it
        System.out.println("java -jar GenMat <A/B> <input> <output>");
        System.exit(1);
    }
}
