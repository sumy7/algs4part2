public class myCC {
    public static void main(String[] args) {
        Digraph dig = new Digraph(10);
        for (int i = 0; i < 17; i++) {
            String a = StdIn.readString();
            String b = StdIn.readString();
            StdOut.println(a + " " + b);
            dig.addEdge(a.charAt(0) - 'A', b.charAt(0) - 'A');
        }
        KosarajuSharirSCC scc = new KosarajuSharirSCC(dig);

        for (int i = 0; i < 10; i++) {
            StdOut.print(scc.id(i) + " ");
        }
    }
}