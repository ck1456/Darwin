package hps.nyu.fa14;

public interface ISolutionMonitor {

    /**
     * Notify a monitor that a new (better) assignment has been produced
     * @param a
     */
    void updateSolution(Matrix solution);
}
