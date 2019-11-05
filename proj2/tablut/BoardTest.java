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
        assert testboard.allPieces.get(03).toString() == "B";
    }

    @Test
    public void copytest() {
        Board testboard = new Board();
        Board modelboard = new Board();
        Piece help = WHITE;
        testboard.copy(modelboard);

        System.out.println(modelboard.allPieces.get(03));
        System.out.println(testboard.allPieces.get(80));

        Iterator it = testboard.allPieces.entrySet().iterator();
        Iterator it2 = modelboard.allPieces.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) (it.next());
            Map.Entry pair2 = (Map.Entry) (it2.next());
            assert pair.getKey() == pair2.getKey();
            assert pair.getValue() == pair2.getValue();
        }
    }


    @Test
    public void kingpositiontest() {
        Board testboard = new Board();
        assert Board.THRONE == testboard.kingPosition();
    }

    @Test
    public void setMoveLimittest() {
        Board testboard = new Board();
        testboard.setMoveLimit(10);
        assert testboard.movelimit() == 10;
    }

    @Test
    public void pieceLocationstest() {
        Board testboard = new Board();
        Piece testblack = BLACK;
        HashSet a = testboard.pieceLocations(testblack);
        assert(a.size() == 16);
        Piece testwhite = WHITE;
        HashSet b = testboard.pieceLocations(testwhite);
        assert(b.size() == 9);
    }

}
