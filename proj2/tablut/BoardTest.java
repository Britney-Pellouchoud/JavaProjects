package tablut;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.*;
import static tablut.Piece.*;



public class BoardTest {

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
    }


    @Test
    public void kingpositiontest() {
        Board testboard = new Board();
        assert testboard.THRONE == testboard.kingPosition();
    }

    @Test
    public void legalMovestest() {
        Board test = new Board();
        System.out.println(test.legalMoves(WHITE));
    }

    @Test
    public void gettest() {
        Board testboard = new Board();
        Piece k = KING;
        assert testboard.get(4,4).equals(k);
        assert testboard.get('e', '5').equals(k);
    }

    @Test
    public void puttest() {
        Board testboard = new Board();
        Piece k = KING;
        testboard.put(k, testboard.NTHRONE);
        System.out.println(testboard.get(testboard.NTHRONE));
    }

    @Test
    public void unblockedtest() {
        Board testboard = new Board();
        Square s1 = Square.sq(0, 3);
        Square s2 = Square.sq(2, 3);
        assert testboard.isUnblockedMove(s1, s2) == true;
        Square a1 = Square.sq(0,4);
        Square a2 = Square.sq(2, 4);
        assert testboard.isUnblockedMove(a1, a2) == false;
    }



}
