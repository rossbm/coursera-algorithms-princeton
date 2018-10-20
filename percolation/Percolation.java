import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] gridOpeness; // 2d array that will be we will initiliaze in constructor?
    private int numOpen = 0;
    private final int gridSize; // should this be the number of rows`cols or total number of sites?
    private final int numSites;
    private final WeightedQuickUnionUF union;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        // Adding 2 to size to represent top and bottom virtual sites
        // Index of top site will be 0;
        // Index of bottom site is n**2 +2n +1
        // Adding n to represent virtual botoom and top sites
        numSites = (int) Math.pow(n, 2)+2+2*n;
        if (!(n > 0)) throw new IllegalArgumentException("invalid grid size");

        // Adding 1 to dimensions to make 1 based indexing easier.
        gridOpeness = new boolean[n+2][n+2];
        gridSize = n;

        // creating UnionFind object.
        union = new WeightedQuickUnionUF(numSites);

        // connect top virtual row to top site
         for (int  i = 1; i <= n+1; i++) {
            union.union(flattenIndexes(0, i), 0);
            gridOpeness[0][i] = true;
        }

        // connect bottom virtual row to bottom site
        for (int  i = 1; i <= n+1; i++) {
            union.union(flattenIndexes(n+1, i), numSites-1);
            gridOpeness[n+1][i] = true;
        }
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        checkIndexes(row, col);
        if (!gridOpeness[row][col]) {
            gridOpeness[row][col] = true;
            numOpen++;
            unionize(row, col, row+1, col);
            unionize(row, col, row, col+1);
            unionize(row, col, row-1, col);
            unionize(row, col, row, col-1);
        }
    }

    private void unionize(int thisRow, int thisCol, int nearRow, int nearCol) {
        // only connect sites that are open
        if (gridOpeness[nearRow][nearCol]) {
            int thisIndex = flattenIndexes(thisRow, thisCol);
            int nearIndex = flattenIndexes(nearRow, nearCol);
            union.union(thisIndex, nearIndex);
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkIndexes(row, col);
        return gridOpeness[row][col];
    }
    // is site (row, col) full(connected totop)?
    public boolean isFull(int row, int col) {
        checkIndexes(row, col);
        return union.connected(flattenIndexes(row, col), 0) && isOpen(row, col);
    }
    // number of open sites
    public int numberOfOpenSites() {
        return numOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        // check if virtual bottom site is connected to virtual top site.
        return union.connected(numSites-1, 0);
    }

    public static void main(String[] args) {
        // test client (optional)
    }

    // converts 2d index to 1d (flattends)
    private int flattenIndexes(int row, int col) {
        // if (row > gridSize) return numSites-1;
        int index;
        index = ((row-1) * this.gridSize) + col;
        return index > 0 ? index : 0;
    }
    private void checkIndexes(int row, int col) {
        if ((row > gridSize)
                || (col > gridSize)
                || (row <= 0)
                || (col <= 0)) throw new IllegalArgumentException("invalid index \n row: "+ row + " col: " + col);
    }
}