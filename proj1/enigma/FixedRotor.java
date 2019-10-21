package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotor that has no ratchet and does not advance.
 *  @author Britney Pellouchoud
 */
class FixedRotor extends Rotor {

    /** Every permutation needs a setting.
     * @param _set **Permutation**
     */
    private Permutation _set;
    /** Every permutation needs a setting.
     * @param setting **Integer**
     */
    private int _setting;
    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is given by PERM. */
    FixedRotor(String name, Permutation perm) {
        super(name, perm);
        _set = perm;
        _setting = 0;

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

    @Override
    boolean fixed() {
        return true; }

    @Override
    boolean rotates() {
        return false;
    }
}
