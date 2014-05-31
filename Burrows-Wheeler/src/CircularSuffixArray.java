/*************************************************************************
 * 
 * @author sumy
 * 
 *************************************************************************/

public class CircularSuffixArray {
    private int[] suffixindex = null;
    private int strlen;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        strlen = s.length();
        suffixindex = new int[strlen];

        MySuffixArrayX array = new MySuffixArrayX(s);

        for (int i = 0; i < strlen; i++) {
            suffixindex[i] = array.index(i);
        }

    }

    // length of s
    public int length() {
        return strlen;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        return suffixindex[i];
    }

    public static void main(String[] args) {
        CircularSuffixArray array = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < array.length(); i++) {
            StdOut.println(array.index(i));
        }
    }
}