import com.sun.xml.internal.xsom.impl.scd.Iterators;
import org.antlr.v4.runtime.tree.Tree;

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
    private int[] structure;
    private int[] lengths;

    public UnionFind(int N) {
        structure = new int[N];
        for (int i = 0; i < N; i++) {
            structure[i] = i;
            lengths[i] = 1;
        }
        // FIXME
    }

    /** Return the representative of the partition currently containing V.
     *  Assumes V is contained in one of the partitions.  */
    public int find(int v) {
        while (v != structure[v]) {
            structure[v] = structure[structure[v]];
            v = structure[v];// FIXME
        }
        return v;
    }

    /** Return true iff U and V are in the same partition. */
    public boolean samePartition(int u, int v) {
        return find(u) == find(v);
    }

    /** Union U and V into a single partition, returning its representative. */
    public int union(int u, int v) {
        int upos = find(u);
        int vpos = find(v);
        if (samePartition(u, v)) {
            return upos;
        } else if (lengths[upos] < lengths[vpos]) {
            structure[upos] = vpos;
            lengths[vpos] += lengths[upos];
            return upos;
        } else {
            structure[vpos] = upos;
            lengths[upos] += lengths[vpos];
            return vpos;
        }
    }

    // FIXME
}
