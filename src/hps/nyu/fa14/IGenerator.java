package hps.nyu.fa14;

import java.util.List;

public interface IGenerator {

    List<Matrix> generate(TableSum tableSum);
    void addSolutionMonitor(ISolutionMonitor monitor);
}
