/*************************************************************************
 * Compilation: javac CapitalTrie.java <br>
 * Execution: java CapitalTrie < words.txt <br>
 * Dependencies: StdIn.java <br>
 * 
 * 字典树，用于大写字母的单词。 <br>
 * @author sumy
 * 
 *************************************************************************/

public class UpperTrie {
    private static final int R = 26; // 26个大写字母

    private Node root; // 字典数的根结点
    private int N; // 字典数中单词的数量

    // 字典树结点
    private static class Node {
        private boolean val = false;
        private Node[] next = new Node[R];
    }

    public UpperTrie() {
    }

    /**
     * 插入一个大写单词。
     * 
     * @param key
     *            大写单词
     * @throws NullPointerException
     *             <tt>key</tt>为<tt>null</tt>的时候抛出
     * 
     */
    public void put(String key) {
        String upperkey = key.toUpperCase();
        root = put(root, upperkey, 0);
    }

    private Node put(Node x, String key, int d) {
        if (x == null)
            x = new Node();
        if (d == key.length()) {
            if (x.val == false)
                N++;
            x.val = true;
            return x;
        }
        int c = key.charAt(d) - 'A';
        x.next[c] = put(x.next[c], key, d + 1);
        return x;
    }

    /**
     * 返回字典树中单词的数量。
     * 
     * @return 单词的数量
     */
    public int size() {
        return N;
    }

    /**
     * 字典树是否为空？
     * 
     * @return 如果字典树为空，返回 <tt>true</tt>；否则返回 <tt>false</tt> 。
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * 如果单词存在，则从字典树中删除。
     * 
     * @param key
     *            单词
     * @throws NullPointerException
     *             如果<tt>key</tt>为 <tt>null</tt>，抛出该异常。
     */
    public void delete(String key) {
        root = delete(root, key, 0);
    }

    private Node delete(Node x, String key, int d) {
        if (x == null)
            return null;
        if (d == key.length()) {
            if (x.val != false)
                N--;
            x.val = false;
        } else {
            int c = key.charAt(d) - 'A';
            x.next[c] = delete(x.next[c], key, d + 1);
        }

        // 如果字典树的子树为空，则删除
        if (x.val != false)
            return x;
        for (int c = 0; c < R; c++)
            if (x.next[c] != null)
                return x;
        return null;
    }

    /**
     * 是否存在包含指定前缀的单词？
     * 
     * @param prefix
     *            指定的单词前缀
     * @return 如果包含指定前缀，返回 <tt>true</tt>；否则返回 <tt>false</tt> 。
     * @throws NullPointerException
     *             如果<tt>key</tt>为 <tt>null</tt>，抛出该异常。
     */
    public boolean hasPrefix(String prefix) {
        if (prefix == "") {
            return true;
        }
        return hasPrefix(root, prefix, 0);
    }

    private boolean hasPrefix(Node x, String prefix, int d) {
        if (x == null)
            return false;
        if (d >= prefix.length())
            return true;
        int c = prefix.charAt(d) - 'A';
        if (x.next[c] == null)
            return false;
        return hasPrefix(x.next[c], prefix, d + 1);
    }

    /**
     * 是否存在指定的单词？
     * 
     * @param key
     *            单词
     * @return 如果包含指定前缀，返回 <tt>true</tt>；否则返回 <tt>false</tt> 。
     * @throws NullPointerException
     *             如果<tt>key</tt>为 <tt>null</tt>，抛出该异常。
     */
    public boolean contains(String key) {
        return contains(root, key, 0);
    }

    private boolean contains(Node x, String key, int d) {
        if (x == null)
            return false;
        if (d == key.length()) {
            return x.val;
        }
        int c = key.charAt(d) - 'A';
        return contains(x.next[c], key, d + 1);
    }

}
