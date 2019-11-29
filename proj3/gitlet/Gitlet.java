package gitlet;

import org.antlr.v4.runtime.tree.Tree;

import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.lang.String;


public class Gitlet {
    void init() {
        if (!checkunique()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        }
        Commit initialcommit = new Commit();
        Instant now = Instant.now();
        String initial = "initial commit";
        initialcommit.init(this,"initial commit", Utils.sha1(initial), now.toString(), null, null);
        committree = new CommitTree<>(initialcommit);
        Branch master = new Branch();
        master.init("master");
        makestagedir();

    }

    private CommitTree committree;

    public CommitTree getCommittree() {
        return committree;
    }

    boolean checkunique() {
        String directory = ".gitlet";
        File file = new File(directory);
        if (file.isDirectory()) {
            return false;
        }
        return true;
    }

    void makestagedir() {
        new File(".gitlet/staging").mkdirs();
    }

    void add(String filename) throws IOException {
       File toadd = new File(filename);
       String sha1 = Utils.sha1(toadd);
       if (new File(".gitlet/staging/" + sha1).exists()) {
           return;
       }

        Path file = Paths.get(filename);
        Path tofile = Paths.get(".gitlet/staging/" + sha1);
        Files.copy(file, tofile, StandardCopyOption.REPLACE_EXISTING);
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
        //if file is tracked in current commit, mark it
        System.out.println("No reason to remove the file.");
    }

}
