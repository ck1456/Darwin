package hps.nyu.fa14;

import static org.junit.Assert.*;

import org.junit.Test;

public class TableSumTest {

    @Test
    public void testParseFile() throws Exception {
        TableSum tableSum = TableSum.parseFile("data/input_darwin.txt");
        assertNotNull(tableSum);
        assertEquals(13, tableSum.rows);
        assertEquals(17, tableSum.cols);
        assertEquals(12, tableSum.rowSums[4]);
        assertEquals(9, tableSum.colSums[14]);
    }

}
