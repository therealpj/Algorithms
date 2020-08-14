/* *****************************************************************************
 *  Name: Prajjwal Juyal
 *  Date: 14/08/2020
 *  Description: RandomizedQueue data structure, similar to a stack or a queue,
 *  except that the item removed is chosen uniformly at random among items in the
 *  data structure
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] queue;
    private int tail, size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        queue = (Item[]) new Object[5];
        tail = -1;
        size = 0;
    }

    // for doubling or halving the size of the queue
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            copy[i] = queue[i];
        }

        queue = copy;
        copy = null;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("argument can't be null");
        }

        // if the queue is full, double its size
        if (size == queue.length) {
            resize(2 * size);
        }

        size++;
        queue[++tail] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size == 0) {
            throw new NoSuchElementException("no element to remove");
        }

        int randomIndex = StdRandom.uniform(0, size);
        // replacing the deleted item with last item of our queue
        Item deletedItem = queue[randomIndex];
        queue[randomIndex] = queue[tail];
        queue[tail] = null;
        tail--;
        size--;

        // resizing queue if it becomes sufficiently empty
        if (size <= queue.length / 4) {
            resize(size + 1);
        }

        return deletedItem;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size == 0) {
            throw new NoSuchElementException("no element to remove");
        }

        int randomIndex = StdRandom.uniform(0, size);
        return queue[randomIndex];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        public RandomizedQueueIterator() {
            StdRandom.shuffle(queue);
            int start = 0;
        }

        public boolean hasNext() {
            return size == 0;
        }

        public Item next() {

            if (tailClone == -1) {
                throw new NoSuchElementException("no items to sample");
            }

            int randomIndex = StdRandom.uniform(0, tailClone + 1);
            Item randomItem = queueClone[randomIndex];
            queueClone[randomIndex] = queueClone[tailClone];
            tailClone--;
            return randomItem;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove not supported");
        }
    }

    // unit testing
    public static void main(String[] args) {
        RandomizedQueue<Integer> rqueue = new RandomizedQueue<>();

        // checking if enqueue() works
        assert rqueue.size() == 0;
        rqueue.enqueue(5);
        assert rqueue.sample() == 5;
        assert rqueue.sample() == 5;
        assert rqueue.size() == 1;
        for (int i = 0; i < 50; i++) {
            rqueue.enqueue(i);
        }
        assert rqueue.size() == 51;
        for (int i = 0; i < 25; i++) {
            rqueue.dequeue();
        }
        assert rqueue.size() == 26;

        // testing the iterator
        int n = 5;
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        for (int i = 0; i < n; i++)
            queue.enqueue(i);
        for (int a : queue) {
            for (int b : queue)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }
    }


}
