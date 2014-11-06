package hps.nyu.fa14.solve;

import hps.nyu.fa14.IGenerator;
import hps.nyu.fa14.ISolutionMonitor;
import hps.nyu.fa14.Matrix;
import hps.nyu.fa14.TableSum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiGenerator extends AbstractGenerator implements ISolutionMonitor {

	private final List<IGenerator> generators = new ArrayList<IGenerator>();

	public void addGenerator(IGenerator generator) {
		generator.addSolutionMonitor(this);
		generators.add(generator);
	}

	private TableSum currentTableSum;

	@Override
	public List<Matrix> generate(TableSum tableSum) {
	    currentTableSum = tableSum;

		List<Thread> runningThreads = new ArrayList<Thread>();
		for (IGenerator g : generators) {
			SolutionRunner newRunner = new SolutionRunner(g);

			Thread t = new Thread(newRunner);
			t.setDaemon(true); // OK to die when the JVM exits
			runningThreads.add(t);
			t.start();
		}

		// Wait on these threads to complete
		for (Thread t : runningThreads) {
			try {
				t.join();
			} catch (Exception ex) {
				System.out.println("Thread exception");
				ex.printStackTrace();
			}
		}
		synchronized (currentTableSum) {
			return Arrays.asList(bestSolution);
		}
	}

	private class SolutionRunner implements Runnable {

		private IGenerator generator;
		public SolutionRunner(IGenerator g) {
			generator = g;
		}

		@Override
		public void run() {
			List<Matrix> s = generator.generate(currentTableSum);
			updateSolution(s.get(0)); // see if this is actually the best
		}
	}

    @Override
    public void updateSolution(Matrix solution) {
        synchronized(currentTableSum){
            if(solution.satisfies(currentTableSum)){
                updateIfBest(solution);
            }
        }
    }
}
