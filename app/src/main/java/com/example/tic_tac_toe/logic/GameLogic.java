package com.example.tic_tac_toe.logic;

import android.content.Context;
import android.widget.Button;

import com.example.tic_tac_toe.utils.SoundManager;

public abstract class GameLogic {

    protected Button[][] buttons;
    protected boolean playerXTurn = true;
    protected int roundCount = 0;
    protected Context context;
    protected boolean gameOver = false;
    protected SoundManager soundManager;

    public GameLogic(Button[][] buttons, Context context, SoundManager soundManager) {
        this.buttons = buttons;
        this.context = context;
        this.soundManager = soundManager;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public abstract void makeMove(int row, int col);

    protected boolean checkForWin() {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (!field[i][0].equals("") &&
                    field[i][0].equals(field[i][1]) &&
                    field[i][0].equals(field[i][2])) {
                return true;
            }

            if (!field[0][i].equals("") &&
                    field[0][i].equals(field[1][i]) &&
                    field[0][i].equals(field[2][i])) {
                return true;
            }
        }

        if (!field[0][0].equals("") &&
                field[0][0].equals(field[1][1]) &&
                field[0][0].equals(field[2][2])) {
            return true;
        }

        if (!field[0][2].equals("") &&
                field[0][2].equals(field[1][1]) &&
                field[0][2].equals(field[2][0])) {
            return true;
        }

        return false;
    }

    protected void disableButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }
}
