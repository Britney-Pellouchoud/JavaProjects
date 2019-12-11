package gitlet;
import java.io.File;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


public class Branch implements Serializable {
    private String name;
    private ArrayList<Commit>  commits = new ArrayList<>();
    private String mrcsha1;
    private Commit latest;
    private Commit splitpoint;
    void init(String name) {
        this.name = name;
        File branch = new File(name);
        branch.mkdirs();
    }

    String getName() {
        return name;
    }

    void addCsha1(Commit c) {
        commits.add(c);
        latest = c;
    }



    String getMrcsha1() {
        return mrcsha1;
    }

    ArrayList<Commit> getCsha1s (){
        return commits;
    }

    Commit latestcommit() {
        return latest;
    }

    void setSplitpoint(Commit c) {
        splitpoint = c;
    }

    Commit getSplitpoint() {
        return splitpoint;
    }
}
