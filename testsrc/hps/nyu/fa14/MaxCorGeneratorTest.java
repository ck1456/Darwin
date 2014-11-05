package hps.nyu.fa14;

import static org.junit.Assert.assertEquals;
import hps.nyu.fa14.solve.MaxCorGenerator;

import java.util.List;

import org.junit.Test;

public class MaxCorGeneratorTest {

    @Test
    public void testGenerateBest_3() throws Exception {
        TableSum tableSum = TableSum.parseFile("data/input_3.txt");
        IGenerator g = new MaxCorGenerator();
        List<Matrix> sol = g.generate(tableSum);
        Matrix m = sol.get(0);
        assertEquals(8770, m.correlation());
    }
}
