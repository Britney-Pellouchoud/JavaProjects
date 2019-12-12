package gitlet;

import java.io.Serializable;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileNotFoundException;


/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author BritneyPellouchoud
 */


public class Main implements Serializable {
    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */

    private static String apple = "ClassNotFoundException "
            + "while deserializing gitlet.";

    /**.
     *
     */
    private static String astring = "Not in an initialized Gitlet directory.";

    /**.
     *
     */
    private static String moe = ".gitlet/mostrecent";

    /** .
     *
     * @param args .
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void main(String... args)
            throws IOException, ClassNotFoundException {
        String command = null;
        if (args.length == 0) {
            command = " ";
        } else {
            command = args[0];
        }
        Gitlet baby = null; Commit mr = null; File commitfiles = null;
        File g = new File(".gitlet"); String s = ".gitlet/.gitlet.ser";
        FileInputStream fi; ObjectInputStream oi; FileInputStream fileIn2;
        ObjectInputStream objectIn2; boolean letby = true;
        if (g.exists()) {
            try {
                fi = new FileInputStream(s); oi = new ObjectInputStream(fi);
                fileIn2 = new FileInputStream(moe);
                objectIn2 = new ObjectInputStream(fileIn2);
                mr = (Commit) objectIn2.readObject();
                baby = (Gitlet) oi.readObject(); oi.close();
            } catch (IOException e0) {
                System.out.println("IOException while deserializing gitlet.");
            } catch (ClassNotFoundException e1) {
                System.out.println(apple);
            }
        }
        switch (command) {
        case " " : System.out.println("Please enter a command"); break;
        case "init": baby = new Gitlet(); baby.init(); break;
        case "add": String filename = args[1]; baby.add(filename); break;
        case "commit": String mess = args[1];
            if (mess.equals("")) {
                System.out.println("Please enter a commit message."); break;
            }
            baby.commit(mess); break;
        case "checkout":
            if (args[1].equals("--")) {
                String fname = args[2]; baby.checkoutfile(fname); break;
            } else if (args.length == 4) {
                String commitid = args[1]; String fname = args[3];
                baby.checkoutversionfile(commitid, fname); break;
            } else if (args.length == 2) {
                String b = args[1]; baby.checkoutbranch(b); break;
            } else {
                throw new GitletException("Wrong number of arguments");
            }
        case "log": baby.log("master"); break;
        case "global-log": baby.globallog(); break;
        case "branch": baby.branch(args[1]); break;
        case "rm-branch": baby.removebranch(args[1]); break;
        case "find" : baby.find(args[1]); break;
        case "status":
            if (baby == null) {
                System.out.println(astring); letby = false; break;
            }
            baby.status(); break;
        case "reset": baby.reset(args[1]); break;
        case "merge": baby.merge(args[1]); break;
        case "rm" : baby.rm(args[1]); break;
        default: System.out.println(blubber); return; }
        checker(command, letby, s, baby, mr, moe, commitfiles);
    }

    /**.
     *
     */
    private static String blubber = "No command with that name exists.";


    /** .
     *
     * @param command .
     * @param letby .
     * @param serialize .
     * @param baby .
     * @param mr .
     * @param mostrecent .
     * @param commitfiles .
     */
    static void checker(String command, boolean letby,
                        String serialize, Gitlet baby,
                        Commit mr, String mostrecent, File commitfiles) {
        if (!command.equals(" ") && letby) {
            try {
                FileOutputStream fileOut = new FileOutputStream(serialize);
                ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
                objectOut.writeObject(baby);
                FileOutputStream fileOut2 = new FileOutputStream(mostrecent);
                ObjectOutputStream objectOut2 = new ObjectOutputStream(
                        fileOut2);
                objectOut2.writeObject(mr);
                objectOut.writeObject(commitfiles);
            } catch (GitletException | FileNotFoundException e) {
                e.printStackTrace();
                System.out.println("Error while serializing gitlet.");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
