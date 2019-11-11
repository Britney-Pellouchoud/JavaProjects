package tablut;

import java.util.*;

import static tablut.Move.ROOK_MOVES;
import static tablut.Piece.*;
import static tablut.Square.*;
import static tablut.Move.mv;


/** The state of a Tablut Game.
 *  @BritneyPellouchoud
 */
class Board {

    /** The number of squares on a side of the board. */
    static final int SIZE = 9;

    /** The throne (or castle) square and its four surrounding squares.. */
    static final Square THRONE = sq(4, 4),
        NTHRONE = sq(4, 5),
        STHRONE = sq(4, 3),
        WTHRONE = sq(3, 4),
        ETHRONE = sq(5, 4);

    /** Initial positions of attackers. */
    static final Square[] INITIAL_ATTACKERS = {
        sq(0, 3), sq(0, 4), sq(0, 5), sq(1, 4),
        sq(8, 3), sq(8, 4), sq(8, 5), sq(7, 4),
        sq(3, 0), sq(4, 0), sq(5, 0), sq(4, 1),
        sq(3, 8), sq(4, 8), sq(5, 8), sq(4, 7)
    };

    /** Initial positions of defenders of the king. */
    static final Square[] INITIAL_DEFENDERS = {
        NTHRONE, ETHRONE, STHRONE, WTHRONE,
        sq(4, 6), sq(4, 2), sq(2, 4), sq(6, 4)
    };

    private HashMap<Integer, Piece> allPieces = new HashMap<>();


    /** Initializes a game board with SIZE squares on a side in the
     *  initial position. */
    Board() {
        init();
    }

    /** Initializes a copy of MODEL. */
    Board(Board model) {
        copy(model);
    }


    HashMap<Integer, Piece> getallPieces() {
        return this.allPieces;
    }

    /** Copies MODEL into me. */

    void copy(Board model) {
        if (model == this) {
            return;
        }
        //Something is wrong with this.
        HashMap<Piece, Integer> reverse = new HashMap<>();
        for (int index : model.allPieces.keySet()) {
            Piece p = model.allPieces.get(index);
            if (reverse.get(p) != null) {
                this.allPieces.remove(index);
            }
            this.allPieces.put(index, p);
            reverse.put(p, index);
        }

        // FIXME
    }

    /** Clears the board to the initial position. */
    void init() {
        _turn = BLACK;
        this.allPieces = new HashMap<>();
        for (Square s : INITIAL_ATTACKERS) {
            Piece black = BLACK;
            this.allPieces.put(s.index(), black);
        }
        for (Square s :INITIAL_DEFENDERS) {
            Piece white = WHITE;
            this.allPieces.put(s.index(), white);
        }
        this.allPieces.put(THRONE.index(), KING);
        for (int i = 0; i <= 80; i++) {
            if (this.allPieces.get(i) == null) {
                this.allPieces.put(i, EMPTY);
            }
        }
        // FIXME
    }

    /** Set the move limit to LIM.  It is an error if 2*LIM <= moveCount(). */
    void setMoveLimit(int n) {
        //assert 2 * n <= moveCount() : "CANNOT HAVE SET THIS MOVE LIMIT.";
        _moveLimit = n;
        // FIXME
    }

    /** Return a Piece representing whose move it is (WHITE or BLACK). */
    Piece turn() {
        return _turn;
    }

    /** Return the winner in the current position, or null if there is no winner
     *  yet. */
    Piece winner() {
        return _winner;
    }



    /** Returns true iff this is a win due to a repeated position. */
    boolean repeatedPosition() {
        return _repeated;
    }

    /** Record current position and set winner() next mover if the current
     *  position is a repeat. */
    private void checkRepeated() {
        if (positions.contains(this.allPieces)) {
            if (_turn == WHITE) {
                _winner = BLACK;
            } else {
                _winner = WHITE;
            }
        }
        // FIXME
    }

    /** Return the number of moves since the initial position that have not been
     *  undone. */
    int moveCount() {
        return moves.size();
    }

