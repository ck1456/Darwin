package hps.nyu.fa14;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import hps.nyu.fa14.solve.TrivialGenerator;

import org.junit.Test;

public class TrivialGeneratorTest {

    @Test
    public void testSatisfies() throws Exception {
        TableSum tableSum = TableSum.parseFile("data/input_darwin.txt");
        IGenerator g = new TrivialGenerator();
        Matrix m = g.generate(tableSum).get(0);
        assertNotNull(m);
        assertTrue(m.satisfies(tableSum));
    }
    
    @Test
    public void testRandomSatisfies() throws Exception {
        // Generate a random matrix
        Matrix m0 = Matrix.random(30, 40);
        // get the table sums
        TableSum tableSum = m0.getTableSum();
        assertTrue(m0.satisfies(tableSum));
        
        // Then try to solve it
        IGenerator g = new TrivialGenerator();
        Matrix m = g.generate(tableSum).get(0);
        assertTrue(m.satisfies(tableSum));
    }

}
