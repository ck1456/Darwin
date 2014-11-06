package hps.nyu.fa14.view;

import hps.nyu.fa14.IGenerator;
import hps.nyu.fa14.ISolutionMonitor;
import hps.nyu.fa14.Matrix;
import hps.nyu.fa14.TableSum;
import hps.nyu.fa14.solve.LocalSearchGenerator;
import hps.nyu.fa14.solve.MaxCorGenerator;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class DarwinMapplet extends Applet implements Runnable, ISolutionMonitor {

	private static final long serialVersionUID = 4901830364284199595L;

	private Thread t;
	private Graphics buffer;
	private Image image;
	private int width = 0;
	private int height = 0;

	@Override
	public void update(Graphics g) {
		if (width != getWidth() || height != getHeight()) {
			width = getWidth();
			height = getHeight();
			image = createImage(width, height);
			buffer = image.getGraphics();
		}
		render(buffer);
		g.drawImage(image, 0, 0, this);
	}

	@Override
	public void start() {
		if (t == null) {
			(t = new Thread(this)).start();
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				repaint();
				Thread.sleep(30);
			}
		} catch (Exception ex) {
		}
	}

	private Matrix solution;
	@SuppressWarnings("unused")
    private final Thread calcThread;

	public DarwinMapplet() throws Exception {
	    TableSum tableSum = TableSum.parseFile("../data/input_darwin.txt");

	    IGenerator g = new LocalSearchGenerator(new MaxCorGenerator());
		SolutionRunner runner = new SolutionRunner(g, tableSum);
		g.addSolutionMonitor(this);
		(calcThread = new Thread(runner)).start();
	}

	/**
	 * The main render routine
	 * 
	 * @param g
	 */
	private void render(Graphics g) {

		Matrix sol = solution;
		g.setColor(Color.black);
		g.fillRect(0, 0, width, height);

		int pWidth = width / sol.cols;
		int pHeight = height / sol.rows;
		
		int x = 0;
		int y = 0;
		g.setColor(Color.cyan);
		for(int i = 0; i < sol.rows; i++){
		    for(int j = 0; j < sol.cols; j++){
		        if(sol.values[i][j]){
		            x = pWidth * j;
		            y = pHeight * i;
		            g.fillRect(x,  y,  pWidth, pHeight);
		        }
		        
		    }
		}
	}

	private static class SolutionRunner implements Runnable {

        private IGenerator generator;
        private TableSum tableSum;
        public SolutionRunner(IGenerator g, TableSum t) {
            generator = g;
            tableSum = t;
        }

        @Override
        public void run() {
            generator.generate(tableSum);
        }
    }

    @Override
    public void updateSolution(Matrix solution) {
        this.solution = solution;
    }
}
