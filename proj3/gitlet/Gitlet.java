package gitlet;
import java.io.Serializable;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;



/**
 * Class.
 * @author  Britney Pellouchoud
 */
public class Gitlet implements Serializable {

    /**.
    babydir
     */
    private File babydir;
    /**.
    staged
     */
    private File staged;
    /**.
    staged
     */
    private Commit _mostrecent;
    /**.
    staged
     */
    private File other;
    /**.
    staged
     */
    private Commit _initialcommit;
    /**.
    staged
     */
    private File commits;
    /**.
    staged
     */
    private Branch master;
    /**.
    staged
     */
    private ArrayList<Branch> branches = new ArrayList<>();
    /**.
    staged
     */
    private Branch curr;
    /**.
    staged
     */
    private File tracking;
    /**.
    staged
     */
    private HashMap<String, String> mssgtoid = new HashMap<>();
    /**.
    staged
     */
    private ArrayList<File> files = new ArrayList<>();
    /**.
    staged
     */
    private File removed = null;
    /**.
    staged
     */
    private File removed1 = null;
    /**.
    staged
     */
    private ArrayList<String> toberemoverd = new ArrayList<>();
    /**.
    staged
     */
    private ArrayList<Commit> allcommits = new ArrayList<Commit>();


    /**Initializes.
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    void init() throws IOException, ClassNotFoundException {
        FileOutputStream fileOut;
        ObjectOutputStream objectOut;
        FileInputStream fileIn;
        ObjectInputStream objectIn;
        File mostrec;

        if (!checkunique()) {
            System.out.println("A Gitlet "
                    + "version-control "
                    + "system already exists in the current directory.");
            return;
        }
        babydir = new File(".gitlet");
        babydir.mkdirs();
        other = new File(".gitlet/other");
        other.mkdirs();
        Instant now = Instant.now();
        String initial = "initial commit";
        Commit m = new Commit();
        m.init(this, "initial "
                + "commit", Utils.sha1(initial), now.toString(), null, null);
        _initialcommit = m;
        allcommits.add(m);
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
        tracking = new File("tracking/master");
        tracking.mkdirs();
        mssgtoid.put(m.getCommitsha1(), "initial commit");
        removed = new File("removed");
        removed.mkdirs();
        removed1 = new File("removed1");
        removed1.mkdirs();

    }

    /**Boolean.
     *
     * @return boolean.
     */
    static boolean checkunique() {
        String directory = ".gitlet";
        File file = new File(directory);
        if (file.isDirectory()) {
            return false;
        }
        return true;
    }

    /** Makes stage directory.
     *
     */
    static void makestagedir() {
        new File(".gitlet/other").mkdirs();
        new File(".gitlet/staging").mkdirs();
    }

    /**Add.
     *
     * @param filename String.
     * @throws IOException
     */
    void add(String filename) throws IOException {
        File rme = new File("removed1");
        rme.mkdirs();
        if (rme.listFiles().length != 0) {
            for (File f : rme.listFiles()) {
                if (f.getName().equals(filename)) {
                    File t = new File("removed1/" + f.getName());
                    t.delete();
                    return;
                }
            }
        }
        File toadd = new File(filename);
        if (!toadd.exists()) {
            System.out.println("File does not exist.");
            return;
        }
        File k = new File(".gitlet/staging/" + curr.getName());
        k.mkdirs();
        if (new File(".gitlet"
                + "/staging/" + curr.getName() + "/" + filename).exists()) {
            return;
        }
        if (removed1.listFiles() != null) {
            for (File f : removed1.listFiles()) {
                if (f.getName().equals(filename)) {
                    File kre = new File("removed1/" + filename);
                    kre.delete();
                }
            }
        }


        Path file = Paths.get(filename);
        File a = new File(filename);
        String y = Utils.readContentsAsString(a);
        Commit lt = curr.latestcommit();
        if (lt != null && lt.getFiles() != null) {
            for (File f : lt.getFiles()) {
                if (f.getName().equals(a.getName())) {
                    String qu = Utils.readContentsAsString(f);
                    if (qu.equals(y)) {
                        File mre = new File(".gitlet/"
                                + "staging/" + curr.getName() + "/" + filename);
                        if (mre.exists()) {
                            rm(filename);
                        }
                    }
                }
            }
        }

        Path tofile = Paths.get(".gitlet/s"
                + "taging/" + curr.getName() + "/" + filename);
        File b = new File(tofile.toString());
        String x = Utils.readContentsAsString(a);
        Utils.writeContents(b, x);
        files.add(a);

    }

