/*************************************************************************
 * 
 * @author sumy
 * 
 *************************************************************************/

public class BoggleSolver {
    private static final int[][] DIRE = { { 1, 0 }, { -1, 0 }, { 0, -1 },
            { 0, 1 }, { -1, 1 }, { 1, 1 }, { 1, -1 }, { -1, -1 } };
    private UpperTrie trie;

    // Initializes the data structure using the given array of strings as the
    // dictionary.
    // (You can assume each word in the dictionary contains only the uppercase
    // letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        trie = new UpperTrie();
        for (int i = 0; i < dictionary.length; i++) {
            trie.put(dictionary[i]);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an
    // Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        SET<String> list = new SET<String>();
        boolean[][] vis = new boolean[board.rows()][board.cols()];

        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                vis[i][j] = false;
            }
        }

        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                vis[i][j] = true;
                if (board.getLetter(i, j) == 'Q') {
                    boardDfs(board, i, j, vis,
                            "" + board.getLetter(i, j) + 'U', list);
                } else {
                    boardDfs(board, i, j, vis, "" + board.getLetter(i, j), list);
                }
                vis[i][j] = false;
            }
        }

        return list;
    }

    // Returns the score of the given word if it is in the dictionary, zero
    // otherwise.
    // (You can assume the word contains only the uppercase letters A through
    // Z.)
    public int scoreOf(String word) {
        if (trie.contains(word)) {
            int len = word.length();
            if (len <= 2) {
                return 0;
            }
            if (len <= 4) {
                return 1;
            }
            if (len == 5) {
                return 2;
            }
            if (len == 6) {
                return 3;
            }
            if (len == 7) {
                return 5;
            }
            return 11;
        }
        return 0;
    }

    private void boardDfs(BoggleBoard board, int x, int y, boolean[][] vis,
            String word, SET<String> list) {

        if (!trie.hasPrefix(word)) {
            return;
        }
        if (trie.contains(word) && word.length() >= 3) {
            list.add(word);
        }
        for (int i = 0; i < 8; i++) {
            int newx = x + DIRE[i][0];
            int newy = y + DIRE[i][1];
            if (newx >= 0 && newx < board.rows() && newy >= 0
                    && newy < board.cols() && !vis[newx][newy]) {
                String newword;
                if (board.getLetter(newx, newy) == 'Q') {
                    newword = word + board.getLetter(newx, newy) + "U";
                } else {
                    newword = word + board.getLetter(newx, newy);
                }
                vis[newx][newy] = true;
                boardDfs(board, newx, newy, vis, newword, list);
                vis[newx][newy] = false;
            }
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}