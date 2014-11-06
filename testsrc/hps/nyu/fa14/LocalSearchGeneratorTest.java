package hps.nyu.fa14;

import static org.junit.Assert.assertTrue;
import hps.nyu.fa14.solve.LocalSearchGenerator;
import hps.nyu.fa14.solve.MaxCorGenerator;
import hps.nyu.fa14.solve.TimedGenerator;

import org.junit.Test;

public class LocalSearchGeneratorTest {

    @Test
    public void testLocalSearchGenerateInTime() throws Exception {
        TableSum tableSum = TableSum.parseFile("data/input_2.txt");
        int maxTime = 120;

        LocalSearchGenerator mg = new LocalSearchGenerator(new MaxCorGenerator());
        IGenerator tg = new TimedGenerator(mg, maxTime);
        Matrix m = tg.generate(tableSum).get(0);
        System.out.println("Correlation: " + m.correlation());
                
        assertTrue(m.correlation() > 63940);
    }
}
