package me.sanchithhegde.tictactoe.model;

/**
 * Class for implementing a position on the board.
 */
public class Position {
    private int valueX;
    private int valueY;

    public Position(final int x, final int y) {
        this.valueX = x;
        this.valueY = y;
    }

    int getX() {
        return valueX;
    }

    int getY() {
        return valueY;
    }
}