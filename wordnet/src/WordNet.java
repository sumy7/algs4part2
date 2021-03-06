/*************************************************************************
 * Compilation: javac WordNet.java <br>
 * Execution: <br>
 * Dependencies: Digraph.class <br>
 * 
 * Email: sunmingjian8@gmail.com
 * 
 * @author sumy <br>
 * 
 *************************************************************************/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class WordNet {

    private Digraph dig = null;
    private HashMap<String, HashSet<Integer>> index = null;
    private ArrayList<String> list = null;
    private SAP sap = null;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        index = new HashMap<String, HashSet<Integer>>();
        list = new ArrayList<String>();

        In synsetsIn = new In(synsets);
        while (synsetsIn.hasNextLine()) {
            String str = synsetsIn.readLine();
            String[] ls = str.split(",");
            String[] word = ls[1].split(" ");
            int num = Integer.parseInt(ls[0]);
            for (String singleword : word) {
                HashSet<Integer> indexset = index.get(singleword);
                if (indexset == null) {
                    indexset = new HashSet<Integer>();
                    index.put(singleword, indexset);
                }
                indexset.add(num);
            }
            list.add(ls[1]);
        }
        dig = new Digraph(list.size());
        In hypernymsIn = new In(hypernyms);
        while (hypernymsIn.hasNextLine()) {
            String str = hypernymsIn.readLine();
            String[] ls = str.split(",");
            for (int i = 1; i < ls.length; i++) {
                dig.addEdge(Integer.parseInt(ls[0]), Integer.parseInt(ls[i]));
            }
        }
        Topological topological = new Topological(dig);

        if (!topological.hasOrder())
            throw new java.lang.IllegalArgumentException();
        int count = 0;
        for (int i = 0; i < dig.V(); i++) {
            if (!dig.adj(i).iterator().hasNext())
                count++;
            if (count >= 2)
                throw new java.lang.IllegalArgumentException();
        }
        sap = new SAP(dig);

    }

    // the set of nouns (no duplicates), returned as an Iterable
    public Iterable<String> nouns() {
        return index.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return index.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (index.get(nounA) == null)
            throw new java.lang.IllegalArgumentException();
        if (index.get(nounB) == null)
            throw new java.lang.IllegalArgumentException();
        Iterable<Integer> nounAindex = index.get(nounA);
        Iterable<Integer> nounBindex = index.get(nounB);
        return sap.length(nounAindex, nounBindex);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of
    // nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (index.get(nounA) == null)
            throw new java.lang.IllegalArgumentException();
        if (index.get(nounB) == null)
            throw new java.lang.IllegalArgumentException();
        Iterable<Integer> nounAindex = index.get(nounA);
        Iterable<Integer> nounBindex = index.get(nounB);
        int ancestor = sap.ancestor(nounAindex, nounBindex);
        return list.get(ancestor);
    }

    private String pathtoString() {
        return dig.toString();
    }

    // for unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        StdOut.println(wordnet.pathtoString());
    }
}
