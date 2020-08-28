/* *****************************************************************************
 *  Name: Prajjwal Juyal
 *  Date: 28/08/20
 *  Description: A circular suffix API, which returns the index of ith sorted suffix
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {
    private static String s;

    private static class CircularSuffix implements Comparable<CircularSuffix> {

        private int offset;

        public CircularSuffix(int offset) {
            this.offset = offset;
        }

        public int getOffset() {
            return offset;
        }

        // returns the position of a char in the current CircularSuffix
        public char get(int index) {
            return s.charAt((index + offset) % s.length());
        }

        // comparing two instances of a circular suffix
        public int compareTo(CircularSuffix that) {
            for (int i = 0; i < s.length(); i++) {
                char thisC = this.get(i);
                char thatC = that.get(i);
                if (thisC < thatC) return -1;
                if (thisC > thatC) return 1;
            }
            return 0;
        }

        // string representation of a circular array. Takes O(n) space and memory.
        public String toString() {
            StringBuilder suffix = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                suffix.append(get(i));
            }
            return suffix.toString();
        }

    }

    private CircularSuffix[] circularSuffixes;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException("String passed can't be null");
        }
        CircularSuffixArray.s = s;
        circularSuffixes = new CircularSuffix[s.length()];
        for (int i = 0; i < s.length(); i++) {
            circularSuffixes[i] = new CircularSuffix(i);
        }

        Arrays.sort(circularSuffixes);
    }

    // length of suffix array
    public int length() {
        return circularSuffixes.length;
    }

    // returns the position of suffix array in sorted array
    public int index(int i) {
        if (i < 0 || i >= length()) {
            throw new IllegalArgumentException("i should be between [0, n)");
        }
        return circularSuffixes[i].getOffset();
    }

    public static void main(String[] args) {
        String s = args[0];
        CircularSuffixArray csa = new CircularSuffixArray(s);

        // check the offset for each integer input
        while (StdIn.hasNextLine()) {
            StdOut.println(csa.index(Integer.parseInt(StdIn.readLine())));
        }
    }
}
