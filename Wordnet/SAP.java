/* *****************************************************************************
 *  Name: Prajjwal Juyal
 *  Date: 22/08/20
 *  Description: An immutable data type which finds the shortest ancestral paths
 *  between two vertices or two subsets of vertices
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class SAP {

    private final Digraph G;

    // constructor that takes in a digraph
    public SAP(Digraph G) {
        validate(G);
        this.G = new Digraph(G);
    }


    public int length(int v, int w) {
        // a vertex is an ancestor of itself
        if (v == w) {
            return 0;
        }

        HashMap<Integer, Integer> vDistTo = new HashMap<>();
        Queue<Integer> ancestors = new Queue<>();
        vDistTo.put(v, 0);
        ancestors.enqueue(v);

        while (!ancestors.isEmpty()) {
            int ancestor = ancestors.dequeue();
            for (int i : G.adj(ancestor)) {
                if (!vDistTo.containsKey(i)) {
                    vDistTo.put(i, vDistTo.get(ancestor) + 1);
                    ancestors.enqueue(i);
                }
            }
        }


        HashMap<Integer, Integer> wDistTo = new HashMap<>();
        int minAnc = -1;
        int minDist = -1;
        wDistTo.put(w, 0);
        ancestors.enqueue(w);

        while (!ancestors.isEmpty()) {
            int ancestor = ancestors.dequeue();
            if (vDistTo.containsKey(ancestor)) {
                if (minDist == -1) {
                    minDist = vDistTo.get(ancestor) + wDistTo.get(ancestor);
                    minAnc = ancestor;
                }
                else if (minDist > vDistTo.get(ancestor) + wDistTo.get(ancestor)) {
                    minDist = vDistTo.get(ancestor) + wDistTo.get(ancestor);
                    minAnc = ancestor;
                }

            }
            for (int i : G.adj(ancestor)) {
                if (!wDistTo.containsKey(i)) {
                    ancestors.enqueue(i);
                    wDistTo.put(i, wDistTo.get(ancestor) + 1);
                }
            }


        }


        return minDist;
    }

    public int ancestor(int v, int w) {

        HashMap<Integer, Integer> vDistTo = new HashMap<>();
        Queue<Integer> ancestors = new Queue<>();
        vDistTo.put(v, 0);
        ancestors.enqueue(v);

        while (!ancestors.isEmpty()) {
            int ancestor = ancestors.dequeue();
            for (int i : G.adj(ancestor)) {
                if (!vDistTo.containsKey(i)) {
                    vDistTo.put(i, vDistTo.get(ancestor) + 1);
                    ancestors.enqueue(i);
                }
            }
        }

        HashMap<Integer, Integer> wDistTo = new HashMap<>();
        int minAnc = -1;
        int minDist = -1;
        wDistTo.put(w, 0);
        ancestors.enqueue(w);

        while (!ancestors.isEmpty()) {
            int ancestor = ancestors.dequeue();
            if (vDistTo.containsKey(ancestor)) {
                if (minDist == -1) {
                    minDist = vDistTo.get(ancestor) + wDistTo.get(ancestor);
                    minAnc = ancestor;
                }
                else if (minDist > vDistTo.get(ancestor) + wDistTo.get(ancestor)) {
                    minDist = vDistTo.get(ancestor) + wDistTo.get(ancestor);
                    minAnc = ancestor;
                }

            }
            for (int i : G.adj(ancestor)) {
                if (!wDistTo.containsKey(i)) {
                    ancestors.enqueue(i);
                    wDistTo.put(i, wDistTo.get(ancestor) + 1);
                }
            }


        }

        return minAnc;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validate(v);
        validate(w);
        if (checkIterable(v) == -1) return -1;
        if (checkIterable(w) == -1) return -1;

        HashMap<Integer, Integer> wDistTo = new HashMap<>();
        BreadthFirstDirectedPaths bfdsW = new BreadthFirstDirectedPaths(G, w);

        int minDist = -1, minAnc = -1;
        Queue<Integer> ancestors = new Queue<>();
        for (int adj : v) {
            ancestors.enqueue(adj);
            wDistTo.put(adj, 0);
        }

        while (!ancestors.isEmpty()) {
            int adj = ancestors.dequeue();

            if (bfdsW.hasPathTo(adj)) {
                if (minAnc == -1) {
                    minAnc = adj;
                    minDist = bfdsW.distTo(adj) + wDistTo.get(adj);
                }
                else if (minDist > bfdsW.distTo(adj) + wDistTo.get(adj)) {
                    minAnc = adj;
                    minDist = bfdsW.distTo(adj) + wDistTo.get(adj);
                }
            }

            for (int i : G.adj(adj)) {
                if (!wDistTo.containsKey(i)) {
                    ancestors.enqueue(i);
                    wDistTo.put(i, wDistTo.get(adj) + 1);
                }
            }
        }
        return minAnc;
    }
    

    private int checkIterable(Iterable<Integer> v) {
        boolean zero = true;
        for (Object i : v) {
            if (i == null)
                throw new IllegalArgumentException("vertex can't be null");
            zero = false;
        }

        if (zero) {
            return -1;
        }
        return 0;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {

        validate(v);
        validate(w);
        if (checkIterable(v) == -1) return -1;
        if (checkIterable(w) == -1) return -1;

        HashMap<Integer, Integer> wDistTo = new HashMap<>();
        BreadthFirstDirectedPaths bfdsW = new BreadthFirstDirectedPaths(G, w);

        int minDist = -1, minAnc = -1;
        Queue<Integer> ancestors = new Queue<>();
        for (int adj : v) {
            ancestors.enqueue(adj);
            wDistTo.put(adj, 0);
        }

        while (!ancestors.isEmpty()) {
            int adj = ancestors.dequeue();

            if (bfdsW.hasPathTo(adj)) {
                if (minAnc == -1) {
                    minAnc = adj;
                    minDist = bfdsW.distTo(adj) + wDistTo.get(adj);
                }
                else if (minDist > bfdsW.distTo(adj) + wDistTo.get(adj)) {
                    minAnc = adj;
                    minDist = bfdsW.distTo(adj) + wDistTo.get(adj);
                }
            }

            for (int i : G.adj(adj)) {
                if (!wDistTo.containsKey(i)) {
                    ancestors.enqueue(i);
                    wDistTo.put(i, wDistTo.get(adj) + 1);
                }
            }
        }
        return minDist;
    }

    // validate argument
    private void validate(Object o) {
        if (o == null) {
            throw new IllegalArgumentException("argument can't be null");
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        Queue<Integer> q = new Queue<>();
        q.enqueue(null);
        StdOut.println(sap.length(q, q));
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }


    }
}
