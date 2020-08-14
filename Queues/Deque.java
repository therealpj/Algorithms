/* *****************************************************************************
 *  Name: Prajjwal Juyal
 *  Date: 14/08/2020
 *  Description: A doubly-ended-queue or deque is a generalization of a stack and
 *  a queue that supports adding and removing items from either the front or the
 *  back.
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node head, tail;
    private int size;

    // a single node of the deque
    private class Node {
        private Item item;
        private Node next;
        private Node prev;

        public Node() {
        }

        public Node(Item item, Node next, Node prev) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }

    // construct an empty deque
    public Deque() {
        head = null;
        tail = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return head == null;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("argument can't be null");
        }

        Node newNode = new Node(item, null, null);
        size++;

        // if the deque doesn't have any items, make it the head and the tail
        if (size == 1) {
            head = newNode;
            tail = newNode;
            return;
        }

        head.prev = newNode;
        newNode.next = head;
        head = newNode;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("argument can't be null");
        }

        size++;
        Node newNode = new Node(item, null, null);

        // if the deque doesn't have any items, make it the head and the tail
        if (size == 1) {
            head = newNode;
            tail = newNode;
            return;
        }

        tail.next = newNode;
        newNode.prev = tail;
        tail = newNode;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException("no item to remove");
        }

        size--;
        Node deleted = head;

        // if deque has only one item, remove it and reset pointers
        if (size == 0) {
            head = null;
            tail = null;
            return deleted.item;
        }

        head = head.next;
        head.prev = null;

        return deleted.item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (size == 0) {
            throw new NoSuchElementException("no item to remove");
        }

        size--;
        Node deleted = tail;

        // if deque has only one item, remove it and reset pointers
        if (size == 0) {
            tail = null;
            head = null;
            return deleted.item;
        }

        tail = tail.prev;
        tail.next = null;

        return deleted.item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = head;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("no more elements");
            }

            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove operation not supported");
        }
    }
    
    public static void main(String[] args) {

        Deque<Integer> deque = new Deque<>();
        assert deque.size == 0;

        // adding a single element and checking
        // addFirst(n), addLast(n), removeFirst(), removeLast()
        assert deque.isEmpty();
        deque.addFirst(4);
        assert deque.size() == 1;
        int deletedNum = deque.removeFirst();
        assert deletedNum == 4;
        assert deque.size() == 0;
        deque.addFirst(1);
        assert deque.size() == 1;
        deletedNum = deque.removeLast();
        assert deletedNum == 1;
        assert deque.size() == 0;
        deque.addLast(1);
        assert deque.size() == 1;
        deletedNum = deque.removeLast();
        assert deletedNum == 1;
        assert deque.size() == 0;
        assert deque.isEmpty();

        // checking if the deque works when adding and removing more than one item
        deque.addLast(1);
        deque.addLast(5);
        deque.addLast(6);
        assert deque.size() == 3;
        deletedNum = deque.removeLast();
        assert deletedNum == 6;
        deletedNum = deque.removeFirst();
        assert deletedNum == 1;
        assert deque.size() == 1;
        assert !deque.isEmpty();


    }
}
