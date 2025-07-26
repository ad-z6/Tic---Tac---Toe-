// PlayerVsPlayer.java
// Logic class for Player vs Player mode – מצב של שחקן נגד שחקן
package com.example.tic_tac_toe.logic;

// Importing AlertDialog for displaying game results – יבוא של AlertDialog לצורך תוצאות המשחק
import android.app.AlertDialog;

// Context is used for UI interaction – קונטקסט (Context) משמש לגישה למשאבים ולהצגת דיאלוגים
import android.content.Context;

// Button class for accessing the buttons of the game board – כפתורים בלוח המשחק
import android.widget.Button;

// This class inherits (بورث) the core game logic and customizes it for PvP – מחלקה שיורשת את הלוגיקה הכללית ומיישמת אותה עבור מצב PvP
public class PlayerVsPlayer extends GameLogic {
    // Constructor – receives the game board and the context – בנאי שמקבל את הלוח והקונטקסט
    public PlayerVsPlayer(Button[][] buttons, Context context) {
        super(buttons, context); // Call to superclass constructor – קריאה לבנאי במחלקה העליונה
    }

    // Override the abstract method to implement PvP game moves – מימוש ספציפי של מהלך עבור שני שחקנים
    @Override
    public void makeMove(int row, int col) {
        // Ignore if cell is not empty – אם התא לא ריק, לא לעשות כלום
        if (!buttons[row][col].getText().toString().equals("")) return;

        // Set text to X or O depending on the current turn – הגדרת טקסט על הכפתור לפי השחקן הנוכחי
        if (playerXTurn) {
            buttons[row][col].setText("X");
        } else {
            buttons[row][col].setText("O");
        }

        roundCount++; // Increase move count – מגדיל את מונה התורות

        if (checkForWin()) {
            gameOver = true; // If someone won, mark game over – אם יש מנצח, המשחק נגמר
            showResultDialog((playerXTurn ? "X" : "O") + " Win!"); // Show result – הצגת הודעה של ניצחון
            disableButtons(); // Disable further play – חסימת הלוח
        } else if (roundCount == 9) {
            gameOver = true; // If no empty cells left, it’s a draw – אם כל הלוח מלא ואין מנצח → תיקו
            showResultDialog("It's a draw!");
        } else {
            playerXTurn = !playerXTurn; // Toggle turn – החלפת תור
        }
    }

    // Show a dialog box to present the result – דיאלוג להצגת תוצאת המשחק
    private void showResultDialog(String message) {
        new AlertDialog.Builder(context) // Create AlertDialog – יצירת דיאלוג חדש
                .setTitle("The Game is over") // Dialog title – כותרת
                .setMessage(message)         // Dialog message – הודעה
                .setCancelable(false)        // Cannot cancel manually – אי אפשר לבטל עם Back
                .setPositiveButton("Back to list", (dialog, which) -> {
                    dialog.dismiss();       // Close dialog – סגירת דיאלוג
                    if (context instanceof android.app.Activity) {
                        ((android.app.Activity) context).finish(); // Close activity – סגירת המסך
                    }
                })
                .show(); // Show dialog – הצגה
    }
}
