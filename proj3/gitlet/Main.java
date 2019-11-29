package gitlet;

import java.util.Scanner;

import static gitlet.Utils.error;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @BritneyPellouchoud
 */
public class Main {


    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        Main a = new Main(args);
        String command = arg.next();
        if (command.equals("init")) {
            baby = new Gitlet();
            baby.init();
        }
        // FILL THIS IN
    }

    Main(String[] args) {
        if (args.length < 1) {
            throw error("Must input something");
        } else {
            arg = new Scanner(System.in);
        }
    }


    private static Scanner arg;
    private static Gitlet baby;



}
