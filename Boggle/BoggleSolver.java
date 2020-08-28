/* *****************************************************************************
 *  Name: Prajjwal Juyal
 *  Date: 27/08/20
 *  Description: An immutable data type which takes in a dictionary as an argument,
 *  and solves any BoggleBoard passed to it
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.HashSet;

public class BoggleSolver {
    private Custom<Integer> rstst;
    private HashSet<String> words;
    private BoggleBoard board;
    private HashMap<Integer, Queue<int[]>> neigh;
    private int row, col;
    private HashMap<Integer, Integer> points;
    private boolean[][] marked;

    public BoggleSolver(String[] dictionary) {
	rstst = new Custom<>();
	for (int i = 0; i < dictionary.length; i++)
	    if (dictionary[i].length() > 2)
		rstst.put(dictionary[i], i + 1);

	points = new HashMap<>();
	points.put(3, 1);
	points.put(4, 1);
	points.put(5, 2);
	points.put(6, 3);
	points.put(7, 5);
	marked = new boolean[4][4];
	row = 4;
	col = 4;
	compute();
    }

    private void compute() {
	neigh = new HashMap<>();
	for (int i = 0; i < row; i++) {
	    for (int j = 0; j < col; j++) {
		neigh.put(i * col + j, neighbors(i, j));
	    }
	}
    }

    public Iterable<String> getAllValidWords(BoggleBoard boggleBoard) {
	words = new HashSet<>();
	this.board = boggleBoard;

	if (row != boggleBoard.rows() || col != boggleBoard.cols()) {
	    this.row = boggleBoard.rows();
	    this.col = boggleBoard.cols();
	    compute();
	}

	if (marked == null || row != marked.length || col != marked[0].length)
	    marked = new boolean[row][col];

	for (int i = 0; i < row; i++) {
	    for (int j = 0; j < col; j++) {
		dfs("", i, j);
	    }
	}
	return words;
    }

    private void dfs(String word, int i, int j) {
	char c = board.getLetter(i, j);
	word += c == 'Q' ? "QU" : c;

	if (word.length() > 2 && rstst.contains(word)) {
	    words.add(word);
	}
	if (!rstst.isPrefix(word)) return;

	marked[i][j] = true;
	for (int[] n : neigh.get(i * col + j))
	    if (!marked[n[0]][n[1]])
		dfs(word, n[0], n[1]);
	marked[i][j] = false;
    }

    private Queue<int[]> neighbors(int i, int j) {
	Queue<int[]> neighbors = new Queue<>();
	if (j > 0) {
	    neighbors.enqueue(new int[] { i, j - 1 });
	    if (i > 0) neighbors.enqueue(new int[] { i - 1, j - 1 });
	    if (i + 1 < row) neighbors.enqueue(new int[] { i + 1, j - 1 });
	}

	if (j + 1 < col) {
	    neighbors.enqueue(new int[] { i, j + 1 });
	    if (i > 0) neighbors.enqueue(new int[] { i - 1, j + 1 });
	    if (i + 1 < row) neighbors.enqueue(new int[] { i + 1, j + 1 });
	}
	if (i > 0) neighbors.enqueue(new int[] { i - 1, j });
	if (i + 1 < row) neighbors.enqueue(new int[] { i + 1, j });

	return neighbors;
    }

    public int scoreOf(String word) {
	int l = word.length();
	if (!rstst.contains(word) || l < 3) return 0;
	return points.getOrDefault(l, 11);
    }


    public static void main(String[] args) {
	long start = System.currentTimeMillis();
	In in = new In(args[0]);
	String[] dictionary = in.readAllStrings();
	BoggleSolver solver = new BoggleSolver(dictionary);
	BoggleBoard board = new BoggleBoard(args[1]);
	int score = 0;
	for (String word : solver.getAllValidWords(board)) {
	    StdOut.println(word);
	    score += solver.scoreOf(word);
	}
	StdOut.println("Score = " + score);

	StdOut.println(System.currentTimeMillis() - start);


    }
}
