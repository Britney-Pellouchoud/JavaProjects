import java.util.AbstractQueue;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
   //  Inherits visible fields:
   /* protected int[] distTo;
    protected int[] edgeTo;
    protected boolean[] marked;

    */


    private int s;
    private int t;
    private boolean targetfound = false;
    private Maze maze;
    /** A breadth-first search of paths in M from (SOURCEX, SOURCEY) to
     *  (TARGETX, TARGETY). */
    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY,
                                 int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;



        // Add more variables here!
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        Queue<Integer> queue = new PriorityQueue<Integer>();
        for (int i = 0; i < maze.V(); i++) {
            distTo[i] = Integer.MAX_VALUE; distTo[s] = 0;
            marked[s] = true;
            queue.add(s);
            announce();
            while (!(queue.size() == 0)) {
                int j = queue.remove(); //check if this pops
                if (j == t) {
                    System.out.println("End find.");
                    return;
                }
                announce();
                for (int k : maze.adj(j)) {
                    if (!marked[k]) {
                        edgeTo[k] = j;
                        announce();
                        distTo[k] = distTo[j] + 1;
                        marked[k] = true;
                        queue.add(k);
                    }
                }
            }
        }

        // TODO: Your code here. Don't forget to update distTo, edgeTo,
        // and marked, as well as call announce()

    }


    @Override
    public void solve() {
        bfs();
    }
}

