package lists;

/* NOTE: The file Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2, Problem #1. */

/** List problem.
 *  @author
 */
class Lists {
    /** Return the list of lists formed by breaking up L into "natural runs":
     *  that is, maximal strictly ascending sublists, in the same order as
     *  the original.  For example, if L is (1, 3, 7, 5, 4, 6, 9, 10, 10, 11),
     *  then result is the four-item list
     *            ((1, 3, 7), (5), (4, 6, 9, 10), (10, 11)).
     *  Destructive: creates no new IntList items, and may modify the
     *  original list pointed to by L. */
    public static int [] naturalRuns(int [] L) {
        int[] answer = new int[]{};
        int i;
        i = 0;
        while (i < L.length) {
            int j;
            j = i;
            while (L[j] < L[j + 1] && j+1 < L.length ){
                j += 1;
            }
            answer[j] = (IntListList.list(i, j)));
            i = j;
        }
        return answer;
    }

    static IntListList naturalRuns(IntList L) {
        /* *Replace this body with the solution. */
        return null;
    }
}
