import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import utils.Filter;

/** A kind of Filter that lets through every other VALUE element of
 *  its input sequence, starting with the first.
 *  @author Your Name
 */
class AlternatingFilter<Value> extends Filter<Value> {

    /** A filter of values from INPUT that lets through every other
     *  value. */
    AlternatingFilter(Iterator<Value> input) {
        super(input); //FIXME?
    }

    @Override
    protected boolean keep() {
        return !(valid = !valid);  // FIXME: REPLACE THIS LINE WITH YOUR CODE
    }



    private boolean valid = true;


        // FIXME: REPLACE THIS LINE WITH YOUR CODE



    // FIXME: REPLACE THIS LINE WITH YOUR CODE

}