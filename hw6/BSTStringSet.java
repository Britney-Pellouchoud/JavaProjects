import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author
 */
public class BSTStringSet implements StringSet, Iterable<String> {
    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        public Node(String sp) {
            s = sp;
        }
    }
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        _root = puthelp(s, _root);
    }

    private Node puthelp(String s, Node r) {
        if (r == null) {
            Node newnode = new Node(s);
            return newnode;
        }

        int sameval = s.compareTo(r.s);

        if (sameval > 0) {
            r.right = puthelp(s, r.right);
        } else {
            r.left = puthelp(s, r.left);
        }
        return r;
    }

    @Override
    public boolean contains(String s) {
        return doesitcontain(s, _root); // FIXME: PART A
    }

    private boolean doesitcontain(String s, Node r) {
        if (r == null) {
            return false;
        } if (r.s.equals(s)) {
            return true;
        } int comparison = s.compareTo(r.s);
        if (comparison > 0) {
            return doesitcontain(s, r.right);
        } else if (comparison < 0) {
            return doesitcontain(s, r.left);
        }
        return true;


    }

    @Override
    public List<String> asList() {
        return new ArrayList<String>();
         // FIXME: PART A
    }




    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    // FIXME: UNCOMMENT THE NEXT LINE FOR PART B
    //@Override
    public Iterator<String> iterator(String low, String high) {
        return new LimitedIterator(low, high);
    }

    private class LimitedIterator implements Iterator<String> {
        private Node current;
        private Stack<Node> position;
        private String lowerstring;
        private String higherstring;

        public LimitedIterator(String lower, String higher) {
            this.position = new Stack<Node>();
            this.lowerstring = lower;
            this.higherstring = higher;
            current = _root;
        }

        public boolean hasNext() {
            if (position.isEmpty()) {
                return false;
            } else if (current.s.compareTo(higherstring) > 0 && current == null) {
                return false;
            }
            return true;
        }

        public String next() {
            while (current.s.compareTo(lowerstring) > -1 && current != null) {
                position.push(current);
                current = current.left;
            }
            Node x = position.pop();
            current = x.right;
            return current.s;
        }
    }


    /** Root node of the tree. */
    private Node _root;
}
