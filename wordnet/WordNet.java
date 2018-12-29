/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
public class WordNet {
    // from noun to array of synset IDs
    private ST<String, Bag<Integer>> nounST;
    // from synset ID to array of nouns
    private ST<Integer, String[]> synST;
    // the hypernyms file will be used to create a digraph

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null) throw new java.lang.IllegalArgumentException();
        if (hypernyms == null) throw new java.lang.IllegalArgumentException();

        // Read in synsets file
        In inSyn = new In(synsets);
        nounST = new ST<String, Bag<Integer>>();
        synST = new ST<Integer, String[]>();
        while (!inSyn.isEmpty()) {
            String[] synset = inSyn.readLine().split(",");
            int id = Integer.parseInt(synset[0]);
            String[] nouns = synset[1].split(" ");
            synST.put(id, nouns);
            // Iterate over all nouns in this synset
            for (String noun : nouns) {
                // Perhaps this noun has already been encountered in an earlier synset?
                if (!nounST.contains(noun)) {
                    // If not, add it
                    nounST.put(noun, new Bag<Integer>());
                }
                // Add current synset ID to this noun
                nounST.get(noun).add(id);
            }
        }
        // Now read in hypernyms file
        // The number of vertices should match the synyms file
        // We will have to throw an exception if this is not the case (not true!!)
        In inHyp = new In(hypernyms);
        int numSyn = synST.size();
        Digraph diHyp = new Digraph(numSyn);
        while (!inHyp.isEmpty()) {
            String[] hypernym = inHyp.readLine().split(",");
            int child = Integer.parseInt(hypernym[0]);
            if (child < 0) throw new java.lang.IllegalArgumentException();
            if (child >= numSyn) throw new java.lang.IllegalArgumentException();
            for (int i = 1; i < hypernym.length; i++) {
                int parent = Integer.parseInt(hypernym[i]);
                diHyp.addEdge(child, parent);
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounST.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return nounST.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        return 0;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        return null;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet tester = new WordNet("synsets3.txt", "hypernams.txt");
        Iterable<String> nouns = tester.nouns();
        for (String noun : nouns) {
            StdOut.println(noun);
        }

    }
}
