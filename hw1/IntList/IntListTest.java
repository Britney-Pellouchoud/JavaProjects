import static org.junit.Assert.*;
import org.junit.Test;

public class IntListTest {

    /** Sample test that verifies correctness of the IntList.list static
     *  method. The main point of this is to convince you that
     *  assertEquals knows how to handle IntLists just fine.
     */

    @Test
    public void testList() {
        IntList one = new IntList(1, null);
        IntList twoOne = new IntList(2, one);
        IntList threeTwoOne = new IntList(3, twoOne);

        IntList x = IntList.list(3, 2, 1);
        assertEquals(threeTwoOne, x);
    }

    /** Do not use the new keyword in your tests. You can create
     *  lists using the handy IntList.list method.
     *
     *  Make sure to include test cases involving lists of various sizes
     *  on both sides of the operation. That includes the empty list, which
     *  can be instantiated, for example, with
     *  IntList empty = IntList.list().
     *
     *  Keep in mind that dcatenate(A, B) is NOT required to leave A untouched.
     *  Anything can happen to A.
     */

    @Test
    public void testDcatenate() {
        IntList a = IntList.list(1, 2);
        IntList b = IntList.list(3, 4);
        IntList answer = IntList.list(1,2,3,4);
        assertEquals(answer, IntList.dcatenate(a, b));
        assertEquals(IntList.list(1,2,3,4), a);
        assert(a.tail.head == 2);

    }

    /** Tests that subtail works properly. Again, don't use new.
     *
     *  Make sure to test that subtail does not modify the list.
     */

    @Test
    public void testSubtail() {
        IntList first_list = IntList.list(1,2,3,4);
        IntList answer = IntList.list(3,4);
        IntList helper = IntList.subTail(first_list, 2);
        assertEquals(answer, helper);
        assert(helper.head == 3);
    }

    /** Tests that sublist works properly. Again, don't use new.
     *
     *  Make sure to test that sublist does not modify the list.
     */

    @Test
    public void testSublist() {


        /** Tests that dSublist works properly. Again, don't use new.
         *
         *  As with testDcatenate, it is not safe to assume that list passed
         *  to dSublist is the same after any call to dSublist
         */
        IntList tester = IntList.list(1, 2, 3, 4, 5);
        IntList answer_test = IntList.list(3, 4);
        assertEquals(answer_test, IntList.sublist(tester, 2, 2));
        //assert(answer_test == IntList.sublist(tester, 2, 3));
    }

    @Test
    public void testDsublist() {
        IntList a = IntList.list(1,2,3,4,5);
        IntList b = IntList.list(3,4);
        IntList c = IntList.dsublist(a, 2, 2);
        assertEquals(b, c);
        assert(a.head == 3);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(IntListTest.class));
    }
}
