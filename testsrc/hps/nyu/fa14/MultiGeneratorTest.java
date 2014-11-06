package hps.nyu.fa14;

import static org.junit.Assert.assertTrue;
import hps.nyu.fa14.solve.MaxCorGenerator;
import hps.nyu.fa14.solve.MultiGenerator;
import hps.nyu.fa14.solve.TimedGenerator;

import org.junit.Test;

public class MultiGeneratorTest {

    @Test
    public void testMaxGenerateInTime() throws Exception {
        TableSum tableSum = TableSum.parseFile("data/input_3.txt");
        int maxTime = 20;

        MultiGenerator mg = new MultiGenerator();
        mg.addGenerator(new MaxCorGenerator());
        mg.addGenerator(new MaxCorGenerator());
        mg.addGenerator(new MaxCorGenerator());
        mg.addGenerator(new MaxCorGenerator());
        mg.addGenerator(new MaxCorGenerator());
        mg.addGenerator(new MaxCorGenerator());
        //mg.addGenerator(new MaxGenerator());
        IGenerator tg = new TimedGenerator(mg, maxTime);
        Matrix m = tg.generate(tableSum).get(0);
        System.out.println("Correlation: " + m.correlation());
                
        assertTrue(m.correlation() > 63940);
    }

}
