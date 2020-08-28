/* *****************************************************************************
 *  Name: Prajjwal Juyal
 *  Date: 28/08/20
 *  Description: Implementation of move-to-front encoding and decoding
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    private static class Node {
        private char c;
        private Node next = null;
    }

    public static void encode() {
        Node root = new Node();
        Node temp = new Node();
        root.next = temp;

        for (char i = 0; i < 256; i++) {
            temp.c = i;
            temp.next = new Node();
            temp = temp.next;
        }

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar(8);
            char j = 0;
            for (Node n = root; n != null; n = n.next) {

                if (n.next.c == c) {
                    BinaryStdOut.write(j);
                    Node a = new Node();
                    a.c = c;
                    a.next = root.next;
                    root.next = a;
                    n.next = n.next.next;
                    break;
                }
                j += 1;

            }
        }
        BinaryStdOut.flush();
    }

    public static void decode() {

        Node root = new Node();
        Node temp = new Node();
        root.next = temp;

        for (char i = 0; i < 256; i++) {
            temp.c = i;
            temp.next = new Node();
            temp = temp.next;
        }

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar(8);
            Node tr = root;
            for (char a = 0; a < c; a++) {
                tr = tr.next;
            }
            BinaryStdOut.write(tr.next.c);
            Node a = new Node();
            a.c = tr.next.c;
            a.next = root.next;
            root.next = a;
            tr.next = tr.next.next;

        }
        BinaryStdOut.flush();
    }

    public static void main(String[] args) {

        if (args[0].equals("-")) {
            encode();
        }
        else if (args[0].equals("+")) {
            decode();
        }
        else {
            throw new IllegalArgumentException("wrong argument passed");
        }
    }
}
