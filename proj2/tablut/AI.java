package tablut;

import java.util.HashMap;

import static java.lang.Math.*;

import static tablut.Square.sq;
import static tablut.Board.THRONE;
import static tablut.Piece.*;

/** A Player that automatically generates moves.
 *  @BritneyPellouchoud
 */
class AI extends Player {

    /** A position-score magnitude indicating a win (for white if positive,
     *  black if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A position-score magnitude indicating a forced win in a subsequent
     *  move.  This differs from WINNING_VALUE to avoid putting off wins. */
    private static final int WILL_WIN_VALUE = Integer.MAX_VALUE - 40;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI with no piece or controller (intended to produce
     *  a template). */
    AI() {
        this(null, null);
    }

    /** A new AI playing PIECE under control of CONTROLLER. */
    AI(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new AI(piece, controller);
    }

    @Override
    String myMove() {
        return findMove().toString(); // FIXME
    }

    @Override
    boolean isManual() {
        return false;
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(board());
        int side = 0;
        if (this.myPiece() == BLACK) {
            side = -1;
        } else {
            side = 1;
        }
        findMove(b, maxDepth(b), true, side, INFTY, -INFTY);
        return _lastFoundMove;
    }

    /** The move found by the last call to one of the ...FindMove methods
     *  below. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _lastFoundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _lastMoveFound. */

    private int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {
        assert sense == -1 || sense == 1;
        if (depth == 0) {
            return staticScore(board);
        } if (sense == -1) {
            int bestval = INFTY;
            Move bestsofar = null;
            for (Move m : board.legalMoves(myPiece())) {
                Board test = new Board();
                board.makeMove(m);
                test.copy(board);
                board.undo();
                int value = findMove(test, depth - 1, false, 1, alpha, beta);
                bestval = min(bestval, value);
                beta = min(bestval, value);
                if (beta >= alpha) {
                    break;
                }
                if (value == bestval) {
                    bestsofar = m;
                }
            }
            if (saveMove) {
                _lastFoundMove = bestsofar;
            }
            return bestval;
        } else {
            int bestval = -INFTY;
            Move bestsofar = null;
            for (Move m : board.legalMoves(myPiece())) {
                Board test = new Board();
                board.makeMove(m);
                test.copy(board);
                board.undo();
                bestsofar = m;
                int value = findMove(test, depth - 1, false, -1, alpha, beta);
                bestval = max(bestval, value);
                alpha = max(bestval, value);
                if (beta >= alpha) {
                    break;
                }
                if (value == bestval) {
                    bestsofar = m;
                }
            }
            if (saveMove) {
                _lastFoundMove = bestsofar;
            }
            return bestval;
        }


    }








    /** Return a heuristically determined maximum search depth
     *  based on characteristics of BOARD. */
    private static int maxDepth(Board board) {
        return 4; // FIXME?
    }

    /** Return a heuristic value for BOARD. */
    //positive is better for white
    //negative is better for black
    private int staticScore(Board board) {
        int blwh = whiteminusblack(board);
        Square k = board.kingPosition();
        double distfromcenter = Math.sqrt((k.row() - 4) ^ 2 + (k.col() - 4) ^ 2 );
        int captureking = 0;
        for (Square s : board.pieceLocations(BLACK)) {
            if (s.isRookMove(k)) {
                captureking -= 5;
            }
        }
        int clearpathforking = clearpathwhitewin(k, board);

        return blwh + (int) distfromcenter + captureking + clearpathforking;
    }

    private int clearpathwhitewin(Square kingpos, Board board) {
        int clearpathforking = 0;
        if (board.isUnblockedMove(kingpos, Square.sq(0, kingpos.col()))) {
            clearpathforking += 5;
        } if (board.isUnblockedMove(kingpos, Square.sq(kingpos.row(), 0))) {
            clearpathforking += 5;
        } if (board.isUnblockedMove(kingpos, Square.sq(8, kingpos.col()))) {
            clearpathforking += 5;
        } if (board.isUnblockedMove(kingpos, Square.sq(kingpos.row(), 8))) {
            clearpathforking += 5;
        }
        return clearpathforking;
    }





    private int whiteminusblack(Board board) {
        HashMap<Integer, Piece> x = board.getallPieces();
        int numwhite = 0;
        int numblack = 0;
        for (int i : board.getallPieces().keySet()){
            if (board.getallPieces().get(i) == BLACK) {
                numblack += 1;
            } if (board.getallPieces().get(i) == EMPTY) {
                continue;
            } else {
                numwhite += 1;
            }
        }
        return numwhite - numblack;  // FIXME
    }

    // FIXME: More here.

}
