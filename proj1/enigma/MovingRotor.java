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

    private String notchstr;
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        String notchstr = notches;
    }


    @Override
    void advance() {
        if (rotates()) {
            this._setting += 1;
        }
    }

    @Override
    void set(int v) {
        _setting = v;
    }

    @Override
    boolean rotates() {
        if (atNotch()) {
            return true;
        }
        return false;
    }

    @Override
    boolean atNotch() {
        if (notchstr == null) {
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
