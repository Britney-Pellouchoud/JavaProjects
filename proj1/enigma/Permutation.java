package enigma;

import java.util.ArrayList;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @ Britney Pellouchoud
 */
class Permutation {


    private String _cycles;

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
        // FIXME
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        this._cycles = this._cycles + cycle;
        // FIXME
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size(); // FIXME
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        return p % _alphabet.size();  // FIXME
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        return permute(c);  // FIXME
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        int index = _cycles.indexOf(p);
        if (_cycles.charAt(index + 1) != ')' && index != -1) {
            return _cycles.charAt(index + 1);
        } else if (_cycles.charAt(index + 1) == ')' && index != -1){
            char perm = _cycles.charAt(index);
            for (int i = index; _cycles.charAt(i) != '('; i--) {
                perm = _cycles.charAt(i);
            }
            return perm;
        }
        return p;
        // FIXME
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        int index = _cycles.indexOf(c);
        if (index != -1 && _cycles.charAt(index - 1) != '(') {
            return _cycles.charAt(index - 1);
        } else if (index != -1 && _cycles.charAt(index - 1) == '(') {
            char perm = _cycles.charAt(index);
            for (int i = index; _cycles.charAt(i) != ')'; i++) {
                perm = _cycles.charAt(i);
            }
            return perm;
        }
        return c;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < _alphabet.size(); i++) {
            if (permute(_alphabet.toChar(i)) == _alphabet.toChar(i)) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    // FIXME: ADDITIONAL FIELDS HERE, AS NEEDED
}
