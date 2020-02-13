package me.sanchithhegde.tictactoe.model;

import java.util.List;

/**
 * Class implementing the MCTS algorithm.
 */
public class MonteCarloTreeSearch {
    private static final int WIN_SCORE = 10;
    private int level;
    private int opponent;

    public MonteCarloTreeSearch() {
        this.level = 3;
    }

    private int getMillisForCurrentLevel() {
        return 2 * (this.level - 1) + 1;
    }

    /**
     * Find next move using the MCTS algorithm.
     *
     * @param board    Current board state.
     * @param playerNo Current player.
     * @return Board object at the end of the move.
     */
    public Board findNextMove(Board board, int playerNo) {
        opponent = 3 - playerNo;
        Tree tree = new Tree();
        Node rootNode = tree.getRoot();
        rootNode.getState().setBoard(board);
        rootNode.getState().setPlayerNo(opponent);

        long start = System.currentTimeMillis();
        long end = start + 60 * getMillisForCurrentLevel();

        while (System.currentTimeMillis() < end) {
            // Phase 1 - Selection
            Node promisingNode = selectPromisingNode(rootNode);

            // Phase 2 - Expansion
            if (promisingNode.getState().getBoard().checkStatus() == Board.IN_PROGRESS) {
                expandNode(promisingNode);
            }

            // Phase 3 - Simulation
            Node nodeToExplore = promisingNode;
            if (promisingNode.getChildArray().size() > 0) {
                nodeToExplore = promisingNode.getRandomChildNode();
            }
            int playoutResults = simulateRandomPlayout(nodeToExplore);

            // Phase 4 - Update
            backpropagation(nodeToExplore, playoutResults);
        }

        Node winnerNode = rootNode.getChildWithMaxScore();
        tree.setRoot(winnerNode);
        return winnerNode.getState().getBoard();
    }

    private Node selectPromisingNode(Node rootNode) {
        Node node = rootNode;

        while (node.getChildArray().size() != 0) {
            node = Uct.findBestNodeWithUct(node);
        }
        return node;
    }

    private void expandNode(Node node) {
        List<State> possibleStates = node.getState().getAllPossibleStates();
        possibleStates.forEach(state -> {
            Node newNode = new Node(state);
            newNode.setParent(node);
            newNode.getState().setPlayerNo(node.getState().getOpponent());
            node.getChildArray().add(newNode);
        });
    }

    private void backpropagation(Node nodeToExplore, int playerNo) {
        Node tempNode = nodeToExplore;
        while (tempNode != null) {
            tempNode.getState().incrementVisit();

            if (tempNode.getState().getPlayerNo() == playerNo) {
                tempNode.getState().addScore(WIN_SCORE);
            }

            tempNode = tempNode.getParent();
        }
    }

    private int simulateRandomPlayout(Node node) {
        Node tempNode = new Node(node);
        State tempState = tempNode.getState();
        int boardStatus = tempState.getBoard().checkStatus();

        if (boardStatus == opponent) {
            tempNode.getParent().getState().setWinScore(Integer.MIN_VALUE);
            return boardStatus;
        }

        while (boardStatus == Board.IN_PROGRESS) {
            tempState.togglePlayer();
            tempState.randomPlay();
            boardStatus = tempState.getBoard().checkStatus();
        }

        return boardStatus;
    }
}