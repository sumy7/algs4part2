/*************************************************************************
 * 
 * @author sumy
 * 
 *************************************************************************/

public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to
    // standard output
    private static final int R = 256;

    public static void encode() {
        // read the input
        String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();

        char[] ch = new char[R];

        for (int i = 0; i < R; i++) {
            ch[i] = (char) i;
        }

        char tmp, tmp2;
        for (int i = 0; i < input.length; i++) {
            tmp = ch[0];
            char j;
            for (j = 0; ch[j] != input[i]; j++) {
                tmp2 = ch[j];
                ch[j] = tmp;
                tmp = tmp2;
            }
            ch[j] = tmp;
            BinaryStdOut.write(j);
            ch[0] = input[i];
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to
    // standard output
    public static void decode() {
        char[] ch = new char[R];
        for (int i = 0; i < R; i++) {
            ch[i] = (char) i;
        }

        char tmp;
        while (!BinaryStdIn.isEmpty()) {
            // BUG:需要读入 char 而不是 byte
            int pos = BinaryStdIn.readChar();
            for (tmp = ch[pos]; pos > 0; pos--) {
                ch[pos] = ch[pos - 1];
            }
            ch[pos] = tmp;
            BinaryStdOut.write(tmp);
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-"))
            encode();
        else if (args[0].equals("+"))
            decode();
        else
            throw new IllegalArgumentException("Illegal command line argument");
    }
}