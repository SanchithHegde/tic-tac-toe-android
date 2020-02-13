package me.sanchithhegde.tictactoe.viewmodel;

import androidx.gridlayout.widget.GridLayout;
import androidx.lifecycle.ViewModel;

import me.sanchithhegde.tictactoe.model.Board;

public class BoardViewModel extends ViewModel {
    private Board board;
    private GridLayout gridLayout;

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public GridLayout getGridLayout() {
        return gridLayout;
    }

    public void setGridLayout(GridLayout gridLayout) {
        this.gridLayout = gridLayout;
    }
}