    /** Return location of the king. */
    Square kingPosition() {
        int ind = -1;
        Iterator it = this.allPieces.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (pair.getValue().toString().equals("K")) {
                ind = (int) pair.getKey();
            }
        }
        assert ind != -1 : "KING NOT FOUND";
        return Square.sq(ind); // FIXME
    }

    /** Return the contents the square at S. */
    final Piece get(Square s) {
        return get(s.col(), s.row());
    }

    /** Return the contents of the square at (COL, ROW), where
     *  0 <= COL, ROW <= 9. */
    final Piece get(int col, int row) {
        assert col >= 0 && col < 9 && row >= 0 && row < 9 : "OUT OF BOUNDS FOR THIS BOARD";
        Square s = sq(col, row);
        int ind = s.index();
        return this.allPieces.get(ind);
        // FIXME
    }

    /** Return the contents of the square at COL ROW. */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /** Set square S to P. */
    final void put(Piece p, Square s) {
        int i = s.index();
        this.allPieces.put(i, p);
        // FIXME
    }

    /** Set square S to P and record for undoing. */
    final void revPut(Piece p, Square s) {
        int i = s.index();
        this.allPieces.put(i, p);
        // FIXME
    }

    /** Set square COL ROW to P. */
    final void put(Piece p, char col, char row) {
        put(p, sq(col - 'a', row - '1'));
    }

    /** Return true iff FROM - TO is an unblocked rook move on the current
     *  board.  For this to be true, FROM-TO must be a rook move and the
     *  squares along it, other than FROM, must be empty. */
    boolean isUnblockedMove(Square from, Square to) {
        assert from.isRookMove(to) : "CANNOT MAKE THIS MOVE, NOT A ROOK MOVE";
        int i = from.index();
        int dir = from.direction(to);
        for (Square s : ROOK_SQUARES[i][dir]) {
            int ind = s.index();
            Piece p = this.get(s.col(), s.row());
            if (s.equals(to)) {
                if (this.allPieces.get(ind) != EMPTY) {
                    return false;
                }
                break;
            }
            if (!p.equals(EMPTY)) {
                return false;
            }
        }

        return true; // FIXME
    }

    /** Return true iff FROM is a valid starting square for a move. */
    boolean isLegal(Square from) {
        return get(from).side() == _turn;
    }

    /** Return true iff FROM-TO is a valid move. */
    boolean isLegal(Square from, Square to) {
        if (!isUnblockedMove(from, to)) {
            return false;
        } if (!isLegal(from)) {
            return false;
        } if (!from.isRookMove(to)) {
            return false;
        } if (this.allPieces.get(from.index()) != KING && to.equals(THRONE)) {
            return false;
        }
        /* if (moves.size() >= movelimit()) {
            return false;
        }*/
        return true; // FIXME
    }

    /** Return true iff MOVE is a legal move in the current
     *  position. */
    boolean isLegal(Move move) {
        return isLegal(move.from(), move.to());
    }

    /** Move FROM-TO, assuming this is a legal move. */
    void makeMove(Square from, Square to) {
        assert isLegal(from, to);
        Piece p = this.allPieces.get(from.index());
        this.allPieces.put(from.index(), EMPTY);
        this.allPieces.put(to.index(), p);
        if (_turn == WHITE) {
            _turn = BLACK;
        } else {
            _turn = WHITE;
        }
        moves.add(Move.mv(from, to));
        recordpositions();
        // FIXME
    }

    /** Move according to MOVE, assuming it is a legal move. */
    void makeMove(Move move) {
        makeMove(move.from(), move.to());
    }

    /** Capture the piece between SQ0 and SQ2, assuming a piece just moved to
     *  SQ0 and the necessary conditions are satisfied. */
    private void capture(Square sq0, Square sq2) {
        if (iscapturable(sq0, sq2)) {

        }
        // FIXME
    }

    /*
    Hostile when
    - contains an enemy piece
    - if it is the throne square and empty
    - Hostile to white when 3/4 squares surrounding is black
    - King is captured when like other pieces except when on throne(s)
        - must be surrounded all four places by black pieces
     */
    boolean iscapturable(Square sq0, Square sq1) {
        int sq0_index = sq0.index();
        int sq1_index = sq1.index();
        Piece p0 = this.allPieces.get(sq0_index);
        Piece p1 = this.allPieces.get(sq1_index);
        if (p0.equals(p1)) {
            return true;
        } if (sq1.equals(THRONE) && p1.equals(EMPTY)){
            return true;
        }
        return false;
    }



    /** Undo one move.  Has no effect on the initial board. */
    void undo() {
        if (_moveCount > 0) {
            undoPosition();
            // FIXME
        }
    }

    /** Remove record of current position in the set of positions encountered,
     *  unless it is a repeated position or we are at the first move. */
    private void undoPosition() {
        // FIXME
        int lastmove = moves.size();
        if (lastmove == 0) {
            return;
        }
        Move m = moves.get(lastmove - 1);
        moves.remove(lastmove - 1);
        if (positions.contains(this.allPieces)) {
            _repeated = true;
            moves.add(m);
        }
        _repeated = false;
    }

    /** Clear the undo stack and board-position counts. Does not modify the
     *  current position or win status. */
    //starting a new game from the current position
    void clearUndo() {
        moves.clear();
        // FIXME
    }

    /** Return a new mutable list of all legal moves on the current board for
     *  SIDE (ignoring whose turn it is at the moment). */
    List<Move> legalMoves(Piece side) {
        HashSet s = pieceLocations(side);
        List<Move> _legalmoves = new ArrayList<>();
        for (Object sq : s) {
            Square a = (Square) sq;
            int i = a.index();
            for (int dir = 0; dir <=3; dir++) {
                for (Square b : ((Square) sq).ROOK_SQUARES[i][dir]) {
                    Move m = new Move(a, b);
                    if (isUnblockedMove(a,b)) {
                        _legalmoves.add(m);
                    }
                }
            }

        }
        return _legalmoves;  // FIXME
    }

    /** Return true iff SIDE has a legal move. */
    boolean hasMove(Piece side) {
        List legal = legalMoves(side);
        if (legal.size() > 0) {
            return true;
        }
        return false; // FIXME
    }

    @Override
    public String toString() {
        return toString(true);
    }

    /** Return a text representation of this Board.  If COORDINATES, then row
     *  and column designations are included along the left and bottom sides.
     */
    String toString(boolean coordinates) {
        Formatter out = new Formatter();
        for (int r = SIZE - 1; r >= 0; r -= 1) {
            if (coordinates) {
                out.format("%2d", r + 1);
            } else {
                out.format("  ");
            }
            for (int c = 0; c < SIZE; c += 1) {
                out.format(" %s", get(c, r));
            }
            out.format("%n");
        }
        if (coordinates) {
            out.format("  ");
            for (char c = 'a'; c <= 'i'; c += 1) {
                out.format(" %c", c);
            }
            out.format("%n");
        }
        return out.toString();
    }

    //reset to private after done testing
    /** Return the locations of all pieces on SIDE. */
    private HashSet<Square> pieceLocations(Piece side) {
        assert side != EMPTY;
        assert side == BLACK || side == WHITE || side == KING: "There is no side with this color";
        HashSet<Square> locs = new HashSet<>();
        Iterator it = this.allPieces.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry Pair = (Map.Entry) it.next();
            if (Pair.getValue() == side) {
                Square s = Square.sq((int) Pair.getKey());
                locs.add(s);
            }
        }
        if (side == WHITE) {
            locs.add(this.kingPosition());
        }
        return locs; // FIXME
    }

    private void recordpositions() {
        if (positions.contains(this.allPieces)){
            _repeated = false;
            return;
        }
        positions.add(this.allPieces);
    }

    /** Return the contents of _board in the order of SQUARE_LIST as a sequence
     *  of characters: the toString values of the current turn and Pieces. */
    String encodedBoard() {
        char[] result = new char[Square.SQUARE_LIST.size() + 1];
        result[0] = turn().toString().charAt(0);
        for (Square sq : SQUARE_LIST) {
            result[sq.index() + 1] = get(sq).toString().charAt(0);
        }
        return new String(result);
    }

    /** Piece whose turn it is (WHITE or BLACK). */
    //private
    public Piece _turn;

    /** Cached value of winner on this board, or EMPTY if it has not been
     *  computed. */
    private Piece _winner = null;

    /** Number of (still undone) moves since initial position. */

    /** True when current board is a repeated position (ending the game). */
    private boolean _repeated;

    List<HashMap> positions = new ArrayList<>();

    private int _moveLimit;

    int movelimit() {
        return _moveLimit;
    }



    List<Move> moves = new ArrayList<>();
    private int _moveCount = moves.size();




    // FIXME: Other state?

}
