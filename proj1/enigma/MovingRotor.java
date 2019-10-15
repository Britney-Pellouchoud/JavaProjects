package enigma;

/** Class that represents a rotating rotor in the enigma machine.
 *  @ Britney Pellouchoud
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */

    String _notches;
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        String _notches = notches;
        _setting = 0;
        // FIXME
    }

    // FIXME?

    @Override
    void advance() {
        if (rotates()) {
            this._setting += 1;
        }
        //this._setting += 1;
        // FIXME
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
        if (_notches == null) {
            return true;
        }
        for (int i = 0; i < _notches.length(); i++) {
            if (alphabet().toChar(_setting) == _notches.charAt(i)) {
                return true;
            }
        }
        return false;
    }

    // FIXME: ADDITIONAL FIELDS HERE, AS NEEDED

}
