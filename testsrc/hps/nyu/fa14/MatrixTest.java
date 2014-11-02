package hps.nyu.fa14;

import static org.junit.Assert.*;

import org.junit.Test;

public class MatrixTest {

    @Test
    public void testSatisfies() {
        // Example from the spec
        TableSum tSum = new TableSum(4, 4);
        tSum.rowSums[0] = 3;
        tSum.rowSums[1] = 3;
        tSum.rowSums[2] = 2;
        tSum.rowSums[3] = 2;

        tSum.colSums[0] = 3;
        tSum.colSums[1] = 3;
        tSum.colSums[2] = 3;
        tSum.colSums[3] = 1;

        Matrix m = new Matrix(4, 4);
        m.values[0][0] = true;
        m.values[0][1] = true;
        m.values[0][2] = true;
        m.values[1][0] = true;
        m.values[1][1] = true;
        m.values[1][3] = true;
        m.values[2][0] = true;
        m.values[2][2] = true;
        m.values[3][1] = true;
        assertFalse(m.satisfies(tSum));

        m.values[3][2] = true;
        assertTrue(m.satisfies(tSum));
    }

}
