package enigma;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import java.sql.SQLOutput;
import java.util.Collection;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;


public class MachineTest {
    @Test
    public void checkMachine() {
        Alphabet alph = new Alphabet();
        Permutation perm = new Permutation("(BAC)", alph);
        Rotor C = new Rotor("C", perm);
        FixedRotor B = new FixedRotor("B", perm);
        Reflector A = new Reflector("A", perm);
        Collection<Rotor> Rotors = new Collection<Rotor>(A,B,C);
        Machine X = new Machine(alph, 3, 2, Rotors);
    }
}
