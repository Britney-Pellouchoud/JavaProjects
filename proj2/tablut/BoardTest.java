package tablut;
import org.junit.Test;

import java.util.*;

import static tablut.Piece.*;



public class BoardTest {



    @Test
    public void copytestr() {
        Square s = Square.sq(7, 4);
        Square s1 = Square.sq(7, 5);
        Board test = new Board();
        test.makeMove(s, s1);
        Board test2 = new Board();
        test2.copy(test);
        test.undo();
        System.out.println(test.getallPieces().get(s1.index()));
        System.out.println(test2.getallPieces().get(s1.index()));

        assert test.getallPieces().equals(test2.getallPieces());
    }

    @Test
    public void copytester(){
        Square s = Square.sq(7, 4);
        Square s1 = Square.sq(7, 5);
        Board test = new Board();
        test.makeMove(s, s1);
        System.out.println(test.turn());
        Board test2 = new Board();
        test2.copy(test);
        System.out.println(test2.turn());
    }

    @Test
    public void undoer() {
        Square s = Square.sq(7, 4);
        Square s1 = Square.sq(7, 5);
        Board testboard = new Board();
        testboard.makeMove(s, s1);
        testboard.makeMove(Square.sq(6, 4), Square.sq(6, 5));
        testboard.undo();
        testboard.undo();
        testboard.makeMove(s, s1);
        assert testboard.getallPieces().get(s1.index()) != EMPTY;
        System.out.println(testboard);



    }

    @Test
    public void squareindex() {
        Square s = Square.sq(0,1);
        System.out.println(s.index());
        Square s1 = Square.sq(0,2);
        System.out.println(s1.index());
        Square s2 = Square.sq(0, 3);
        System.out.println(s2.index());

    }

    @Test
    public void test() {
        Board testboard = new Board();
        testboard.makeMove(Square.sq(8, 5), Square.sq(6, 5));
        System.out.println(testboard);
        testboard.makeMove(Square.sq(2, 4), Square.sq(2, 3));
        System.out.println(testboard);
        testboard.makeMove(Square.sq(8, 3), Square.sq(6, 3));
        System.out.println(testboard);
        testboard.undo();
        System.out.println(testboard);
    }


    @Test
    public void sectiontest() {
        Square sthrone = Square.sq(4, 3);
        Board test = new Board();
        Square s = Square.sq(1,0);
        System.out.println(test.whichsection(s));
    }


    @Test
    public void capturetest() {
        Board testboard = new Board();
        Square s1 = Square.sq(8, 5);
        Square s01 = Square.sq(6, 5);
        testboard.makeMove(s1, s01);
        System.out.println(testboard);
        Square d1 = Square.sq(3, 4);
        Square d2 = Square.sq(3, 6);
        testboard.makeMove(d1, d2);
        System.out.println(testboard);
        Square e1 = Square.sq(8, 3);
        Square e2 = Square.sq(6, 3);
        testboard.makeMove(e1, e2);
        System.out.println(testboard);
        Square fix =  Square.sq(6, 4);
        assert testboard.getallPieces().get(fix.index()) == EMPTY;

    }

    @Test
    public void inittest() {
        Board testboard = new Board();
        assert testboard.getallPieces().get(03).toString().equals("B");
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

    @Test
    public void undotest() {
        Board testboard = new Board();
        testboard.makeMove((Square.sq(7,4)), Square.sq(7,5));
        System.out.println(testboard);
        testboard.undo();
        System.out.println(testboard);
    }

    @Test
    public void testremove() {
        java.util.List<Integer> k = new java.util.ArrayList<Integer>();
        k.add(1);
        k.add(2);
        k.remove(k.size() - 1);
        System.out.println(k);

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


    @Test
    public void kingmovestest() {
        Board testboard = new Board();
        Square h5 = Square.sq(7,4);
        Square h6 = Square.sq(7,5);
        testboard.makeMove(h5, h6);
        Square e4 = Square.sq(4, 3);
        Square b4 = Square.sq(1, 3);
        testboard.makeMove(e4, b4);
        Square h7 = Square.sq(7,6);
        testboard.makeMove(h6, h7);
        System.out.println(testboard);
        Square e5 = Square.sq(4, 4);
        testboard.makeMove(e5, e4);
        System.out.println(testboard);
        Square h8 = Square.sq(7,7);
        testboard.makeMove(h7, h8);
        Square h4 = Square.sq(7, 3);
        testboard.makeMove(e4, h4);
        System.out.println(testboard);
    }

    @Test
    public void initialclear() {
        Board testboard = new Board();
        Square h5 = Square.sq(7,4);
        Square h6 = Square.sq(7,5);
        testboard.makeMove(h5, h6);
        System.out.println(testboard);
        testboard.init();
        System.out.println(testboard);

    }

}
