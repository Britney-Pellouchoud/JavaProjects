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

    protected String notchstr;
    protected boolean rotated;
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
        return true;
    }

    @Override
    boolean atNotch() {
        if (notchstr == "") {
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
