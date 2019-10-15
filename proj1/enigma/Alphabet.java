package enigma;
import java.util.*;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Britney Pellouchoud
 */
class Alphabet {
    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    private ArrayList chars;
    /* Alphabet takes in a string of characters
     * checks for duplicates */
    Alphabet(String chars) {
        ArrayList<Character> noduplicates = new ArrayList<>();
        for (int i = 0; i < chars.length(); i++) {
            if (!noduplicates.contains(chars.charAt(i))) {
                noduplicates.add(chars.charAt(i));
            }
        }
        this.chars = noduplicates;
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return chars.size();
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
        return (char) chars.get(index % 26);
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
    }

}
