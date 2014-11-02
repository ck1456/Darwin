package hps.nyu.fa14;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
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
        
        // get the table sums
        
        // Then try to solve it
        
        fail();
    }

}
