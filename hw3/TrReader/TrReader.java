import java.io.Reader;
import java.io.IOException;

/** Translating Reader: a stream that is a translation of an
 *  existing reader.
 *  @author your name here
 */
public class TrReader extends Reader {
    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters
     *  in STR unchanged.  FROM and TO must have the same length. */
    public TrReader(Reader str, String from, String to) {
        this.str = str;
        this.from = from;
        this.to = to;
    }

    public int read() throws IOException {
        int r = str.read();
        if (c == -1) {
            return c
        }
        else {
            return translate((char) c);
        }
    }

    public int read(char[] f) throws IOException {
        int x = str.read(cbuf);
        for(int i = 0; i <= x + 1; i++) {
            return n;
        }
    }

    //read:str.read


    /* TODO: IMPLEMENT ANY MISSING ABSTRACT METHODS HERE
     * NOTE: Until you fill in the necessary methods, the compiler will
     *       reject this file, saying that you must declare TrReader
     *       abstract. Don't do that; define the right methods instead!
     */
}
