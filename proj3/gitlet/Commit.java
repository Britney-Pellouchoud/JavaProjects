package gitlet;

import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.io.Serializable;




public class Commit implements Serializable {
    private String message;
    private String sha1;
    private String timestamp;
    private ArrayList<File> files;
    private HashMap<String, Boolean> tracked;
    private String commitsha1;
    private Commit parent;
    private FileOutputStream fileOut;
    private ObjectOutputStream objectOut;
    private File csha;
    public void init(Gitlet g, String message, String sha1, String timestamp, ArrayList<File> files, Commit parentcommit) throws IOException {
        this.message = message;
        this.sha1 = sha1;
        this.timestamp = timestamp;
        this.files = files;
        CommitTree t = g.getCommittree();
        //t.setRoot(this);
        if (!message.equals("initial commit")) {
            parent = parentcommit;
            g.clearstaged();
            //connecttoparent(parentcommit, this, t);
            //for (String file : files) {
            //    tracked.put(file, false);
            //}
        }
        g.setPointer(this);

        String h = "";
        if (files != null) {
            for (File f : files) {
                String a = f.toString();
                h += a;
            }
        }

        String m = "";
        if (parentcommit != null) {
            m += parentcommit.getCommitsha1();
        }
        commitsha1 = Utils.sha1(h, m, message, timestamp);
        File k = new File("commit");
        k.mkdirs();
        csha = new File("commit/" + commitsha1);
        csha.mkdir();
        //need to make separate sha1 based on parent
    }

    String getcsha() {
        return csha.getName();
    }

    String getSha1() {
        return sha1;
    }

    Commit getParent() {
        return parent;
    }

    String getCommitsha1() {
        return commitsha1;
    }

    String getTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy XX");
        Date commitDate = new Date();
        return dateFormat.format(commitDate);
    }
    String getMessage() {
        return message;
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    void markfortracking(String f) {
        tracked.put(f, true);
    }


/*
    public void connecttoparent(Commit parentcommit, Commit childcommit, CommitTree t) {
        if (t.getdata().equals(parentcommit)) {
            CommitTree<Commit> child = new CommitTree<>(childcommit);
            t.root().getChildren().add(child);
            child.setParent(parentcommit);
            return;
        }
    }

 */




}
