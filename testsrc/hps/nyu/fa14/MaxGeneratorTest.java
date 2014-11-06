package hps.nyu.fa14;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import hps.nyu.fa14.solve.MaxGenerator;

import java.util.List;

import org.junit.Test;

public class MaxGeneratorTest {

    //@Test
    public void testGenerateBest() throws Exception {
        TableSum tableSum = TableSum.parseFile("data/input_darwin.txt");
        MaxGenerator g = new MaxGenerator();
        List<Matrix> sol = g.generate(tableSum);
        Matrix m = sol.get(0);
        assertTrue(m.satisfies(tableSum));
        assertEquals(8910, m.correlation());
    }
}
