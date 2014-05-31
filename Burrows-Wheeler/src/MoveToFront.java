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
        char[] ch = new char[R];

        for (int i = 0; i < R; i++) {
            ch[i] = (char) i;
        }

        char c;
        char j, tmp, tmp2;
        while (!BinaryStdIn.isEmpty()) {
            c = BinaryStdIn.readChar();
            tmp = ch[0];
            for (j = 0; ch[j] != c; j++) {
                tmp2 = ch[j];
                ch[j] = tmp;
                tmp = tmp2;
            }
            ch[j] = tmp;
            BinaryStdOut.write(j);
            ch[0] = c;
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
        char pos;
        while (!BinaryStdIn.isEmpty()) {
            // BUG:需要读入 char 而不是 byte
            pos = BinaryStdIn.readChar();
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