package tablut;

import org.junit.Test;
import static org.junit.Assert.*;
import static tablut.Piece.EMPTY;
import static tablut.Piece.KING;

import ucb.junit.textui;

/** The suite of all JUnit tests for the enigma package.
 *  @BritneyPellouchoud
 */
public class UnitTest {

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }

    /** A dummy test as a placeholder for real ones. */
    @Test
    public void dummyTest() {
        return;
    }

    @Test
    public void inittest() {
        Board testboard = new Board();
        assert testboard.getallPieces().get(03).toString() == "B";
    }

    @Test
    public void copytest() {
        Board testboard = new Board();
        Board modelboard = new Board();
        Piece k = KING;
        modelboard.setMoveLimit(5);
        modelboard.makeMove(Square.sq(3,0), Square.sq(2,0));
        modelboard.makeMove(modelboard.NTHRONE, Square.sq(5,5));
        modelboard.makeMove(Square.sq(5, 0), Square.sq(6,0));
        modelboard.makeMove(modelboard.THRONE, modelboard.NTHRONE);
        assert modelboard.get(modelboard.NTHRONE) == KING;
        assert modelboard.get(modelboard.THRONE) == EMPTY;
        testboard.copy(modelboard);
        assert testboard.get(testboard.NTHRONE) == KING;
        assert testboard.get(testboard.THRONE) == EMPTY;
        System.out.println(testboard);
    }

}


