import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private Node head;
    private Node tail;
    private Node sentinel;
    private int length;

    /**
     * Initializes an deque.
     */
    public Deque() {
        sentinel = new Node(null);
        length = 0;
        tail = sentinel;
        head = tail;
    }

    /**
     * is the deque empty?
     */
    public boolean isEmpty() {
        return length == 0;
    }

    /**
     * return the number of items on the deque
     */
    public int size() {
        return length;
    }

    /**
     * add the item to the front
     */
    public void addFirst(Item data) {
        if (data == null) throw new java.lang.IllegalArgumentException("item is null");
        Node node = new Node(data);
        newHead(node);
        if (length == 1) newTail(node);
    }

    /**
     * add the item to the back
     */
    public void addLast(Item data) {
        if (data == null) throw new java.lang.IllegalArgumentException("item is null");
        Node node = new Node(data);
        newTail(node);
        if (length == 1) newHead(node);
    }

    private void newTail(Node node) {
        // link (former) tail and new Node
        linkNodes(tail, node);
        // the new Node now tails the deque
        tail = node;
    }

    private void newHead(Node node) {
        // link (former) head and new Node
        linkNodes(node, head);
        head = node;
    }

    private void linkNodes(Node left, Node right) {
            left.next = right;
            right.prev = left;
    }

    public Item removeFirst() {
        if (length ==0) throw new java.util.NoSuchElementException();
        // remove and return the item from the front
        Node node = head;
        head = node.next;
        if (length == 1) tail = sentinel;
        return node.pop();
    }
    public Item removeLast() {
        // remove and return the item from the end
        if (length == 0) throw new java.util.NoSuchElementException();
        Node node = tail;
        tail = node.prev;
        if (length == 1) head = sentinel;
        return node.pop();
    }

    public Iterator<Item> iterator() {
        // return an iterator over items in order from front to end
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item>{
        private Node current = head;
        @Override
        public boolean hasNext() {return current.getData() != null;}
        @Override
        public void remove () { throw new java.lang.UnsupportedOperationException();}
        @Override
        public Item next() {
            Item item = current.getData();
            if (item == null) throw new java.util.NoSuchElementException();
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        // unit testing (optional)
        Deque<String> deque = new Deque<String>();
        deque.addLast("SMNHJWFVTO");
        deque.addLast("ODCLXGFJBU");
        deque.addLast("ZYGGJCLBIR");
        deque.addLast("IIBGOYXUGN");
        deque.addLast("VVFKIQSWHN");
        deque.addLast("MPYYPDAPMZ");
        System.out.println(deque.removeFirst());
        System.out.println(deque.removeFirst());
    }

    private class Node {
        public Node next;
        public Node prev;
        private Item data;

        public Node(Item item) {
            data = item;
            //increment length so size method returns correct number
            length++;
            next = sentinel;
            prev = sentinel;
        }


        public Item getData() {
            return data;
        }
        public Item pop() {
            length--;
            next.prev = prev;
            prev.next = next;
            return getData();
        }
    }
}
