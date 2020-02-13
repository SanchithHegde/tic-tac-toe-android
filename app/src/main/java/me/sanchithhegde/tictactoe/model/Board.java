package me.sanchithhegde.tictactoe.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to maintain the board status.
 */
public class Board {
    public static final int DEFAULT_BOARD_SIZE = 3;
    public static final int DRAW = 0;
    public static final int P1 = 1;
    public static final int P2 = 2;
    static final int IN_PROGRESS = -1;
    private int[][] boardValues;

    public Board() {
        boardValues = new int[DEFAULT_BOARD_SIZE][DEFAULT_BOARD_SIZE];
    }

    /**
     * Clone a Board object.
     *
     * @param board Board object to clone.
     */
    public Board(Board board) {
        int boardLength = board.getBoardValues().length;
        this.boardValues = new int[boardLength][boardLength];
        int[][] boardValues = board.getBoardValues();
        int n = boardValues.length;

        for (int i = 0; i < n; i++) {
            int m = boardValues[i].length;
            for (int j = 0; j < m; j++) {
                this.boardValues[i][j] = boardValues[i][j];
            }
        }
    }

    /**
     * Find last move played on board.
     *
     * @return Index of position of last played move.
     */
    public static int findLastMove(Board oldBoard, Board newBoard) {
        int size = oldBoard.boardValues.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (oldBoard.boardValues[i][j] != newBoard.boardValues[i][j]) {
                    return i * size + j;
                }
            }
        }
        return -1;
    }

    public void performMove(int player, Position p) {
        boardValues[p.getX()][p.getY()] = player;
    }

    private int[][] getBoardValues() {
        return boardValues;
    }

    /**
     * Check if any player won, or if the game is a draw, or still in progress.
     *
     * @return Integer value indicating status of game.
     */
    public int checkStatus() {
        int boardSize = boardValues.length;
        int maxIndex = boardSize - 1;
        int[] diag1 = new int[boardSize];
        int[] diag2 = new int[boardSize];

        for (int i = 0; i < boardSize; i++) {
            int[] row = boardValues[i];
            int[] col = new int[boardSize];

            for (int j = 0; j < boardSize; j++) {
                col[j] = boardValues[j][i];
            }

            int checkRowForWin = checkForWin(row);
            if (checkRowForWin != 0) {
                return checkRowForWin;
            }

            int checkColForWin = checkForWin(col);
            if (checkColForWin != 0) {
                return checkColForWin;
            }

            diag1[i] = boardValues[i][i];
            diag2[i] = boardValues[maxIndex - i][i];
        }

        int checkDiag1ForWin = checkForWin(diag1);
        if (checkDiag1ForWin != 0) {
            return checkDiag1ForWin;
        }

        int checkDiag2ForWin = checkForWin(diag2);
        if (checkDiag2ForWin != 0) {
            return checkDiag2ForWin;
        }

        if (getEmptyPositions().size() > 0) {
            return IN_PROGRESS;
        } else {
            return DRAW;
        }
    }

    private int checkForWin(int[] row) {
        boolean isEqual = true;
        int previous = row[0];

        for (int i : row) {
            if (previous != i) {
                isEqual = false;
                break;
            }
            previous = i;
        }

        if (isEqual) {
            return previous;
        } else {
            return 0;
        }
    }

    /**
     * Get list of available positions in board.
     *
     * @return List of Position objects available for playing a move.
     */
    List<Position> getEmptyPositions() {
        int size = this.boardValues.length;
        List<Position> emptyPositions = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (boardValues[i][j] == 0) {
                    emptyPositions.add(new Position(i, j));
                }
            }
        }
        return emptyPositions;
    }
}