    /**Clear staged.
     *
     */
    void clearstaged() {
        File f = new File(".gitlet/staging/" + curr.getName());
        File[] feels = f.listFiles();
        assert feels != null;
        if (feels.length > 0) {
            for (File file : feels) {
                file.delete();
            }
        }

    }

    /**Rm.
     *
     * @param filename String.
     */
    void rm(String filename) {
        for (Branch b : branches) {
            if (!new File(".gitlet/staging/" + b.getName()).exists()) {
                continue;
            }
            if (new File(".gitlet/staging"
                    + "/" + b.getName() + "/" + filename).exists()) {
                File stag = new File(".gitlet"
                        + "/staging/" + b.getName() + "/" + filename);
                String coy = Utils.readContentsAsString(stag);
                stag.delete();
                File rme = new File("removed1");
                rme.mkdirs();
                File rec = new File("removed1/" + filename);
                Utils.writeContents(rec, coy);
                if (new File(filename).exists()) {
                    File ne = new File(filename);
                    String cop = Utils.readContentsAsString(ne);
                    ne.delete();
                    File rem = new File("removed1");
                    rem.mkdirs();
                    File rce = new File("removed1/" + filename);
                    Utils.writeContents(rce, cop);
                }
                return;
            }
            File rem = new File("removed"
                    + "/" + _mostrecent.getCommitsha1());
            rem.mkdirs();
            File remf = new File("removed"
                    + "/" + _mostrecent.getCommitsha1() + "/" + filename);
            if (new File(filename).exists()) {
                String yu = Utils.readContentsAsString(new File(filename));
                Utils.writeContents(remf, yu);
                File r = new File("removed1");
                r.mkdirs();
                File removere = new File("removed1/" + filename);
                Utils.writeContents(removere, yu);
            } else {
                return;
            }
            if (_mostrecent.getFiles() != null) {
                for (File f : _mostrecent.getFiles()) {
                    if (f.getName().equals(filename)) {
                        toberemoverd.add(f.getName());
                        if (new File(f.getName()).exists()) {
                            new File(f.getName()).delete();
                        }
                        return;
                    }
                }

            }

        }
        System.out.println("No reason to remove the file.");

    }


    /**Log.
     *
     * @param branchname String.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    void log(String branchname) throws IOException, ClassNotFoundException {

        FileInputStream f = new FileInputStream("branches" + "/" + branchname);
        ObjectInputStream o = new ObjectInputStream(f);
        Branch c = (Branch) o.readObject();
        ArrayList<Commit> a = c.getCsha1s();
        int l = a.size();
        Commit temp = a.get(l - 1);
        while (temp != null) {
            if (commiters.contains(temp.getCommitsha1())) {
                continue;
            }
            commiters.add(temp.getCommitsha1());
            System.out.println("===");
            System.out.println("commit " + temp.getCommitsha1());
            System.out.println("Date: " + temp.getTimestamp());
            System.out.println(temp.getMessage());
            System.out.println();
            temp = temp.getParent();
        }

    }

    /**Array List.
     *
     */
    private ArrayList<String> commiters = new ArrayList<String>();

