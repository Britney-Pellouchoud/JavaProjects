package enigma;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        Collection<Rotor> rotors = new ArrayList<Rotor>();
        rotors.add(A);
        rotors.add(B);
        rotors.add(C);
        Machine X = new Machine(alph, 3, 2, rotors);
        assert (X.numRotors() == 3);
        assert (X.numPawls() == 2);
        System.out.println(X.usedrotors());
        X.insertRotors(new String[]{"A", "B", "C"});
        System.out.println(X.usedrotors);
        X.setRotors("CD");
        for (Rotor K: X.usedrotors) {
            System.out.println(K.setting());
        }
        Permutation p = new Permutation("(AB)", alph);
        X.setPlugboard(p);
        int k = X.convert(0);
        System.out.println(alph.toChar(k));
    }

    @Test
    public void checkarray() {
        List<Integer> jerry = new ArrayList<Integer>();
        jerry.add(1);
        System.out.println(jerry);
    }

    @Test
    public void checkMachine3() {
        Alphabet alph = new Alphabet();
        Permutation bPerm = new Permutation("(AE) (BN) (CK) (DQ) (FU) "
                + "(GY) (HW) (IJ) (LO) (MP) (RX) (SZ) (TV)", UPPER);
        Permutation f = new Permutation("", UPPER);
        Reflector B = new Reflector("B", bPerm);
        MovingRotor fast = new MovingRotor("F", f, "");
        Collection<Rotor> bRotors = new ArrayList<Rotor>();
        bRotors.add(B);
        bRotors.add(fast);
        Machine X = new Machine(alph, 2, 0, bRotors);
        String[] c = new String[] {"B", "F"};
        X.insertRotors(c);
        Permutation plugboard = new Permutation("", UPPER);
        X.setPlugboard(plugboard);
        int j = X.convert(0);
        assert (j == 4);
    }

    @Test
    public void checkMachine2() {
        Permutation cBPerm = new Permutation("(AE) (BN) (CK) (DQ) (FU) "
                + "(GY) (HW) (IJ) (LO) (MP) (RX) (SZ) (TV)", UPPER);
        Permutation cBetaPerm = new Permutation("(ALBEVFCYODJWUG"
               + "NMQTZSKPR) (HIX)", UPPER);
        Permutation cIIIPerm = new Permutation("(ABDHPEJT) (CFLVMZO"
                + "YQIRWUKXSG)", UPPER);
        Permutation cIVPerm = new Permutation("(AEPLIYWCOXMRFZ"
                + "BSTGJQNH) (DV) (KU)", UPPER);
        Permutation cIPerm = new Permutation("(AELTPHQXRU) (BKNW) (CMOY) "
                + "(DFG) (IV) (JZ) (S)", UPPER);
        Permutation plugboard = new Permutation("(HQ) (EX) (IP) "
                + "(TR) (BY)", UPPER);
        String setting = "AXLE";
        Reflector aB = new Reflector("B", cBPerm);
        FixedRotor aBeta = new FixedRotor("Beta", cBetaPerm);
        MovingRotor aIII = new MovingRotor("III", cIIIPerm, "V");
        MovingRotor aIV = new MovingRotor("IV", cIVPerm, "J");
        MovingRotor aI = new MovingRotor("I", cIPerm, "Q");
        Collection<Rotor> kRotors = new ArrayList<Rotor>();
        kRotors.add(aB);
        kRotors.add(aBeta);
        kRotors.add(aIII);
        kRotors.add(aIV);
        kRotors.add(aI);
        Alphabet alph = new Alphabet();
        Machine K = new Machine(alph, 5, 3, kRotors);
        String [] c = new String[] {"B", "Beta", "III", "IV", "I"};
        K.insertRotors(c);
        K.setRotors(setting);
        K.setPlugboard(plugboard);
        String msg = "FROMHISSHOULDERHIAWATHATOOKTHECAMERAOFROSEWOOD"
                + "MADEOFSLIDINGFOLDINGROSEWOOD"
                + "NEATLYPUTITALLTOGETHERINITSCASEITLAYCOMP"
                + "ACTLYFOLDEDINTONEARLYNOTHINGBUTHEOPENEDOUTTHEHINGES"
                + "PUSHEDANDPULLEDTHEJOINTSANDHINGESTILLI"
                + "TLOOKEDALLSQUARESANDOBLONGSLIKEACOMPLICATEDFIGURE"
                + "INTHESECONDBOOKOFEUCLID";

        for (Rotor r : K.usedrotors) {
            System.out.println("SET " + r.setting());
        }
        System.out.println("B setting: " + aB.setting());
        System.out.println("Beta setting: " + aBeta.setting());
        System.out.println("III setting: " + aIII.setting());
        System.out.println("IV setting: " + aIV.setting());
        System.out.println("I setting: " + aI.setting());

        String reality = "QVPQSOKOILPUBKJZPISFXDWBHCNSCXNUOAA"
                + "TZXSRCFYDGUFLPNXGXIXTYJUJRCAUGEUNCF"
                + "MKUFWJFGKCIIRGXODJGVCGPQOHALWEBUHTZM"
                + "OXIIVXUEFPRPRKCGVPFPYKIKITLBURVGTSFU"
                + "SMBNKFRIIMPDOFJVTTUGRZMUVCYLFDZPGIBXRE"
                + "WXUEBZQJOYMHIPGRREGOHETUXDTWLCMMWAVN"
                + "VJVHOUFANTQACKKTOZZRDABQNNVPOIEFQAFSVVICVUDUEREYNPFFMNBJVGQ";

        assertEquals(K.convert(reality), msg);
    }




}
