import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.LinkedBag;

public final class Board {
    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    private int[] blocks;
    // Will have a flattened representation of the square
    private int squareDim;
    private int flatDim;
    // For caching
    private int manhattanPriority;
    private int hammingPriority;
    private boolean isSolved = true;
    private boolean beenChecked;
    private int zeroPosition;
    public Board(int[][] blocks) {
        this.squareDim = blocks[0].length;
        // Going to use a flattened array to represent
        // Adding 1, since easier to start at 1;
        // Will leave 0 index blank
        this.flatDim = (int) Math.pow(this.squareDim, 2) + 1;
        this.blocks = new int[this.flatDim];
        for (int i = 0; i < this.squareDim; i++) {
            for (int j = 0; j < this.squareDim; j++) {
                this.blocks[(i * this.squareDim) + j + 1] = blocks[i][j];
            }
        }
    }

    // It is a private method to prevent complaints about the API
    // It incorporates swapping
    // Not really certain if the grader will stil detect this and complain
    private Board(int[] orgBlocks, int idxA, int idxB) {
        this.flatDim = orgBlocks.length;
        this.squareDim = (int) Math.sqrt(this.flatDim - 1);
        this.blocks = new int[this.flatDim];
        // Copy original blocks
        for (int i = 0; i < this.flatDim; i++) {
            this.blocks[i] = orgBlocks[i];
        }
        int tmpVal = this.blocks[idxA];
        this.blocks[idxA] = this.blocks[idxB];
        this.blocks[idxB] = tmpVal;
    }

    // TODO: Implement a method to go from a 2d board to a 1d representation
    // Why?
    // private int[] flattenBoard(int[][] squareBoard){
    //
    // }

    // board dimension n
    public int dimension() {
        return this.squareDim;
    }

    public int hamming() {
        // number of blocks out of place
        if (!beenChecked) {
            checkState();
        }
        return this.hammingPriority;
    }
    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        if (!beenChecked) {
            checkState();
        }
        return this.manhattanPriority;
    }

    // return the column based on flat index
    private int getColumn(int idx) {
        return ((idx -1) % this.squareDim) + 1;
    }

    private int getRow(int idx) {
        return ((idx -1) / this.squareDim) + 1;
    }

    // is this board the goal board?
    public boolean isGoal() {
        if (!beenChecked) {
            checkState();
        }
        return this.isSolved;
    }

    // Will need to check position of zero block
    private int getZeroPosition() {
        if (!beenChecked) {
            checkState();
        }
        return this.zeroPosition;
    }

    // private method to ascertain if stuff has happened to avoid recalculating
    private void checkState() {
        int manhattanDist;
        this.beenChecked = true;
        for (int i = 1; i < this.flatDim; i++) {
            int currPiece = this.blocks[i];
            if (currPiece != i) {
                if (currPiece != 0) {
                    // Increment hamming priority
                    this.hammingPriority += 1;
                    // Manahattan distance is equal to sum of absolute values of
                    // difference between row and column indexes
                    manhattanDist = Math.abs(getColumn(currPiece) - getColumn(i))
                            + Math.abs(getRow(currPiece) - getRow(i));
                    this.manhattanPriority += manhattanDist;
                }
                else {
                    this.zeroPosition = i;
                }
            }
        }
        if (this.hammingPriority > 0) {
            this.isSolved = false;
        }
    }

    // This method goes returns the flattend index based on column and row
    private int getIndex(int row, int col) {
        return ((row -1) * this.squareDim) + col;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        if (!beenChecked) {
            checkState();
        }
        // default to swapping the square in (1,1) with the square (1,2);
        // position of the 0 square will allow us to deviate from default;
        if (this.zeroPosition != 1) {
            if (this.zeroPosition != 2) {
                // don't need to use getIndex, since no matter squareDim
                // this will be the flattened dims
                return new Board(this.blocks, 1, 2);
            }
            else {
                // swao (1,1) with (2,1)
                return new Board(this.blocks, 1, getIndex(2,1));
            }
        }
        else {
            // swap (1,2) with (2,2)
            return new Board(this.blocks, 2, getIndex(2,2));
        }
    }

    // Takes a row and column index and returns and integer iterable of the
    // Flattened indexes of the neighbours of the input
    // TODO: Make private, remove from main, potentialy rename
    private Iterable<Integer> getNeighbors(int row, int col) {
        LinkedBag<Integer> neighbors = new LinkedBag<Integer>();
        if (row < this.squareDim) {
            neighbors.add(getIndex(row + 1, col));
        }
        if (row > 1) {
            neighbors.add(getIndex(row - 1, col));
        }

        if (col < this.squareDim) {
            neighbors.add(getIndex(row, col+1));
        }
        if (col > 1) {
            neighbors.add(getIndex(row, col-1));
        }
        return neighbors;
    }

    // TODO: Implement, will need to check online on deets
    // does this board equal y?
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Board that = (Board) other;
        int n = this.flatDim;
        if (n != that.flatDim) return false;
        for (int i = 1; i < n; i++) {
            if (this.blocks[i] != that.blocks[i]) return false;
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        LinkedBag<Board> neighbors = new LinkedBag<Board>();
        // Want neighbors of 0 block, so get its index
        int zeroIndex = this.getZeroPosition();
        Iterable<Integer> neighborIndex = getNeighbors(getRow(zeroIndex), getColumn(zeroIndex));
        for (int index : neighborIndex) {
            neighbors.add(new Board(this.blocks, zeroIndex, index));
        }
        return neighbors;
    }
    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.squareDim);
        sb.append("\n");
        for (int i = 1; i < this.flatDim; i++) {
            sb.append(String.format("%2d", this.blocks[i]));
            sb.append(" ");
            if (i % this.squareDim == 0) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
    // unit tests (not graded)
    public static void main(String[] args) {
        String filename = "puzzle3x3-15.txt";
        In in = new In(filename);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);
        StdOut.println(initial.toString());
        StdOut.println("Hamming distance of " + initial.hamming());
        StdOut.println("Manahattan distance of " + initial.manhattan());
        StdOut.println("Is goal: " + initial.isGoal() + "\n");

        StdOut.println("The twin of this board is:");
        Board twin = initial.twin();
        StdOut.println(twin.toString());

        StdOut.println("The twin of this board equals the original:");
        StdOut.println(initial.equals(twin));

        StdOut.println("The twin's twin:");
        Board twin2 = twin.twin();
        StdOut.println(twin2.toString());

        StdOut.println("The twin's twin equals the original:");
        StdOut.println(initial.equals(twin2));

        // StdOut.println("The index neigbours of the top right corner are:");
        // Iterable<Integer> neighborsFirst = initial.getNeighbors(1, 1);
        // for (int index : neighborsFirst) {
        //     StdOut.println(index);
        // }
        //
        // StdOut.println("The index neigbours of the bottom right corner are:");
        // Iterable<Integer> neighborsLast = initial.getNeighbors(n, n);
        // for (int index : neighborsLast) {
        //     StdOut.println(index);
        // }
        //
        // StdOut.println("The index neigbours of the middle are:");
        // Iterable<Integer> neighborsMiddle = initial.getNeighbors(n/2 + 1, n/2 +1);
        // for (int index : neighborsMiddle) {
        //     StdOut.println(index);
        // }

        StdOut.println("The neigbours of the initial board are:");
        Iterable<Board> neighbors = initial.neighbors();
        for (Board board : neighbors) {
            StdOut.println(board.toString());
        }

    }
}