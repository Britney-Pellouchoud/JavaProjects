package enigma;

import org.junit.Test;


/** Superclass that represents a rotor in the enigma machine.
 *  @ Britney Pellouchoud
 */
class Rotor {

    int _setting;
    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _setting = 0;
        // FIXME
    }

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
    //moving rotors always have a ratchet
    boolean rotates() {
        if (!reflecting() && !fixed()){
            return true;
        }
        return false;
    }

    boolean fixed() {return false;}
    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _setting; // FIXME
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
    int convertForward(int p) {
       // System.out.println(alphabet().size());
        System.out.println("*" + (p - setting()));
        System.out.println((p - setting()) % alphabet().size());
        return _permutation.permute((p - setting()) % alphabet().size());  // FIXME
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        //  System.out.println(setting());
        return _permutation.invert((e + setting()) % alphabet().size());  // FIXME
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
        if (this.rotates()) {
            _setting += 1;
        }
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    // FIXME: ADDITIONAL FIELDS HERE, AS NEEDED


}
