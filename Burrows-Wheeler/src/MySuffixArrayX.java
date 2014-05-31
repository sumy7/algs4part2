/**
 * Modify algs4.jar -> SuffixArrayX.java
 * 
 * @author sumy
 * 
 */

public class MySuffixArrayX {
    private static final int CUTOFF = 5; // cutoff to insertion sort (any value
                                         // between 0 and 12)

    private final char[] text;
    private final int[] index; // index[i] = j means text.substring(j) is ith
                               // largest suffix
    private final int N; // number of characters in text

    /**
     * Initializes a suffix array for the given <tt>text</tt> string.
     * 
     * @param text
     *            the input string
     */
    public MySuffixArrayX(String text) {
        N = text.length();
        text = text + '\0';
        this.text = text.toCharArray();
        this.index = new int[N];
        for (int i = 0; i < N; i++)
            index[i] = i;

        // shuffle

        sort(0, N - 1, 0);
    }

    // 3-way string quicksort lo..hi starting at dth character
    private void sort(int lo, int hi, int d) {

        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(lo, hi, d);
            return;
        }

        int lt = lo, gt = hi;
        // BUG: 注意取余
        char v = text[(index[lo] + d) % N];
        int i = lo + 1;
        while (i <= gt) {
            int t = text[(index[i] + d) % N];
            if (t < v)
                exch(lt++, i++);
            else if (t > v)
                exch(i, gt--);
            else
                i++;
        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi].
        sort(lo, lt - 1, d);
        if (d < N)
            sort(lt, gt, d + 1);
        sort(gt + 1, hi, d);
    }

    // sort from a[lo] to a[hi], starting at the dth character
    private void insertion(int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(index[j], index[j - 1], d); j--)
                exch(j, j - 1);
    }

    // is text[i+d..N) < text[j+d..N) ?
    private boolean less(int i, int j, int d) {
        if (i == j)
            return false;
        i = (i + d) % N;
        j = (j + d) % N;
        while (d < N) {
            if (text[i] < text[j])
                return true;
            if (text[i] > text[j])
                return false;
            i++;
            j++;
            i %= N;
            j %= N;
            d++;
        }
        return i > j;
    }

    // exchange index[i] and index[j]
    private void exch(int i, int j) {
        int swap = index[i];
        index[i] = index[j];
        index[j] = swap;
    }

    /**
     * Returns the length of the input string.
     * 
     * @return the length of the input string
     */
    public int length() {
        return N;
    }

    /**
     * Returns the index into the original string of the <em>i</em>th smallest
     * suffix. That is, <tt>text.substring(sa.index(i))</tt> is the <em>i</em>
     * smallest suffix.
     * 
     * @param i
     *            an integer between 0 and <em>N</em>-1
     * @return the index into the original string of the <em>i</em>th smallest
     *         suffix
     * @throws java.lang.IndexOutOfBoundsException
     *             unless 0 &le; <em>i</em> &lt; <Em>N</em>
     */
    public int index(int i) {
        if (i < 0 || i >= N)
            throw new IndexOutOfBoundsException();
        return index[i];
    }

}
