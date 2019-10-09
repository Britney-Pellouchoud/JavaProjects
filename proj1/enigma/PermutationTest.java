package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @ Britney Pellouchoud
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */

    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void checkAlphabet() {
        Alphabet alph = new Alphabet();
        assertEquals(alph.size(), 26);
        Character b = 'B';
        assertEquals(true, alph.contains(b));
        Character d = 'D';
        assert(alph.toChar(3) == d);
        assert(alph.toInt(d) == 3);
    }

    @Test
    public void checksize() {
        Alphabet alph = new Alphabet();
        Permutation perm = new Permutation("(ABC)", alph);
        assert(perm.size() == 26);
    }

    @Test
    public void checkcharperm() {
        Alphabet alph = new Alphabet();
        Permutation perm = new Permutation("(BAC)", alph);
        assert(perm.permute('A') == 'C');
        assert(perm.permute('D') == 'D');
        assert(perm.permute('C') == 'B');
        Permutation perm2 = new Permutation("(BAC)(DEF)", alph);
        assert(perm2.permute('D') == 'E');
    }

    @Test
    public void checkcharinvert() {
        Alphabet alph = new Alphabet();
        Permutation perm = new Permutation("(BAC)", alph);
        assert(perm.invert('A') == 'B');
        assert(perm.invert('D') == 'D');
        assert(perm.invert('B') == 'C');
        Permutation perm2 = new Permutation("(BAC)(DEF)", alph);
        assert(perm2.invert('D') == 'F');
    }

    @Test
    public void checkderangement() {
        Alphabet alph = new Alphabet();
        Permutation perm = new Permutation("(BAC)", alph);
        assert(perm.derangement() == false);
        Permutation perm2 = new Permutation("(ABCDEFGHIJKLMNOPQRSTUVWXYZ)", alph);
        assert(perm2.derangement() == true);
    }

    @Test
    public void checkintperm() {
        Alphabet alph = new Alphabet();
        Permutation perm = new Permutation("(BAC)", alph);
        assert(perm.permute(0) == 2);
        assert(perm.permute(2) == 1);
        assert(perm.permute(5) == 5);
    }

    @Test
    public void checkintinvert() {
        Alphabet alph = new Alphabet();
        Permutation perm = new Permutation("(BAC)", alph);
        assert(perm.invert(0) == 1);
        assert(perm.invert(1) == 2);
        assert(perm.invert(12) == 12);
    }

}
