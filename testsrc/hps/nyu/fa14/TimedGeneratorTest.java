package hps.nyu.fa14;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import hps.nyu.fa14.solve.MaxGenerator;
import hps.nyu.fa14.solve.SwapGenerator;
import hps.nyu.fa14.solve.TimedGenerator;

import java.util.List;

import org.junit.Test;

public class TimedGeneratorTest {

    //@Test
    public void testMaxGenerateInTime() throws Exception {
        TableSum tableSum = TableSum.parseFile("data/input_3.txt");
        MaxGenerator g = new MaxGenerator();
        int maxTime = 10;
        IGenerator tg = new TimedGenerator(g, maxTime);
        long start = System.currentTimeMillis();
        List<Matrix> sol = tg.generate(tableSum);
        Matrix m = sol.get(0);
        assertTrue("TableSum is not satisfied", m.satisfies(tableSum));
        long elapsed = System.currentTimeMillis() - start;
        assertTrue("Solution took too long to find",
                elapsed < (maxTime * 1000));
        assertEquals(63940, m.correlation());
    }
    
    @Test
    public void testMaxSwapGenerateInTime() throws Exception {
        TableSum tableSum = TableSum.parseFile("data/input_3.txt");
        SwapGenerator g = new SwapGenerator();
        g.maxToGenerate = Integer.MAX_VALUE;
        int maxTime = 10;
        IGenerator tg = new TimedGenerator(g, maxTime);
        long start = System.currentTimeMillis();
        List<Matrix> sol = tg.generate(tableSum);
        Matrix m = sol.get(0);
        assertTrue("TableSum is not satisfied", m.satisfies(tableSum));
        long elapsed = System.currentTimeMillis() - start;
        assertTrue("Solution took too long to find",
                elapsed < (maxTime * 1000));
        assertTrue(m.correlation() > 63940);
    }
}
