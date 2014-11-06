package hps.nyu.fa14.solve;

import hps.nyu.fa14.IGenerator;
import hps.nyu.fa14.ISolutionMonitor;
import hps.nyu.fa14.Matrix;
import hps.nyu.fa14.SwapPosition;
import hps.nyu.fa14.TableSum;

import java.util.List;

public class LocalSearchGenerator extends AbstractGenerator implements
        ISolutionMonitor {

    private TableSum tableSum;
    private final IGenerator generator;

    public LocalSearchGenerator(IGenerator generator) {
        this.generator = generator;
        this.generator.addSolutionMonitor(this);
    }

    @Override
    public List<Matrix> generate(TableSum tableSum) {
        this.tableSum = tableSum;

        SolutionRunner newRunner = new SolutionRunner(generator);

        Thread t = new Thread(newRunner);
        t.setDaemon(true); // OK to die when the JVM exits
        t.start();

        while (true) {
            Matrix best = null;
            synchronized (tableSum) {
                best = bestSolution;
            }
            if (best == null) {
                try{
                Thread.sleep(10); // wait until an initial solution is ready
                } catch (Exception ex){ }
                continue;
            }

            int bestCorrelation = best.correlation();
            for (SwapPosition swapPos : SwapGenerator.getSwapPositionsRandom(best)) {
                Matrix newM = best.clone();
                swapPos.swap(newM);
                int corr = newM.correlation();
                if (corr > bestCorrelation) {
                    //System.out.println("Local Search improvement: " + corr);
                    synchronized (tableSum) {
                        updateIfBest(newM);
                    }
                    break;
                }
            }
        }
    }

    private class SolutionRunner implements Runnable {

        private IGenerator generator;

        public SolutionRunner(IGenerator g) {
            generator = g;
        }

        @Override
        public void run() {
            List<Matrix> s = generator.generate(tableSum);
            updateIfBest(s.get(0)); // see if this is actually the best
        }
    }

    @Override
    public void updateSolution(Matrix solution) {
        synchronized (tableSum) {
            if (solution.satisfies(tableSum)) {
//                int corr = solution.correlation();
//                if(bestSolution == null || corr > bestSolution.correlation()){
//                    System.out.println("External improvement: " + corr);
//                }
                updateIfBest(solution);
            }
        }
    }
}
