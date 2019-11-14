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

        return _lastFoundMove.toString(); // FIXME
    }

    @Override
    boolean isManual() {
        return false;
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(board());
        _lastFoundMove = null;
        int x = findMove(b, maxDepth(b), true, 2, 2, 2);
        // FIXME
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
        if (sense == 1) {
            Move m = findmax(board, depth, alpha, beta);
            board.makeMove(m);
            return staticScore(board);
            //return the maximal value
        }
        else {
            Move m = findmin(board, depth, alpha, beta);
            board.makeMove(m);
            return staticScore(board);
            //return the minimum value
        }
    }

    private Move findmax(Board b, int depth, int alpha, int beta) {
        if (depth == 0 || b.winner() != null) {
            return simplefindmax(b, alpha, beta);
        }
        //random move
        Move bestsofar = new Move(Square.sq(13), Square.sq(12));
        int best = Integer.MIN_VALUE;
        for (Move m : b.legalMoves(_myPiece)) {
            b.makeMove(m);
            Board possible = b;
            Move response = findmin(possible, depth - 1, alpha, beta);
            if (staticScore(b) >= best) {
                bestsofar = m;
                best = staticScore(b);
                alpha = max(alpha, best);
                if (beta < alpha) {
                    break;
                }
            }
        }
        return bestsofar;
    }

    private Move simplefindmax(Board b, int alpha, int beta) {
        Move bestsofar = new Move(Square.sq(13), Square.sq(12));
        int best = Integer.MIN_VALUE;
        for (Move m : b.legalMoves(_myPiece)) {
            b.makeMove(m);
            Board possible = b;
            if (staticScore(b) >= best) {
                bestsofar = m;
                alpha = max(alpha, best);
                if (beta <= alpha) {
                    break;
                }
            }
        }
        return bestsofar;
    }

    private Move findmin(Board b, int depth, int alpha, int beta) {
        if (depth == 0 || b.winner() != null) {
            return simplefindmin(b, alpha, beta);
        }
        Move bestsofar = new Move(Square.sq(13), Square.sq(12));
        int best = Integer.MAX_VALUE;
        Board possible = b;
        for (Move m : b.legalMoves(_myPiece)) {
            b.makeMove(m);
            possible = b;
            Move response = findmax(possible, depth - 1, alpha, beta);
            b.makeMove(response);
            if (staticScore(b) <= best) {
                bestsofar = m;
                best = staticScore(b);
                beta = min(beta, staticScore(b));
                if (beta <= alpha) {
                    break;
                }
            }
        }
        return bestsofar;

    }

    private Move simplefindmin(Board b, int alpha, int beta) {
        Move bestsofar =  new Move(Square.sq(13), Square.sq(12));
        int best = Integer.MAX_VALUE;
        for (Move m : b.legalMoves(_myPiece)) {
            b.makeMove(m);
            Board x = b;
            if (staticScore(x) <= best) {
                bestsofar = m;
                best = staticScore(x);
                beta = min(beta, best);
                if (beta <= alpha) {
                    break;
                }
            }
        }
        return bestsofar;
    }





    /** Return a heuristically determined maximum search depth
     *  based on characteristics of BOARD. */
    private static int maxDepth(Board board) {
        return 4; // FIXME?
    }

    /** Return a heuristic value for BOARD. */
    //higher is better for white
    //lower is better for black
    private int staticScore(Board board) {
        int blwh = whiteminusblack(board);
        Square k = board.kingPosition();
        double distfromcenter = Math.sqrt((k.row() - 4) ^ 2 + (k.col() - 4) ^ 2 );
        return blwh + (int) distfromcenter;
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
