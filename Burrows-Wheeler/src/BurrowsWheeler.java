/*************************************************************************
 * 
 * @author sumy
 * 
 *************************************************************************/

public class BurrowsWheeler {
    private static final int R = 256;

    // apply Burrows-Wheeler encoding, reading from standard input and writing
    // to standard output
    public static void encode() {
        // read the input
        String s = BinaryStdIn.readString();
        int len = s.length();

        CircularSuffixArray array = new CircularSuffixArray(s);

        int first = 0;
        for (int i = 0; i < array.length(); i++) {
            if (array.index(i) == 0) {
                first = i;
                BinaryStdOut.write(first);
                break;
            }
        }
        for (int i = 0; i < array.length(); i++) {
            int index = array.index(i);
            char x = s.charAt((index - 1 + len) % len);
            BinaryStdOut.write(x);
        }

        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing
    // to standard output
    public static void decode() {
        int first = BinaryStdIn.readInt(32);
        String s = BinaryStdIn.readString();
        int len = s.length();

        char[] ch = s.toCharArray();

        int[] next = new int[len];
        int[] count = new int[R + 1];

        // WARING:桶排序
        for (int i = 0; i < len; i++) {
            count[ch[i] + 1]++;
        }
        for (int i = 0; i < R; i++) {
            count[i + 1] += count[i];
        }
        for (int i = 0; i < len; i++) {
            next[count[ch[i]]++] = i;
        }

        for (int i = next[first], c = 0; c < len; i = next[i], c++) {
            BinaryStdOut.write(s.charAt(i));
        }

        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        if (args[0].equals("-"))
            encode();
        else if (args[0].equals("+"))
            decode();
        else
            throw new IllegalArgumentException("Illegal command line argument");
    }
}