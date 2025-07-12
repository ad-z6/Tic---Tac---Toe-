package com.example.tic_tac_toe.logic;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Button;

public class PlayerVsPlayer extends GameLogic {

    public PlayerVsPlayer(Button[][] buttons, Context context) {
        super(buttons, context);
    }

    @Override
    public void makeMove(int row, int col) {
        if (!buttons[row][col].getText().toString().equals("")) return;

        if (playerXTurn) {
            buttons[row][col].setText("X");
        } else {
            buttons[row][col].setText("O");
        }

        roundCount++;

        if (checkForWin()) {
            gameOver = true;
            showResultDialog((playerXTurn ? "X" : "O") + " Win!");
            disableButtons();
        } else if (roundCount == 9) {
            gameOver = true;
            showResultDialog("It's a draw!");
        } else {
            playerXTurn = !playerXTurn;
        }
    }

    private void showResultDialog(String message) {
        new AlertDialog.Builder(context)
                .setTitle("The Game is over")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Back to list", (dialog, which) -> {
                    dialog.dismiss();
                    if (context instanceof android.app.Activity) {
                        ((android.app.Activity) context).finish();
                    }
                })
                .show();
    }
}
