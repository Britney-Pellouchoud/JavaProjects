package enigma;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Britney Pellouchoud
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */

    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output.
     *  @return String
     *  @param M*/

    private String setup2(Machine M) {
        M.usedrotors = new ArrayList<Rotor>();
        String[] used = new String[M.numRotors()];
        int counter = 0;
        String nurm;
        Scanner sett = new Scanner(settline);
        while (counter < M.numRotors()) {
            if (!settline.isEmpty()) {
                try {
                    nurm = sett.next();
                } catch (NoSuchElementException excp) {
                    throw new EnigmaException("Bad configuration");
                }
            } else {
                try {
                    nurm = _input.next();
                } catch (NoSuchElementException excp) {
                    throw new EnigmaException("Bad configuration");
                }
            }
            if (nurm.contains("*")) {
                continue;
            }
            used[counter] = nurm;
            counter += 1;
        }

        M.insertRotors(used);
        if (!settline.isEmpty()) {
            setUp(M, sett.next());
        } else {
            setUp(M, _input.next());
        }
        String cycles = "";
        String a;
        if (!settline.isEmpty()) {
            if (!sett.hasNext()) {
                a = _input.next();
            } else {
                a = sett.next();
            }
            while (a.contains("(")) {
                cycles += a;
                if (sett.hasNext()) {
                    a = sett.next();
                } else {
                    a = _input.next();
                }
            }
        } else {
            a = _input.next();
            while (a.contains("(")) {
                cycles += a;
                a = _input.next();
            }
        }
        Permutation forplug = new Permutation(cycles, _alphabet);
        M.setPlugboard(forplug);
        return a;
    }

    /**
     * String settline to know if I need to reset.
     */
    private String settline = "";

    /**
     * The entire process of going through the document.
     */
    private void process() {
        Machine M = readConfig();
        String l = setup2(M);
        String msge = l;
        if (_input.hasNextLine()) {
            l = _input.nextLine();
            msge += l;
        }

        String converted = M.convert(msge);
        printMessageLine(converted);
        while (_input.hasNextLine()) {
            String x = _input.nextLine();
            if (!x.contains("*")) {
                String converted2 = M.convert(x);
                printMessageLine(converted2);
            } else {
                settline = x;
                String n = setup2(M);
                if (x == settline) {
                    if (_input.hasNextLine()) {
                        x = n + _input.nextLine();
                        String converted3 = M.convert(x);
                        printMessageLine(converted3);
                        continue;
                    } else {
                        x = n;
                        String converted3 = M.convert(x);
                        printMessageLine(converted3);
                        continue;
                    }

                }
                String converted3 = M.convert(x);
                printMessageLine(converted3);
            }

        }


    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.next());
            int numrot = _config.nextInt();
            int numpaw = _config.nextInt();
            ArrayList<Rotor> allrots = new ArrayList<>();
            while (_config.hasNextLine() && !starter.contains("(")) {
                allrots.add(readRotor());
            }
            return new Machine(_alphabet, numrot, numpaw, allrots);

        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name;
            if (starter.isEmpty()) {
                name = _config.next();
            } else {
                name = starter;
            }
            String orientation = _config.next();
            String typer = "";
            String notches = "";
            if (orientation.charAt(0) == 'R') {
                typer = "Reflector";
            } else if (orientation.charAt(0) == 'M') {
                typer = "Moving";
                for (int i = 1; i < orientation.length(); i++) {
                    notches += orientation.charAt(i);
                }
            } else if (orientation.charAt(0) == 'N') {
                typer = "Nonmoving";
            }

            StringBuilder permutation = new StringBuilder(new String());

            String permer = _config.next();
            while (permer.contains("(") && permer.contains(")")) {
                permutation.append(permer);
                if (_config.hasNext()) {
                    permer = _config.next();
                } else {
                    break;
                }
            }
            starter = permer;
            Alphabet alph = _alphabet;
            Permutation perm = new Permutation(permutation.toString(), alph);
            if (typer.equals("Reflector")) {
                return new Reflector(name, perm);
            } else if (typer.equals("Moving")) {
                return new MovingRotor(name, perm, notches);
            }
            return new FixedRotor(name, perm);
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /**Starter.
     */
    private String starter = "";

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private static void setUp(Machine M, String settings) {
        M.setRotors(settings);
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */


    static void printMessageLine(String msg) {
        for (int i = 0; i < msg.length(); i += 5) {
            if (i + 5 < msg.length()) {
                message += msg.substring(i, i + 5) + " ";
                continue;
            }
            message += msg.substring(i, msg.length());
        }
        _output.append(message);
        _output.println();
        message = "";
    }


    /** Message.
     */
    private static String message = "";

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private static PrintStream _output;
}
