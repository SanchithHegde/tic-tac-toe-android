package me.sanchithhegde.tictactoe.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Class for a node in the tree.
 */
public class Node {
    private State state;
    private Node parent;
    private List<Node> childArray;

    Node() {
        this.state = new State();
        childArray = new ArrayList<>();
    }

    Node(State state) {
        this.state = state;
        childArray = new ArrayList<>();
    }

    /**
     * Clone a Node object.
     *
     * @param node Node object to clone.
     */
    Node(Node node) {
        this.childArray = new ArrayList<>();
        this.state = new State(node.getState());

        if (node.getParent() != null) {
            this.parent = node.getParent();
        }

        List<Node> childArray = node.getChildArray();

        for (Node child : childArray) {
            this.childArray.add(new Node(child));
        }
    }

    State getState() {
        return state;
    }

    Node getParent() {
        return parent;
    }

    void setParent(Node parent) {
        this.parent = parent;
    }

    List<Node> getChildArray() {
        return childArray;
    }

    /**
     * Selects a random child node from the available nodes.
     *
     * @return Selected child node.
     */
    Node getRandomChildNode() {
        int noOfPossibleMoves = this.childArray.size();
        int selectRandom = (int) (Math.random() * noOfPossibleMoves);
        return this.childArray.get(selectRandom);
    }

    /**
     * Selects child node that reduces the computer's chances of losing.
     *
     * @return Child node with best score.
     */
    Node getChildWithMaxScore() {
        return Collections.max(this.childArray, Comparator.comparing(c -> c.getState().getVisitCount()));
    }

}