package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotor that has no ratchet and does not advance.
 *  @ Britney Pellouchoud
 */
class FixedRotor extends Rotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is given by PERM. */
    FixedRotor(String name, Permutation perm) {
        super(name, perm);
    }

    @Override
    void set(int posn) {
        super.set(posn);
    }

    @Override
    Permutation permutation() {
        return super.permutation();
    }

    @Override
    int size() {
        return super.size();
    }

    // FIXME ?
}
