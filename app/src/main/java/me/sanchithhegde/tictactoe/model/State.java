package me.sanchithhegde.tictactoe.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class maintaining the state of the game.
 */
class State {
    private Board board;
    private int playerNo;
    private int visitCount;
    private double winScore;

    State() {
        board = new Board();
    }

    /**
     * Clone a State object.
     *
     * @param state State object to clone.
     */
    State(State state) {
        this.board = new Board(state.getBoard());
        this.playerNo = state.getPlayerNo();
        this.visitCount = state.getVisitCount();
        this.winScore = state.getWinScore();
    }

    State(Board board) {
        this.board = new Board(board);
    }

    Board getBoard() {
        return board;
    }

    void setBoard(Board board) {
        this.board = board;
    }

    int getPlayerNo() {
        return playerNo;
    }

    void setPlayerNo(int playerNo) {
        this.playerNo = playerNo;
    }

    int getOpponent() {
        return 3 - playerNo;
    }

    int getVisitCount() {
        return visitCount;
    }

    double getWinScore() {
        return winScore;
    }

    void setWinScore(double winScore) {
        this.winScore = winScore;
    }

    /**
     * Return all possible states from the current state of the game.
     *
     * @return List of State objects.
     */
    List<State> getAllPossibleStates() {
        List<State> possibleStates = new ArrayList<>();
        List<Position> availablePositions = this.board.getEmptyPositions();

        availablePositions.forEach(p -> {
            State newState = new State(this.board);
            newState.setPlayerNo(3 - this.playerNo);
            newState.getBoard().performMove(newState.getPlayerNo(), p);
            possibleStates.add(newState);
        });
        return possibleStates;
    }

    void incrementVisit() {
        this.visitCount++;
    }

    void addScore(double score) {
        if (this.winScore != Integer.MIN_VALUE) {
            this.winScore += score;
        }
    }

    void randomPlay() {
        List<Position> availablePositions = this.board.getEmptyPositions();
        int totalPossibilities = availablePositions.size();
        int selectRandom = (int) (Math.random() * totalPossibilities);
        this.board.performMove(this.playerNo, availablePositions.get(selectRandom));
    }

    void togglePlayer() {
        this.playerNo = 3 - this.playerNo;
    }

}