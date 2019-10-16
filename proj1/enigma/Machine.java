package enigma;

import com.sun.xml.internal.xsom.impl.scd.Iterators;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;
import java.util.List;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Britney Pellouchoud
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */

    protected int rotornum;
    protected int numpawl;
    Collection<Rotor> rotorlist;
    protected List<Rotor> usedrotors;
    protected Permutation plugbrd;
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        int rotornum = numRotors;
        int numpawl = pawls;
        rotorlist = allRotors;
        List<Rotor> usedrotors;
        Permutation plugbrd = new Permutation("", alpha);
        assert(numRotors > 1);
        assert(numpawl < numRotors && numpawl <= 0);

        // FIXME
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return rotornum;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return numpawl;
    }

    List<Rotor> usedrotors() {
        return usedrotors;
    }


    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        for (int i = 0; i < rotors.length; i ++) {
            for (Rotor r : rotorlist) {
                if (r.name().equals(rotors[i])) {
                    if (usedrotors.contains(r)) {
                        throw EnigmaException.error("Cannot input two of the same rotor");
                    } else {
                        usedrotors.add(r);
                    }
                }
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        assert(setting.length() == numRotors() - 1);
        for (int i = 0; i < setting.length(); i++) {
            int c = _alphabet.toInt(setting.charAt(i));
            usedrotors.get(i + 1).set(c);
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        plugbrd = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing

     *  the machine. */
    int convert(int c) {
        int x = plugbrd.permute(c);
        Rotor fast = usedrotors.get(usedrotors.size() - 1);
        fast.advance();
        for (int i = 1; i < numpawl; i++) {
            Rotor before = usedrotors.get(usedrotors.size() - i);
            Rotor after = usedrotors.get(usedrotors.size() - 1 - i);
            if (before.atNotch()) {
                if (!before.hasRotated()){
                    after.advance();
                    before.advance();
                    after.rotated = true;
                    before.rotated = true;
                }
            }
        }
        for (int i = usedrotors.size() - 1; i > 0; i--) {
            x = usedrotors.get(i).convertForward(x);
        }
        for (int i = 0; i < usedrotors.size(); i++) {
            x = usedrotors.get(i).convertBackward(x);
        }
        return plugbrd.invert(x);
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String message = new String();
        for (int i = 0; i < msg.length(); i++) {
            int j = convert(_alphabet.toInt(msg.charAt(i)));
            char k = _alphabet.toChar(j);
            message += k;
        }
        return message; // FIXME
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    // FIXME: ADDITIONAL FIELDS HERE, IF NEEDED.
}
