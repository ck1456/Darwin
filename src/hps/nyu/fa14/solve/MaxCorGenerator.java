package hps.nyu.fa14.solve;

import hps.nyu.fa14.Matrix;
import hps.nyu.fa14.TableSum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Given a matrix, find a distinct matrix that satisfies the same constraints by
 * swapping two col positions in two different rows
 * 
 * @author ck1456@nyu.edu
 */
public class MaxCorGenerator extends AbstractGenerator {

  public int populationSize = 100;
  public int generations = 3500;
  public double mutationProb = 0.9;
  public double flipProb = 0.5;
  private Random rand = new Random();
  private List<Integer> rowsNotAllowed = new ArrayList<Integer>();
  private List<Integer> colsNotAllowed = new ArrayList<Integer>();

  //    @Override
  /*
    public List<Matrix> generate2(TableSum tableSum) {

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
          Matrix newM = mutate2(m0);
          corr = newM.correlation2();
          if(maxCorr < corr) {
            maxCorr = corr;
            bestCorMatrix = newM;
            updateIfBest(bestCorMatrix);
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
            Matrix newM = mutate2(m0);
            corr = newM.correlation2();
            if(maxCorr < corr) {
              maxCorr = corr;
              bestCorMatrix = newM;
              updateIfBest(bestCorMatrix);
            }
            population.add(newM);
          }
          populations.add(population);
          t++;
        }

        System.out.println("Best correlation " + maxCorr);
        System.out.println("Is satisfied: "+bestCorMatrix.satisfies(tableSum));

        return Arrays.asList(bestCorMatrix);
    }
   */

  public List<Matrix> generate(TableSum tableSum) {
    int bestCorr = 0;
    List<Matrix> bestMatrix = new ArrayList<Matrix>();
    while(true) {
      bestMatrix = generate4(tableSum);
      int corr = bestMatrix.get(0).correlation2();
      if(bestCorr < corr) {
        bestCorr = corr;
        updateIfBest(bestMatrix.get(0));
      }
    }
  }
  
  //@Override
  public List<Matrix> generate4(TableSum tableSum) {

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

    RandomGenerator r = new RandomGenerator();
    r.maxToGenerate = 1;
    List<Matrix> population = new ArrayList<Matrix>();
    Matrix m0 = r.generate(tableSum).get(0);
    population.add(m0);
    for(int i=1;i<populationSize;i++) {
      //change random bits and fix the matrix
      //mutate3 does this
      population.add(mutate3(m0,tableSum));
    }
    int t = 0;
    Matrix bestMatrix = m0.clone();
    int bestCorr = bestMatrix.correlation2();
    while(t < generations) {
      Matrix [] bestTwo = getBestParents(population);
      int corr1 = bestTwo[0].correlation2();
      int corr2 = bestTwo[1].correlation2();
      if(corr1 <= corr2) {
        if(bestCorr < corr2) {
          //bestCorr = corr2;
          //updateIfBest(bestTwo[1]);
          //bestTwo[1] = MaxGenerator.localSearch(bestTwo[1]);
          bestCorr = bestTwo[1].correlation2();
          bestMatrix = bestTwo[1];
          updateIfBest(bestMatrix);
        }
      }
      else {
        if(bestCorr < corr1) {
          //updateIfBest(bestTwo[0]);
          //bestTwo[0] = MaxGenerator.localSearch(bestTwo[0]);
          bestCorr = bestTwo[0].correlation2();
          bestMatrix = bestTwo[0];
          updateIfBest(bestMatrix);
        }
      }
      //combine to get another population
      for(int i=0;i<populationSize/2;i++) {
        m0 = combine(bestTwo[0], bestTwo[1], tableSum);
        population.set(i,m0);
      }
      for(int i=populationSize/2;i<populationSize;i++) {
        //change random bits and fix the matrix
        //mutate3 does this
        population.set(i,mutate2(population.get(i - populationSize/2)));
      }
      t++;
    }

    //System.out.println("Best correlation " + bestCorr);
    //System.out.println("Is satisfied: "+bestMatrix.satisfies(tableSum));

    return Arrays.asList(bestMatrix);
  }


  //@Override
  public List<Matrix> generate3(TableSum tableSum) {
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

    RandomGenerator r = new RandomGenerator();
    r.maxToGenerate = 1;
    List<Matrix> population = new ArrayList<Matrix>();
    Matrix m0 = r.generate(tableSum).get(0);
    population.add(m0);
    for(int i=1;i<populationSize;i++) {
      //change random bits and fix the matrix
      //mutate3 does this
      population.add(mutate3(m0,tableSum));
    }
    Comparator<Matrix> cmp = new Comparator<Matrix>() {
      public int compare(Matrix m1, Matrix m2) {
        return (m1.correlation2() - m2.correlation2());
      }
    };
    Collections.sort(population,cmp);
    Collections.reverse(population);
    int t = 0;
    int bestCorr = 0;
    Matrix bestMatrix = population.get(0).clone();
    bestCorr = bestMatrix.correlation2();
    updateIfBest(bestMatrix);
    while(t < generations) {
      for(int i=0;i<populationSize/2;i++) {
        m0 = combine2(population, tableSum);
        population.set(i,m0);
      }
      for(int i=populationSize/2;i<populationSize;i++) {
        //mutate in a way that the row and col sums are still valid
        population.set(i,mutate2(population.get(i - populationSize/2)));
      }
      Collections.sort(population,cmp);
      Collections.reverse(population);
      int corr = population.get(0).correlation2();
      if(bestCorr < corr) {
        bestCorr = corr;
        bestMatrix = population.get(0);
        updateIfBest(bestMatrix);
      }
      t++;
    }

    System.out.println("Best correlation " + bestCorr);
    System.out.println("Is satisfied: "+bestMatrix.satisfies(tableSum));

    return Arrays.asList(bestMatrix);
  }

