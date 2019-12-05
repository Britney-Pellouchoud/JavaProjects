package gitlet;



//IDEA: MAKE A LINKED LIST WITH FILES FOR EACH COMMIT

import net.sf.saxon.style.XSLOutput;

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
    private Branch master;
    private ArrayList<Branch> branches = new ArrayList<>();
    private Branch curr;


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
        other = new File(".gitlet/other");
        other.mkdirs();
        staged.mkdirs();
        Instant now = Instant.now();
        String initial = "initial commit";
        Commit m = new Commit();
        m.init(this, "initial commit", Utils.sha1(initial), now.toString(), null, null);
        master = new Branch();
        master.init("master");
        master.addCsha1(m);
        branches.add(master);
        File br = new File("branches");
        br.mkdirs();
        File ms = new File("branches/" + "master");
        Utils.writeObject(ms, master);
        curr = master;
        commits = new File(".gitlet/commit");
        commits.mkdirs();
        _mostrecent = m;
        fileOut = new FileOutputStream(".gitlet/mostrecent");
        objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(_mostrecent);
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

    void log(String branchname) throws IOException, ClassNotFoundException {

        FileInputStream f = new FileInputStream("branches" + "/" + branchname);
        ObjectInputStream o = new ObjectInputStream(f);
        Branch c = (Branch) o.readObject();
        //System.out.println(c.getCsha1s().size());
        ArrayList<Commit> a = c.getCsha1s();
        int l = a.size();
        Commit temp = a.get(l - 1);
        while (temp != null) {
            System.out.println("===");
            System.out.println("commit " + temp.getCommitsha1());
            System.out.println("Date: " + temp.getTimestamp());
            System.out.println(temp.getMessage());
            System.out.println();
            temp = temp.getParent();
        }

    }

    void globallog() throws IOException, ClassNotFoundException {
        for (Branch b : branches) {
            log(b.getName());
        }
    }

    /*
    Takes the version of the file as it exists in the head commit, the front of the current branch,
    and puts it in the working directory,
     overwriting the version of the file that's already there if there is one.
     The new version of the file is not staged.
     */

    void checkoutversionfile(String commitid, String filename) throws IOException {

        Commit temp = _mostrecent;


        Path x = Paths.get(filename);
        File p = new File(x.toString());

        String mssg = "";

        while(temp != null) {
            File w = new File("./commit/" + temp.getMessage());
            w.mkdirs();
            for (File i : w.listFiles()) {
                if (i.getName().equals(commitid)) {
                    mssg += temp.getMessage();
                } else {
                    break;
                }
            }
            temp = temp.getParent();
        }


        Path b = Paths.get(".gitlet/commit/" + mssg + "/" + filename);
        File j = new File(b.toString());
        String h = Utils.readContentsAsString(j);

        Path file = Paths.get(filename);
        File i = new File(file.toString());
        Utils.writeContents(i, h);


        //Files.copy(b, file, StandardCopyOption.REPLACE_EXISTING);




    }

    void checkoutbranch(String branchname) {
        Branch k = null;
        for (Branch b : branches) {
            if (b.getName().equals(branchname)) {
                k = b;
            }
        }
        if (k == null) {
            System.out.println("No such branch exists.");
        } else if (k.getName().equals(curr.getName())) {
            System.out.println("No need to checkout the current branch.");
        }
        Commit c = k.latestcommit();
        Path a = Paths.get(".gitlet/commit/" + c.getMessage());
        File x = new File(a.toString());
        x.mkdirs();
        assert x.listFiles() != null;
        for (File f : x.listFiles()) {
            if (new File(f.getName()).exists()) {
                String contents = Utils.readContentsAsString(f);
                Utils.writeContents(new File(f.getName()), contents);
            }
        }
    }


    void branch (String branchname) {
        for (Branch b : branches) {
            if (b.getName().equals(branchname)) {
                throw new GitletException("A branch with that name already exists.");
            }
        }
        Branch b = new Branch();
        b.init(branchname);
        b.addCsha1(curr.latestcommit());
    }


    void removebranch(String branchname) {
        Branch toremove = null;
        for (Branch b : branches) {
            if (b.getName().equals(branchname)) {
                if (b.getName().equals(curr.getName())) {
                    throw new GitletException("Cannot remove the current branch.");
                }
                toremove = b;
                branches.remove(b);
            }
        }
        if (toremove == null) {
            throw new GitletException("A branch with that name does not exist.");
        }
    }

    void find(String commitmsg) {
        int x = 0;
        for (Branch b : branches) {
            for (Commit c : b.getCsha1s()) {
                if (c.getMessage().equals(commitmsg)) {
                    System.out.println(c.getCommitsha1());
                    x += 1;
                }
            }
        }
        if (x == 0) {
            System.out.println("Found no commit with that message.");
        }
    }




    void checkoutfile(String filename) throws IOException, ClassNotFoundException {
        Path a = Paths.get(".gitlet/commit/" + _mostrecent.getMessage() + "/" + filename);
        File b = new File(a.toString());
        //assert new File(a.toString()).exists();
        Path file = Paths.get(filename);
        File j = new File(file.toString());
        String p = Utils.readContentsAsString(b);
        Utils.writeContents(j, p);
        //assert new File(file.toString()).exists();
        //Files.copy(a, file, StandardCopyOption.REPLACE_EXISTING);

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
        File r = new File(".gitlet/commit/"+ message);
        r.mkdirs();
        for (File x: files) {
            String filename = x.toPath().toString().substring(16);
            //System.out.println(x.getName());
            //System.out.println("HIIIII " + p.toString().substring(16, p.toString().length()));
            Path file = Paths.get(filename);
            Path tofile = Paths.get(".gitlet/commit/" + message + "/" + filename);
            File g = new File(tofile.toString());
            File a = new File(file.toString());
            String f = Utils.readContentsAsString(a);
            Utils.writeContents(g, f);
            //Files.copy(file, tofile, StandardCopyOption.REPLACE_EXISTING);
            fil.add(x);;
        }

        Commit a = new Commit();
        a.init(this, message, Utils.sha1(message + all + now.toString() +
                null), now.toString(), fil, _mostrecent);
        _mostrecent = a;
        fileOut = new FileOutputStream(".gitlet/mostrecent");
        objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(_mostrecent);

        FileInputStream fi = new FileInputStream("branches/" + curr.getName());
        ObjectInputStream oi = new ObjectInputStream(fi);
        curr = (Branch) oi.readObject();
        //System.out.println("HITS HERE HITS HERE " + curr.getCsha1s());
        curr.addCsha1(a);
        File f = new File("branches/" + curr.getName());
        Utils.writeObject(f, curr);

        //System.out.println("HITS HERE HITS HERE " + curr.getCsha1s());

    }

    void status() {
        System.out.println("=== Branches ===");
        for (Branch b : branches) {
            if (b.getName().equals(curr.getName())) {
                System.out.println("*" + b.getName());
            } else {
                System.out.println(b.getName());
            }
        }
        System.out.println();
        File f = new File(".gitlet/staging");
        f.mkdirs();
        System.out.println("=== Staged Files ===");
        for (File c : f.listFiles()) {
            System.out.println(c.getName());
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit");
        for (File d : f.listFiles()) {
            if (!new File(d.getName()).exists()) {
                System.out.println(d.getName() + " (deleted)");
                continue;
            }
            String dcont = Utils.readContentsAsString(d);
            String dcur = Utils.readContentsAsString(new File(d.getName()));
            if (!dcont.equals(dcur)) {
                System.out.println(d.getName() + " (modified)");
            }
        }
        System.out.println();
        System.out.println("=== Untracked Files ===");
    }



    //serialize EVERYTHING







    private String globallog;


}
