import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        /** Sample assert statement for comparing integers.
        assertEquals(0, 0); */
        assertEquals(0, CompoundInterest.numYears(2019));
        assertEquals(10, CompoundInterest.numYears(2029));
    }

    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(10, CompoundInterest.futureValue(10, 12, 2019), tolerance);
        assertEquals(12.544, CompoundInterest.futureValue(10, 12, 2021), tolerance);
        assertEquals(8.1, CompoundInterest.futureValue(10, -10, 2021), tolerance);
    }

        @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(11.8026496, CompoundInterest.futureValueReal(10, 12, 2021, 3), tolerance);
        assertEquals(7.157159, CompoundInterest.futureValueReal(10, -10, 2021, 6), tolerance);
    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(16550, CompoundInterest.totalSavings(5000, 2021, 10), tolerance);
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        assertEquals(15571.895, CompoundInterest.totalSavingsReal(5000, 2021, 10, 3), tolerance);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
