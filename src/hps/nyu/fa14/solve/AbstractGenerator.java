package hps.nyu.fa14.solve;

import hps.nyu.fa14.IGenerator;
import hps.nyu.fa14.ISolutionMonitor;
import hps.nyu.fa14.Matrix;
import hps.nyu.fa14.TableSum;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGenerator implements IGenerator {

    @Override
    public abstract List<Matrix> generate(TableSum tableSum);

    protected List<ISolutionMonitor> monitors = new ArrayList<ISolutionMonitor>();

    @Override
    public void addSolutionMonitor(ISolutionMonitor monitor) {
        monitors.add(monitor);
    }

    protected void notifyNewSolution(Matrix solution) {
        for (ISolutionMonitor m : monitors) {
            m.updateSolution(solution);
        }
    }

    private Matrix bestSolution;
    private int bestValue;

    protected boolean updateIfBest(Matrix m) {
        int corr = m.correlation();
        if (corr > bestValue) {
            bestSolution = m;
            bestValue = corr;
            notifyNewSolution(m);
            return true;
        }
        return false;
    }
}
