// GameLogic.java
// Abstract base class for game logic in Tic Tac Toe
// This class defines shared logic for both Player vs Player and Player vs Bot modes.
// מחלקה אבסטרקטית - לוגיקה משותפת של המשחק גם למצב שחקן מול שחקן וגם שחקן מול בוט

package com.example.tic_tac_toe.logic;

// Imports the Context class from the Android SDK
// Context is used to access application-specific resources and classes
import android.content.Context;
// קונטקסט (Context) - מאפשר גישה למשאבים של האפליקציה

// Imports the Button class from the Android SDK
// Button represents a UI component that users can click in the game board
import android.widget.Button;
// כפתור (Button) - רכיב ממשק גרפי לחיצה

public abstract class GameLogic {
    // 2D array holding the buttons of the game board (3x3)
    protected Button[][] buttons;
// מערך דו-מימדי (Two-dimensional array) – לוח המשחק

    // Keeps track of whose turn it is (true = X, false = O)
    protected boolean playerXTurn = true;
// בוליאני (boolean) – משתנה לבדיקת תור השחקן

    // Number of moves played in current game
    protected int roundCount = 0;
// מונה סיבובים (round count)

    // Reference to Android Context (used for UI features, if needed)
    protected Context context;
// הפניה לקונטקסט של האפליקציה

    // Indicates whether the game is over
    protected boolean gameOver = false;
// דגל (flag) – משתנה שמציין האם המשחק נגמר

    // Constructor – sets the board buttons and context
    public GameLogic(Button[][] buttons, Context context) {
        this.buttons = buttons;
        this.context = context;
    }
// בנאי (Constructor) – אתחול משתנים בעת יצירת אובייקט

    // Getter to check if game is over
    public boolean isGameOver() {
        return gameOver;
    }
// גטר (Getter) – מחזיר ערך של משתנה

    // Abstract method – implemented differently by PvP and PvBot subclasses
    public abstract void makeMove(int row, int col);
// פונקציה אבסטרקטית (Abstract Method) – מחייבת מימוש במחלקות יורשות

    // Checks if there is a winning condition on the board
    protected boolean checkForWin() {
        // Convert button texts into a 2D string array for easier checking
        String[][] field = new String[3][3];
        // המרת טקסטים ממערך כפתורים למערך מחרוזות

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }
        // לולאות לקליטת מצב הלוח

        // Check all rows and columns for a win
        for (int i = 0; i < 3; i++) {
            // Check row i العرض
            if (!field[i][0].equals("") &&
                    field[i][0].equals(field[i][1]) &&
                    field[i][0].equals(field[i][2])) {
                return true;
            }

            // Check column i الطول
            if (!field[0][i].equals("") &&
                    field[0][i].equals(field[1][i]) &&
                    field[0][i].equals(field[2][i])) {
                return true;
            }
        }
        // בדיקת ניצחון בשורות ועמודות

        // Check main diagonal
        if (!field[0][0].equals("") &&
                field[0][0].equals(field[1][1]) &&
                field[0][0].equals(field[2][2])) {
            return true;
        }
        // בדיקת אלכסון ראשי

        // Check anti-diagonal
        if (!field[0][2].equals("") &&
                field[0][2].equals(field[1][1]) &&
                field[0][2].equals(field[2][0])) {
            return true;
        }
        // בדיקת אלכסון הפוך

        // No win found
        return false;
    }
// פונקציית בדיקה אם יש מנצח (Winning logic)

    // Disables all buttons on the board (called when the game is over)
    protected void disableButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }
// ביטול לחיצה על כפתורים (Disable UI interaction) בסיום המשחק
}
