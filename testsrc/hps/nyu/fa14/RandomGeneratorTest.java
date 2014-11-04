package hps.nyu.fa14;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import hps.nyu.fa14.solve.RandomGenerator;

import java.util.List;

import org.junit.Test;

public class RandomGeneratorTest {

    @Test
    public void testGenerateRandom() throws Exception {
        TableSum tableSum = TableSum.parseFile("data/input_darwin.txt");
        IGenerator g = new RandomGenerator();
        List<Matrix> sol = g.generate(tableSum);
        Matrix m = sol.get(0);
        assertTrue(m.satisfies(tableSum));
    }

    @Test
    public void testGenerateRandom_25() throws Exception {

        for (int i = 0; i < 25; i++) {
            Matrix m = Matrix.random(25, 30);
            TableSum tableSum = m.getTableSum();
            assertTrue(m.satisfies(tableSum));

            IGenerator g = new RandomGenerator();
            List<Matrix> sol = g.generate(tableSum);
            Matrix m0 = sol.get(0);
            assertTrue(m0.satisfies(tableSum));
        }
    }

    @Test
    public void testGenerateRandom_10K() throws Exception {
        TableSum tableSum = TableSum.parseFile("data/input_darwin.txt");
        RandomGenerator g = new RandomGenerator();
        g.maxToGenerate = 10000;
        List<Matrix> sol = g.generate(tableSum);
        assertEquals(g.maxToGenerate, sol.size());
        for (int i = 0; i < sol.size(); i++) {
            assertTrue(sol.get(i).satisfies(tableSum));
        }
    }
}
