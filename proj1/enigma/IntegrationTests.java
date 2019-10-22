package enigma;
import org.junit.Test;

public class IntegrationTests {
    @Test
    public void check1() {
        String[] lst = new String[] {"enigma/config", "enigma"
                + "/input", "enigma/output"};
        Main k = new Main(lst);
        k.main(lst);
    }

    @Test
    public void check2() {
        String[] lst = new String[] {"enigma/config2", "enigma/"
                + "input2", "enigma/output2"};
        Main s = new Main(lst);
        s.main(lst);

    }

    @Test
    public void check3() {
        String[] lst = new String[] {"testing/correct/default.conf", "testing/"
                + "correct/"
                + "trivial.in", "testing/correct/trivial.out"};
        Main r = new Main(lst);
        r.main(lst);
    }

    @Test
    public void check4() {
        String[] lst = new String[] {"testing/correct/default.conf", "testing"
                + "/correct/trivial1.in", "testing/correct/trivial1.out"};
        Main r = new Main(lst);
        r.main(lst);
    }

}
