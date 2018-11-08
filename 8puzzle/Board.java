import edu.princeton.cs.algs4.StdOut;

public final class Board {
    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    private int[] blocks;
    private int pub_dim;
    private int dim;
    public Board(int[][] blocks) {
        this.pub_dim = blocks[0].length;
        this.dim = (int) Math.pow(this.pub_dim, 2);
        this.blocks = new int[this.dim];
        for (int i = 0; i < this.pub_dim; i++) {
            for (int j = 0; j < this.pub_dim; j++) {
                this.blocks[(i * this.pub_dim ) + j] = blocks[i][j];
            }
        }
    }
    // board dimension n
    public int dimension() {
        return this.dim;
    }
    // number of blocks out of place
    public int hamming() {
        //TODO: Actually calculate
        return 99;
    }
    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        //TODO: Actually calculate
        return 99;
    }
    // is this board the goal board?
    public boolean isGoal() {
        int prev_piece;
        int curr_piece;
        for (int i = 0; i < this.dim; i++) {
            //doesn't matter if the previous piece is a 0
            if (prev_piece != 0) {
                if (curr_piece == 0) {
                    prev_piece = curr_piece;
                }
                else if (this.blocks[i] != (prev_piece + 1)) {
                    return false;
                }
            }
        }
        return true;
    }
    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {

    }
    // does this board equal y?
    public boolean equals(Object y) {

    }
    // all neighboring boards
    public Iterable<Board> neighbors() {

    }
    // string representation of this board (in the output format specified below)
    public String toString() {
        StdOut.println(this.dim);
        for (int i = 0; i < this.dim)

    }
    // unit tests (not graded)
    public static void main(String[] args) {

    }
}