package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @ Britney Pellouchoud
 */
class Reflector extends FixedRotor {
    private Permutation _setting;

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
        _setting = perm;
        // FIXME
    }

    @Override
    boolean rotates() {
        return false;
    }

    // FIXME?

    @Override
    void set(int posn) {
        if (posn != 0) {
            throw error("reflector has only one position");
        }
    }

    @Override
    boolean reflecting() {
        return true;
    }

}
