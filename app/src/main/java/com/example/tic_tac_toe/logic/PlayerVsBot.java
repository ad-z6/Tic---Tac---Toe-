// PlayerVsBot.java
// Implements game logic for playing against a bot at three difficulty levels (easy, medium, hard)

package com.example.tic_tac_toe.logic;

// Android Context and UI
import android.content.Context;       // הקשר של האפליקציה (Context)
import android.os.Handler;           // טיפול באירועים עם השהיה (Handler)
import android.widget.Button;        // כפתור (Button)

// Java Utilities
import java.util.ArrayList;         // מערך דינמי (ArrayList)
import java.util.List;              // רשימה (List)
import java.util.Random;            // מחלקה ליצירת מספרים רנדומליים (Random)

public class PlayerVsBot extends GameLogic {
    // Stores current difficulty level: “easy”, “medium”, or “hard”
    private String botLevel;  // רמת קושי של הבוט
    // Interfaces for updating UI and handling game results
    private OnTurnChangeListener turnChangeListener;  // ממשק לשינוי תור
    private OnGameEndListener gameEndListener;        // ממשק לסיום משחק

    // Tracks if it's player X's turn
    private boolean playerXTurn;     // משתנה שמייצג את תור השחקן

    //For Easy Mode
    private final Random random = new Random();       // רנדומליות

    // Used for delay between player and bot turns
    private final Handler botHandler = new Handler(); // מטפל זמן לבוט

    // Constructor: passes board and context, and sets bot difficulty
    public PlayerVsBot(Button[][] buttons, Context context, String level) {
        super(buttons, context);
        this.botLevel = level;
        this.playerXTurn = true;
    }

    // Allows the activity to register a listener for turn change updates
    public void setTurnChangeListener(OnTurnChangeListener listener) {
        this.turnChangeListener = listener;
    }

    // Player move logic (when clicking a button)
    @Override
    public void makeMove(int row, int col) {
        if (!buttons[row][col].getText().toString().equals("")) return;

        buttons[row][col].setText("X"); // השחקן תמיד הוא X
        roundCount++;

        if (checkForWin()) {
            gameOver = true;
            disableButtons();
            if (gameEndListener != null) gameEndListener.onPlayerWin();
            return;
        } else if (roundCount == 9) {
            gameOver = true;
            if (gameEndListener != null) gameEndListener.onDraw();
            return;
        }

        playerXTurn = false;

        // Delay bot's move by 500ms
        botHandler.postDelayed(this::botMove, 500); // דיליי בין תור שחקן לבוט
    }

