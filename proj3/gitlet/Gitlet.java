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
    private File tracking;
    private HashMap<String, String> mssgtoid = new HashMap<>();
    private ArrayList<File> files = new ArrayList<>();


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
        other = new File(".gitlet/other");
        other.mkdirs();
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
        staged = new File(".gitlet/staging/" + master.getName());
        staged.mkdirs();
        commits = new File(".gitlet/commit");
        commits.mkdirs();
        _mostrecent = m;
        fileOut = new FileOutputStream(".gitlet/mostrecent");
        objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(_mostrecent);
        tracking = new File("tracking");
        tracking.mkdirs();
        mssgtoid.put(m.getCommitsha1(), "initial commit");
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
        File k = new File(".gitlet/staging/" + curr.getName());
        k.mkdirs();
        if (new File(".gitlet/staging/" + curr.getName() + "/" + filename).exists()) {
           return;
       }

        Path file = Paths.get(filename);
        File a = new File(filename);
        Path tofile = Paths.get(".gitlet/staging/" + curr.getName() + "/" + filename);
        File b = new File(tofile.toString());
        String x = Utils.readContentsAsString(a);
        Utils.writeContents(b, x);
        files.add(a);
        //Path secfile = Paths.get(".gitlet/other/" + filename);

        //Files.copy(file, tofile, StandardCopyOption.REPLACE_EXISTING);
        //Files.copy(file, secfile, StandardCopyOption.REPLACE_EXISTING);

        //assert new File(".gitlet/staging/filename" + sha1).exists();

    }

    void clearstaged() {
        File f = new File(".gitlet/staging/" + curr.getName());
        File[] files = f.listFiles();
        assert files != null;
        if (files.length > 0) {
            for (File file : files) {
                file.delete();
            }
        }

    }

    void rm(String filename) {
        //File staged = new File(".gitlet/staging/");
        //File[] stagedfiles = staged.listFiles();
        for (Branch b : branches) {
            File[] stagedfiles = new File(".gitlet/staging/" + b.getName()).listFiles();
            if (! new File(".gitlet/staging/" + b.getName()).exists()) {
                continue;
            }
            for (File file : stagedfiles) {
                if (file.getName().equals(filename)) {
                    File cur = new File(file.getName());
                    if (cur.exists()) {
                        File m = new File("tracking/" + file.getName());
                        String s = Utils.readContentsAsString(cur);
                        Utils.writeContents(m, s);
                    }
                    cur.delete();

                    return;
                }
            }

        }
        if (new File(filename).exists()) {
            File del = new File(filename);
            del.delete();
            return;
        }


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
        mssg = mssgtoid.get(commitid);
        //System.out.println("MESSAGE " + mssg);

        /*
        while(temp != null) {
            File w = new File(".gitlet/commit/" + temp.getMessage());
            w.mkdirs();
            for (File i : w.listFiles()) {
                System.out.println("NAME " + i.getName());
                if (i.getName().equals(commitid)) {
                    mssg += temp.getMessage();
                    //System.out.println("THIS IS THE MESSAGE " + mssg);
                } else {
                    break;
                }
            }
            temp = temp.getParent();
        }

         */

        //".gitlet/commit/" + message + "/" + filename
        Path b = Paths.get(".gitlet/commit/" + mssg + "/" + filename);
        File j = new File(b.toString());
        String h = Utils.readContentsAsString(j); //text to copy
        //System.out.println("THIS IS ORIG " + h);

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
        ArrayList<File> fls = c.getFiles();

        if (fls != null) {
            for (File f : fls) {
                String m = Utils.readContentsAsString(f);
                Utils.writeContents(new File(f.getName()), m);
            }
        }




        Path a = Paths.get(".gitlet/commit/" + c.getMessage());
        File x = new File(a.toString());
        x.mkdirs();
        ArrayList<File> u = new ArrayList<>();

        if (curr.latestcommit().getFiles() != null) {
            for (File f : curr.latestcommit().getFiles()) {
                File r = new File(f.getName());
                if (r.exists() && !u.contains(r)) {
                    r.delete();
                }
            }
        }



        curr = k;
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
        b.setSplitpoint(curr.latestcommit());
        Utils.writeObject(new File("branches/" + branchname), b);
        File dir = new File(".gitlet/staging/" + branchname);
        dir.mkdirs();
        branches.add(b);
        curr = b;
        Commit t = curr.latestcommit();

        clearstaged();
        File tr = new File("tracking/" + curr.getName());
        tr.mkdirs();

        /*
        ArrayList<File> o = t.getFiles();
        if (o != null && o.size() != 0) {
            System.out.println(o);
        }

         */
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



    void reset(String commitid) throws IOException {
        for (Commit c : curr.getCsha1s()) {
            if (c.getCommitsha1().equals(commitid)) {
                for (File f : c.getFiles()) {
                    checkoutversionfile(commitid, f.getName());
                }
            }
        }
    }


    void checkoutfile(String filename) throws IOException, ClassNotFoundException {
        Path a = Paths.get(".gitlet/commit/" + _mostrecent.getMessage() + "/" + filename);
        File b = new File(a.toString());
        Path file = Paths.get(filename);
        File j = new File(file.toString());
        String p = Utils.readContentsAsString(b);
        Utils.writeContents(j, p);


    }


    void commit(String message) throws IOException, ClassNotFoundException {
        FileOutputStream fileOut;
        ObjectOutputStream objectOut;
        String all = "";
        File u = new File(".gitlet/staging/" + curr.getName());
        File[] files = u.listFiles();
        ArrayList<File> fil = new ArrayList<File>();
        Instant now = Instant.now();
        String m = Utils.sha1(message + all + now.toString() +
                null);
        File r = new File(".gitlet/commit/"+ message);
        r.mkdirs();
        for (File x: files) {
            String filename = x.getName();
            //System.out.println("THIS IS FILENAME " + filename);
            fil.add(x);

            Path file = Paths.get(".gitlet/staging/" + curr.getName() + "/" + filename);
            Path tofile = Paths.get(".gitlet/commit/" + message + "/" + filename);

            //Files.copy(t, tofile, StandardCopyOption.REPLACE_EXISTING);
            File g = new File(tofile.toString());
            File a = new File(file.toString());

            String s = Utils.readContentsAsString(a);
            Utils.writeContents(g, s);
            File brtrack = new File("tracking/" + curr.getName());
            brtrack.mkdirs();
            File track = new File("tracking/" + curr.getName() + "/" + filename);
            Utils.writeContents(track, s);

        }
        Commit a = new Commit();
        a.init(this, message, Utils.sha1(message + all + now.toString() +
                null), now.toString(), fil, _mostrecent);
        _mostrecent = a;
        mssgtoid.put(a.getCommitsha1(), message);



        fileOut = new FileOutputStream(".gitlet/mostrecent");
        objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(_mostrecent);
        FileInputStream fi = new FileInputStream("branches/" + curr.getName());
        ObjectInputStream oi = new ObjectInputStream(fi);
        curr = (Branch) oi.readObject();
        curr.addCsha1(a);
        File f = new File("branches/" + curr.getName());
        Utils.writeObject(f, curr);
        clearstaged();

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


    void merge(String branchname) throws IOException {
        File [] staging = staged.listFiles();
        if (staging.length != 0) {
            throw new GitletException("You have uncommitted changes.");
        }
        Branch cur = null;
        for (Branch b : branches) {
            if (b.getName().equals(branchname)) {
                cur = b;
                break;
            }
        }
        if (cur == null) {
            throw new GitletException("A branch with that name does not exist.");
        } if (cur.getName().equals(curr.getName())) {
            throw new GitletException("Cannot merge a branch with itself.");
        }
        Commit c = cur.latestcommit();
        Commit d = cur.getSplitpoint();
        for (File f : c.getFiles()) {
            File orig = new File(".gitlet/commit/" + c.getMessage() + "/" + f.getName());
            String m = Utils.readContentsAsString(orig);
            File version = new File(".gitlet/commit/" + d.getMessage() + "/" + f.getName());
            if (!version.exists()) {
                checkoutversionfile(d.getCommitsha1(), f.getName());
                File k = new File(".gitlet/staging/" + f.getName());
                String s = Utils.readContentsAsString(new File(f.getName()));
                Utils.writeContents(k, s);
                continue;
            }
            if (version.exists() && !f.exists() && ! new File(".gitlet/commit/" + curr.latestcommit().getMessage()
                    + "/" + f.getName()).exists()) {
                continue;
            }
            String a = Utils.readContentsAsString(version);
            if (!a.equals(m)) { //modified in the given branch since the split point but not modified in the current branch
                Utils.writeContents(version, m);
                File k = new File(".gitlet/staging/" + f.getName());
                String s = Utils.readContentsAsString(new File(f.getName()));
                Utils.writeContents(k, s);
            }
        }
        System.out.println("Merged " + branchname + " into " + curr.getName());

    }










    private String globallog;


}
