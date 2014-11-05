package hps.nyu.fa14;

// Defines corners of a sub matrix which can be swapped and still preserve
// row and column sums:
// x11,y11 x12,y12
// x21,y21 x22,y22
public class SwapPosition {
    public int r11;
    public int c11;

    public int r12;
    public int c12;

    public int r21;
    public int c21;

    public int r22;
    public int c22;

    // Swap must be of the form
    // 1 0 or 0 1
    // 0 1 -- 1 0
    public boolean isValid(Matrix m) {
        boolean base = m.values[r11][c11];
        if ((base == m.values[r22][c22]) && (base != m.values[r12][c12])
                && (base != m.values[r21][c21])) {
            return true;
        }

        return false;
    }

    // Modifies the matrix according to this swap
    public void swap(Matrix m) {
        m.values[r11][c11] = !m.values[r11][c11];
        m.values[r12][c12] = !m.values[r12][c12];
        m.values[r21][c21] = !m.values[r21][c21];
        m.values[r22][c22] = !m.values[r22][c22];
    }
}
