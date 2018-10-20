import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
public class Permutation {
    public static void main(String[] args) {
        // Read and print out each line.
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> randomQueuer = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            randomQueuer.enqueue(StdIn.readString());
        }
        for (int i = 0; i < k; i++) {
            StdOut.println(randomQueuer.dequeue());
        }
    }
}
