package enigma;
import java.util.Map;
import java.util.HashMap;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @ Britney Pellouchoud
 */
class Alphabet {
    String chars;
    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        this.chars = chars;
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return chars.length(); // FIXME
    }

    /** Returns true if preprocess(CH) is in this alphabet. */
    boolean contains(char ch) {
        for (int i = 0; i < chars.length(); i++) {
            if (chars.indexOf(i) == ch) {
                return true;
            }
        }
        return false;

    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return (char) chars.indexOf(index);
        //return (char) ('A' + index); // FIXME
    }

    /** Returns the index of character preprocess(CH), which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        int index = 0;
        for (int i = 0; i < chars.length(); i++) {
            if (chars.indexOf(i) == ch) {
               index = i;
            }
        }
        return index;
        //return ch - 'A'; // FIXME
    }

}
