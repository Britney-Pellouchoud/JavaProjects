package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @ Britney Pellouchoud
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */

    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);

        name();
        // FIXME
    }

    // FIXME?

    @Override
    void advance() {
        if (permutation().size() > 0) {
            _setting += 1;
        }

        // FIXME
    }

    // FIXME: ADDITIONAL FIELDS HERE, AS NEEDED

}
