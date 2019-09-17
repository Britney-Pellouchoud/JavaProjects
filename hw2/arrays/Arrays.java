package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author
 */
class Arrays {
    /* C. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
            int[] k = new int[A.length + B.length];
            int counter = 0;
            for (; counter < A.length; counter++) {
                k[counter] = A[counter];
            }
            for (; counter < A.length + B.length; counter++) {
                k[counter] = B[counter - A.length];
            }
            return k;
        /* *Replace this body with the solution. */
    }

    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. */
    static int[] remove(int[] A, int start, int len) {
        /* *Replace this body with the solution. */
        int x = 0;
        int[] y = new int[A.length - len];
        for (; x < start; x++) {
            y[x] = A[x];
        }
        x += len;
        for (; x < A.length; x++) {
            y[x - len] = A[x];
        }
        return y;
    }




    /* E. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A) {
        if (A.length == 0)
            return new int[0][];
        int k = 1;
        int [][] l;
        int m = 0;
        for (int len = 1; len < A.length; len++) {


        	if (A[len] < A[len - 1]) k++;
        }
        l = new int[k][];
        k = 0;
        for (int i = 1; i < A.length; i++) {
        	if (A[i] < A[i - 1]) {
        		l[k++] = Utils.subarray(A, m, i - m);
        		m = i;
        	}
        }
        if (k != l.length) {
        	l[k] = Utils.subarray(A, m, A.length - m);
        }
        return l;

    }
}



//go over this weekend to really understand arrays
