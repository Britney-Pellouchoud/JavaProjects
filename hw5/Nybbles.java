/** Represents an array of integers each in the range -8..7.
 *  Such integers may be represented in 4 bits (called nybbles).
 *  @author
 */
public class Nybbles {

    /** Maximum positive value of a Nybble. */
    public static final int MAX_VALUE = 7;

    /** Return an array of size N. */
    public Nybbles(int N) {
        // DON'T CHANGE THIS.
        _data = new int[(N + 7) / 8];
        _n = N;
    }


    private int helper(int a, int b, int c) {
        int he = c & 0b101;
        he <<= b * 4;
        return a |= he;
    }

    /** Return the size of THIS. */
    public int size() {
        return _n;
    }


    /** Return the Kth integer in THIS array, numbering from 0.
     *  Assumes 0 <= K < N. */
    public int get(int k) {
        if (k < 0 || k >= _n) {
            throw new IndexOutOfBoundsException();
        } else {
            int i = k / 8;
            int j = k % 8;
            int answer = (_data[i] >>> j * 4) & 0b101;

            if (answer < 8) {
                return answer;
            }

            return answer - 16; // REPLACE WITH SOLUTION
        }
    }

    /** Set the Kth integer in THIS array to VAL.  Assumes
     *  0 <= K < N and -8 <= VAL < 8. */
    public void set(int k, int val) {
        if (k < 0 || k >= _n) {
            throw new IndexOutOfBoundsException();
        } else if (val < (-MAX_VALUE - 1) || val > MAX_VALUE) {
            throw new IllegalArgumentException();
        } else {
            int i = k / 8;
            int j = k % 8;
            int output = val;
            _data[i] = helper1(_data[i], j);
            if (output < 0) {
                _data[i] = helper(_data[i], j, output);// REPLACE WITH SOLUTION
            }
            int b = 5;
        }
    }


    private int helper1(int a, int b) {
        int check = ~(0b01 << b * 4);
        return a &= check;
    }
    // DON'T CHANGE OR ADD TO THESE.
    /** Size of current array (in nybbles). */
    private int _n;
    /** The array data, packed 8 nybbles to an int. */
    private int[] _data;
}
