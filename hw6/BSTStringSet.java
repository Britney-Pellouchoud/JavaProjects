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

        boolean sameval = s.equals(r.s);

        if (sameval) {
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
            return doesitcontain(s, r.left);
        } else {
            return doesitcontain(s, r.right);
        }

    }

    @Override
    public List<String> asList() {
        return new ArrayList<String>();
         // FIXME: PART A
    }


    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
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
    // @Override
    public Iterator<String> iterator(String low, String high) {
        return null;  // FIXME: PART B
    }


    /** Root node of the tree. */
    private Node _root;
}
