package gitlet;

import com.sun.codemodel.internal.JWhileLoop;
import org.antlr.v4.runtime.tree.Tree;
import org.w3c.dom.Node;

//IDEA: MAKE A LINKED LIST WITH FILES FOR EACH COMMIT

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.lang.String;
import java.util.*;


public class Gitlet implements Serializable {


    private static CommitTree<Commit> committree;
    private File babydir;
    private File staged;
    private Commit _mostrecent;
    private File other;
    private Commit _initialcommit;
    private File commits;



    void init() throws IOException, ClassNotFoundException {
        FileOutputStream fileOut;
        ObjectOutputStream objectOut;
        FileInputStream fileIn;
        ObjectInputStream objectIn;
        File mostrec;

        if (!checkunique()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        }
        babydir = new File(".gitlet");
        babydir.mkdirs();
        staged = new File(".gitlet/staging");
        //other stores the previous commit
        other = new File(".gitlet/other");
        other.mkdirs();
        staged.mkdirs();
        Instant now = Instant.now();
        String initial = "initial commit";
        Commit m = new Commit();
        m.init(this, "initial commit", Utils.sha1(initial), now.toString(), null, null);
        //committree = new CommitTree<>(m);
        _initialcommit = m;
        Branch master = new Branch();
        master.init("master");
        commits = new File(".gitlet/commit");
        commits.mkdirs();



        _mostrecent = m;
        fileOut = new FileOutputStream(".gitlet/mostrecent");
        objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(_mostrecent);

        //mostrec = new File(".gitlet/mostrecent");
        //mostrec.mkdir();
        //Utils.writeObject(mostrec, m);


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

        if (new File(".gitlet/staging/" + filename).exists()) {
           return;
       }

        //File n = new File(".gitlet/other/" + filename + "/" + sha1);
        Path file = Paths.get(filename);
        Path tofile = Paths.get(".gitlet/staging/" + filename);
        Path secfile = Paths.get(".gitlet/other/" + filename);
        Files.copy(file, tofile, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(file, secfile, StandardCopyOption.REPLACE_EXISTING);

        //assert new File(".gitlet/staging/filename" + sha1).exists();




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

    void log() throws IOException, ClassNotFoundException {
        //File k = mostrec;
        //mostrec.mkdirs();
        //System.out.println(mostrec.listFiles().length);
        //Commit c = (Commit) k.readObject();

        Commit temp = _mostrecent;
        while (temp != null) {
            System.out.println("===");
            System.out.println("commit " + temp.getCommitsha1());
            System.out.println("Date: " + temp.getTimestamp());
            System.out.println(temp.getMessage());
            System.out.println();
            temp = temp.getParent();
        }
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

    void checkoutversionfile(String commitid, String filename) throws IOException {

        Commit temp = _mostrecent;

        while (temp != null) {
            //System.out.println("HITS HERE");

            File r = new File("./commit");
            File[] q = r.listFiles();
            File a = q[0];
            if (a.getName().equals(commitid)) {
                File d = new File("./.gitlet/commit/version 1 of wug.txt");
                d.mkdirs();
                //System.out.println(" AHAHHAHAHAHAHAHAHAHAHAHAHAHAH ");
                Path t = Paths.get("./.gitlet/commit/" + temp.getMessage() + "/" + filename);
                //System.out.println("HELLLOOOOOOOOOOOOOOOO   " + Utils.readContentsAsString(new File(t.toString())));
                Path f = Paths.get(filename);
                //File h = new File(f.toString());
                //System.out.println("HERE WE GOOOOOOOOOOOOO" + h.toPath());
                //System.out.println("GOODBYEEEEEEEEEEEEEE " + Utils.readContentsAsString(new File(f.toString())));
                Files.copy(t, f, StandardCopyOption.REPLACE_EXISTING);
                break;
            } else {
                temp = temp.getParent();
            }
        }






    }





    void checkoutfile(String filename) throws IOException, ClassNotFoundException {
        Path a = Paths.get(".gitlet/commit/" + _mostrecent.getMessage() + "/" + filename);
        //assert new File(a.toString()).exists();
        Path file = Paths.get(filename);
        //assert new File(file.toString()).exists();
        Files.copy(a, file, StandardCopyOption.REPLACE_EXISTING);

    }


    void commit(String message) throws IOException, ClassNotFoundException {
        /*
        Path file = Paths.get(filename);
        Path tofile = Paths.get(".gitlet/staging/" + filename);
        Path secfile = Paths.get(".gitlet/other/" + filename);
        Files.copy(file, tofile, StandardCopyOption.REPLACE_EXISTING);

         */

        FileOutputStream fileOut;
        ObjectOutputStream objectOut;
        FileInputStream fileIn;
        ObjectInputStream objectIn;
        File mostrec;

        String all = "";
        File staged = new File(".gitlet/staging/");
        File[] files = staged.listFiles();
        ArrayList<File> fil = new ArrayList<File>();

        Instant now = Instant.now();
        String m = Utils.sha1(message + all + now.toString() +
                null);
        File r = new File(".gitlet/commit/" + message + "/");
        r.mkdirs();
        for (File x: files) {
            String filename = x.toPath().toString().substring(16);
            //System.out.println(x.getName());
            //System.out.println("HIIIII " + p.toString().substring(16, p.toString().length()));
            Path file = Paths.get(filename);
            Path tofile = Paths.get(".gitlet/commit/" + message + "/" + filename);
            Files.copy(file, tofile, StandardCopyOption.REPLACE_EXISTING);
            fil.add(x);;
        }


        //List<String> q = Utils.plainFilenamesIn(new File(".gitlet/alreadycommit/"));
        Commit a = new Commit();
        a.init(this, message, Utils.sha1(message + all + now.toString() +
                null), now.toString(), fil, _mostrecent);
        _mostrecent = a;
        fileOut = new FileOutputStream(".gitlet/mostrecent");
        objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(_mostrecent);


        //Utils.writeObject(mostrec, a);





    }

    //serialize EVERYTHING







    private String globallog;


}
