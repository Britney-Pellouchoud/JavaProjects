package gitlet;
import java.io.Serializable;


public class Branch implements Serializable {
    private String name;
    void init(String name) {
        this.name = name;
    }
}
