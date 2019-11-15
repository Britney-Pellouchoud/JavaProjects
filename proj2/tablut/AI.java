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
        findMove();
        return "* " + _lastFoundMove.toString(); // FIXME
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
        int x = findMove(b, maxDepth(b), true, side, -INFTY, INFTY);
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
        assert sense == 1 || sense == -1;
        if (sense == -1) {
            Move min = findmin(board, depth, alpha, beta);
            board.makeMove(min);
            if (saveMove) {
                _lastFoundMove = min;
            }
            return staticScore(board);
        } else {
            Move max = findmax(board, depth, alpha, beta);
            board.makeMove(max);
            if (saveMove) {
                _lastFoundMove = max;
            }
            return staticScore(board);
        }
    }

    private Move simplefindmax(Board board, int alpha, int beta) {
        Move bestsofar = null;
        int best = -INFTY;
        for (Move m : board.legalMoves(myPiece())) {
            Board next = new Board();
            board.makeMove(m);
            next.copy(board);
            board.undo();
            if (staticScore(next) >= best) {
                bestsofar = m;
                best = staticScore(next);
                alpha = max(alpha, best);
                if (beta <= alpha) {
                    break;
                }
            }
        }
        return bestsofar;
    }


    private Move findmax(Board board, int depth, int alpha, int beta) {
        if (depth == 0 || staticScore(board) == WINNING_VALUE) {
            return simplefindmax(board, alpha, beta);
        }
        Move bestsofar = null;
        int bestsofarval = -INFTY;
        for (Move m : board.legalMoves(WHITE)) {
             board.makeMove(m);
             Board next = new Board();
             next.copy(board);
             board.undo();
             Move response = findmin(next, depth - 1, alpha, beta);
             next.makeMove(response);
             if (staticScore(next) >= bestsofarval) {
                 bestsofar = m;
                 alpha = max(alpha, staticScore(next));
                 if (beta <= alpha) {
                     break;
                 }
             }
        }
        return bestsofar;
   }


   private Move simplefindmin(Board board, int alpha, int beta) {
        if (board.winner() != null) {
            return null;
        }
        Move bestsofar = null;
        int best = INFTY;
        for (Move m : board.legalMoves(myPiece())) {
            Board next = new Board();
            board.makeMove(m);
            next.copy(board);
            board.undo();
            if (staticScore(next) <= best) {
                bestsofar = m;
                best = staticScore(next);
                beta = min(beta, best);
                if (beta <= alpha) {
                    break;
                }
            }
        }
        return bestsofar;
   }

    private Move findmin(Board board, int depth, int alpha, int beta) {
        if (depth == 0 || staticScore(board) == -WINNING_VALUE) {
            return simplefindmin(board, alpha, beta);
        }
        Move bestsofar = null;
        int best = INFTY;
        for (Move m : board.legalMoves(BLACK)) {
            Board next = new Board();
            board.makeMove(m);
            next.copy(board);
            Board respboard = new Board();
            Move response = findmax(next, depth - 1, alpha, beta);
            respboard.copy(board);
            if (staticScore(respboard) <= best) {
                bestsofar = m;
                best = staticScore(respboard);
                beta = min(beta, staticScore(respboard));
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
