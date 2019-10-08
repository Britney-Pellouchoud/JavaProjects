package enigma;
import java.util.*;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @ Britney Pellouchoud
 */
class Alphabet {
    ArrayList chars;
    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        ArrayList <Character> no_duplicates = new ArrayList<>();
        for (int i = 0; i < chars.length(); i++) {
            if (!no_duplicates.contains(chars.charAt(i))){
                no_duplicates.add(chars.charAt(i));
            }
        }
        this.chars = no_duplicates;
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return chars.size(); // FIXME
    }

    /** Returns true if preprocess(CH) is in this alphabet. */
    boolean contains(char ch) {
        for (int i = 0; i < chars.size(); i++) {
            if ((char) chars.get(i) == ch) {
                return true;
            }
        }
        return false;

    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        assert(index >= 0 && index < chars.size());
        return (char) chars.get(index);
        //return (char) ('A' + index); // FIXME
    }

    /** Returns the index of character preprocess(CH), which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        int index = 0;
        for (int i = 0; i < chars.size(); i++) {
            if ((char) chars.get(i) == ch) {
               index = i;
            }
        }
        return index;
        //return ch - 'A'; // FIXME
    }

}