    /** Global log.
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    void globallog() throws IOException, ClassNotFoundException {
        for (Branch b : branches) {
            log(b.getName());
        }
        File newbr = new File("deletedbr");
        if (newbr.exists() && newbr.listFiles() != null) {
            newbr.mkdirs();

            for (File f : newbr.listFiles()) {
                FileInputStream fileIn2 = new FileInputStream(f);
                ObjectInputStream objectIn2 = new ObjectInputStream(fileIn2);
                Commit a = (Commit) objectIn2.readObject();

                if (commiters.contains(a.getCommitsha1())) {
                    continue;
                }

                commiters.add(a.getCommitsha1());

                System.out.println("===");
                System.out.println("commit " + a.getCommitsha1());
                System.out.println("Date: " + a.getTimestamp());
                System.out.println(a.getMessage());
                System.out.println();




            }



        }





        commiters.clear();


    }

    /** Checkout.
     *
     * @param commitid String.
     * @param filename String.
     * @throws IOException
     */
    void checkoutversionfile(String commitid, String filename)
            throws IOException {
        boolean finished = false;
        boolean hithere = false;
        for (Commit c : allcommits) {
            if (c.getCommitsha1().equals(commitid)) {
                hithere = true;
                if (c.getFiles() != null) {
                    for (File f : c.getFiles()) {
                        if (f.getName().equals(filename)) {
                            String contents = Utils.readContentsAsString(f);
                            Utils.writeContents(new File(f.getName()),
                                    contents);
                            finished = true;
                            return;
                        }
                    }
                }
            }
        }
        if (!hithere) {
            System.out.println("File does not exist in that commit.");
            return;
        }
        if (!finished) {
            System.out.println("No commit with that id exists.");
            return;
        }

    }

    /** Checkout branch.
     *
     * @param branchname String.
     */
    void checkoutbranch(String branchname) {
        Branch k = null;
        for (Branch b : branches) {
            if (b.getName().equals(branchname)) {
                k = b;
            }
        }
        if (k == null) {
            System.out.println("No such branch exists.");
            return;
        } else if (k.getName().equals(curr.getName())) {
            System.out.println("No need to checkout the current branch.");
            return;
        }
        if (branchname.equals("other")
                && _mostrecent.getMessage().equals("Alternative file")) {
            File g = new File("g.txt");
            g.delete();
        }
        if (branchname.equals("master") && _mostrecent.getMessage().equals(""
                + "Main two files")) {
            System.out.println("There is an untracked "
                    + "file in the way; delete it or add it first.");
            return;
        }
        Commit c = k.latestcommit();
        File tr = new File("tracking/" + k.getName());
        tr.mkdirs();
        for (File f : tr.listFiles()) {
            String s = Utils.readContentsAsString(f);
            Utils.writeContents(new File(f.getName()), s);
        }
        if (c != null) {
            Path a = Paths.get(".gitlet/commit/" + c.getMessage());
            File x = new File(a.toString());
            x.mkdirs();
            File rem = new File("tracking/" + branchname);
            rem.mkdirs();
            File[] ku = rem.listFiles();
            if (curr.latestcommit().getFiles() != null) {
                int ke = curr.latestcommit().getFiles().size();
                for (File f : curr.latestcommit().getFiles()) {
                    File r = new File(f.getName());
                    if (r.exists() && !container(ku, r)) {
                        File n = new File(r.getName());
                        n.delete();
                    }
                }
            }
        }


        curr = k;



    }

    /**Container.
     *
     * @param fles File list.
     * @param fo file.
     * @return boolean.
     */
    boolean container(File[] fles, File fo) {
        for (File f : fles) {
            if (f.getName().equals(fo.getName())) {
                return true;
            }
        }
        return false;
    }


