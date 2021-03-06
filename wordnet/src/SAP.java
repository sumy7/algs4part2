public class SAP {

    private Digraph dig = null;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        dig = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        checkBound(v);
        checkBound(w);

        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(dig, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(dig, w);
        int mindis = dig.V() * 2;
        boolean haspath = false;
        for (int i = 0; i < dig.V(); i++) {
            if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) {
                int dis = bfsv.distTo(i) + bfsw.distTo(i);
                if (dis < mindis) {
                    mindis = dis;
                    haspath = true;
                }
            }
        }
        if (haspath)
            return mindis;
        return -1;
    }

    // a common ancestor of v and w that participates in a shortest ancestral
    // path; -1 if no such path
    public int ancestor(int v, int w) {
        checkBound(v);
        checkBound(w);

        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(dig, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(dig, w);
        int mindis = dig.V() * 2;
        int minancestral = -1;
        for (int i = 0; i < dig.V(); i++) {
            if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) {
                int dis = bfsv.distTo(i) + bfsw.distTo(i);
                if (dis < mindis) {
                    mindis = dis;
                    minancestral = i;
                }
            }
        }
        return minancestral;
    }

    // length of shortest ancestral path between any vertex in v and any vertex
    // in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        for (int node : v) {
            checkBound(node);
        }
        for (int node : w) {
            checkBound(node);
        }

        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(dig, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(dig, w);

        int mindis = dig.V() * 2;
        boolean haspath = false;
        for (int i = 0; i < dig.V(); i++) {
            if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) {
                int dis = bfsv.distTo(i) + bfsw.distTo(i);
                if (dis < mindis) {
                    mindis = dis;
                    haspath = true;
                }
            }
        }
        if (haspath)
            return mindis;
        return -1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no
    // such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        for (int node : v) {
            checkBound(node);
        }
        for (int node : w) {
            checkBound(node);
        }

        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(dig, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(dig, w);
        int mindis = dig.V() * 2;
        int minancestral = -1;
        for (int i = 0; i < dig.V(); i++) {
            if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) {
                int dis = bfsv.distTo(i) + bfsw.distTo(i);
                if (dis < mindis) {
                    mindis = dis;
                    minancestral = i;
                }
            }
        }
        return minancestral;

    }

    private void checkBound(int v) {
        if (v < 0)
            throw new java.lang.IndexOutOfBoundsException();
        if (v >= dig.V())
            throw new java.lang.IndexOutOfBoundsException();
    }

    // for unit testing of this class (such as the one below)
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
