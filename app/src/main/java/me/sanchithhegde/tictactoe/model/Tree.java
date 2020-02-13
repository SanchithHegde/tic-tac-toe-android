package me.sanchithhegde.tictactoe.model;

/**
 * Class implementing trees.
 */
class Tree {
    private Node root;

    Tree() {
        root = new Node();
    }

    Node getRoot() {
        return root;
    }

    void setRoot(Node root) {
        this.root = root;
    }
}