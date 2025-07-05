package com.example.tic_tac_toe.logic;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Button;

import com.example.tic_tac_toe.utils.SoundManager;

public class PlayerVsPlayer extends GameLogic {

    public PlayerVsPlayer(Button[][] buttons, Context context, SoundManager soundManager) {
        super(buttons, context, soundManager);
    }

    @Override
    public void makeMove(int row, int col) {
        if (!buttons[row][col].getText().toString().equals("")) return;

        if (playerXTurn) {
            buttons[row][col].setText("X");
        } else {
            buttons[row][col].setText("O");
        }

        soundManager.playClick();
        roundCount++;

        if (checkForWin()) {
            gameOver = true;
            soundManager.playWin();
            showResultDialog((playerXTurn ? "X" : "O") + " Win!");
            disableButtons();
        } else if (roundCount == 9) {
            gameOver = true;
            soundManager.playDraw();
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
