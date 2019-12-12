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
    private File removed = null;
    private File removed1 = null;
    private ArrayList<String> toberemoverd = new ArrayList<>();
    private ArrayList<Commit> allcommits = new ArrayList<Commit>();



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





    private static Commit pointer;

    void setPointer(Commit com) {
        pointer = com;
    }

    Commit getPointer() {
        return pointer;
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
        // If the current working version of the file is identical
        // to the version in the current commit, do not stage it
        // to be added, and remove it from the staging area if it is already there


        //If the file had been marked to be removed (see gitlet rm), delete that mark.

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
        if (new File(".gitlet/staging/" + curr.getName() + "/" + filename).exists()) {
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
                        File mre = new File(".gitlet/staging/"+curr.getName()+ "/" + filename);
                        if (mre.exists()) {
                            rm(filename);
                        }
                    }
                }
            }
        }

        Path tofile = Paths.get(".gitlet/staging/" + curr.getName() + "/" + filename);
        File b = new File(tofile.toString());
        String x = Utils.readContentsAsString(a);
        Utils.writeContents(b, x);
        files.add(a);

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
        for (Branch b : branches) {
            if (! new File(".gitlet/staging/" + b.getName()).exists()) {
                continue;
            }
            if (new File(".gitlet/staging/" + b.getName() + "/" + filename).exists()) {
                File stag = new File(".gitlet/staging/" + b.getName() + "/" + filename);
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
            File rem = new File("removed/" + _mostrecent.getCommitsha1());
            rem.mkdirs();
            File remf = new File("removed/" + _mostrecent.getCommitsha1() + "/" + filename);
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

    void log(String branchname) throws IOException, ClassNotFoundException {

        FileInputStream f = new FileInputStream("branches" + "/" + branchname);
        ObjectInputStream o = new ObjectInputStream(f);
        Branch c = (Branch) o.readObject();
        //System.out.println(c.getCsha1s().size());
        ArrayList<Commit> a = c.getCsha1s();
        int l = a.size();
        Commit temp = a.get(l - 1);
        //System.out.println("HITS HERE ");
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

    private ArrayList<String> commiters = new ArrayList<String>();

    void globallog() throws IOException, ClassNotFoundException {

        for (Branch b : branches) {
            log(b.getName());
            //System.out.println("BRANCH IS " + b.getName());
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

    /*
    Takes the version of the file as it exists in the head commit, the front of the current branch,
    and puts it in the working directory,
     overwriting the version of the file that's already there if there is one.
     The new version of the file is not staged.
     */

    void checkoutversionfile(String commitid, String filename) throws IOException {
        boolean finished = false;
        boolean hithere = false;
        for (Commit c : allcommits) {
            if (c.getCommitsha1().equals(commitid)) {
                hithere = true;
                if (c.getFiles() != null) {
                    for (File f : c.getFiles()) {
                        if (f.getName().equals(filename)) {
                            String contents = Utils.readContentsAsString(f);
                            Utils.writeContents(new File(f.getName()), contents);
                            finished = true;
                            return;
                        }
                    }
                }
            }
        }
        if (! hithere) {
            System.out.println("File does not exist in that commit.");
            return;
        }
        if (! finished) {
            System.out.println("No commit with that id exists.");
            return;
        }

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


        //If a working file is untracked in the current branch
        // and would be overwritten by the checkout, print
        // There is an untracked file in the way; delete it or
        // add it first. and exit; perform this check before doing anything else.

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


            //Any files that are tracked in the current branch
            // but are not present in the checked-out branch are deleted
            if (curr.latestcommit().getFiles() != null) {
                int ke = curr.latestcommit().getFiles().size();
                for (File f : curr.latestcommit().getFiles()) {
                    File r = new File(f.getName());
                    if (r.exists() && ! container(ku, r)) {
                        File n = new File(r.getName());
                        n.delete();
                    }
                }
            }
        }


        curr = k;



    }

    boolean container (File[] files, File file) {
        for (File f : files) {
            if (f.getName().equals(file.getName())) {
                return true;
            }
        }
        return false;
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
        //b.addCsha1(curr.latestcommit());
        //curr = b;

        clearstaged();
        File tr = new File("tracking/" + b.getName());
        tr.mkdirs();


    }


    void removebranch(String branchname) throws IOException, ClassNotFoundException {
        Branch toremove = null;
        if (branchname.equals(curr.getName())) {
            throw new GitletException("Cannot remove the current branch.");
        }
        for (Branch b : branches) {
            if (branchname.equals(b.getName())) {
                toremove = b;
            }
        }
        if (toremove == null) {
            throw new GitletException("A branch with that name does not exist.");
        }
        branches.remove(toremove);
        //System.out.println("THIS IS BRANCHES " + branches);
        FileInputStream fileIn2 = new FileInputStream(new File("branches/" + branchname));
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
        Path a = Paths.get(".gitlet/commit/" + curr.latestcommit().getMessage() + "/" + filename);
        File b = new File(a.toString());
        if (! b.exists()) {
            System.out.println("File does not exist in that commit.");
            return;
        }
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
        File[] kru = removed1.listFiles();


        if ((files.length == 0 && kru.length == 0) && ! message.contains("Merged")) {
            System.out.println("No changes added to the commit.");
            return;
        }

        for (File f : kru) {
            File fe = new File("removed1/" + f.getName());
            fe.delete();
        }
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




        //tracking all files in parent commit
        if (curr.latestcommit().getFiles() != null) {
            for (File ke : curr.latestcommit().getFiles()) {
                File ne = new File(".gitlet/staging/"+curr.getName()+"/" + ke.getName());
                if (ne.exists()) {
                    continue;
                } else {
                    // if it was marked to be removed
                    if (toberemoverd.contains(ke.getName())) {
                        continue;
                    }
                    fil.add(ke);
                    String yu = Utils.readContentsAsString(ke);
                    File bre = new File("tracking/" + curr.getName() + "/" + ke.getName());
                    Utils.writeContents(bre, yu);
                    //File ner = new File(message + "/" + ke.getName());
                    //Utils.writeContents(ner, yu);
                }
            }
        }
        toberemoverd = new ArrayList<>();

        Commit parent = curr.latestcommit();

        Commit a = new Commit();
        a.init(this, message, Utils.sha1(message + all + now.toString() +
                null), now.toString(), fil, parent);
        _mostrecent = a;
        mssgtoid.put(a.getCommitsha1(), message);
        allcommits.add(a);

        if (message.contains("Merged") && message.contains("into")){
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
                String dcur = Utils.readContentsAsString(new File(fe.getName()));
                if (!dcont.equals(dcur)) {
                    System.out.println(fe.getName() + " (modified)");
                }
            }
        }
        //STILL HAVE TO DO:
        //Not staged for removal, but tracked in the current commit and deleted from the working directory.
        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }


    void merge(String branchname) throws IOException, ClassNotFoundException {

        File [] staging = staged.listFiles();
        if (staging.length != 0) {
            throw new GitletException("You have uncommitted changes.");
        }

        FileInputStream fir = new FileInputStream("branches/" + branchname);
        ObjectInputStream o = new ObjectInputStream(fir);
        Branch cur = (Branch) o.readObject();

        FileInputStream fier = new FileInputStream("branches/B2");
        ObjectInputStream oe = new ObjectInputStream(fier);
        Branch cure = (Branch) oe.readObject();

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
        Commit fo = cure.latestcommit();
        Commit chosen = c;
        while (chosen != null) {
            File kur = new File("removed/" + chosen.getCommitsha1());
            if (kur.exists()) {
                for (File f : kur.listFiles()) {
                    File ne = new File(f.getName());
                    File kurie = new File("removed/" + chosen.getCommitsha1() + "/" + f.getName());
                    String readk = Utils.readContentsAsString(kurie);
                    Utils.writeContents(ne, readk);
                }
            }
            chosen = chosen.getParent();
        }

        //Any files that were not present at the split point and are
        // present only in the given branch should be checked out and staged
        for (File f : c.getFiles()) {
            if (d != null) {
                if (!contains(d, f)) {
                    checkoutversionfile(c.getCommitsha1(), f.getName());
                    File stager = new File("staging/" + curr.getName());
                    stager.mkdirs();
                    File stager1 = new File("staging/" + curr.getName() + f.getName());
                    String fe = Utils.readContentsAsString(f);
                    Utils.writeContents(stager1, fe);
                }
            }
        }
        if (d.getFiles() != null) {
            for(File f : d.getFiles()) {
                if (!contains(e, f)) {
                    File k = new File(f.getName());
                    k.delete();
                }
            }
        }




        //Any files present at the split point, unmodified in the current branch,
        // and absent in the given branch should be removed (and untracked).
        if (d != null) {
            if (d.getFiles() != null) {
                for (File f : d.getFiles()) { //present at split point
                    if (unmodified(e, f.getName(), d)) { //unmodified in current branch
                        if (absent(f.getName(), branchname)) { // absent at the given branch;
                            commit("Merged " + branchname + " into " + curr.getName());
                            rm(f.getName());
                        }
                    }
                }
            }
        }

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
                    //System.out.println("HITS HERE HITS HERE " + f.getName());
                }
            }
        }


        //if a file is "modified in the given branch since the split point"
        // this means the version of the file as it exists in the commit at
        // the front of the given branch has different content from the version of the file at the split point.
        //should be changed to their versions in the given branch
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
        commit("Merged " + branchname + " into " + curr.getName());
        _mostrecent.settwoparents(cur.latestcommit(), curr.latestcommit());







    }

    int containerfile(File f, ArrayList<File> files) {
        for (int i = 0; i < files.size(); i++) {
            if (f.getName().equals(files.get(i).getName())) {
                return i;
            }
        }
        return -1;
    }

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
            //System.out.println("THIS IS SHORTER GETTER " + shorter.get(i).getMessage());
            if (containt(longer, shorter.get(i))) {
                return shorter.get(i);
            }
        }

        return _initialcommit;
    }

    boolean containt(ArrayList<Commit> commits, Commit c) {
        for(Commit a : commits) {
            if (a.getCommitsha1().equals(c.getCommitsha1())) {
                return true;
            }
        }
        return false;
    }

    boolean unmodified(Commit e, String filename, Commit spltpnt) {
        Commit temp = e;
        while (temp != null && !temp.getMessage().equals("initial commit") && temp != spltpnt) {
            for (File f : temp.getFiles()) {
                if (f.getName().equals(filename)) {
                    return false;
                }
            }
            temp = temp.getParent();
        }
        return true;
    }


    boolean absent(String filename, String branchname) {
        File track = new File("tracking/" + branchname + "/" + filename);
        if (track.exists()) {
            return false;
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
