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
                //System.out.println("HITS HERE HITS HERE ");
                if (file.getName().equals(filename)) {
                    File cur = new File(file.getName());
                    if (cur.exists()) {
                        File m = new File("tracking/" +curr.getName() + "/" + file.getName());
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

        File tr = new File("tracking/" + k.getName());
        tr.mkdirs();
        for (File f : tr.listFiles()) {
            String s = Utils.readContentsAsString(f);
            Utils.writeContents(new File(f.getName()), s);
        }


        Path a = Paths.get(".gitlet/commit/" + c.getMessage());
        File x = new File(a.toString());
        x.mkdirs();
        ArrayList<File> u = new ArrayList<>();

        if (curr.latestcommit().getFiles() != null) {
            int ke = curr.latestcommit().getFiles().size();
            for (File f : curr.latestcommit().getFiles()) {
                File r = new File(f.getName());
                if (r.exists() && !u.contains(r)) {
                    File n = new File(r.getName());
                    //System.out.println("THIS IS THE PATH " + n.toPath());
                    n.delete();
                    assert curr.latestcommit().getFiles().size() == ke;
                    //r.delete();
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
        //b.addCsha1(curr.latestcommit());
        b.setSplitpoint(curr.latestcommit());
        Utils.writeObject(new File("branches/" + branchname), b);
        File dir = new File(".gitlet/staging/" + branchname);
        dir.mkdirs();
        branches.add(b);
        b.addCsha1(curr.latestcommit());
        curr = b;

        clearstaged();
        File tr = new File("tracking/" + curr.getName());
        tr.mkdirs();


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
        File k = new File(message);
        k.mkdirs();
        File mod = new File("modified/" + curr.getName());
        mod.mkdirs();
        for (File x: files) {
            String filename = x.getName();
            String content = Utils.readContentsAsString(x);
            File kr = new File(message + "/" + filename);
            Utils.writeContents(kr, content);
            fil.add(kr);
            File modi = new File("modified/" + curr.getName() + "/" + filename);

            Path file = Paths.get(".gitlet/staging/" + curr.getName() + "/" + filename);
            Path tofile = Paths.get(".gitlet/commit/" + message + "/" + filename);
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


    void merge(String branchname) throws IOException, ClassNotFoundException {
        File [] staging = staged.listFiles();
        if (staging.length != 0) {
            throw new GitletException("You have uncommitted changes.");
        }

        FileInputStream fir = new FileInputStream("branches/" + branchname);
        ObjectInputStream o = new ObjectInputStream(fir);
        Branch cur = (Branch) o.readObject();
        FileInputStream fore = new FileInputStream("branches/" + curr.getName());
        ObjectInputStream or = new ObjectInputStream(fore);
        curr = (Branch) or.readObject();

        if (cur == null) {
            throw new GitletException("A branch with that name does not exist.");
        } if (cur.getName().equals(curr.getName())) {
            throw new GitletException("Cannot merge a branch with itself.");
        }
       if (cur.latestcommit().equals(curr.latestcommit())) {
         throw new GitletException("Given branch is an ancestor of the current branch.");
        }
        Commit c = cur.latestcommit();
        Commit d = cur.getSplitpoint();
        Commit e = curr.latestcommit();

        //Any files that were not present at the split point and are
        // present only in the given branch should be checked out and staged.
        for (File f : c.getFiles()) {
            if (!contains(d, f)) {
                checkoutversionfile(c.getCommitsha1(), f.getName());
                File stager = new File("staging/" + curr.getName());
                stager.mkdirs();
                File stager1 = new File("staging/" + curr.getName() + f.getName());
                String fe = Utils.readContentsAsString(f);
                Utils.writeContents(stager1, fe);
            }
        }

        Commit r = null;
        if (d.getParent() != null) {
            r = d.getParent();
        }

        //Any files present at the split point, unmodified in the current branch,
        // and absent in the given branch should be removed (and untracked).
        for (File f : d.getFiles()) { //present at split point
            if (unmodified(e, f.getName())) { //absent in current branch
                if (unmodified(c, f.getName())) { //absent in given branch
                    rm(f.getName());
                }
            }
        }

        //Any files present at the split point, unmodified in the given branch,
        // and absent in the current branch should remain absent.


        //Any files modified in different ways in the current and given branches are in conflict.
        // "Modified in different ways" can mean that the contents of both are changed and different
        // from other, or the contents of one are changed and the other file is deleted, or the file was absent
        // at the split point and has different contents in the given and current branches.
        // In this case, replace the contents of the conflicted file with
        File mod = new File("modified/" + cur.getName());
        mod.mkdirs();
        File mod2 = new File("modified/" + curr.getName());
        mod2.mkdirs();
        for (File f : mod.listFiles()) {
            for (File j : mod2.listFiles()) {
                if (f.getName().equals(j.getName())) {

                }
            }
        }


        //if a file is "modified in the given branch since the split point"
        // this means the version of the file as it exists in the commit at
        // the front of the given branch has different content from the version of the file at the split point.
        //should be changed to their versions in the given branch
       for (File i : c.getFiles()) {
           for (File j : d.getFiles()) {
               if (i.getName().equals(j.getName())) {
                   String is = Utils.readContentsAsString(i);
                   String js = Utils.readContentsAsString(j);
                   if (!is.equals(js)) {
                       File stagers = new File(".gitlet/staging/" + curr.getName() + "/" + i.getName());
                        File correct = new File(i.getName());
                        Utils.writeContents(correct, js);
                        Utils.writeContents(stagers, js);
                   }
               }
           }
       }




    }

    boolean unmodified(Commit e, String filename) {
        Commit temp = e;
        while (temp != null && !temp.getMessage().equals("initial commit")) {
            for (File f : temp.getFiles()) {
                if (f.getName().equals(filename)) {
                    return false;
                }
            }
            temp = temp.getParent();
        }
        return true;
    }


    boolean contains(Commit d, File f) {
        if (d.getFiles() == null) {
            return true;
        }
        for (File h : d.getFiles()) {
            if (h.getName().equals(f.getName())) {
                return true;
            }
        }
        return false;
    }










    private String globallog;


}