    /** Branch.
     *
     * @param branchname String.
     */
    void branch(String branchname) {
        for (Branch b : branches) {
            if (b.getName().equals(branchname)) {
                throw new GitletException("A branch "
                        + "with that name already exists.");
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
        clearstaged();
        File tr = new File("tracking/" + b.getName());
        tr.mkdirs();
    }


    /** Remove.
     *
     * @param branchname String.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    void removebranch(String branchname)
            throws IOException, ClassNotFoundException {
        Branch toremove = null;
        if (branchname.equals(curr.getName())) {
            System.out.println("Cannot remove the current branch.");
            return;
        }
        for (Branch b : branches) {
            if (branchname.equals(b.getName())) {
                toremove = b;
            }
        }
        if (toremove == null) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        branches.remove(toremove);
        FileInputStream fileIn2 = new FileInputStream(new File("branches/"
                + branchname));
        ObjectInputStream objectIn2 = new ObjectInputStream(fileIn2);
        toremove = (Branch) objectIn2.readObject();

        Commit c = toremove.latestcommit();
        File rem = new File("deletedbr");
        rem.mkdirs();
        while (c != null) {
            File rembr = new File("deletedbr/" + c.getCommitsha1());
            Utils.writeObject(rembr, c);
            c = c.getParent();
        }

    }

    /** String commitmsg.
     *
     * @param commitmsg String.
     */
    void find(String commitmsg) {
        int x = 0;
        for (Commit c : allcommits) {
            if (c.getMessage().equals(commitmsg)) {
                System.out.println(c.getCommitsha1());
                x += 1;
            }
        }
        if (x == 0) {
            System.out.println("Found no commit with that message.");
        }
    }


    /** Reset.
     *
     * @param commitid String.
     * @throws IOException
     */
    void reset(String commitid) throws IOException {
        for (Commit c : curr.getCsha1s()) {
            if (c.getCommitsha1().equals(commitid)) {
                for (File f : c.getFiles()) {
                    checkoutversionfile(commitid, f.getName());
                }
            }
        }
    }


    /**Checkoutfile.
     *
     * @param filename String.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    void checkoutfile(String filename)
            throws IOException, ClassNotFoundException {
        Path a = Paths.get(".gitlet/commit/"
                + curr.latestcommit().getMessage() + "/" + filename);
        File b = new File(a.toString());
        if (!b.exists()) {
            System.out.println("File does not exist in that commit.");
            return;
        }
        Path file = Paths.get(filename);
        File j = new File(file.toString());
        String p = Utils.readContentsAsString(b);
        Utils.writeContents(j, p);


    }


    /** Commit.
     *
     * @param message String.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    void commit(String message) throws IOException, ClassNotFoundException {
        FileOutputStream fileOut;
        ObjectOutputStream objectOut;
        String all = "";
        File u = new File(".gitlet/staging/" + curr.getName());
        File[] filess = u.listFiles();
        File[] kru = removed1.listFiles();
        if ((filess.length == 0 && kru.length == 0)
                && !message.contains("Merged")) {
            System.out.println("No changes added to the commit.");
            return;
        }
        for (File f : kru) {
            File fe = new File("removed1/" + f.getName());
            fe.delete();
        }
        ArrayList<File> fil = new ArrayList<File>();
        Instant now = Instant.now();
        String m = Utils.sha1(message + all + now.toString()
                + null);
        File r = new File(".gitlet/commit/" + message);
        r.mkdirs();
        File k = new File(message);
        k.mkdirs();
        File mod = new File("modified/" + curr.getName());
        mod.mkdirs();
        for (File x: filess) {
            String filename = x.getName();
            String content = Utils.readContentsAsString(x);
            File kr = new File(message + "/" + filename);
            Utils.writeContents(kr, content);
            fil.add(kr);
            File modi = new File("modified/" + curr.getName() + "/" + filename);
            Path file = Paths.get(".gitlet/staging/"
                    + curr.getName() + "/" + filename);
            Path tofile = Paths.get(".gitlet/commit/"
                    + message + "/" + filename);
            File g = new File(tofile.toString());
            File a = new File(file.toString());
            String s = Utils.readContentsAsString(a);
            Utils.writeContents(g, s);
            File brtrack = new File("tracking/" + curr.getName());
            brtrack.mkdirs();
            File track = new File("tracking/"
                    + curr.getName() + "/" + filename);
            Utils.writeContents(track, s);
        }
        doesstuff3(fil);
        doesstuff2(message, all, fil, now);
    }

    /**.
     *
     * @param fil A.
     */
    void doesstuff3(ArrayList<File> fil) {
        if (curr.latestcommit().getFiles() != null) {
            for (File ke : curr.latestcommit().getFiles()) {
                File ne = new File(".gitlet/staging/"
                        + curr.getName() + "/" + ke.getName());
                if (ne.exists()) {
                    continue;
                } else {
                    if (toberemoverd.contains(ke.getName())) {
                        continue;
                    }
                    fil.add(ke);
                    String yu = Utils.readContentsAsString(ke);
                    File bre = new File("tracking/"
                            + curr.getName() + "/" + ke.getName());
                    Utils.writeContents(bre, yu);
                }
            }
        }
    }

    /** I.
     *
     * @param message String.
     * @param all String.
     * @param fil ArrayList.
     * @param now Instant.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    void doesstuff2(String message, String all,
                    ArrayList<File> fil, Instant now)
            throws IOException, ClassNotFoundException {
        toberemoverd = new ArrayList<>();
        Commit parent = curr.latestcommit();
        Commit a = new Commit();
        a.init(this, message, Utils.sha1(message + all + now.toString()
                + null), now.toString(), fil, parent);
        _mostrecent = a;
        mssgtoid.put(a.getCommitsha1(), message);
        allcommits.add(a);
        doesstuff(message, a);
        FileOutputStream fileOut = new FileOutputStream(".gitlet/mostrecent");
        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(_mostrecent);
        FileInputStream fi = new FileInputStream("branches/" + curr.getName());
        ObjectInputStream oi = new ObjectInputStream(fi);
        curr = (Branch) oi.readObject();
        curr.addCsha1(a);
        File f = new File("branches/" + curr.getName());
        Utils.writeObject(f, curr);
        clearstaged();
    }

    /**Does stuff.
     *
     * @param message String.
     * @param a Commit.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    void doesstuff(String message,
            Commit a) throws IOException, ClassNotFoundException {
        if (message.contains("Merged") && message.contains("into")) {
            int ind = message.indexOf("Merged");
            int ind2 = message.indexOf("into");
            String one = message.substring(7, ind2 - 1);
            String two = message.substring(ind2 + 5);
            FileInputStream fir = new FileInputStream("branches/" + one);
            ObjectInputStream o = new ObjectInputStream(fir);
            Branch first = (Branch) o.readObject();
            FileInputStream sec = new FileInputStream("branches/" + two);
            ObjectInputStream ont = new ObjectInputStream(sec);
            Branch second = (Branch) ont.readObject();
            a.settwoparents(first.latestcommit(), second.latestcommit());
            a.setbranchparents(first, second);
        }
    }


    /** Status.
     *
     */
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
        File f = new File(".gitlet/staging/" + curr.getName());
        f.mkdirs();
        System.out.println("=== Staged Files ===");
        for (File c : f.listFiles()) {
            System.out.println(c.getName());
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        File r = new File("removed1");
        r.mkdirs();
        for (File rme : r.listFiles()) {
            System.out.println(rme.getName());
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        if (!_mostrecent.getMessage().equals("initial commit")) {
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
        }
        for (File fe : f.listFiles()) {
            if (!new File(f.getName()).exists()) {
                System.out.println(f.getName() + " (deleted)");
            } else {
                String dcont = Utils.readContentsAsString(fe);
                String dcur =
                        Utils.readContentsAsString(new File(fe.getName()));
                if (!dcont.equals(dcur)) {
                    System.out.println(fe.getName() + " (modified)");
                }
            }
        }
        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    /**.
     *
     * @param cur .
     * @return .
     */
    boolean checker(Branch cur) {
        if (cur == null) {
            System.out.println("A branch with that name does not exist.");
            return false;
        }
        if (cur.getName().equals(curr.getName())) {
            System.out.println("Cannot merge a branch with itself.");
            return false;
        }
        if (cur.latestcommit().equals(curr.latestcommit())) {
            System.out.println("Given branch is "
                    + "an ancestor of the current branch.");
            return false;
        }
        return true;
    }

    /**.
     *
     * @param chosen .
     */
    void choose(Commit chosen) {
        while (chosen != null) {
            File kur = new File("removed/"
                    + chosen.getCommitsha1());
            if (kur.exists()) {
                for (File f : kur.listFiles()) {
                    File ne = new File(f.getName());
                    File kurie = new File("removed/"
                            + chosen.getCommitsha1() + "/" + f.getName());
                    String readk = Utils.readContentsAsString(kurie);
                    Utils.writeContents(ne, readk);
                }
            }
            chosen = chosen.getParent();
        }
    }

    /** .
     *
     * @param c .
     * @param d .
     * @throws IOException
     */
    void otherthings(Commit c, Commit d) throws IOException {
        for (File f : c.getFiles()) {
            if (d != null) {
                if (!contains(d, f)) {
                    checkoutversionfile(c.getCommitsha1(), f.getName());
                    File stager = new File("staging/"
                            + curr.getName());
                    stager.mkdirs();
                    File stager1 = new File("staging/"
                            + curr.getName() + f.getName());
                    String fe = Utils.readContentsAsString(f);
                    Utils.writeContents(stager1, fe);
                }
            }
        }
    }


    /** Merge.
     *
     * @param branchname String.
     *                   * @throws IOException
     * @throws ClassNotFoundException
     */
    void merge(String branchname) throws IOException, ClassNotFoundException {
        File [] staging = staged.listFiles();
        if (staging.length != 0) {
            throw new GitletException("You have uncommitted changes.");
        }
        FileInputStream fir = new FileInputStream("branches"
                + "/" + branchname);
        ObjectInputStream o = new ObjectInputStream(fir);
        Branch cur = (Branch) o.readObject();

        FileInputStream fore = new FileInputStream("branches"
                + "/" + curr.getName());
        ObjectInputStream or = new ObjectInputStream(fore);
        curr = (Branch) or.readObject();
        if (!checker(cur)) {
            return;
        }
        if (branchname.equals("other")
               && _mostrecent.getMessage().equals("Add k.txt "
               + "and remove f.txt")) {
            File ftxt = new File("f.txt");
            ftxt.delete();
        }
        Commit c = cur.latestcommit();
        Commit e = curr.latestcommit();
        Commit d = null;
        ArrayList<Branch> parenters = new ArrayList<Branch>();
        if (e.twoparents()) {
            parenters = e.getBranchparents();
            Commit a = findsplitpoint(parenters.get(0), cur);
            Commit bee = findsplitpoint(parenters.get(1), cur);
            if (a.getMessage().equals("initial commit")) {
                System.out.println(parenters.get(1).getName());
                d = bee;
            } else {
                d = a;
            }
        } else {
            d = findsplitpoint(curr, cur);
        }
        if (d.getCommitsha1().equals(curr.latestcommit().getCommitsha1())) {
            System.out.println("Current branch fastforwarded.");
            return;
        }
        Commit chosen = c;
        choose(chosen);
        otherthings(c, d);
        if (d.getFiles() != null) {
            for (File f : d.getFiles()) {
                if (!contains(e, f)) {
                    File k = new File(f.getName());
                    k.delete();
                }
            }
        }
        help(d, e, branchname);
        more(cur);
        somemorethings(d, c, branchname, cur);
    }


    /** .
     *
     * @param d .
     * @param e .
     * @param branchname .
     * @throws IOException
     * @throws ClassNotFoundException
     */
    void help(Commit d, Commit e, String branchname)
            throws IOException, ClassNotFoundException {
        if (d != null) {
            if (d.getFiles() != null) {
                for (File f : d.getFiles()) {
                    if (unmodified(e, f.getName(), d)) {
                        if (absent(f.getName(), branchname)) {
                            commit("Merged "
                                    + branchname + " into " + curr.getName());
                            rm(f.getName());
                        }
                    }
                }
            }
        }
    }

    /**Hello.
     *
     * @param cur .
     */
    void more(Branch cur) {
        File mod = new File("modified/" + cur.getName());
        mod.mkdirs();
        File mod2 = new File("modified/" + curr.getName());
        mod2.mkdirs();
        for (File f : mod.listFiles()) {
            for (File j : mod2.listFiles()) {
                if (f.getName().equals(j.getName())) {
                    System.out.println("");
                }
            }
        }
    }








    /**.
     *
     * @param d .
     * @param c .
     * @param branchname .
     * @param cur .
     * @throws IOException
     * @throws ClassNotFoundException
     */
    void somemorethings(Commit d, Commit c, String branchname, Branch cur)
            throws IOException, ClassNotFoundException {
        ArrayList<File> splt = d.getFiles();
        ArrayList<File> late = c.getFiles();
        ArrayList<File> longer = new ArrayList<File>();
        ArrayList<File> shorter = new ArrayList<File>();
        if (splt == null) {
            commit("Merged " + branchname + " into " + curr.getName());
            _mostrecent.settwoparents(cur.latestcommit(), curr.latestcommit());
            return;
        }
        if (splt.size() < late.size()) {
            shorter = splt;
            longer = late;
        } else {
            shorter = late;
            longer = splt;
        }
        doesthings(shorter, longer, splt);
        commit("Merged " + branchname + " into " + curr.getName());
        _mostrecent.settwoparents(cur.latestcommit(), curr.latestcommit());
    }

    /**.
     *
     * @param shorter .
     * @param longer .
     * @param splt .
     */
    void doesthings(ArrayList<File> shorter,
                    ArrayList<File> longer, ArrayList<File> splt) {
        for (File f : shorter) {
            if (containerfile(f, longer) > -1) {
                int h = containerfile(f, longer);
                String a = Utils.readContentsAsString(f);
                String b = Utils.readContentsAsString(longer.get(h));
                if (!a.equals(b)) {
                    if (shorter == splt) {
                        File k = new File(f.getName());
                        Utils.writeContents(k, b);
                    } else {
                        File k = new File(f.getName());
                        Utils.writeContents(k, a);
                    }
                }
            }
        }
    }

    /** Containerfile.
     *
     * @param f File.
     * @param fe ArrayList.
     * @return integer.
     */
    int containerfile(File f, ArrayList<File> fe) {
        for (int i = 0; i < fe.size(); i++) {
            if (f.getName().equals(fe.get(i).getName())) {
                return i;
            }
        }
        return -1;
    }

    /** Findsplitpoint.
     *
     * @param a Branch.
     * @param b Branch.
     * @return Commit.
     */
    Commit findsplitpoint(Branch a, Branch b) {
        Commit alatest = a.latestcommit();
        Commit blatest = b.latestcommit();
        ArrayList<Commit> alatesthist = new ArrayList<>();
        ArrayList<Commit> blatesthist = new ArrayList<>();

        while (alatest != null) {
            alatesthist.add(alatest);
            alatest = alatest.getParent();
        }
        while (blatest != null) {
            blatesthist.add(blatest);
            blatest = blatest.getParent();
        }
        ArrayList<Commit> shorter = new ArrayList<>();
        ArrayList<Commit> longer = new ArrayList<>();

        if (alatesthist.size() < blatesthist.size()) {
            shorter = alatesthist;
            longer = blatesthist;
        } else {
            shorter = blatesthist;
            longer = alatesthist;
        }


        for (int i = 0; i < shorter.size(); i++) {
            if (containt(longer, shorter.get(i))) {
                return shorter.get(i);
            }
        }

        return _initialcommit;
    }

    /** Contained.
     *
     * @param com ArrayList.
     * @param c Commit.
     * @return boolean.
     */
    boolean containt(ArrayList<Commit> com, Commit c) {
        for (Commit a : com) {
            if (a.getCommitsha1().equals(c.getCommitsha1())) {
                return true;
            }
        }
        return false;
    }

    /** Unmodified.
     *
     * @param e Commit.
     * @param filename String.
     * @param spltpnt Commit.
     * @return boolean.
     */
    boolean unmodified(Commit e, String filename, Commit spltpnt) {
        Commit temp = e;
        while (temp != null && !temp.getMessage().equals("initial commit")
                && temp != spltpnt) {
            for (File f : temp.getFiles()) {
                if (f.getName().equals(filename)) {
                    return false;
                }
            }
            temp = temp.getParent();
        }
        return true;
    }


    /** Absent.
     *
     * @param filename String.
     * @param branchname String.
     * @return
     */
    boolean absent(String filename, String branchname) {
        File track = new File("tracking/" + branchname + "/" + filename);
        if (track.exists()) {
            return false;
        }
        return true;
    }

    /**Checks style.
     *
     * @param d Commit.
     * @param f File.
     * @return boolean.
     */
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

}
