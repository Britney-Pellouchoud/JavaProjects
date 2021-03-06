import com.sun.xml.internal.xsom.impl.scd.Iterators;
import org.antlr.v4.runtime.tree.Tree;
import org.junit.Test;

import java.util.Arrays;

/** A partition of a set of contiguous integers that allows (a) finding whether
 *  two integers are in the same partition set and (b) replacing two partitions
 *  with their union.  At any given time, for a structure partitioning
 *  the integers 1-N, each partition is represented by a unique member of that
 *  partition, called its representative.
 *  @BritneyPellouchoud
 */
public class UnionFind {

    /** A union-find structure consisting of the sets { 1 }, { 2 }, ... { N }.
     */

    private int[] parent;

    public UnionFind(int N) {
       parent = new int[N + 1];
       for(int i = 1; i <= N; i++) {
           parent[i] = i;
       }
        // FIXME
    }

    /** Return the representative of the partition currently containing V.
     *  Assumes V is contained in one of the partitions.  */
    public int find(int v) {
        while(v != parent[v] && v != 0) {
            v = parent[v];
        }
        return v;
    }




    /** Return true iff U and V are in the same partition. */
    public boolean samePartition(int u, int v) {
        return find(u) == find(v);
    }

    /** Union U and V into a single partition, returning its representative. */
    public int union(int u, int v) {
        if(samePartition(u, v)) {
            return find(u);
        }
        int uroot = find(u);
        int vroot = find(v);
        parent[uroot] = vroot;
        return vroot;
    }

    // FIXME
}
