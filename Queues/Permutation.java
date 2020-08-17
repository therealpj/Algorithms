/* *****************************************************************************
 *  Name: Prajjwal Juyal
 *  Date: 14/08/2020
 *  Description: A test client that takes in an integer, reads a sequence of strings
 *  from StdIn, and prints exactly k of them, uniformly at random. Each string is
 *  printed atmost once
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        int n = Integer.parseInt(args[0]);

        // Reservoir Sampling
        int stringNumber = 1;
        while (!StdIn.isEmpty()) {

            // if n items are enqueued, randomly replace them with the next incoming items with a probability
            if (stringNumber <= n) {
                randomizedQueue.enqueue(StdIn.readString());
            }
            else {
                int randomNumber = StdRandom.uniform(0, stringNumber);
                if (randomNumber <= n - 1) {
                    randomizedQueue.dequeue();
                    randomizedQueue.enqueue(StdIn.readString());
                }
                else {
                    StdIn.readString();
                }
            }
            stringNumber++;
        }

        for (String i : randomizedQueue) {
            StdOut.println(i);
        }
    }

}

