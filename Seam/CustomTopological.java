/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;

public class CustomTopological {
    private double[][] energy;
    private int width, height;
    private int[] edgeTo;

    public CustomTopological(double[][] energy) {
        this.energy = energy;

        this.width = energy.length;
        this.height = energy[0].length;

        // boolean[] marked = new boolean[width * height + 2];
        // dfs(width * height, marked, order);

        double[] distTo = new double[width * height + 2];
        edgeTo = new int[width * height + 2];
        for (int i = 0; i < width * height + 2; i++) {
            distTo[i] = Double.POSITIVE_INFINITY;
            edgeTo[i] = -1;
        }

        distTo[width * height] = 0;
        int v = width * height;
        for (int e : edges(v))
            relax(v, e, distTo);

        for (v = 0; v < width * height + 2; v++) {
            for (int e : edges(v))
                relax(v, e, distTo);
        }
    }

    private void relax(int v, int e, double[] distTo) {
        double weight;
        if (e == width * height) {
            return;
        }
        if (e == width * height + 1) {
            weight = 1000;
        }
        else {
            int[] pair = zToxy(e);
            weight = energy[pair[0]][pair[1]];
        }
        if (distTo[e] > distTo[v] + weight) {
            distTo[e] = distTo[v] + weight;
            edgeTo[e] = v;
        }
    }

    private void dfs(int v, boolean[] marked, Stack<Integer> order) {
        if (marked[v]) {
            return;
        }
        marked[v] = true;

        for (int e : edges(v)) {
            dfs(e, marked, order);
        }
        order.push(v);
    }


    private Stack<Integer> edges(int v) {
        Stack<Integer> edges = new Stack<>();
        if (v == width * height + 1) return edges;

        if (v == width * height) {
            for (int i = 0; i < width; i++)
                edges.push(i);
            return edges;
        }

        int[] pair = zToxy(v);
        int col = pair[0];
        int row = pair[1];
        if (row == height - 1) {
            edges.push(width * height + 1);
            return edges;
        }

        if (col == 0) {
            edges.push(xyToz(col + 1, row + 1));
        }
        else if (col == width - 1) {
            edges.push(xyToz(col - 1, row + 1));
        }
        else {
            edges.push(xyToz(col + 1, row + 1));
            edges.push(xyToz(col - 1, row + 1));
        }
        edges.push(xyToz(col, row + 1));
        return edges;
    }

    // mapping from a 2-dimensional (row, col) pair to a 1-D index
    private int xyToz(int col, int row) {
        return row * width + col;
    }

    // mapping from 1-dimensional z to 2-D (row, col) pair
    private int[] zToxy(int z) {
        int[] pair = new int[2];
        pair[1] = z / width;
        pair[0] = z % width;

        return pair;
    }

    public Stack<Integer> pathTo(int e) {
        Stack<Integer> path = new Stack<>();
        for (int v = edgeTo[e]; v != -1; v = edgeTo[v]) {
            path.push(v);
        }
        return path;
    }
}
