public class Outcast {
    private WordNet wordnet = null;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int[][] dis = new int[nouns.length][nouns.length];
        for (int i = 0; i < nouns.length; i++) {
            for (int j = 0; j < nouns.length; j++)
                dis[i][j] = -1;
        }
        int mindis = -1;
        int outcastword = -1;
        for (int i = 0; i < nouns.length; i++) {
            int tmp = 0;
            for (int j = 0; j < nouns.length; j++) {
                if (i == j)
                    continue;
                if (dis[i][j] == -1) {
                    dis[i][j] = wordnet.distance(nouns[i], nouns[j]);
                    dis[j][i] = dis[i][j];
                }
                tmp += dis[i][j];
            }
            if (tmp > mindis) {
                mindis = tmp;
                outcastword = i;
            }
        }
        return nouns[outcastword];
    }

    // for unit testing of this class (such as the one below)
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            String[] nouns = In.readStrings(args[t]);
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
