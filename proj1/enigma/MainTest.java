package enigma;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;

public class MainTest {
    @Test
    public void testprintMsg() {
        Main.printMessageLine("THISISATESTINMYABILITIIESKLNM");
    }

    @Test
    public void testsetUP() {
        Alphabet alph = new Alphabet();
        Permutation perm = new Permutation("(BAC)", alph);
        Rotor C = new Rotor("C", perm);
        FixedRotor B = new FixedRotor("B", perm);
        Reflector A = new Reflector("A", perm);
        Collection<Rotor> rotors = new ArrayList<Rotor>();
        rotors.add(A);
        rotors.add(B);
        rotors.add(C);
        Machine M = new Machine(alph, 3, 2, rotors);
        String[] rots = new String[3];
        rots[0] = "A";
        rots[1] = "B";
        rots[2] = "C";
        M.insertRotors(rots);
        for (int i = 0; i < M.usedrotors().size(); i++) {
            System.out.println(M.usedrotors.get(i).setting());
        }
    }

    public void testread() {

    }
}
