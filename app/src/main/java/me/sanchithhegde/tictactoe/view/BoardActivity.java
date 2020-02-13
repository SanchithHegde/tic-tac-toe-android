package me.sanchithhegde.tictactoe.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import me.sanchithhegde.tictactoe.R;
import me.sanchithhegde.tictactoe.model.Board;
import me.sanchithhegde.tictactoe.model.MonteCarloTreeSearch;
import me.sanchithhegde.tictactoe.model.Position;
import me.sanchithhegde.tictactoe.viewmodel.BoardViewModel;

import static me.sanchithhegde.tictactoe.model.Board.DEFAULT_BOARD_SIZE;

public class BoardActivity extends AppCompatActivity {
    Board board;
    BoardViewModel boardViewModel;
    GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        boardViewModel = new ViewModelProvider(this).get(BoardViewModel.class);
        gridLayout = findViewById(R.id.gridLayout);

        gridLayout.setRowCount(DEFAULT_BOARD_SIZE);
        gridLayout.setColumnCount(DEFAULT_BOARD_SIZE);

        // If a Board instance is saved in the ViewModel, load it from the ViewModel
        if (boardViewModel.getBoard() == null) {
            board = new Board();
        } else {
            board = new Board(boardViewModel.getBoard());
        }

        // If a GridLayout instance is saved in the ViewModel, load it from the ViewModel
        if (boardViewModel.getGridLayout() != null) {
            GridLayout savedGridLayout = boardViewModel.getGridLayout();
            int count = savedGridLayout.getChildCount();
            for (int i = 0; i < count; i++) {
                ImageView imageView = (ImageView) savedGridLayout.getChildAt(0);
                savedGridLayout.removeView(imageView);
                addImageOnClickListener(imageView);
                gridLayout.addView(imageView);
            }

            // Make a copy of gridLayout and board in ViewModel
            boardViewModel.setGridLayout(gridLayout);
            boardViewModel.setBoard(board);
            return;
        }

        // Else, create new ImageView instances, set their parameters and add them to gridLayout
        for (int i = 0; i < DEFAULT_BOARD_SIZE * DEFAULT_BOARD_SIZE; ++i) {
            final ImageView imageView = new ImageView(getApplicationContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setLayoutParams(new GridLayout.LayoutParams(
                    GridLayout.spec(GridLayout.UNDEFINED, 1f),
                    GridLayout.spec(GridLayout.UNDEFINED, 1f)
            ));
            imageView.setImageResource(R.drawable.ic_player_bg);

            addImageOnClickListener(imageView);
            gridLayout.addView(imageView);
        }
    }

    // Disable all imageView's in board after any player wins or game draws
    private void disableBoard(GridLayout gridLayout) {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ImageView imageView = (ImageView) gridLayout.getChildAt(i);
            imageView.setClickable(false);
        }
    }

    // Build material alert dialog using title and message as parameters
    private void buildMaterialAlertDialog(int titleResId, int messageResId) {
        new MaterialAlertDialogBuilder(this, R.style.AppTheme_MaterialAlertDialog)
                .setTitle(titleResId)
                .setMessage(messageResId)
                .setPositiveButton(R.string.play_again,
                        (DialogInterface dialogInterface, int i) -> {
                            boardViewModel.setBoard(null);
                            boardViewModel.setGridLayout(null);
                            recreate();
                        })
                .setNegativeButton(R.string.main_menu, (DialogInterface dialogInterface, int i) ->
                        finish())
                .show();
    }

    // Check win status and display result
    boolean checkWinStatus(Board board) {
        switch (board.checkStatus()) {
            case Board.DRAW:
                buildMaterialAlertDialog(R.string.well_played, R.string.game_draw);
                return true;

            case Board.P2:
                buildMaterialAlertDialog(R.string.better_luck, R.string.game_lose);
                return true;

            default:
                // Game in progress
                return false;
        }
    }

    // Play computer move and reflect it in gridLayout
    void playComputerMove(Board board) {
        Board newBoard;
        MonteCarloTreeSearch mcts = new MonteCarloTreeSearch();

        newBoard = mcts.findNextMove(board, 2);
        int move = Board.findLastMove(board, newBoard);
        this.board = newBoard;

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        ImageView imageView = (ImageView) gridLayout.getChildAt(move);
        imageView.setImageResource(R.drawable.ic_player_x);
        imageView.setClickable(false);
    }

    // Add OnClickListener (to play move) to imageView
    private void addImageOnClickListener(ImageView imageView) {
        View.OnClickListener imageOnClickListener = view -> {
            imageView.setImageResource(R.drawable.ic_player_o);

            int index = gridLayout.indexOfChild(imageView);
            board.performMove(1,
                    new Position(index / DEFAULT_BOARD_SIZE, index % DEFAULT_BOARD_SIZE));
            boardViewModel.setGridLayout(gridLayout);
            boardViewModel.setBoard(board);
            if (checkWinStatus(board)) {
                disableBoard(gridLayout);
                return;
            }

            playComputerMove(board);
            boardViewModel.setGridLayout(gridLayout);
            boardViewModel.setBoard(board);
            if (checkWinStatus(board)) {
                disableBoard(gridLayout);
                return;
            }

            imageView.setClickable(false);
        };
        imageView.setOnClickListener(imageOnClickListener);
    }
}
