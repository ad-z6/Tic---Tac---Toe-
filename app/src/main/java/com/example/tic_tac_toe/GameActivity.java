package com.example.tic_tac_toe;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tic_tac_toe.logic.PlayerVsBot;
import com.example.tic_tac_toe.utils.SoundManager;

public class GameActivity extends AppCompatActivity {

    private int playerScore = 0;
    private int botScore = 0;
    private int scoreX = 0;
    private int scoreO = 0;

    private String botLevel = "eazy";
    private SoundManager soundManager;
    private PlayerVsBot playerVsBot;
    private Button[][] buttons = new Button[3][3];
    private TextView tvTurn;
    private TextView playerScoreText, botScoreText;
    private boolean botMode;
    private String currentPlayer = "X";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tvTurn = findViewById(R.id.tvTurn);
        soundManager = new SoundManager(this);

        playerScoreText = findViewById(R.id.playerScoreText);
        botScoreText = findViewById(R.id.botScoreText);

        buttons[0][0] = findViewById(R.id.btn00);
        buttons[0][1] = findViewById(R.id.btn01);
        buttons[0][2] = findViewById(R.id.btn02);
        buttons[1][0] = findViewById(R.id.btn10);
        buttons[1][1] = findViewById(R.id.btn11);
        buttons[1][2] = findViewById(R.id.btn12);
        buttons[2][0] = findViewById(R.id.btn20);
        buttons[2][1] = findViewById(R.id.btn21);
        buttons[2][2] = findViewById(R.id.btn22);

        botMode = getIntent().getBooleanExtra("bot_mode", false);
        botLevel = getIntent().getStringExtra("bot_level");
        if (botLevel == null) botLevel = "eazy";

        if (botMode) {
            playerScoreText.setText("You: 0");
            botScoreText.setText("Bot: 0");
            startGameAgainstBot();
        } else {
            playerScoreText.setText("X: 0");
            botScoreText.setText("O: 0");
            startGamePvP();
        }

        Button btnBack = findViewById(R.id.btnBackToMenu);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(GameActivity.this, MainMenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

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

    private void startGameAgainstBot() {
        tvTurn.setText("Your Turn (You are X)");

        // 👇 إنشاء كائن PlayerVsBot مرة واحدة فقط
        playerVsBot = new PlayerVsBot(buttons, this, soundManager, botLevel);

        // 👇 ربط الأحداث مرة واحدة
        playerVsBot.setTurnChangeListener(message -> tvTurn.setText(message));

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

        // 👇 ربط OnClick مرة واحدة فقط هنا
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int finalRow = row;
                int finalCol = col;
                buttons[row][col].setOnClickListener(v -> {
                    if (playerVsBot.isGameOver()) return;

                    playerVsBot.makeMove(finalRow, finalCol);

                    // ✅ تحديث الدور حسب من يلعب
                    if (!playerVsBot.isPlayerXTurn()) {
                        tvTurn.setText("Bot's Turn...");
                    } else {
                        tvTurn.setText("Your Turn (You are X)");
                    }
                });
            }
        }
    }

    private void startGamePvP() {
        currentPlayer = "X";
        tvTurn.setText("Turn: " + currentPlayer);

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int finalRow = row;
                int finalCol = col;

                buttons[row][col].setOnClickListener(v -> {
                    if (!buttons[finalRow][finalCol].getText().toString().equals("")) return;

                    buttons[finalRow][finalCol].setText(currentPlayer);

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

                    if (isBoardFull()) {
                        showDrawDialog();
                        return;
                    }

                    currentPlayer = currentPlayer.equals("X") ? "O" : "X";
                    tvTurn.setText("Turn: " + currentPlayer);
                });
            }
        }
    }

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

    private boolean isBoardFull() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (buttons[row][col].getText().toString().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void showWinDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Resut 🏆")
                .setMessage(message)
                .setPositiveButton("Play again", (dialog, which) -> resetBoard())
                .setNegativeButton("Exit", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    private void showDrawDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Draw 🤝")
                .setMessage("Nobody won this time.")
                .setPositiveButton("Play again", (dialog, which) -> resetBoard())
                .setNegativeButton("Exit", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    private void resetBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setText("");
                buttons[row][col].setEnabled(true);
            }
        }

        if (botMode) {
            playerVsBot.resetBoard(); // فقط إعادة تعيين دون إنشاء كائن جديد

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