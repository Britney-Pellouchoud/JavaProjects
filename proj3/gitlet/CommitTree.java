package gitlet;

import java.util.ArrayList;
import java.util.List;

public class CommitTree<Commit> {
    private Node<Commit> root;

    public CommitTree(Commit rootData) {
        root = new Node<Commit>();
        root.data = rootData;
        root.children = new ArrayList<CommitTree>();
    }

    public static class Node<Commit> {
        private Commit data;
        private Node<Commit> parent;
        private List<CommitTree> children;
        Commit getData() {
            return data;
        }
        List<CommitTree> getChildren() {
            return children;
        }
        void setParent(Node p) {
            parent = p;
        }
        Node<Commit> getParent() {
            return parent;
        }

    }

    Node<Commit> root() {
        return root;
    }
}
