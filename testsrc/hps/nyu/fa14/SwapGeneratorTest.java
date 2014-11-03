package hps.nyu.fa14;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import hps.nyu.fa14.solve.SwapGenerator;

import java.util.List;

import org.junit.Test;

public class SwapGeneratorTest {

    @Test
    public void testGenerateBySwap_1K() throws Exception {
        TableSum tableSum = TableSum.parseFile("data/input_darwin.txt");
        SwapGenerator g = new SwapGenerator();
        g.maxToGenerate = 1000;
        List<Matrix> sol = g.generate(tableSum);
        assertNotNull(sol);
        assertEquals(g.maxToGenerate, sol.size());
    }

    @Test
    public void testGenerateBySwap_10K() throws Exception {
        TableSum tableSum = TableSum.parseFile("data/input_darwin.txt");
        SwapGenerator g = new SwapGenerator();
        g.maxToGenerate = 10000;
        List<Matrix> sol = g.generate(tableSum);
        assertNotNull(sol);
        assertEquals(g.maxToGenerate, sol.size());
    }
    
    @Test
    public void testGenerateBySwap_25K() throws Exception {
        TableSum tableSum = TableSum.parseFile("data/input_darwin.txt");
        SwapGenerator g = new SwapGenerator();
        g.maxToGenerate = 25000;
        List<Matrix> sol = g.generate(tableSum);
        assertNotNull(sol);
        assertEquals(g.maxToGenerate, sol.size());
    }
}
