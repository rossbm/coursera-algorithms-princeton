import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int queueSize;
    private Item[] a;

    /**
     * construct an empty randomized queue
     */
    public RandomizedQueue() {
        queueSize = 0;
        a = (Item[]) new Object[1];
    }

    /**
     * is the randomized queue empty?
     * @return
     */
    public boolean isEmpty() {
        return queueSize == 0;
    }

    /**
     * return the number of items on the randomized queue
     * @return
     */
    public int size() {
        return queueSize;
    }

    /**
     * add the item
     * @param data
     */
    public void enqueue(Item data) {
        if (data == null) throw new java.lang.IllegalArgumentException();
        if (queueSize == a.length) resize(2 * a.length);
        a[queueSize++] = data;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < queueSize; i++) {
            copy[i] = a[i];
        }
        a = copy;
    }

    /**
     * remove and return a random item
     * @return
     */
    public Item dequeue() {
        if (queueSize <= 0) throw new java.util.NoSuchElementException();
        int i = StdRandom.uniform(queueSize);
        Item data = a[i];
        a[i] = null;
        swap(i, --queueSize);
        if (queueSize > 0 && queueSize <= a.length/4) resize(a.length/2);
        return data;
    }

    private void swap(int idx_0, int idx_1) {
        Item inter = a[idx_0];
        a[idx_0] = a[idx_1];
        a[idx_1] = inter;
    }

    /**
     *  return a random item (but do not remove it)
     * @return
     */
    public Item sample() {
        if (size() == 0) throw new java.util.NoSuchElementException();
        return a[StdRandom.uniform(queueSize)];
    }

    /**
     * return an independent iterator over items in random order
     * @return
     */
    public Iterator<Item> iterator() {
        return new RandomIterator();
    }

    private class RandomIterator implements Iterator<Item> {
        private int[] idxs = new int[queueSize];
        int n = queueSize;

        public RandomIterator () {
            for (int i = 0; i < queueSize; i++) {
                idxs[i] = i;
            }
        }

        @Override
        public boolean hasNext() { return n > 0; }
        @Override
        public void remove () { throw new java.lang.UnsupportedOperationException();}
        @Override
        public Item next() {
            if (n == 0) throw new java.util.NoSuchElementException();
            int idx = StdRandom.uniform(n);
            int arrayIndex = idxs[idx];
            Item data = a[arrayIndex];
            idxs[idx] = idxs[--n];
            return data;
        }
    }


    public static void main(String[] args) {
        RandomizedQueue<Integer> deque = new RandomizedQueue<Integer>();
        for (int i = 0; i < 10; i++) {
            deque.enqueue(i);
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(deque.dequeue());
        }
    }// unit testing (optional)
}