package gitlet;

import java.util.ArrayList;

public class Commit {
    private String message;
    private String sha1;
    private String timestamp;
    private ArrayList<String> files;
    public void init(String message, String sha1, String timestamp, ArrayList<String> files) {
        this.message = message;
        this.sha1 = sha1;
        this.timestamp = timestamp;
        this.files = files;
    }
}
