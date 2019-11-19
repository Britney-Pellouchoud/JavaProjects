import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Distribution Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
        int len = array.length;
        for (int i = 1; i < len; i++){
            int spot = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > spot) {
                array[j + 1] = array[j];
                j = j - 1;
            }
            array[j + 1] = spot;
        }
    }


        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            if (array.length == 1 || array.length == 0) {
                return;
            }
            int minind;
            int j;
            for (int i = 0; i < k - 1; i++) {
                minind = i;
                for (j = i+1; j< k; j++) {
                    if (array[j] < array[minind]) {
                        minind = j;
                        swap(array, minind, i);
                    }
                }
            }
        }

        public void swap(int[] list, int orig, int n) {
            int temp = list[orig];
            list[orig] = list[n];
            list[n] = temp;
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }

    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            int[] answer = new int[array.length];
            int smol = Math.min(k - 1, array.length - 1);
            sorter(array, answer, 0, smol);
        }
        public void merge(int[] filler, int[] answer, int small, int midind, int big) {
            for (int i = 0; i <= big; i++) {
                answer[i] = filler[i];
            }
            int j = small;
            int k = midind + 1;
            for (int i = small; i <= big; i++) {
                if (j > midind) {
                    filler[i] = answer[k++];
                }
                else if (k > big) {
                    filler[i] = answer[j++];
                }
                else if (answer[k] < answer[j]){
                    filler[i] = answer[k++];
                }
                else {
                    filler[i] = answer[j++];
                }
            }
        }
        public void sorter(int[] filler, int[] answer, int small, int big) {
            if (big <= small) {
                return;
            }
            int midind = small + (big - small) / 2;
            sorter(filler, answer, small, midind);
            sorter(filler, answer, midind + 1, big);
            merge(filler, answer, small, midind, big);
        }


        // may want to add additional methods

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Distribution Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class DistributionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME: to be implemented
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Distribution Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            int bitsforbyte = 8;
            int w = 32/bitsforbyte;
            int rshift = 1 << bitsforbyte;
            int mask = rshift - 1;
            int n  = Math.min(a.length, k);

            int [] answer = new int[n];

            for (int ind = 0; ind < w; ind++) {
                int [] count = new int[rshift + 1];
                for (int i = 0; i < n; i++) {
                    int c = (a[i] >> bitsforbyte * ind) & mask;
                    count[c + 1]++;
                }
                for (int r = 0; r < rshift; r++) {
                    count[r + 1] = count[r + 1] + count[r];
                }
                if (ind == w - 1) {
                    int s1 = count[rshift] - count[rshift/2];
                    int s2 = count[rshift/2];
                    for (int x = 0; x < rshift/2; x++) {
                        count[x] += s1;
                    }
                    for (int y = rshift/2; y < rshift; y++) {
                        count[rshift] -= s2;
                    }
                }
                for (int f = 0; f < n; f++) {
                    int c = (a[f] >> bitsforbyte * ind) & mask;
                    answer[count[c]++] = a[f];
                }
                for (int i = 0; i < n; i++) {
                    a[i] = answer[i];
                }
            }
        }

        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
