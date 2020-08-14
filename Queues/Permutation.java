/* *****************************************************************************
 *  Name: Prajjwal Juyal
 *  Date: 14/08/2020
 *  Description: A test client that takes in an integer, reads a sequence of strings
 *  from StdIn, and prints exactly k of them, uniformly at random. Each string is
 *  printed atmost once
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();

        // read a sequence of strings
        /* while (true) {
            String line = StdIn.readLine();
            if (line.length() == 0) {
                break;
            }
            randomizedQueue.enqueue(line);
        } */

        while (!StdIn.isEmpty()) {
            randomizedQueue.enqueue(StdIn.readString());
        }

        // Print the sequence of strings randomly
        int n = Integer.parseInt(args[0]);
        Iterator<String> iterator = randomizedQueue.iterator();
        for (int i = 0; i < n; i++) {
            StdOut.println(iterator.next());
        }
    }
}
