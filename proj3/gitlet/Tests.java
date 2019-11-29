package gitlet;

import org.junit.Test;

public class Tests {
    @Test
    public void initialization() {
        Gitlet babygit = new Gitlet();
        babygit.init();
        babygit.clearstaged();
    }
}
