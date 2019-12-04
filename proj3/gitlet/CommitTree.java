package gitlet;

import java.util.ArrayList;
import java.io.Serializable;

import java.util.List;

public class CommitTree<Commit> implements Serializable {
    private Node<Commit> root;

    public CommitTree(Commit rootData) {
        root = new Node<Commit>();
        root.data = rootData;
        root.children = new ArrayList<CommitTree>();

    }

    public Commit getdata() {
        return root.data;
    }



    public static class Node<Commit> {
        private Commit data;
        private Node parent;
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
        Node getParent() {
            return parent;
        }
    }

    public void setParent(Commit c) {
        Node n = new Node();
        //n.data = c;
        this.root.parent = n;
    }
    public void setRoot(Commit c){
        this.root = new Node<Commit>();
        root.data = c;
        root.parent = null;
    }

    Node<Commit> root() {
        return root;
    }
}
