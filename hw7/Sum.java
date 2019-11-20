import org.junit.Test;

/** HW #7, Two-sum problem.
 * @author
 */
public class Sum {

    /** Returns true iff A[i]+B[j] = M for some i and j. */
    public static boolean sumsTo(int[] A, int[] B, int m) {
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < B.length; j++) {
                if(A[i] + B[j] == m) {
                    return true;
                }
            }
        }
        return false;  // REPLACE WITH YOUR ANSWER
    }

    @Test
    public void sumtest() {
        int[] A = new int[]{1,2,3,4,5};
        int[] B = new int[]{7,6,5,4,3};
        System.out.println(sumsTo(A, B, 0));
    }

}
