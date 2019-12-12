package gitlet;

import java.io.Serializable;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


/**Yes.
 * @author  Britney Pellouchoud.
 */
public class Commit implements Serializable {
    /**.
    u.*
     */
    private String message;
    /**.
    u.
     */
    private String sha1;
    /**.
    u.
     */
    private String timestamp;
    /**.
    u.
     */
    private ArrayList<File> files;
    /**.
    u.
     */
    private HashMap<String, Boolean> tracked;
    /**.
    u.
     */
    private String commitsha1;
    /**.
    u.
     */
    private Commit parent;
    /**.
    u.
     */
    private FileOutputStream fileOut;
    /**.
    u.
     */
    private ObjectOutputStream objectOut;
    /**.
    u.
     */
    private File csha;
    /**.
    u.
     */
    private File jk;
    /**.
    u.
     */
    private ArrayList<Commit> parents = new ArrayList<Commit>();
    /**.
    u.
     */
    private boolean doubleparent = false;
    /**.
    u.
     */
    private ArrayList<Branch> branchparents = new ArrayList<>();
    /**.
    u.
     */

    /**Yes.
     *
     * @param g Gitlet.
     * @param messagee String.
     * @param sha11 String.
     * @param timestampp String.
     * @param filess ArrayList.
     * @param parentcommit Commit.
     * @throws IOException
     */
    public void init(Gitlet g, String messagee, String sha11,
                     String timestampp, ArrayList<File> filess,
                     Commit parentcommit) throws IOException {
        this.message = messagee;
        this.sha1 = sha11;
        this.timestamp = timestampp;
        this.files = filess;
        if (!messagee.equals("initial commit")) {
            parent = parentcommit;
            g.clearstaged();
        }
        String h = "";
        if (filess != null) {
            for (File f : filess) {
                String a = f.toString();
                h += a;
            }
        }
        String m = "";
        if (parentcommit != null) {
            m += parentcommit.getCommitsha1();
        }
        commitsha1 = Utils.sha1(h, m, messagee, timestampp);
        File k = new File("commit");
        k.mkdirs();
        csha = new File("commit/" + messagee);
        csha.mkdir();
        jk = new File("commit/" + messagee + "/" + commitsha1);
        jk.mkdirs();
    }

    /**.
     *
     * @param a Commit.
     * @param b Commit.
     */
    void settwoparents(Commit a, Commit b) {
        parents.add(a);
        parents.add(b);
        doubleparent = true;

    }

    /**.
     *
     * @return Boolean.
     */
    boolean twoparents() {
        return doubleparent;
    }

    /**.
     *
     * @return ArrayList.
     */
    ArrayList<Commit> gettwoparents() {
        return parents;
    }

    /**.
     *
     * @param a Branch.
     * @param b Branch.
     */
    void setbranchparents(Branch a, Branch b) {
        branchparents.add(a);
        branchparents.add(b);
    }

    /**.
     *
     * @return ArrayList.
     */
    ArrayList<Branch> getBranchparents() {
        return branchparents;
    }

    /**.
     *
     * @return String.
     */
    String getcsha() {
        return csha.getName();
    }

    /**.
     *
     * @return String.
     */
    String getSha1() {
        return sha1;
    }

    /**.
     *
     * @return Commit.
     */
    Commit getParent() {
        return parent;
    }

    /**.
     *
     * @return String.
     */
    String getCommitsha1() {
        return commitsha1;
    }

    /**.
     *
     * @return String.
     */
    String getTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE "
                + "MMM dd HH:mm:ss yyyy XX");
        Date commitDate = new Date();
        return dateFormat.format(commitDate);
    }

    /**.
     *
     * @return String.
     */
    String getMessage() {
        return message;
    }

    /** Array.
     *
     * @return ArrayList.
     */
    public ArrayList<File> getFiles() {
        return files;
    }

    /**.
     *
     * @param f String.
     */
    void markfortracking(String f) {
        tracked.put(f, true);
    }

}
