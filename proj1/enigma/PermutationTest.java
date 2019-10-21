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
        assert (alph.toChar(3) == d);
        assert (alph.toInt(d) == 3);
    }

    @Test
    public void checksize() {
        Alphabet alph = new Alphabet();
        Permutation kperm = new Permutation("(ABC)", alph);
        assert (kperm.size() == 26);
    }

    @Test
    public void checkcharperm() {
        Alphabet alph = new Alphabet();
        Permutation dperm = new Permutation("(BAC)", alph);
        assert (dperm.permute('A') == 'C');
        assert (dperm.permute('D') == 'D');
        assert (dperm.permute('C') == 'B');
        Permutation perm2 = new Permutation("(BAC)(DEF)", alph);
        assert (perm2.permute('D') == 'E');
    }

    @Test
    public void checkcharinvert() {
        Alphabet alph = new Alphabet();
        Permutation dperm = new Permutation("(BAC)", alph);
        assert (dperm.invert('A') == 'B');
        assert (dperm.invert('D') == 'D');
        assert (dperm.invert('B') == 'C');
        Permutation perm2 = new Permutation("(BAC)(DEF)", alph);
        assert (perm2.invert('D') == 'F');
    }

    @Test
    public void checkderangement() {
        Alphabet alph = new Alphabet();
        Permutation fperm = new Permutation("(BAC)", alph);
        assert (!fperm.derangement());
        Permutation perm2 = new Permutation("(ABCDEFGHIJKLMNOPQ"
                + "RSTUVWXYZ)", alph);
        assert (perm2.derangement());
    }

    @Test
    public void checkintperm() {
        Alphabet alph = new Alphabet();
        Permutation mperm = new Permutation("(BAC)(DEF)", alph);
        assert (mperm.permute(0) == 2);
        assert (mperm.permute(2) == 1);
        assert (mperm.permute(13) == 13);
        assert (mperm.permute(27) == 0);
        assert (mperm.permute(28) == 1);
        assert (mperm.permute(26) == 2);
        assert (mperm.permute(30) == 5);
    }

    @Test
    public void checkintinvert() {
        Alphabet alph = new Alphabet();
        Permutation jperm = new Permutation("(BAC)", alph);
        assert (jperm.invert(0) == 1);
        assert (jperm.invert(1) == 2);
        assert (jperm.invert(12) == 12);
        Permutation perm2 = new Permutation("(BAC)(DEF)", alph);
        System.out.println(perm2.invert(4));
        assert (perm2.invert(4) == 3);
    }

    @Test
    public void checkRotor() {
        Alphabet alph = new Alphabet();
        Permutation lperm = new Permutation("(BAC)", alph);
        Rotor A = new Rotor("First", lperm);
        assert (A.size() == alph.size());
        assert (A.setting() == 0);
        A.set(2);
        assert (A.setting() == 2);
        A.set('D');
        assert (A.setting() == 3);
    }


    @Test
    public void alphlength() {
        Alphabet alph = new Alphabet("ABC");
        System.out.println(alph.size());
    }

    @Test
    public void checkFixedRotor() {
        Alphabet alph = new Alphabet();
        Permutation eperm = new Permutation("(BAC)", alph);
        FixedRotor A = new FixedRotor("First", eperm);
        A.set(2);
        assert (A.setting() == 2);
        assert (!A.rotates());
    }



}
