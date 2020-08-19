/* *****************************************************************************
 *  Name: Prajjwal Juyal
 *  Date: 18/08/20
 *  Description: An immutable data type that models an n-by-n board with sliding
 *  tiles.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Board {

    private final int[][] tiles;
    private final int size;
    private int manhattan;
    private int hamming;

    // assign the tiles
    public Board(int[][] tiles) {
        size = tiles.length;
        this.tiles = new int[size][size];
        makeClone(tiles, this.tiles);
        manhattan = -1;
        hamming = -1;
    }

    // String representation of this board
    public String toString() {
        StringBuilder board = new StringBuilder();
        board.append(size + "\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board.append(String.format("%2d ", tiles[i][j]));
            }
            board.append("\n");
        }
        return board.toString();
    }

    // is this the goal board?
    public boolean isGoal() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] != value(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }

    // board dimensions
    public int dimension() {
        return size;
    }

    // does this board equal some object?
    public boolean equals(Object y) {

        if (y == this) {
            return true;
        }

        if (y == null) {
            return false;
        }
        // not equal if the y object is not a board
        if (y.getClass() != this.getClass()) {
            return false;
        }

        Board that = (Board) y;
        if (that.size != this.size) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (that.tiles[i][j] != this.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twinTiles = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                twinTiles[i][j] = tiles[i][j];
            }
        }

        // swapping the tile at (0,0) with (0,1) unless either of them is an empty tile
        int i = 0, j = 0;
        if (twinTiles[i][j] == 0 || twinTiles[i][j + 1] == 0) {
            i = 1;
        }
        int temp = twinTiles[i][j];
        twinTiles[i][j] = twinTiles[i][j + 1];
        twinTiles[i][j + 1] = temp;

        return new Board(twinTiles);
    }

    // all neighbouring boards
    public Iterable<Board> neighbors() {
        // storing the neighboring boards in a stack
        Stack<Board> neighbors = new Stack<>();
        int i, j = 0;
        boolean found = false;

        // finds the position of the blank tile
        for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                if (tiles[i][j] == 0) {
                    found = true;
                    break;
                }
            }
            if (found) break;
        }
        int[][] tilesClone = new int[size][size];

        // swapping above
        if (i != 0) {
            makeClone(this.tiles, tilesClone);
            tilesClone[i][j] = tilesClone[i - 1][j];
            tilesClone[i - 1][j] = 0;
            neighbors.push(new Board(tilesClone));
        }

        // swapping below
        if (i != size - 1) {
            makeClone(this.tiles, tilesClone);
            tilesClone[i][j] = tilesClone[i + 1][j];
            tilesClone[i + 1][j] = 0;
            neighbors.push(new Board(tilesClone));
        }

        // swapping left
        if (j != 0) {
            makeClone(this.tiles, tilesClone);
            tilesClone[i][j] = tilesClone[i][j - 1];
            tilesClone[i][j - 1] = 0;
            neighbors.push(new Board(tilesClone));
        }

        // swapping right
        if (j != size - 1) {
            makeClone(this.tiles, tilesClone);
            tilesClone[i][j] = tilesClone[i][j + 1];
            tilesClone[i][j + 1] = 0;
            neighbors.push(new Board(tilesClone));
        }
        return neighbors;
    }

    // makes a clone of the original array
    private void makeClone(int[][] original, int[][] copy) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                copy[i][j] = original[i][j];
            }
        }
    }

    //

    // number of tiles out of place
    public int hamming() {
        // calculate hamming only if we haven't already
        if (hamming == -1) {
            hamming = 0;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (tiles[i][j] != value(i, j) && tiles[i][j] != 0) {
                        hamming += 1;
                    }
                }
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        // compute manhattan only if it not already
        if (manhattan == -1) {
            manhattan = 0;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (tiles[i][j] != value(i, j)) {
                        manhattan += distance(tiles[i][j], i, j);
                    }
                }
            }
        }

        return manhattan;
    }

    // returns which value should the current position be having
    private int value(int i, int j) {
        if (i == size - 1 && j == size - 1) {
            return 0;
        }
        return i * size + (j + 1);
    }

    // returns the distance from the current tile's original position
    private int distance(int num, int i, int j) {
        int actualI, actualJ;
        if (num == 0) {
            return 0;
        }
        else {
            num -= 1;
            actualI = num / size;
            actualJ = num % size;
        }
        return Math.abs(actualI - i) + Math.abs(actualJ - j);
    }

    // unit testing
    public static void main(String[] args) {

        // takes in a file as argument
        String filename = args[0];

        In in = new In(filename);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }

        // checking toString() and constructor
        Board board1 = new Board(tiles);
        StdOut.println("Dimensions of the board: " + board1.dimension());
        StdOut.println(board1);
        StdOut.println(board1.isGoal());

        Board board2 = new Board(tiles);
        StdOut.println(board1.equals(board2));

        StdOut.println("Twin of board:");
        StdOut.println(board1.twin());

        StdOut.println("Hamming distance: " + board1.hamming());
        StdOut.println("Manhattan distance: " + board1.manhattan());
        StdOut.println("Printing neighbors of board");
        StdOut.println("--------");
        for (Board b : board1.neighbors()) {
            StdOut.println(b);
            StdOut.println("--------");
        }

    }
}

