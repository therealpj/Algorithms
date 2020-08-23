import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* *****************************************************************************
 *  Name: Prajjwal Juyal
 *  Date: 22/08/19
 *  Description: An immutable WordNet data type that constructs a digraph from
 *  given hypernym and synset CSV files
 **************************************************************************** */
public class WordNet {
    private HashMap<String, ArrayList<Integer>> nouns;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        validate(synsets);
        validate(hypernyms);

        nouns = new HashMap<>();
        In in = new In(synsets);

        int v = 0;
        while (in.hasNextLine()) {
            String[] line = in.readLine().split(",");
            String[] n = line[1].split(" ");
            for (String s : n) {
                if (nouns.get(s) != null)
                    nouns.get(s).add(Integer.parseInt(line[0]));
                else {
                    nouns.put(s, new ArrayList<>());
                    nouns.get(s).add(Integer.parseInt(line[0]));
                }
            }
            v += 1;
        }

        Digraph g = new Digraph(v);
        in = new In(hypernyms);
        boolean rooted = false;
        while (in.hasNextLine()) {
            String[] line = in.readLine().split(",");
            if (line.length == 1) {
                rooted = true;
            }
            for (int i = 1; i < line.length; i++) {
                g.addEdge(Integer.parseInt(line[0]), Integer.parseInt(line[i]));
            }
        }
        if (!rooted)
            throw new IllegalArgumentException("not a rooted DAG");
        sap = new SAP(g);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return this.nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        validate(word);
        return nouns.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        validate(nounA);
        validate(nounB);
        if (!nouns.containsKey(nounA) || !nouns.containsKey(nounB))
            throw new IllegalArgumentException("noun not in wordnet");

        ArrayList<Integer> A = nouns.get(nounA);
        ArrayList<Integer> B = nouns.get(nounB);

        if (A.size() == 1 && B.size() == 1) {
            return sap.length(A.get(0), B.get(0));
        }
        else {
            return sap.length(A, B);
        }
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        validate(nounA);
        validate(nounB);
        if (!nouns.containsKey(nounA) || !nouns.containsKey(nounB))
            throw new IllegalArgumentException("noun not in wordnet");

        int ancestor;
        ArrayList<Integer> A = nouns.get(nounA);
        ArrayList<Integer> B = nouns.get(nounB);

        if (A.size() == 1 && B.size() == 1) {
            ancestor = sap.ancestor(A.get(0), B.get(0));
        }
        else {
            ancestor = sap.ancestor(A, B);
        }

        StringBuilder s = new StringBuilder();
        for (Map.Entry<String, ArrayList<Integer>> i : this.nouns.entrySet()) {
            for (int j : i.getValue()) {
                if (j == ancestor) {
                    s.append(i.getKey() + " ");
                }
            }
        }
        return s.toString();
    }

    private void validate(Object o) {
        if (o == null) {
            throw new IllegalArgumentException("argument can't be null");
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet("synsets3.txt", "hypernyms3InvalidCycle.txt");

        while (!StdIn.isEmpty()) {
            String a = StdIn.readString();
            String b = StdIn.readString();
            StdOut.println("sap: " + wn.sap(a, b) + " length: " + wn.distance(a, b));
        }
    }
}
