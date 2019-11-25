import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

/** Minimal spanning tree utility.
 *  @author
 */
public class MST {

    /** Given an undirected, weighted, connected graph whose vertices are
     *  numbered 1 to V, and an array E of edges, returns an array of edges
     *  in E that form a minimal spanning tree of the input graph.
     *  Each edge in E is a three-element int array of the form (u, v, w),
     *  where 0 < u < v <= V are vertex numbers, and 0 <= w is the weight
     *  of the edge. The result is an array containing edges from E.
     *  Neither E nor the arrays in it may be modified.  There may be
     *  multiple edges between vertices.  The objects in the returned array
     *  are a subset of those in E (they do not include copies of the
     *  original edges, just the original edges themselves.) */
    public static int[][] mst(int V, int[][] E) {
        int[][] answer;
        int[] weights = new int[E.length];
        for(int i = 0; i < E.length; i++) {
            weights[i] = E[i][2];
        }
        int[] sortedweights = incweight(weights);
        int[][]sortedbyweight = new int[E.length][];
        for (int i = 0; i < sortedbyweight.length; i++) {
            for(int j = 0; j < E.length; j++) {
                if (E[j][2] == sortedweights[i]) {
                    sortedbyweight[j] = E[j];
                }
            }
        }
        //now, sorted by weight


        return null;  // FIXME
    }

    public static int[] incweight(int[] weights) {
        for (int i = 1; i < weights.length; i++) {
            int current = weights[i];
            int j = i - 1;
            while(j >= 0 && current < weights[j]) {
                weights[j+1] = weights[j];
                j--;
            }
            weights[j+1] = current;
        }
        return weights;
    }

    @Test
    public void incweighter() {
        int[] x = new int[]{7,6,5,4,3,2,1};
        System.out.println(Arrays.toString(incweight(x)));
    }

    /** An ordering of edges by weight. */
    private static final Comparator<int[]> EDGE_WEIGHT_COMPARATOR =
        new Comparator<int[]>() {
            @Override
            public int compare(int[] e0, int[] e1) {
                return e0[2] - e1[2];
            }
        };

}
