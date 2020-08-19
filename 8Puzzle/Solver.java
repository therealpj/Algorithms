import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

/* *****************************************************************************
 *  Name: Prajjwal Juyal
 *  Date: 18/08/20
 *  Description: Implements the A* search algorithm to solve the 8 puzzle
 **************************************************************************** */
public class Solver {

    private boolean solved;
    private boolean twinSolved;
    private Stack<Board> setOfMoves;
    private int numberOfMoves;

    // A SearchNode consists of a board, the number of moves required to reach it, and its
    // parent SearchNode
    private class SearchNode {
        private final Board board;
        private final int moves;
        private final SearchNode previous;
        private final int manhattan;

        private SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
            this.manhattan = board.manhattan();
        }

        private int priorityManhattan() {
            return this.manhattan + moves;
        }

        private int priorityHamming() {
            return board.hamming() + moves;
        }

    }

    // Contains priority functions which can be used to solve the puzzle
    private class PriorityFunctions {

        public Comparator<SearchNode> byHamming() {
            return (s1, s2) -> Integer.compare(s1.priorityHamming(), s2.priorityHamming());
        }

        public Comparator<SearchNode> byManhattan() {
            return (s1, s2) -> {

                if (s1.priorityManhattan() == s2.priorityManhattan()) {
                    return Integer.compare(s1.manhattan, s2.manhattan);
                }
                return Integer.compare(s1.priorityManhattan(), s2.priorityManhattan());
            };
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("no board to solve");
        }

        PriorityFunctions pf = new PriorityFunctions();

        // creating a searchnode of the initial board and adding it to the minpq
        MinPQ<SearchNode> minPQ = new MinPQ<>(pf.byManhattan());
        SearchNode first = new SearchNode(initial, 0, null);
        minPQ.insert(first);
        solved = false;

        // making a twin of the board to see if the given initial board is unsolvable
        // this is because only one of the board or its twin is solvable
        MinPQ<SearchNode> minPQTwin = new MinPQ<>(pf.byManhattan());
        SearchNode firstTwin = new SearchNode(initial.twin(), 0, null);
        minPQTwin.insert(firstTwin);
        twinSolved = false;

        SearchNode goalBoard = solve(minPQ, minPQTwin);

        setOfMoves = new Stack<>();
        numberOfMoves = -1;
        // Storing the set of moves required from initial board to goal board
        while (goalBoard != null) {
            setOfMoves.push(goalBoard.board);
            numberOfMoves += 1;
            goalBoard = goalBoard.previous;
        }
    }

    // solves one of initial board or its twin and returns the SearchNode containing the goal board, if exists
    private SearchNode solve(MinPQ<SearchNode> minPQ, MinPQ<SearchNode> minPQTwin) {
        SearchNode next = null;
        while (!solved || !twinSolved) {

            // If the min priority node contains the goal board, terminate search
            next = minPQ.delMin();
            if (next.board.isGoal()) {
                solved = true;
                break;
            }

            // adding its neighbors to the min priority queue
            for (Board b : next.board.neighbors()) {
                // optimization that doesn't add a previously added board
                if (next.previous != null && b.equals(next.previous.board)) {
                }
                else {
                    minPQ.insert(new SearchNode(b, next.moves + 1, next));
                }
            }

            // Check if the twin board is solvable
            next = minPQTwin.delMin();
            if (next.board.isGoal()) {
                twinSolved = true;
                next = null;
                break;
            }

            // adding its neighbors to the min priority queue
            for (Board b : next.board.neighbors()) {
                // optimization that doesn't add a previously added board
                if (next.previous != null && b.equals(next.previous.board)) {
                    continue;
                }
                minPQTwin.insert(new SearchNode(b, next.moves + 1, next));
            }
        }
        return next;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solved;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (twinSolved) {
            return -1;
        }
        return numberOfMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (twinSolved) {
            return null;
        }
        return setOfMoves;
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

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
