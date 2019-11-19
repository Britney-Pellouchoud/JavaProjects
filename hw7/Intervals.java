import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/** HW #7, Sorting ranges.
 *  @author
  */
public class Intervals {
    /** Assuming that INTERVALS contains two-element arrays of integers,
     *  <x,y> with x <= y, representing intervals of ints, this returns the
     *  total length covered by the union of the intervals. */
    public static int coveredLength(List<int[]> intervals) {
        // REPLACE WITH APPROPRIATE STATEMENTS.
        int outer = 0;
        int starter = Integer.MIN_VALUE;
        int ender = Integer.MIN_VALUE;
        for (int i = 0; i < intervals.size(); i++ ){
            if (intervals.get(i)[0] > ender) {
                outer += ender - starter;
                starter = intervals.get(i)[0];
                ender = intervals.get(i)[1];
            } if (intervals.get(i)[1] > ender && intervals.get(i)[0] <= ender){
                ender = intervals.get(i)[1];
            }
        }
        outer += ender - starter;
        return outer;
    }

    /** Test intervals. */
    static final int[][] INTERVALS = {
        {19, 30},  {8, 15}, {3, 10}, {6, 12}, {4, 5},
    };
    /** Covered length of INTERVALS. */
    static final int CORRECT = 23;

    /** Performs a basic functionality test on the coveredLength method. */
    @Test
    public void basicTest() {
        assertEquals(CORRECT, coveredLength(Arrays.asList(INTERVALS)));
    }

    /** Runs provided JUnit test. ARGS is ignored. */
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(Intervals.class));
    }

}
