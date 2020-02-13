package me.sanchithhegde.tictactoe.model;

import java.util.Collections;
import java.util.Comparator;

/**
 * Class implementing the Upper Confidence Bounds applied to trees algorithm.
 */
class Uct {
    /**
     * Find Upper Confidence Bound applied to Trees for the given node.
     *
     * @param totalVisit   Total number of nodes visited.
     * @param nodeWinScore Win score associated with the node.
     * @param nodeVisit    Number of times the node was visited.
     * @return UCT value
     */
    private static double uctValue(int totalVisit, double nodeWinScore, int nodeVisit) {
        if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }

        return (nodeWinScore / (double) nodeVisit) + 1.41
                * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
    }

    static Node findBestNodeWithUct(Node node) {
        int parentVisit = node.getState().getVisitCount();
        return Collections.max(node.getChildArray(),
                Comparator.comparing(c ->
                        uctValue(parentVisit, c.getState().getWinScore(), c.getState().getVisitCount())));
    }
}