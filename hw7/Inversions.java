import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/** HW #7, Count inversions.
 *  @author
 */
public class Inversions {

    /** A main program for testing purposes.  Prints the number of inversions
     *  in the sequence ARGS. */
    public static void main(String[] args) {
        System.out.println(inversions(Arrays.asList(new String[]{"1", "4", "3", "8", "2"})));
    }

    /** Return the number of inversions of T objects in ARGS. */
    public static <T extends Comparable<? super T>> int inversions(List<String> args) {
        String[] arg = new String[args.size()];
        for(int i = 0; i < args.size(); i++) {
            arg[i] = args.get(i);
        }
        int counter = 0;
        for (int i = 0; i < args.size(); i++) {
            for (int j = i + 1; j < args.size(); j++) {
                if(Integer.valueOf(arg[i]) > Integer.valueOf(arg[j])) {
                    swap(arg, i, j);
                    counter += 1;
                }
            }
        }
        return counter;
    }

    public static void swap(String[] array, int index1, int index2) {
        String temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
        System.out.println(Arrays.toString(array));

    }


}
