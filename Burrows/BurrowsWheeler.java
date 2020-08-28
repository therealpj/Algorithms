/* *****************************************************************************
 *  Name: Prajjwal Juyal
 *  Date: 28/08/20
 *  Description: The Burrows-Wheeler transform
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;
import java.util.Comparator;

public class BurrowsWheeler {

    private static class KeyIndexComparator implements Comparator<Integer> {
        private final char[] array;

        private KeyIndexComparator(char[] array) {
            this.array = array;
        }

        private Integer[] createIndexArray() {
            Integer[] indexes = new Integer[array.length];
            for (int i = 0; i < array.length; i++) {
                indexes[i] = i;
            }
            return indexes;
        }

        public int compare(Integer i1, Integer i2) {
            return Character.compare(array[i1], array[i2]);
        }
    }


    // apply the Burrows-Wheeler transform, reading from standard input and writing
    // to standard output
    public static void transform() {
        // read input
        String s = BinaryStdIn.readString();

        int first = 0, suffix;
        StringBuilder t = new StringBuilder();

        CircularSuffixArray csa = new CircularSuffixArray(s);

        for (int i = 0; i < s.length(); i++) {
            suffix = csa.index(i);
            t.append(lastCharOfSuffix(s, suffix));
            if (suffix == 0) first = i;
        }
        BinaryStdOut.write(first);
        BinaryStdOut.write(t.toString());
        BinaryStdOut.flush();
    }

    // returns the position of last character in a circular suffix
    private static char lastCharOfSuffix(String s, int offset) {
        return s.charAt((s.length() - 1 + offset) % s.length());
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();

        char[] array = new char[t.length()];

        for (int i = 0; i < t.length(); i++) {
            array[i] = t.charAt(i);

        }


        KeyIndexComparator comparator = new KeyIndexComparator(array);
        Integer[] next = comparator.createIndexArray();
        Arrays.sort(next, comparator);

        StringBuilder str = new StringBuilder();


        for (int i = 0; i < next.length; i++) {
            str.append(array[next[first]]);
            first = next[first];
        }
        BinaryStdOut.write(str.toString());
        BinaryStdOut.flush();
    }


    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            BurrowsWheeler.transform();
        }
        else if (args[0].equals("+")) {
            BurrowsWheeler.inverseTransform();

        }
        else {
            throw new IllegalArgumentException("First argument should be either - or +");
        }
    }
}
