// GameActivity.java
// This class manages the main game screen – handles both Player vs Player and Player vs Bot modes

package com.example.tic_tac_toe;

// Android imports – רכיבי מערכת אנדרואיד
import android.app.AlertDialog;        // תיבת דיאלוג
import android.content.Intent;         // Intent למעבר בין מסכים
import android.os.Bundle;              // ניהול מצב activity
import android.widget.Button;          // כפתורים
import android.widget.TextView;        // תיבת טקסט

import androidx.appcompat.app.AppCompatActivity; // Activity בסיסית עם תמיכה מלאה

// Logic class for Bot gameplay
import com.example.tic_tac_toe.logic.PlayerVsBot;

public class GameActivity extends AppCompatActivity {

    // Score tracking for each player
    private int playerScore = 0;   // ניקוד השחקן מול הבוט
    private int botScore = 0;      // ניקוד הבוט
    private int scoreX = 0;        // ניקוד לשחקן X
    private int scoreO = 0;        // ניקוד לשחקן O

    // Bot difficulty level, default to "easy"
    private String botLevel = "eazy";

    // Game logic instance (only used in bot mode)
    private PlayerVsBot playerVsBot;

    // 2D array of buttons – מייצג את לוח המשחק
    private Button[][] buttons = new Button[3][3];

    // UI elements
    private TextView tvTurn;               // תור נוכחי
    private TextView playerScoreText;      // תיבת טקסט לניקוד שחקן
    private TextView botScoreText;         // תיבת טקסט לניקוד בוט / שחקן שני

