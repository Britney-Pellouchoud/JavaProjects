import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** A set of String values.
 *  @author
 */
class ECHashStringSet implements StringSet {
    private static float maximum = 5;
    private static float minimum = (float) 0.20;
    private int _len;
    private LinkedList<String>[] finder;

    public int length() {
        return _len;
    }

    public ECHashStringSet() {
        _len = 0;
        int help = (int) (1 / minimum);
        finder = new LinkedList[help];
    }

    private float many() {
        float m = _len / finder.length;
        return m;
    }

    @Override
    public void put(String s) {
        if (s != null) {
            if (many() > maximum) {
                fixsize();
            }
            int position = hashstorer(s.hashCode());
            if (finder[position] == null) {
                finder[position] = new LinkedList<String>();
            }
                finder[position].add(s);
                _len += 1;

        }
    }

    private void fixsize() {
        LinkedList<String>[] original = finder;
        finder = new LinkedList[2 * finder.length];
        _len = 0;
        for (LinkedList<String>l: original) {
            if (l != null) {
                for (String s : l) {
                    this.put(s);
                }
            }
        }
    }

    private int hashstorer (int hash) {
        int last = hash & 1;
        int hashbit = (hash >>> 1) | last;
        return hashbit % finder.length;
    }

    @Override
    public boolean contains(String s) {
       if (s != null) {
           int position = hashstorer(s.hashCode());
           if (finder[position] != null) {
               boolean exists = finder[position].contains(s);
               return exists;
           } return false;
       }
       return false;
    }

    @Override
    public List<String> asList() {
        return new ArrayList<String>();
    }
}
