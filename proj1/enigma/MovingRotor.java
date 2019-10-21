package enigma;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Britney Pellouchoud
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */

    /** String to hold all notches
     * Initialized in Moving rotor.
     */
    protected String notchstr;

    /**Boolean to know whether or not the rotor has rotated.
     *
     */
    protected boolean rotated;

    /**Constructor for moving rotors, overrides Rotor.
     *
     * @param name **String**
     * @param perm **Permutation**
     * @param notches **String**
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        notchstr = notches;
        rotated = false;
        moving = true;
    }



    @Override
    void advance() {
        this._setting += 1;
    }

    @Override
    void set(int v) {
        _setting = v;
    }

    @Override
    boolean rotates() {
        if (this.atNotch()) {
            return true;
        }
        return false;
    }

    @Override
    boolean atNotch() {
        if (notchstr.equals("")) {
            return true;
        }
        for (int i = 0; i < notchstr.length(); i++) {
            if (alphabet().toChar(_setting) == notchstr.charAt(i)) {
                return true;
            }
        }
        return false;
    }


}
