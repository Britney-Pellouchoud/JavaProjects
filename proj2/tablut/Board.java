package tablut;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static tablut.Piece.*;
import static tablut.Square.*;


/** The state of a Tablut Game.
 *  @author BritneyPellouchoud
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

    /** Stores all the pieces in a hashmap indexed by the Square index.
     *
     */
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


    /**
     * Hashmap returns all pieces.
     * @return a Hashmap.
     */
    HashMap<Integer, Piece> getallPieces() {
        return this.allPieces;
    }

    /** Copies MODEL into me. */

    void copy(Board model) {
        if (model == this) {
            return;
        }
        init();


        this.allPieces = deepcopy(model);

    }
    /**
     * Deepcopy.
     * @param model board.
     * @return Hashmap.
     */
    HashMap<Integer, Piece> deepcopy(Board model) {
        HashMap<Integer, Piece> copy = new HashMap<>();
        copy.putAll(model.getallPieces());
        return copy;
    }

    /** Clears the board to the initial position. */
    void init() {

        _winner = null;
        prevmoves.clear();
        positions.clear();
        _repeated = false;
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
        for (int i = 0; i <= l; i++) {
            if (this.allPieces.get(i) == null) {
                this.allPieces.put(i, EMPTY);
            }
        }
        positions.add(this.encodedBoard());
    }

    /**
     * l.
     */
    private final int l = 80;




    /** @param n is an integer
     *Set the move limit to LIM.  It is an error if 2*LIM <= moveCount(). */
    void setMoveLimit(int n) {
        _moveLimit = n;
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
        if (positions.contains(this.encodedBoard())) {
            _repeated = true;
            if (_turn == WHITE) {

                _winner = BLACK;
            } else {

                _winner = WHITE;
            }
            return;
        }
        positions.add(this.encodedBoard());
    }

    /** Return the number of moves since the initial position that have not been
     *  undone. */
    int moveCount() {
        return prevmoves.size();
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
        if (ind == -1) {
            _winner = BLACK;
        }
        return Square.sq(ind);
    }

    /** Return the contents the square at S. */
    final Piece get(Square s) {
        return get(s.col(), s.row());
    }

    /** Return the contents of the square at (COL, ROW), where
     *  0 <= COL, ROW <= 9. */
    final Piece get(int col, int row) {
        assert col >= 0 && col < 9
                && row >= 0 && row < 9 : "OUT OF BOUNDS FOR THIS BOARD";
        Square s = sq(col, row);
        int ind = s.index();
        return this.allPieces.get(ind);
    }

    /** Return the contents of the square at COL ROW. */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /** Set square S to P. */
    final void put(Piece p, Square s) {
        int i = s.index();
        this.allPieces.put(i, p);
    }

    /** Set square S to P and record for undoing. */
    final void revPut(Piece p, Square s) {
        int i = s.index();
        this.allPieces.put(i, p);
        ArrayList<Object> move  = new ArrayList<>();
        move.add(s);
        move.add(p);
        prevmoves.add(move);
    }

    /** Set square COL ROW to P. */
    final void put(Piece p, char col, char row) {
        put(p, sq(col - 'a', row - '1'));
    }

    /** Return true iff FROM - TO is an unblocked rook move on the current
     *  board.  For this to be
     *  true, FROM-TO must be a rook move and the
     *  squares along it, other than FROM, must be empty. */
    boolean isUnblockedMove(Square from, Square to) {
        assert from.isRookMove(to) : "NOT A ROOK MOVE "
                + from.toString() + to.toString();
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

        return true;
    }

    /** Return true iff FROM is a valid starting square for a move. */
    boolean isLegal(Square from) {
        return get(from).side() == _turn;
    }

    /** Return true iff FROM-TO is a valid move. */
    boolean isLegal(Square from, Square to) {
        if (!isUnblockedMove(from, to)) {
            return false;
        }
        if (!isLegal(from)) {
            return false;
        }
        if (!from.isRookMove(to)) {
            return false;
        }
        if (this.allPieces.get(from.index()) != KING && to.equals(THRONE)) {
            return false;
        }
        return true;
    }

    /** Return true iff MOVE is a legal move in the current
     *  position. */
    boolean isLegal(Move move) {
        return isLegal(move.from(), move.to());
    }

    /**
     * Captured piece.
     */
    private Stack<Piece> capturedpieces = new Stack<Piece>();
    /**
     * Numcaptured.
     */
    private List<Integer> numcaptured = new ArrayList<Integer>();
    /**
     * Placecaptured.
     */
    private List<Square> placecaptured = new ArrayList<Square>();

    /** Move FROM-TO, assuming this is a legal move. */
    void makeMove(Square from, Square to) {
        if (!isLegal(from, to)) {
            return;
        }
        Piece p = this.allPieces.get(from.index());
        revPut(EMPTY, from);
        revPut(p, to);
        int row = to.row();
        int col = to.col();
        String section = whichsection(to);
        int capt = 0;
        ArrayList<Integer> sides = new ArrayList<Integer>();
        sides.add(0);
        sides.add(8);
        boolean help = (sides.contains(to.row()) || sides.contains(to.col()));
        if (help && p.equals(KING)) {
            _winner = WHITE;
        }
        if (section.contains("L")) {
            Square left = Square.sq(col - 2, row);
            if (capture(to, left)) {
                capt += 1;
                placecaptured.add(to.between(left));
            }
        }
        if (section.contains("R")) {
            Square right = Square.sq(col + 2, row);

            if (capture(to, right)) {
                capt += 1;
                placecaptured.add(to.between(right));
            }
        }
        if (section.contains("U")) {
            Square up = Square.sq(col,  row + 2);

            if (capture(to, up)) {
                capt += 1;
                placecaptured.add(to.between(up));
            }
        }
        if (section.contains("D")) {
            Square down = Square.sq(col, row - 2);

            if (capture(to, down)) {
                capt += 1;
                placecaptured.add(to.between(down));
            }
        }
        numcaptured.add(capt);
        if (_turn == WHITE) {
            _turn = BLACK;
        } else {
            _turn = WHITE;
        }
        checkRepeated();
        positions.add(this.encodedBoard());

    }


    /**
     * Which section.
     * @param to Square.
     * @return String.
     */
    String whichsection(Square to) {
        int row = to.row();
        int col = to.col();
        if (row <= 1) {
            if (col <= 1) {
                return "RU";
            }
            if (col >= 2 && col <= 6) {
                return "LRU";
            }
            if (col >= 7) {
                return "LU";
            }
        }
        if (row >= 2 && row <= 6) {
            if (col <= 1) {
                return "RUD";
            }
            if (col >= 2 && col <= 6) {
                return "LRUD";
            }
            if (col >= 7) {
                return "LUD";
            }
        } else {
            if (col <= 1) {
                return "RD";
            }
            if (col >= 2 && col <= 6) {
                return "LRD";
            }
            if (col >= 7) {
                return "LD";
            }
        }
        return null;
    }

    /** Move according to MOVE, assuming it is a legal move. */
    void makeMove(Move move) {
        assert move != null : legalMoves(turn());
        makeMove(move.from(), move.to());
    }

    /** Capture the piece between SQ0 and SQ2, assuming a piece just moved to.
     * @param sq0 Square.
     * @param sq2 Square.
     * @return boolean.
     *  SQ0 and the necessary conditions are satisfied. */
    private boolean capture(Square sq0, Square sq2) {
        Square sq1 = sq0.between(sq2);
        Piece between = this.allPieces.get(sq1.index());
        ArrayList<Square> thrones = new ArrayList<Square>();
        thrones.add(Square.sq(4, 3));
        thrones.add(Square.sq(4, 4));
        thrones.add(Square.sq(4, 5));
        thrones.add(Square.sq(5, 4));
        thrones.add(Square.sq(3, 4));

        if (thrones.contains(sq1) && between == KING) {
            if (kingcapture(sq1)) {
                _winner = BLACK;
                this.allPieces.put(sq1.index(), EMPTY);
                capturedpieces.add(KING);
                return true;
            }
            return false;

        }
        if (!iscapturable(sq0, sq2)) {
            return false;
        } else {
            this.allPieces.put(sq1.index(), EMPTY);
            capturedpieces.add(between);
            if (between.equals(KING)) {
                _winner = BLACK;
            }
            return true;
        }
    }

    /**
     * If the king is captured.
     * @param throne Square.
     * @return boolean.
     */
    boolean kingcapture(Square throne) {
        int row = throne.row();
        int col = throne.col();
        Square left = Square.sq(col - 1, row);
        Square right = Square.sq(col + 1, row);
        Square up = Square.sq(col,  row + 1);
        Square down = Square.sq(col, row - 1);
        ArrayList<Boolean> hostility = new ArrayList<Boolean>();
        hostility.add(kinghostile(left));
        hostility.add(kinghostile(right));
        hostility.add(kinghostile(up));
        hostility.add(kinghostile(down));

        if (!hostility.contains(false)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if king is hostile.
     * @param nextto Square.
     * @return boolean.
     */
    boolean kinghostile(Square nextto) {
        if (this.allPieces.get(nextto.index()) == BLACK) {
            return true;
        }
        if (nextto.row() == THRONE.row() && nextto.col() == THRONE.col()) {
            return true;
        }
        return false;
    }

    /**
     * Describes all times when a piece is capturable.
     * @param sq0 Square
     * @param sq2 Square
     * @return boolean.
     */
    boolean iscapturable(Square sq0, Square sq2) {

        Piece hostile = EMPTY;
        Square between = sq0.between(sq2);
        Piece bet = this.allPieces.get(between.index());
        if (bet == BLACK) {
            hostile = WHITE;
        }
        if (bet == WHITE || bet == KING) {
            hostile = BLACK;
        }
        if (bet == EMPTY) {
            return false;
        }
        int row = between.row();
        int col = between.col();
        if (ishostile(sq0, hostile) && ishostile(sq2, hostile)) {
            return true;
        }
        return false;


    }


    /**Checks if hostile.
     *
     * @param s Square.
     * @param hostile Piece.
     * @return boolean.
     */
    boolean ishostile(Square s, Piece hostile) {
        ArrayList<Square> thronelist = new ArrayList<Square>();
        thronelist.add(NTHRONE);
        thronelist.add(WTHRONE);
        thronelist.add(ETHRONE);
        thronelist.add(STHRONE);

        if (hostile == WHITE) {
            Piece a = this.allPieces.get(s.index());
            if (a == KING || a == WHITE) {
                return true;
            }
        }
        if (this.allPieces.get(s.index()) == hostile) {
            return true;
        } else if (s.equals(THRONE)) {
            if (this.allPieces.get(s.index()) == EMPTY) {
                return true;
            } else {
                if (hostile == BLACK) {
                    int count = 0;
                    for (Square t : thronelist) {
                        if (this.allPieces.get(t.index()) == BLACK) {
                            count += 1;
                        }
                    }
                    if (count >= 3) {
                        return true;
                    }
                }
                return false;

            }
        }
        return false;
    }



    /** Undo one move.  Has no effect on the initial board. */
    void undo() {
        this.undoPosition();
    }




    /** Remove record of current position in the set of positions encountered,
     *  unless it is a repeated position or we are at the first move. */

    private void undoPosition() {
        if (prevmoves.size() <= 2) {
            return;
        }
        _winner = null;
        positions.remove(positions.size() - 1);
        ArrayList k = prevmoves.pop();
        Square s1 = (Square) k.get(0);
        Piece p1 = (Piece) k.get(1);
        ArrayList j = prevmoves.pop();
        Square s2 = (Square) j.get(0);
        Piece opp = null;
        if (p1 == KING || p1 == WHITE) {
            opp = WHITE;
        }
        if (p1 == BLACK) {
            opp = BLACK;
        }
        this._turn = opp;
        Piece p2 = (Piece) j.get(1);
        this.allPieces.put(s2.index(), p1);
        this.allPieces.put(s1.index(), p2);
        int numreplace = numcaptured.remove(numcaptured.size() - 1);
        while (numreplace > 0) {
            Piece replace = capturedpieces.pop();
            Square putter = placecaptured.remove(placecaptured.size() - 1);
            put(replace, putter);
            numreplace -= 1;
        }

        _repeated = false;
    }

    /** Clear the undo stack and board-position counts. Does not modify the
     *  current position or win status. */
    void clearUndo() {
        prevmoves.clear();
        positions.clear();
    }

    /** Return a new mutable list of all legal moves on the current board for
     *  SIDE (ignoring whose turn it is at the moment). */
    List<Move> legalMoves(Piece side) {
        HashSet s = pieceLocations(side);
        List<Move> legalmoves = new ArrayList<>();
        for (Object sq : s) {
            Square a = (Square) sq;
            int i = a.index();
            for (int dir = 0; dir <= 3; dir++) {
                for (Square b : ((Square) sq).ROOK_SQUARES[i][dir]) {
                    Move m = new Move(a, b);
                    if (isUnblockedMove(a, b) && a.isRookMove(b)) {
                        legalmoves.add(m);
                    }
                }
            }
        }
        return legalmoves;
    }

    /** Return true iff SIDE has a legal move. */
    boolean hasMove(Piece side) {
        List legal = legalMoves(side);
        if (legal.size() > 0) {
            return true;
        }
        return false;
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

    /** Return the locations of all pieces on SIDE. */
    protected HashSet<Square> pieceLocations(Piece side) {
        assert side != EMPTY;
        assert side == BLACK
                || side == WHITE
                || side == KING : "There is no side with this color";
        HashSet<Square> locs = new HashSet<>();
        Iterator it = this.allPieces.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (pair.getValue() == side) {
                Square s = Square.sq((int) pair.getKey());
                locs.add(s);
            }
        }
        if (side == WHITE) {
            locs.add(this.kingPosition());
        }
        return locs;
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
    private Piece _turn;

    /** Cached value of winner on this board, or EMPTY if it has not been
     *  computed. */
    private Piece _winner = null;

    /** Number of (still undone) moves since initial position. */

    /** True when current board is a repeated position (ending the game). */
    private boolean _repeated;

    /**
     * Accessor for positions.
     * @return a list of hashmaps.
     */
    protected Stack<ArrayList> prevmoves = new Stack<ArrayList>();

    /**.
     * a list of board positions
     */
    protected ArrayList<String> positions = new ArrayList<String>();


    /**
     * Positions.
     */

    /**
     * Private integer movelimit.
     */
    private int _moveLimit;

    /**
     * The movelimit.
     * @return an integer.
     */
    int movelimit() {
        return _moveLimit;
    }


    /**
     * Accessor for moves.
     * @return a list of moves.
     */

    /**
     * A list of all the completed moves.
     */

    /**
     * Counts the number of moves.
     */





}
