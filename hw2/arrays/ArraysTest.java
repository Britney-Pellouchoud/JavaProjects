package arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author FIXME
 */

public class ArraysTest {
    /** FIXME
     */

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }

    @Test
    public void testCatenate() {
        int a[] = new int[]{1, 2, 3};
        int b[] = new int[]{4,5,6};
        int c[] = new int[]{1, 2, 3, 4, 5, 6};
        assertArrayEquals(c ,Arrays.catenate(a, b));

        int i[] = new int[]{0};
        int j[] = new int[]{1};
        int k[] = new int[]{0,1};
        assertArrayEquals(k, Arrays.catenate(i, j));
    }

    /** Test remove */
    @Test
    public void testRemove() {
        int q[] = new int[]{1, 2, 3, 4, 5, 6};
        int r[] = new int[]{2, 3, 4};
        assertArrayEquals(r, Arrays.remove(q, 1, 3));

        int hello[] = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int world[] = new int[]{4, 5, 6, 7};
        assertArrayEquals(world, Arrays.remove(hello, 3, 4));

    }
}


