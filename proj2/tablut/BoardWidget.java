package tablut;

import ucb.gui2.Pad;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import java.awt.event.MouseEvent;

import static java.awt.Color.*;
import static java.awt.Font.ITALIC;
import static tablut.Move.isGrammaticalMove;
import static tablut.Piece.*;
import static tablut.Move.mv;
import static tablut.Square.*;

/** A widget that displays a Tablut game.
 *  @author
 */
class BoardWidget extends Pad {

    /* Parameters controlling sizes, speeds, colors, and fonts. */

    /** Squares on each side of the board. */
    static final int SIZE = Board.SIZE;

    /** Colors of empty squares, pieces, grid lines, and boundaries. */
    static final Color
        SQUARE_COLOR = new Color(238, 190, 211),
        THRONE_COLOR = new Color(144, 215, 255),
        ADJACENT_THRONE_COLOR = new Color(220, 136, 189),
        CLICKED_SQUARE_COLOR = new Color(255, 158, 60),
        GRID_LINE_COLOR = black,
        WHITE_COLOR = Color.white,
        BLACK_COLOR = black;

    /** Margins. */
    static final int
        OFFSET = 2,
        MARGIN = 16;

    /** Side of single square and of board (in pixels). */
    static final int
        SQUARE_SIDE = 30,
        BOARD_SIDE = SQUARE_SIDE * SIZE + 2 * OFFSET + MARGIN;

    /** The font in which to render the "K" in the king. */
    static final Font KING_FONT = new Font("Serif", Font.BOLD, 18);
    /** The font for labeling rows and columns. */
    static final Font ROW_COL_FONT = new Font("SanSerif", Font.PLAIN, 10);

    /** Squares adjacent to the throne. */
    static final Square[] ADJACENT_THRONE = {
        Board.NTHRONE, Board.ETHRONE, Board.STHRONE, Board.WTHRONE
    };

    /** A graphical representation of a Tablut board that sends commands
     *  derived from mouse clicks to COMMANDS.  */
    BoardWidget(ArrayBlockingQueue<String> commands) {
        _commands = commands;
        setMouseHandler("click", this::mouseClicked);
        setPreferredSize(BOARD_SIDE, BOARD_SIDE);
        _acceptingMoves = false;
    }

    /** Draw the bare board G.  */
    private void drawGrid(Graphics2D g) {
        g.setColor(SQUARE_COLOR);
        g.fillRect(0, 0, BOARD_SIDE, BOARD_SIDE);
        g.setColor(THRONE_COLOR);
        g.fillRect(cx(Board.THRONE), cy(Board.THRONE),
                   SQUARE_SIDE, SQUARE_SIDE);
        g.setColor(yellow);
        g.fillRect(cx(Board.WTHRONE), cy(Board.WTHRONE), SQUARE_SIDE, SQUARE_SIDE);
        g.fillRect(cx(Board.STHRONE), cy(Board.STHRONE), SQUARE_SIDE, SQUARE_SIDE);
        g.fillRect(cx(Board.NTHRONE), cy(Board.NTHRONE), SQUARE_SIDE, SQUARE_SIDE);
        g.fillRect(cx(Board.ETHRONE), cy(Board.ETHRONE), SQUARE_SIDE, SQUARE_SIDE);

        // OTHER SQUARE COLORINGS?
        g.setColor(GRID_LINE_COLOR);
        for (int k = 0; k <= SIZE; k += 1) {
            g.drawLine(cx(0), cy(k - 1), cx(SIZE), cy(k - 1));
            g.drawLine(cx(k), cy(-1), cx(k), cy(SIZE - 1));
        }
        String alph = "abcdefghi";
        g.setColor(green);
        g.setFont(KING_FONT);
        for (int i = 0; i < alph.length(); i++) {
            Square s = SQUARE_LIST.get(i);
            char letter = alph.charAt(i);
            g.drawString(String.valueOf(letter), cx(s) + 10, cy(s) + 45);

        }

        ArrayList<Square> vert = new ArrayList<Square>();
        for (int i = 0; i <= 80; i += 9) {
            Square a = SQUARE_LIST.get(i);
            vert.add(a);
        }

        g.setColor(cyan);
        String nums = "123456789";
        for (int i = 0; i < nums.length(); i++) {
            Square p = vert.get(i);
            char num = nums.charAt(i);
            g.drawString(String.valueOf(num), cx(p) - 15, cy(p) + 25);
        }

        // OTHER STUFF.
    }