  private Matrix combine(List<Matrix> population,TableSum tableSum) {
    Matrix m = new Matrix(tableSum.rows, tableSum.cols);
    boolean flag = true;
    while(!m.satisfies(tableSum) || flag) {
      for(int i=0;i<tableSum.rows/2;i++) {
        //select from the best 2 parents randomly
        if(rand.nextBoolean()) {
          //pick from best
          for(int j=0;j<tableSum.cols;j++) {
            m.values[i][j] = population.get(0).values[i][j];
          }
        }
        else {
          //pick from second best
          for(int j=0;j<tableSum.cols;j++) {
            m.values[i][j] = population.get(1).values[i][j];
          }
        }
      }
      for(int i=tableSum.rows/2;i<tableSum.rows;i++) {
        //select from all parents
        int n = rand.nextInt(population.size());
        //pick from n th member in the population
        for(int j=0;j<tableSum.cols;j++) {
          m.values[i][j] = population.get(n).values[i][j];
        }
      }
      RandomGenerator.repairColumns(m, tableSum);
      flag = false;
    }
    return m;
  }
  
  private Matrix combine2(List<Matrix> population,TableSum tableSum) {
    Matrix m = new Matrix(tableSum.rows, tableSum.cols);
    boolean flag = true;
    while(!m.satisfies(tableSum) || flag) {
      for(int i=0;i<tableSum.rows;i++) {
        //select from the best 4 parents randomly
        int n = rand.nextInt(4);
        for(int j=0;j<tableSum.cols;j++) {
          m.values[i][j] = population.get(n).values[i][j];
        }
      }
      RandomGenerator.repairColumns(m, tableSum);
      flag = false;
    }
    return m;
  }

  private Matrix [] getBestParents(List<Matrix> population) {
    int bestCor = 0;
    Matrix [] bestTwo = new Matrix[2];
    bestTwo[0] = population.get(0);
    bestTwo[1] = population.get(0);
    for(Matrix m:population) {
      int corr = m.correlation2();
      if(bestCor < corr) {
        bestCor = corr;
        bestTwo[1] = bestTwo[0];
        bestTwo[0] = m;
      }
    }
    return bestTwo;
  }

  private Matrix combine(Matrix m1, Matrix m2, TableSum tableSum) {
    Matrix m = m1.clone();
    boolean flag = true;
    while(!m.satisfies(tableSum) || flag) {
      Set<Integer> rowsReplaced = new HashSet<Integer>();
      int i = 0;
      while(i < m.rows/2) {
        int rowNum = rand.nextInt(m.rows);
        if(!rowsReplaced.contains(rowNum)) {
          //we just replace this row with m2's row
          for(int j=0;j<m.cols;j++) {
            m.values[rowNum][j] = m2.values[rowNum][j];
          }
          rowsReplaced.add(rowNum);
          i++;
        }
      }
      RandomGenerator.repairColumns(m, tableSum);
      flag = false;
    }
    return m;
  }

  private Matrix mutate3(Matrix matrix, TableSum tableSum) {
    Matrix m = matrix.clone();
    int numRepairTries = 10;
    boolean flag = true;
    while(!m.satisfies(tableSum) || flag) {
      for(int i=0;i<m.rows;i++) {
        for(int j=0;j<m.cols;j++) {
          int n = rand.nextInt(100);
          if(n < mutationProb * 100) {
            //mutate
            if(rand.nextBoolean()) {
              m.values[i][j] = !m.values[i][j];
            }
          }
        }
      }
      int numRepairAttempts = 0;
      while(numRepairAttempts < numRepairTries) {
        RandomGenerator.repairRows(m, tableSum);
        RandomGenerator.repairColumns(m, tableSum);
        numRepairAttempts++;
      }
      flag = false;
    }
    return m;
  }

  private Matrix mutate2(Matrix m) {
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
      while(!rowsNotAllowed.contains(rowSwap) && rowSwap != rowToSwap && newM.values[rowSwap][colToSwap] != newM.values[rowToSwap][colToSwap]) {
        rowSwap = rand.nextInt(m.rows);
      }
      newM.values[rowSwap][colToSwap] = !newM.values[rowSwap][colToSwap];
      int colSwap = rand.nextInt(m.cols);
      while(!colsNotAllowed.contains(colSwap) && colSwap != colToSwap && newM.values[rowToSwap][colSwap] != newM.values[rowToSwap][colToSwap]) {
        colSwap = rand.nextInt(m.cols);
      }
      newM.values[rowToSwap][colSwap] = !newM.values[rowToSwap][colSwap];
      if(newM.values[rowSwap][colSwap] != newM.values[rowToSwap][colToSwap]) {
        flag = true;
        newM.values[rowSwap][colSwap] = !newM.values[rowSwap][colSwap];
      }
      else {
        newM.values[rowToSwap][colToSwap] = !newM.values[rowToSwap][colToSwap];
        newM.values[rowSwap][colToSwap] = !newM.values[rowSwap][colToSwap];
        newM.values[rowToSwap][colSwap] = !newM.values[rowToSwap][colSwap];
      }
    }
    return newM;
  }
}
