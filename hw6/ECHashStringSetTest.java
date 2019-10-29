import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author
 */
public class ECHashStringSetTest  {
    public static String[] words = new String[] {"a", "b", "c", "d"};
    public ECHashStringSet hash;
    public ECHashStringSet clean;

    @Test
    public void setter() {
        hash = new ECHashStringSet();
        clean = new ECHashStringSet();
    }

    @Test
    public void putter() {
        setter();
        int tracker = 0;
        for (String letter : words) {
            hash.put(letter);
            System.out.println(hash.length());
            tracker += 1;
            assert(hash.length() == tracker);

        }
    }

    @Test
    public void containner() throws Exception {
        setter();
        for (String letter : words) {
            hash.put(letter);
            assert(clean.contains(letter) == false);
            assert(hash.contains(letter) == true);
        }

        if (clean.contains(null)) {
            throw new Exception("Should not contain null");
        }

    }
}
