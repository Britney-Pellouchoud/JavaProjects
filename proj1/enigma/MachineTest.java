package enigma;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import java.sql.SQLOutput;
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
        Collection <Rotor> Rotors = new ArrayList<Rotor>();
        Rotors.add(A);
        Rotors.add(B);
        Rotors.add(C);
        Machine X = new Machine(alph, 3, 2, Rotors);
        assert (X.numRotors() == 3);
        assert(X.numPawls() == 2);
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
        Permutation BPerm = new Permutation("(AE) (BN) (CK) (DQ) (FU) (GY) (HW) (IJ) (LO) (MP) (RX) (SZ) (TV)", UPPER);
        Permutation f = new Permutation("", UPPER);
        Reflector B = new Reflector("B", BPerm);
        MovingRotor Fast = new MovingRotor("F", f, "");
        Collection <Rotor> Rotors = new ArrayList<Rotor>();
        Rotors.add(B);
        Rotors.add(Fast);
        Machine X = new Machine(alph, 2, 0, Rotors);
        String[] c = new String[] {"B", "F"};
        X.insertRotors(c);
        Permutation plugboard = new Permutation("", UPPER);
        X.setPlugboard(plugboard);
        int j = X.convert(0);
        assert(j == 4);
    }

    @Test
    public void checkMachine2() {
        Permutation BPerm = new Permutation("(AE) (BN) (CK) (DQ) (FU) (GY) (HW) (IJ) (LO) (MP) (RX) (SZ) (TV)", UPPER);
        Permutation BetaPerm = new Permutation("(ALBEVFCYODJWUGNMQTZSKPR) (HIX)", UPPER);
        Permutation IIIPerm = new Permutation("(ABDHPEJT) (CFLVMZOYQIRWUKXSG)", UPPER);
        Permutation IVPerm = new Permutation("(AEPLIYWCOXMRFZBSTGJQNH) (DV) (KU)", UPPER);
        Permutation IPerm = new Permutation("(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", UPPER);
        Permutation plugboard = new Permutation("(HQ) (EX) (IP) (TR) (BY)", UPPER);
        String setting = "AXLE";

        Reflector B = new Reflector("B", BPerm);
        FixedRotor Beta = new FixedRotor("Beta", BetaPerm);
        MovingRotor III = new MovingRotor("III", IIIPerm, "V");
        MovingRotor IV = new MovingRotor("IV", IVPerm, "J");
        MovingRotor I = new MovingRotor("I", IPerm, "Q");

        Collection <Rotor> Rotors = new ArrayList<Rotor>();
        Rotors.add(B);
        Rotors.add(Beta);
        Rotors.add(III);
        Rotors.add(IV);
        Rotors.add(I);
        Alphabet alph = new Alphabet();
        Machine K = new Machine(alph, 5, 3, Rotors);
        String [] c = new String[] {"B", "Beta", "III", "IV", "I"};
        K.insertRotors(c);
        K.setRotors(setting);
        K.setPlugboard(plugboard);
        String msg = "FROMHISSHOULDERHIAWATHATOOKTHECAMERAOFROSEWOODMADEOFSLIDINGFOLDINGROSEWOOD " +
                "NEATLYPUTITALLTOGETHERINITSCASEITLAYCOMPACTLYFOLDEDINTONEARLYNOTHINGBUTHEOPENEDOUTTHEHINGES" +
                "PUSHEDANDPULLEDTHEJOINTSANDHINGESTILLITLOOKEDALLSQUARESANDOBLONGSLIKEACOMPLICATEDFIGUREINTHESECONDBOOKOFEUCLID";

        String Vera = msg.substring(0, 11);
        String Ver1 = K.convert(Vera);
        //System.out.println(Ver1);


        String Ava = K.convert(msg.substring(12, msg.length()));
        String reality = "QVPQSOKOILPUBKJZPISFXDWBHCNSCXNUOAATZXSRCFYDGUFLPNXGXIXTYJUJRCAUGEUNCFMKUFWJFGKCIIRGXODJGVCGPQOHALWEBUHTZMOXIIVXUEFPRPRKCGVPFPYKIKITLBURVGTSFU" +
                "SMBNKFRIIMPDOFJVTTUGRZMUVCYLFDZPGIBXREWXUEBZQJOYMHIPGRREGOHETUXDTWLCMMWAVNVJVHOUFANTQACKKTOZZRDABQNNVPOIEFQAFSVVICVUDUEREYNPFFMNBJVGQ";
        String realibee = reality.substring(12, reality.length());
        assertEquals(Ver1, reality.substring(0,11));
        //assert(Ava.equals(realibee));
        System.out.println(msg.charAt(12));
        System.out.println(Ava);
        System.out.println(realibee);

        /*
        for (int i = 0; i < reality.length(); i++) {
            if (Ava.charAt(i) != reality.charAt(i)) {
                System.out.println("At position " + i);
                System.out.println("Ava char: " + Ava.charAt(i));
                System.out.println("reality char: " + reality.charAt(i));
                break;
            }
            continue;
        }

         */
        //System.out.println(Ava);
    }




}