    // Game mode and current player
    private boolean botMode;               // מצב בוט או לא
    private String currentPlayer = "X";    // X מתחיל תמיד

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game); // טוען את עיצוב המסך

        // קישור הרכיבים הגרפיים לפי מזהי XML
        tvTurn = findViewById(R.id.tvTurn);
        playerScoreText = findViewById(R.id.playerScoreText);
        botScoreText = findViewById(R.id.botScoreText);

        // קישור כל כפתור בלוח המשחק
        buttons[0][0] = findViewById(R.id.btn00);
        buttons[0][1] = findViewById(R.id.btn01);
        buttons[0][2] = findViewById(R.id.btn02);
        buttons[1][0] = findViewById(R.id.btn10);
        buttons[1][1] = findViewById(R.id.btn11);
        buttons[1][2] = findViewById(R.id.btn12);
        buttons[2][0] = findViewById(R.id.btn20);
        buttons[2][1] = findViewById(R.id.btn21);
        buttons[2][2] = findViewById(R.id.btn22);

        // קבלת מצב המשחק והקושי מהמסך הראשי
        botMode = getIntent().getBooleanExtra("bot_mode", false);
        botLevel = getIntent().getStringExtra("bot_level");
        if (botLevel == null) botLevel = "eazy"; // ערך ברירת מחדל

        // קביעת טקסטים בהתאם למצב המשחק
        if (botMode) {
            playerScoreText.setText("You: 0");
            botScoreText.setText("Bot: 0");
            startGameAgainstBot();
        } else {
            playerScoreText.setText("X: 0");
            botScoreText.setText("O: 0");
            startGamePvP();
        }

        // כפתור חזרה לתפריט
        // back to list
        Button btnBack = findViewById(R.id.btnBackToMenu);
        btnBack.setOnClickListener(v -> {
            // Create an Intent to navigate from GameActivity to MainMenuActivity
            Intent intent = new Intent(GameActivity.this, MainMenuActivity.class);

            // Set flags to control the behavior of the navigation:
            // FLAG_ACTIVITY_CLEAR_TOP – if MainMenuActivity is already running in the task,
            // clears all other activities on top of it before launching the intent
            // FLAG_ACTIVITY_NEW_TASK – starts the activity in a new task (used for safety in some contexts)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // כפתור איפוס ניקוד
        Button resetScoreButton = findViewById(R.id.resetScoreButton);
        resetScoreButton.setOnClickListener(v -> {
            playerScore = 0;
            botScore = 0;
            scoreX = 0;
            scoreO = 0;
            if (botMode) {
                playerScoreText.setText("You: 0");
                botScoreText.setText("Bot: 0");
            } else {
                playerScoreText.setText("X: 0");
                botScoreText.setText("O: 0");
            }
        });
    }

    // Starts a new game against the bot
    private void startGameAgainstBot() {
        tvTurn.setText("Your Turn (You are X)");

        // יצירת האובייקט פעם אחת בלבד
        playerVsBot = new PlayerVsBot(buttons, this, botLevel);

        // חיבור עדכוני תור למסך
        playerVsBot.setTurnChangeListener(message -> tvTurn.setText(message));

        // חיבור תוצאות משחק (ניצחון, הפסד, תיקו)
        playerVsBot.setGameEndListener(new PlayerVsBot.OnGameEndListener() {
            @Override
            public void onPlayerWin() {
                playerScore++;
                playerScoreText.setText("You: " + playerScore);
                showWinDialog("You beat the bot 🎉");
            }

            @Override
            public void onBotWin() {
                botScore++;
                botScoreText.setText("The Bot: " + botScore);
                showWinDialog("You lost! The bot won 😢");
            }

            @Override
            public void onDraw() {
                showDrawDialog();
            }
        });

        // קביעת פעולת לחיצה לכל כפתור
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int finalRow = row;
                int finalCol = col;
                buttons[row][col].setOnClickListener(v -> {
                    if (playerVsBot.isGameOver()) return;

                    playerVsBot.makeMove(finalRow, finalCol);

                    // עדכון התור
                    if (!playerVsBot.isPlayerXTurn()) {
                        tvTurn.setText("Bot's Turn...");
                    } else {
                        tvTurn.setText("Your Turn (You are X)");
                    }
                });
            }
        }
    }

    // Starts a new game in Player vs Player mode
    private void startGamePvP() {
        currentPlayer = "X";
        tvTurn.setText("Turn: " + currentPlayer);

        // קביעת פעולת לחיצה לשחקן מול שחקן
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int finalRow = row;
                int finalCol = col;
                buttons[row][col].setOnClickListener(v -> {
                    if (!buttons[finalRow][finalCol].getText().toString().equals("")) return;

                    buttons[finalRow][finalCol].setText(currentPlayer);

                    // בדיקת ניצחון
                    if (checkForWin(currentPlayer)) {
                        if (currentPlayer.equals("X")) {
                            scoreX++;
                        } else {
                            scoreO++;
                        }
                        playerScoreText.setText("X: " + scoreX);
                        botScoreText.setText("O: " + scoreO);
                        showWinDialog("The Winner: " + currentPlayer);
                        return;
                    }

                    // בדיקת תיקו
                    if (isBoardFull()) {
                        showDrawDialog();
                        return;
                    }

                    // החלפת תור
                    currentPlayer = currentPlayer.equals("X") ? "O" : "X";
                    tvTurn.setText("Turn: " + currentPlayer);
                });
            }
        }
    }

    // Win check logic for current symbol
    private boolean checkForWin(String symbol) {
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().toString().equals(symbol) &&
                    buttons[i][1].getText().toString().equals(symbol) &&
                    buttons[i][2].getText().toString().equals(symbol)) return true;

            if (buttons[0][i].getText().toString().equals(symbol) &&
                    buttons[1][i].getText().toString().equals(symbol) &&
                    buttons[2][i].getText().toString().equals(symbol)) return true;
        }

        if (buttons[0][0].getText().toString().equals(symbol) &&
                buttons[1][1].getText().toString().equals(symbol) &&
                buttons[2][2].getText().toString().equals(symbol)) return true;

        if (buttons[0][2].getText().toString().equals(symbol) &&
                buttons[1][1].getText().toString().equals(symbol) &&
                buttons[2][0].getText().toString().equals(symbol)) return true;

        return false;
    }

    // Checks if all cells are filled
    private boolean isBoardFull() {
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 3; col++)
                if (buttons[row][col].getText().toString().equals(""))
                    return false;
        return true;
    }

    // Shows win dialog with option to play again
    private void showWinDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Result 🏆")
                .setMessage(message)
                .setPositiveButton("Play again", (dialog, which) -> resetBoard())
                .setNegativeButton("Exit", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    // Shows draw dialog with option to play again
    private void showDrawDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Draw 🤝")
                .setMessage("Nobody won this time.")
                .setPositiveButton("Play again", (dialog, which) -> resetBoard())
                .setNegativeButton("Exit", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    // Resets the board for a new round
    private void resetBoard() {
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setText("");
                buttons[row][col].setEnabled(true);
            }

        if (botMode) {
            playerVsBot.resetBoard();
            if (playerVsBot.isPlayerXTurn()) {
                tvTurn.setText("Your Turn (You are X)");
            } else {
                tvTurn.setText("Bot's Turn...");
            }
        } else {
            currentPlayer = "X";
            tvTurn.setText("Turn: X");
        }
    }
}