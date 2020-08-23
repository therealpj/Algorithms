/* *****************************************************************************
 *  Name: Prajjwal Juyal
 *  Date: 22/08/20
 *  Description: An immutable data type Outcast which finds the least related word
 *  among a group of words
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordNet;

    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    public String outcast(String[] nouns) {
        int d = -1;
        String noun = nouns[0];
        for (int i = 0; i < nouns.length; i++) {
            int dist = 0;
            for (int j = 0; j < nouns.length; j++) {
                dist += wordNet.distance(nouns[i], nouns[j]);
            }
            
            if (d < dist) {
                noun = nouns[i];
                d = dist;
            }
        }
        return noun;
    }


    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
