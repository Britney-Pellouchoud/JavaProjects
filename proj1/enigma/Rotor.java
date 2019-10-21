package enigma;



/** Superclass that represents a rotor in the enigma machine.
 *  @author Britney Pellouchoud
 */
class Rotor {

    /**Notches.
     *
     */
    protected String notches;
    /**Integer _setting.
     *
     */
    protected int _setting;
    /**Boolean moving.
     *
     */
    protected boolean moving;
    /**Boolean rotated.
     *
     */
    protected boolean rotated;
    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _setting = 0;
        rotated = false;

    }

    /**If it has rotated.
     *
     * @return **boolean**
     */
    boolean hasRotated() {

        return rotated;
    }

    /**Where the notches are.
     *
     * @return **string**
     */
    String getNotches() {
        return notches; }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        if (!reflecting() && !fixed()) {
            return true;
        }
        return false;
    }

    /**Returns whether or not the rotor can move.
     *
     * @return **boolean**
     */
    boolean fixed() {
        return false; }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _setting;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        _setting = posn;
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        _setting = _permutation.alphabet().toInt(cposn);
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */

    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
            return wrap(r);
        }
        return r;
    }

    /**Converts letters taking setting into account.
     *
     * @param p **integer**
     * @return **integer**
     */
    int convertForward(int p) {
        int input = (p + setting()) % alphabet().size();
        int answer = (_permutation.permute(input) - setting());
        if (answer < 0) {
            return wrap(answer);
        }
        return answer % alphabet().size();
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int input = (e + setting()) % alphabet().size();
        int answer = (_permutation.invert(input) - setting());
        if (answer < 0) {
            return alphabet().size() + answer;
        }
        return answer % alphabet().size();
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;


}
