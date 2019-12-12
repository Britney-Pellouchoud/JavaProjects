package gitlet;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;


/**This is a tag.
 * @author Britney Pellouchoud
 */
public class Branch implements Serializable {
    /**
    Comment.
     */
    private String name;
    /**
    Comment.
     */
    private ArrayList<Commit>  commits = new ArrayList<>();
    /**
    Comment.
     */
    private String mrcsha1;
    /**
    Comment.
     */
    private Commit latest;
    /**
    Comment.
     */
    private Commit splitpoint;
    /**.
    Comment.
     @param nurm String.
     */
    void init(String nurm) {
        this.name = nurm;
        File branch = new File(nurm);
        branch.mkdirs();
    }

    /**String.
     *
     * @return String.
     */
    String getName() {
        return name;
    }

    /**.
     *
     * @param c Commit
     */
    void addCsha1(Commit c) {
        commits.add(c);
        latest = c;
    }


    /**Get.
     *
     * @return String.
     */
    String getMrcsha1() {
        return mrcsha1;
    }

    /**Arr.
     *
     * @return ArrayList.
     */
    ArrayList<Commit> getCsha1s() {
        return commits;
    }

    /**.
     *
     * @return Commit.
     */
    Commit latestcommit() {
        return latest;
    }

    /**.
     *
     * @param c Commit.
     */
    void setSplitpoint(Commit c) {
        splitpoint = c;
    }

    /**.
     *
     * @return Commit.
     */
    Commit getSplitpoint() {
        return splitpoint;
    }

    /**.
     *
     * @param c Commit.
     */
    void setLatestcommit(Commit c) {
        latest = c;
    }
}
