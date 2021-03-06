package enigma;
/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author BritneyPellouchoud
 */
class Permutation {


    /**Initializes private cycle for permutations.
     *
     */
    private String _cycles;

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        this._cycles = this._cycles.concat(cycle);
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
            return wrap(r);
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char character = _alphabet.toChar(p % _alphabet.size());
        int index = _cycles.indexOf(character);
        if (index != -1 && _cycles.charAt(index + 1) != ')') {
            char next = _cycles.charAt(index + 1);
            return _alphabet.toInt(next);
        }
        if (index != -1 && _cycles.charAt(index + 1) == ')') {
            char perm = _cycles.charAt(index);
            for (int i = index; _cycles.charAt(i) != '('; i--) {
                perm = _cycles.charAt(i);
            }
            return _alphabet.toInt(perm);
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char character = _alphabet.toChar(c % _alphabet.size());
        int index = _cycles.indexOf(character);
        if (index != -1 && _cycles.charAt(index - 1) != '(') {
            char prev = _cycles.charAt(index - 1);
            return _alphabet.toInt(prev);
        }
        if (index != -1 && _cycles.charAt(index - 1) == '(') {
            char perm = _cycles.charAt(index);
            for (int i = index; _cycles.charAt(i) != ')'; i++) {
                perm = _cycles.charAt(i);
            }
            return _alphabet.toInt(perm);
        }
        return c;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        int i = _cycles.indexOf(p);
        if (i != -1) {
            if (_cycles.length() != 0 && _cycles.charAt(i + 1) != ')') {
                return _cycles.charAt(i + 1);
            } else if (_cycles.length() != 0  && _cycles.charAt(i + 1) == ')') {
                char perm = _cycles.charAt(i);
                for (int j = i; _cycles.charAt(j) != '('; j--) {
                    perm = _cycles.charAt(j);
                }
                return perm;
            }
        } else if (_cycles.length() == 0) {
            return p;
        }
        return p;
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
}
