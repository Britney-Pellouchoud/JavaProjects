package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

//import static gitlet.Utils.error;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @BritneyPellouchoud
 */


public class Main {


    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) throws IOException {
        Scanner s = Main(args);
        while(s.hasNext()) {
            String command = s.next();
            if (command.equals("init")) {
                baby.init();
            } if (command.equals("add")) {
                String f = s.next();
                baby.add(f);
            } if (command.equals("commit")) {
                String commitmess = "";
                while (s.hasNext()) {
                    commitmess += s.next() + " ";
                }
                baby.commit(commitmess);
            } if (command.equals("checkout")) {
                s.next();
                String filename = s.next();
                baby.checkoutfile(filename);
            } if (command.equals("log")) {
                baby.log();
            }
        }

        // FILL THIS IN
    }

    static Scanner Main(String[] args) {
        if (args.length < 1) {
            //throw error("Must input something");
            System.out.println("Must input something");
            return null;
        } else {
            a = "";
            for (String arg : args) {
                a += arg + " ";
            }
            arg = new Scanner(String.valueOf(a));
            return arg;
        }
    }


    private static String a;
    private static Scanner arg;
    private static Gitlet baby = new Gitlet();




}
