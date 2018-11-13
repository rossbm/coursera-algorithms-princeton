import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedBag;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    // Will this to false if twin can be solved
    // Will also return
    private boolean solveable = true;
    private int moves;
    private Stack<Board> path;
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        if (initial == null) throw new java.lang.IllegalArgumentException();

        // This is main searcher
        SearchTree solutionFinder = new SearchTree(initial);
        // This is for verifying that a solution does exist
        SearchTree twinFinder = new SearchTree(initial.twin());

        while (this.solveable) {
            // Alternate between tree that will find actual solution
            if (solutionFinder.step()) {
                this.path = solutionFinder.getPath();
                // Don't want to count initial board as a "move"
                this.moves = this.path.size() -1;
                break;
            }
            // And tree that checks if twin has a solution;
            else {
                this.solveable = !twinFinder.step();
                this.moves = -1;
                this.path = null;
            }
        }

    }
    // //is the initial board solvable?
    public boolean isSolvable() {
        return this.solveable;
    }
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return this.moves;
    }
    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return path;
    }

    // Wrapper class for tree
    // Implemented since alternating back and forth between two trees
    private class SearchTree {
        private MinPQ<SearchNode> searchTree = new MinPQ<SearchNode>();
        private SearchNode latestNode;
        private int moves;

        public SearchTree(Board initial) {
            // Initialize search node with 0 moves and no previous board
            // Consider initializing seatch tree with at least 4 points
            searchTree.insert(new SearchNode(initial, 0, null));
        }

        public boolean step() {
            this.moves++;
            // The dequed board (SearchNode) with the lowest priority
            this.latestNode = searchTree.delMin();
            if (latestNode.isGoal()) {
                return true;
            }
            else {
                for (SearchNode neighborNode : latestNode.neighbors()) {
                    this.searchTree.insert(neighborNode);
                }
                return false;
            }
        }

        public Stack<Board> getPath() {
            // chase pointers
            Stack<Board> path = new Stack<Board>();
            SearchNode node = this.latestNode;
            while (node != null) {
                path.push(node.board);
                node = node.parentNode;
            }
            return path;
        }
    }

    // Class that wraps board?
    // For adding to heap?
    private class SearchNode implements Comparable<SearchNode> {
        // This class is mainly used to keep track of priority
        // The compare to method will make use of priority;
        private int priority;
        // priority is based on moves
        private int moves;
        // Need to keep track of previous board to avoid adding it to search tree
        private SearchNode parentNode;
        // Should probably not be public, should wrape all necessary methods
        private Board board;
        public SearchNode(Board board, int moves, SearchNode parentNode) {
            this.board = board;
            this.parentNode = parentNode;
            this.moves = moves;
            // TODO: Implement way of choosing priority function
            this.priority = board.manhattan() + this.moves;
        }
        // TODO: Better way of breaking ties
        public int compareTo(SearchNode that) {
            return Integer.compare(this.priority, that.priority);
        }

        public Board getBoard() {
            return this.board;
        }

        public boolean isGoal() {
            return this.board.isGoal();
        }

        public Iterable<SearchNode> neighbors() {
            Iterable<Board> closeBoards = this.board.neighbors();
            LinkedBag<SearchNode> closeNodes = new LinkedBag<>();
            SearchNode node;
            Board parentBoard;
            // deal with starting null parentNode
            if (this.parentNode == null) {
                parentBoard = null;
            } else {
                parentBoard = parentNode.getBoard();
            }

            for (Board closeBoard : closeBoards) {
                if (!closeBoard.equals(parentBoard)) {
                    node = new SearchNode(closeBoard, this.moves + 1, this);
                    closeNodes.add(node);
                }
            }
            return closeNodes;
        }
    }

public static void main(String[] args) {
    // create initial board from file
    String filename = "puzzle36.txt";
    // In in = new In(args[0]);
    In in = new In(filename);
    int n = in.readInt();
    int[][] blocks = new int[n][n];
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            blocks[i][j] = in.readInt();
    Board initial = new Board(blocks);
    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable())
        StdOut.println("No solution possible");
    else {
        StdOut.println("Minimum number of moves = " + solver.moves());
        for (Board board : solver.solution())
            StdOut.println(board);
    }
}
}