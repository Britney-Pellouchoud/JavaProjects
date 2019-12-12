package gitlet;

import ucb.junit.textui;
import org.junit.Test;
import static org.junit.Assert.*;

/** The suite of all JUnit tests for the gitlet package.
 *  @BritneyPellouchoud
 */
public class UnitTest {
    private int x;

    /** Run the JUnit tests in the loa package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }

    /** A dummy test to avoid complaint. */
    @Test
    public void placeholderTest() {
    }

    @Test
    public void testchange() {
        changertest();
    }
    void next() {
        System.out.println(x);
    }

    void changertest() {
        x = 3;
    }

}


