package gitlet;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;


//import static gitlet.Utils.error;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @BritneyPellouchoud
 */


public class Main implements Serializable{


    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) throws IOException, ClassNotFoundException {
        String command = args[0];
        Gitlet baby = null;
        Commit mr = null;
        File commitfiles = null;
        File gitdir = new File(".gitlet");
        String serialize = ".gitlet/.gitlet.ser";
        String mostrecent = ".gitlet/mostrecent";
        String commits =".gitlet/commitfile/";
        FileOutputStream fileOut = null;
        ObjectOutputStream objectOut = null;
        FileOutputStream fileOut2 = null;
        ObjectOutputStream objectOut2 = null;
        FileInputStream fileIn = null;
        ObjectInputStream objectIn = null;
        FileInputStream fileIn2 = null;
        ObjectInputStream objectIn2 = null;

        if (gitdir.exists()) {
            try {
                fileIn = new FileInputStream(serialize);
                objectIn = new ObjectInputStream(fileIn);
                fileIn2 = new FileInputStream(mostrecent);
                objectIn2 = new ObjectInputStream(fileIn2);
                mr = (Commit) objectIn2.readObject();
                baby = (Gitlet) objectIn.readObject();
                objectIn.close();
                //objectIn2.close();
            } catch (IOException e0) {
                System.out.println("IOException while deserializing gitlet.");
            } catch (ClassNotFoundException e1) {
                System.out.println("ClassNotFoundException while deserializing gitlet.");
            }
        }



        switch (command){
            case "init":
                assert args.length == 1 : "Wrong number of arguments";
                baby = new Gitlet();
                baby.init();
                break;
            case "add":
                String filename = args[1];
                baby.add(filename);
                break;
            case "commit":
                String mess = args[1];
                baby.commit(mess);
                break;
            case "checkout" :
                if (args[1].equals("--")) {
                    String fname = args[2];
                    baby.checkoutfile(fname);
                    break;
                } else if (args.length == 4) {
                    String commitid = args[1];
                    String fname = args[3];
                    baby.checkoutversionfile(commitid, fname);
                    break;
                } else if (args.length == 2) {
                    String branchname = args[1];
                    baby.checkoutbranch(branchname);
                    break;
                } else {
                    throw new GitletException("Wrong number of arguments");
                }

            case "log":
                baby.log("master");
                break;
            case "global-log":
                baby.globallog();
                break;
            case "branch":
                baby.branch(args[1]);
                break;
            case "rm-branch":
                baby.removebranch(args[1]);
                break;
            case "find" :
                baby.find(args[1]);
                break;
            case "status":
                baby.status();
                break;
            case "reset":
                baby.reset(args[1]);
                break;
            case "merge":
                baby.merge(args[1]);
                break;
            case "rm" :
                baby.rm(args[1]);
                break;
            default:
                System.out.println("No command with that name exists.");
                return;
        }

        try {
            fileOut = new FileOutputStream(serialize);
            objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(baby);
            fileOut2 = new FileOutputStream(mostrecent);
            objectOut2 = new ObjectOutputStream(fileOut2);
            objectOut2.writeObject(mr);
            objectOut.writeObject(commitfiles);



        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while serializing gitlet.");
        }



    }


}
