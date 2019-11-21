/**
 *  @author Josh Hug
 */

public class MazeCycles extends MazeExplorer {
    /* Inherits protected fields:
    protected int[] distTo;
    protected int[] edgeTo;
    protected boolean[] marked;
    */
    private int cyc = -1;
    private Maze maze;
    private boolean[] nextmove;

    /** Set up to find cycles of M. */
    public MazeCycles(Maze m) {
        super(m);
        nextmove = new boolean[m.V()];
        for (boolean tf : marked) {
            tf = false;
        }
        maze = m;
    }

    @Override
    public void solve() {
        announce();
        helper(0);
        for (int i = 0; i < maze.V(); i++) {
            marked[i] = false;
        } if (cyc == -1) {
            System.out.println("Cycle hit here");
            return;
        } for (int index = edgeTo[cyc]; index != cyc; index = edgeTo[index]) {
            marked[index] = true;
        } marked[cyc] = true;
        announce();

        // TODO: Your code here!
    }

    private void helper(int x) {
        nextmove[x] = true;
        marked[x] = true;
        for (int i : maze.adj(x)) {
            if (nextmove[i] && (edgeTo[x] != i)) {
                edgeTo[i] = x;
                cyc = i;
                return;
            } if (marked[i] == false) {
                edgeTo[i] = x;
                helper(i);
            }
        }
        nextmove[x] = false;
    }

    // Helper methods go here
}