    @Override
    public synchronized void paintComponent(Graphics2D g) {
        drawGrid(g);
        Square.SQUARE_LIST.iterator().forEachRemaining(s -> drawPiece(g, s));
    }

    /** Draw the contents of S on G. */
    private void drawPiece(Graphics2D g, Square s) {
        Piece p = _board.getallPieces().get(s.index());
        int x = s.col();
        int y = s.row();
        if (p.equals(Piece.BLACK)) {
            g.setColor(black);
            g.drawOval(cx(s), cy(s), 25, 25);
            g.fillOval(cx(s), cy(s), 25, 25);
        } if (p.equals(Piece.WHITE)) {
            g.setColor(white);
            g.drawOval(cx(s), cy(s), 25, 25);
            g.fillOval(cx(s), cy(s), 25, 25);
        } if (p.equals(KING)) {
            g.setColor(white);
            g.drawOval(cx(s), cy(s),25, 25);
            g.fillOval(cx(s), cy(s), 25, 25);
            g.setFont(KING_FONT);
            g.setColor(blue);
            g.drawString("K", cx(s) + 5, cx(y) + 5);
        } if (p.equals(EMPTY)) {
            return;
        }

        // FIXME
    }

    /** Handle a click on S. */
    private void click(Square s) {
        clicked.add(s);
        if (clicked.size() ==  2) {
            Move m = new Move(clicked.get(0), clicked.get(1));
            _commands.offer(m.toString());
            clicked.clear();
        }
        // FIXME
        repaint();
    }

    private ArrayList<Square> clicked = new ArrayList<Square>();

    /** Handle mouse click event E. */
    private synchronized void mouseClicked(String unused, MouseEvent e) {
        int xpos = e.getX(), ypos = e.getY();
        int x = (xpos - OFFSET - MARGIN) / SQUARE_SIDE,
            y = (OFFSET - ypos) / SQUARE_SIDE + SIZE - 1;
        if (_acceptingMoves
            && x >= 0 && x < SIZE && y >= 0 && y < SIZE) {
            click(sq(x, y));
        }
    }

    /** Revise the displayed board according to BOARD. */
    synchronized void update(Board board) {
        _board.copy(board);
        repaint();
    }

    /** Turn on move collection iff COLLECTING, and clear any current
     *  partial selection.  When move collection is off, ignore clicks on
     *  the board. */
    void setMoveCollection(boolean collecting) {
        _acceptingMoves = collecting;
        repaint();
    }

    /** Return x-pixel coordinate of the left corners of column X
     *  relative to the upper-left corner of the board. */
    private int cx(int x) {
        return x * SQUARE_SIDE + OFFSET + MARGIN;
    }

    /** Return y-pixel coordinate of the upper corners of row Y
     *  relative to the upper-left corner of the board. */
    private int cy(int y) {
        return (SIZE - y - 1) * SQUARE_SIDE + OFFSET;
    }

    /** Return x-pixel coordinate of the left corner of S
     *  relative to the upper-left corner of the board. */
    private int cx(Square s) {
        return cx(s.col());
    }

    /** Return y-pixel coordinate of the upper corner of S
     *  relative to the upper-left corner of the board. */
    private int cy(Square s) {
        return cy(s.row());
    }

    /** Queue on which to post move commands (from mouse clicks). */
    private ArrayBlockingQueue<String> _commands;
    /** Board being displayed. */
    private final Board _board = new Board();

    /** True iff accepting moves from user. */
    private boolean _acceptingMoves;

}