    // Bot move logic based on difficulty
    private void botMove() {
        if (gameOver) return;

        int[] move;
        switch (botLevel) {
            case "easy":
                move = getRandomMove();              // רמה קלה - תנועה רנדומלית
                break;
            case "medium":
                move = getBlockingMove();           // רמה בינונית - חסימת שחקן
                if (move == null) move = getRandomMove();
                break;
            case "hard":
                String[][] board = new String[3][3]; // לוח לוגי לצורך מינימקס
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        String text = buttons[i][j].getText().toString();
                        if (!text.equals("X") && !text.equals("O")) text = "";
                        board[i][j] = text;
                    }
                }
                move = getBestMove(board);          // רמה קשה - אלגוריתם Minimax
                if (move == null) move = getRandomMove();
                break;
            default:
                move = getRandomMove();
        }

        if (move != null) {
            buttons[move[0]][move[1]].setText("O");
            roundCount++;

            if (checkForWin()) {
                gameOver = true;
                disableButtons();
                if (gameEndListener != null) gameEndListener.onBotWin();
            } else if (isBoardFull()) {
                gameOver = true;
                if (gameEndListener != null) gameEndListener.onDraw();
            } else {
                playerXTurn = true;
                if (turnChangeListener != null) {
                    turnChangeListener.onTurnChanged("Your Turn (You are X)");
                }
            }
        }
    }

    // Returns a random empty cell
    private int[] getRandomMove() {
        List<int[]> availableMoves = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (buttons[i][j].getText().toString().equals(""))
                    availableMoves.add(new int[]{i, j});

        if (availableMoves.isEmpty()) return null;
        return availableMoves.get(random.nextInt(availableMoves.size()));
    }

    // Checks if placing "X" results in win, used in medium mode
    private int[] getBlockingMove() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (buttons[i][j].getText().toString().equals("")) {
                    buttons[i][j].setText("X");
                    if (checkForWin()) {
                        buttons[i][j].setText("");
                        return new int[]{i, j};
                    }
                    buttons[i][j].setText("");
                }

        // Prefer center or corners if no immediate threat
        if (buttons[1][1].getText().toString().equals("")) return new int[]{1, 1};

        int[][] corners = {{0, 0}, {0, 2}, {2, 0}, {2, 2}};
        for (int[] corner : corners) // (for - each)
            if (buttons[corner[0]][corner[1]].getText().toString().equals(""))
                return corner;

        return getRandomMove();
    }

    // Advanced AI using minimax algorithm
    private int[] getBestMove(String[][] board) {
        int bestScore = Integer.MIN_VALUE; // bestScore = -2^31
        int[] bestMove = null;

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j].equals("")) {
                    board[i][j] = "O";
                    int score = minimax(board, 0, false); // אלגוריתם מינימקס
                    board[i][j] = "";
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new int[]{i, j};
                    }
                }

        return bestMove;
    }

    // Minimax recursive function (simplified version)
    private int minimax(String[][] board, int depth, boolean isMaximizing) {
        if (checkWinner(board, "O")) return 10 - depth;
        if (checkWinner(board, "X")) return depth - 10;
        if (isSimulatedBoardFull(board)) return 0;

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    if (board[i][j].equals("")) {
                        board[i][j] = "O";
                        int score = minimax(board, depth + 1, false);
                        board[i][j] = "";
                        bestScore = Math.max(score, bestScore);
                    }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    if (board[i][j].equals("")) {
                        board[i][j] = "X";
                        int score = minimax(board, depth + 1, true);
                        board[i][j] = "";
                        bestScore = Math.min(score, bestScore);
                    }
            return bestScore;
        }
    }

    // Helper methods to check board state
    private boolean checkWinner(String[][] board, String symbol) { //קוד לזיהוי ניצחון
        for (int i = 0; i < 3; i++) {
            if (board[i][0].equals(symbol) && board[i][1].equals(symbol) && board[i][2].equals(symbol)) return true;
            if (board[0][i].equals(symbol) && board[1][i].equals(symbol) && board[2][i].equals(symbol)) return true;
        }

        if (board[0][0].equals(symbol) && board[1][1].equals(symbol) && board[2][2].equals(symbol)) return true;
        if (board[0][2].equals(symbol) && board[1][1].equals(symbol) && board[2][0].equals(symbol)) return true;

        return false;
    }

    private boolean isBoardFull() { //  בודק אם הלוח מלא
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (buttons[i][j].getText().toString().equals(""))
                    return false;
        return true;
    }

    private boolean isSimulatedBoardFull(String[][] board) { // בדיקה בלוח מדומה
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j].equals(""))
                    return false;
        return true;
    }


    // Resets board and variables for a new round
    public void resetBoard() {
        botHandler.removeCallbacksAndMessages(null);
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setText("");
                buttons[row][col].setEnabled(true);
            }
        gameOver = false;
        roundCount = 0;
        playerXTurn = true;

        if (turnChangeListener != null) {
            turnChangeListener.onTurnChanged("Your Turn (You are X)");
        }
    }

    // Returns whether it's currently X player's turn
    // מחזירה אם עכשיו תור של השחקן X
    public boolean isPlayerXTurn() {
        return playerXTurn;
    }

    // Interface for handling turn change notifications
// ממשק להודעות על שינוי תור
    public interface OnTurnChangeListener {
        // Called when the turn changes, receives a message (e.g., “Your Turn”)
// מופעל כאשר התור משתנה, מקבל הודעה
        void onTurnChanged(String message);
    }

    // Interface for handling game end events (win, lose, draw)
// ממשק לטיפול בסיום משחק – ניצחון, הפסד או תיקו
    public interface OnGameEndListener {
        void onPlayerWin();   // Called when the human player wins - מופעל כששחקן מנצח
        void onBotWin();      // Called when the bot wins - מופעל כשבוט מנצח
        void onDraw();        // Called when the game ends in a draw - מופעל בתיקו
    }

    // Setter method to connect the game with a listener object
// פונקציה שמחברת מאזין חיצוני לאירועים של סיום משחק
    public void setGameEndListener(OnGameEndListener listener) {
        this.gameEndListener = listener;
    }

}