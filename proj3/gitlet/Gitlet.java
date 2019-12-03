package gitlet;

import org.antlr.v4.runtime.tree.Tree;
import org.w3c.dom.Node;

import java.io.File;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.lang.String;
import java.util.*;


public class Gitlet implements Serializable {


    private transient Commit _mostrecent;
    private static CommitTree<Commit> committree;

    void init() {
        if (!checkunique()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        }
        Instant now = Instant.now();
        String initial = "initial commit";
        Commit m = new Commit();
        m.init(this, "initial commit", Utils.sha1(initial), now.toString(), null, null);
        _mostrecent = null;
        committree = new CommitTree<>(m);
        pointer = m;
        Branch master = new Branch();
        master.init("master");
        makestagedir();
    }





    private static Commit pointer;

    void setPointer(Commit com) {
        pointer = com;
    }

    Commit getPointer() {
        return pointer;
    }

    public CommitTree getCommittree() {
        return committree;
    }

    static boolean checkunique() {
        String directory = ".gitlet";
        File file = new File(directory);
        if (file.isDirectory()) {
            return false;
        }
        return true;
    }

    static void makestagedir() {
        new File(".gitlet/other").mkdirs();
        new File(".gitlet/staging").mkdirs();
    }

    void add(String filename) throws IOException {


        File toadd = new File(filename);
       String sha1 = Utils.sha1(Utils.readContentsAsString(toadd));

        if (new File(".gitlet/staging/" + sha1).exists()) {
           return;
       }
        File n = new File(".gitlet/other/" + filename + "/" + sha1);
        Path file = Paths.get(filename);
        Path tofile = Paths.get(".gitlet/staging/" + sha1);
        Path secfile = Paths.get(".gitlet/other/" + filename);
        Files.copy(file, tofile, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(file, secfile, StandardCopyOption.REPLACE_EXISTING);

        assert new File(".gitlet/staging/" + sha1).exists();


    }

    void clearstaged() {
        File f = new File(".gitlet/staging/");
        File[] files = f.listFiles();
        assert files != null;
        if (files.length > 0) {
            for (File file : files) {
                file.delete();
            }
        }

    }

    void rm(String filename) {
        File staged = new File(".gitlet/staging/");
        File[] stagedfiles = staged.listFiles();
        for (File file : stagedfiles) {
            if (file.equals(new File(filename))) {
                file.delete();
                return;
            }
        }
        for (File file : pointer.getFiles()) {
            String fname = file.getName();
            if (fname.equals(filename)) {
                pointer.markfortracking(filename);
                return;
            }
        }
        //if file is tracked in current commit, mark it
        System.out.println("No reason to remove the file.");
    }

    String log() {
        System.out.println(x);
        //find way to convert to regular time
        String log = "===/ncommit " + pointer.getCommitsha1() + "/nDate: " + pointer.getTimestamp() + "/n" + pointer.getMessage();
        Commit temppoint = pointer;
        while (temppoint.getParent() != null) {
            log += "/ncommit " + temppoint.getCommitsha1() + "/nDate: " + temppoint.getTimestamp() + "/n" + temppoint.getMessage();
            temppoint = temppoint.getParent();
        }
        return log;
        //For merge commits (those that have two parent commits), add a line just below the first,
    }

    String globallog(CommitTree ct) {
        CommitTree.Node thisnode = ct.root();
        if (thisnode.getChildren().size() == 0) {
            Commit d = (Commit) thisnode.getData();
            return "===/ncommit " + d.getCommitsha1() + "/nDate: " + d.getTimestamp() + "/n" + d.getMessage();
        }
        List c = ct.root().getChildren();
        for (int i = 0; i < c.size(); i++ ) {
            globallog += globallog((CommitTree) c.get(i));
        }
        return globallog;
    }

    /*
    Takes the version of the file as it exists in the head commit, the front of the current branch,
    and puts it in the working directory,
     overwriting the version of the file that's already there if there is one.
     The new version of the file is not staged.
     */
    void checkoutfile(String filename) throws IOException {
        Path file = Paths.get(filename);
        Path tofile = Paths.get(".gitlet/other/" + filename);

        Files.copy(tofile, file, StandardCopyOption.REPLACE_EXISTING);


        //System.out.println( "HITS HERE " + m);


        //System.out.println(filetoname.get(filename));

        //Path tofile = Paths.get( "a715efcd9d8824ec2636293f233b2462cea45359");// +  "a715efcd9d8824ec2636293f233b2462cea45359");

    }


    void commit(String message) {


        //Path file = Paths.get(filename);
        //Path tofile = Paths.get(".gitlet/staging/" + sha1);
        //Path secfile = Paths.get(".gitlet/excess/" + sha1);
        //Files.copy(file, tofile, StandardCopyOption.REPLACE_EXISTING);
        //Files.copy(file, secfile, StandardCopyOption.REPLACE_EXISTING);

        String all = "";
        File staged = new File(".gitlet/staging/");
        File[] files = staged.listFiles();
        ArrayList<File> fil = new ArrayList<File>();

        for (File x: files) {
            //System.out.println(x.getName());
            fil.add(x);;
        }
        //List<String> q = Utils.plainFilenamesIn(new File(".gitlet/alreadycommit/"));
        Commit a = new Commit();
        Instant now = Instant.now();
        a.init(this, message, Utils.sha1(message + all + now.toString() +
                null), now.toString(), fil, null);
        _mostrecent = a;
        changex();
    }


    void changex() {
        x = 10;
    }

    private static int x;





    private String globallog;


}
