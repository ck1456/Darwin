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
    
    @Test
    public void testEquals(){
       Matrix m1 = new Matrix(2, 2);
       m1.values[0][0] = true;
       m1.values[1][0] = true;
        
       Matrix m2 = new Matrix(2, 2);
       m2.values[0][0] = true;
       assertFalse(m1.equals(m2));
       assertFalse(m2.equals(m1)); // check symmetry
       m2.values[1][0] = true;
       assertTrue(m1.equals(m2));
       assertTrue(m2.equals(m1)); // check symmetry
    }
    
    @Test
    public void testClone(){
       Matrix m1 = Matrix.random(25, 40);
       Matrix m2 = m1.clone();
       assertFalse(m1 == m2);
       assertTrue(m1.equals(m2));
       assertTrue(m2.equals(m1));

       m2.values[13][4] = !m2.values[13][4];
       assertFalse(m1.equals(m2));
       assertFalse(m2.equals(m1));
    }
    
    @Test
    public void testHashCode(){
       Matrix m1 = Matrix.random(25, 40);
       Matrix m2 = m1.clone();
       
       int m1Hash = m1.hashCode();
       int m2Hash = m2.hashCode();
       assertEquals(m1Hash, m2Hash);
    }


}
