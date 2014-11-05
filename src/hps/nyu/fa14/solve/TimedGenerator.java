package hps.nyu.fa14.solve;

import hps.nyu.fa14.IGenerator;
import hps.nyu.fa14.ISolutionMonitor;
import hps.nyu.fa14.Matrix;
import hps.nyu.fa14.TableSum;

import java.util.Arrays;
import java.util.List;

public class TimedGenerator extends AbstractGenerator implements
        ISolutionMonitor, Runnable {

    private final int maxSeconds;
    private final IGenerator generator;
    private TableSum currentTableSum;
    // Make sure to give yourself this much overhead for setting up the thread
    private final int SETUP_MILLIS = 150;

    public TimedGenerator(IGenerator generator, int seconds) {
        this.generator = generator;
        maxSeconds = seconds;
    }

    @SuppressWarnings("deprecation")
    // Yes, I know stop is deprecated, but that is silly
    @Override
    public List<Matrix> generate(TableSum tableSum) {

        // Set up the filler to report the best solution reported so far
        currentTableSum = tableSum;

        // run a thread.
        Thread solveThread = new Thread(this);
        solveThread.start();
        try {
            // Wait until the thread finishes or we time out
            solveThread.join((maxSeconds * 1000) - SETUP_MILLIS);
        } catch (Exception ex) {/* suppress */
            System.out.println();
        }

        if (solveThread.isAlive()) {
            // Interrupt does not do what we need (or expect) it to do, so fail
            // solveThread.interrupt();
            solveThread.stop();
        }
        // Wait a certain amount of time, then kill and output
        return Arrays.asList(bestSolution);
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        generator.addSolutionMonitor(this);
        List<Matrix> s = generator.generate(currentTableSum);
        // If we get this far, assume the solver returned the best assignment
        updateSolution(s.get(0));
    }

    private Matrix bestSolution;

    @Override
    public void updateSolution(Matrix solution) {
        if(updateIfBest(solution)){
            bestSolution = solution;
        }
    }
}
