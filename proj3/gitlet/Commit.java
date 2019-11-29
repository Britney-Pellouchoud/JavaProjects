package gitlet;

import java.util.ArrayList;

public class Commit {
    private String message;
    private String sha1;
    private String timestamp;
    private ArrayList<String> files;
    private String commitsha1;
    private Commit parent;
    public void init(Gitlet g, String message, String sha1, String timestamp, ArrayList<String> files, Commit parentcommit) {
        this.message = message;
        this.sha1 = sha1;
        this.timestamp = timestamp;
        this.files = files;
        if (!message.equals("initialcommit")) {
            parent = parentcommit;
            g.clearstaged();
            CommitTree t = g.getCommittree();
            connecttoparent(parentcommit, this, t);
        }
        commitsha1 = Utils.sha1(files, parentcommit, message, timestamp);
        //need to make separate sha1 based on parent
    }

    public void connecttoparent(Commit parentcommit, Commit childcommit, CommitTree t) {
        if (t.root().getData().equals(parentcommit)) {
            CommitTree<Commit> child = new CommitTree<>(childcommit);
            t.root().getChildren().add(child);
            child.root().setParent(t.root());
            return;
        }
    }
}
