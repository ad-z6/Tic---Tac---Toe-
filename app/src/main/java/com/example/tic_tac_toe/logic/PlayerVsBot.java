package com.example.tic_tac_toe.logic;

import android.content.Context; import android.os.Handler; import android.widget.Button;

import java.util.ArrayList; import java.util.List; import java.util.Random;

public class PlayerVsBot extends GameLogic {

    private String botLevel;
    private OnTurnChangeListener turnChangeListener;
    private boolean playerXTurn;
    private final Random random = new Random();
    private final Handler botHandler = new Handler();

    public PlayerVsBot(Button[][] buttons, Context context, String level) {
        super(buttons, context);
        this.botLevel = level;
        this.playerXTurn = true;
    }

    public void setTurnChangeListener(OnTurnChangeListener listener) {
        this.turnChangeListener = listener;
    }

    @Override
    public void makeMove(int row, int col) {
        if (!buttons[row][col].getText().toString().equals("")) return;

        buttons[row][col].setText("X");
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

        botHandler.postDelayed(this::botMove, 500);
    }

    private void botMove() {
        if (gameOver) return;

        int[] move;
        switch (botLevel) {
            case "easy":
                move = getRandomMove();
                break;
            case "medium":
                move = getBlockingMove();
                if (move == null) move = getRandomMove();
                break;
            case "hard":
                String[][] board = new String[3][3];
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        String text = buttons[i][j].getText().toString();
                        if (!text.equals("X") && !text.equals("O")) text = "";
                        board[i][j] = text;
                    }
                }
                move = getBestMove(board);
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

    private int[] getRandomMove() {
        List<int[]> availableMoves = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (buttons[i][j].getText().toString().equals(""))
                    availableMoves.add(new int[]{i, j});

        if (availableMoves.isEmpty()) return null;
        return availableMoves.get(random.nextInt(availableMoves.size()));
    }

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

        if (buttons[1][1].getText().toString().equals("")) return new int[]{1, 1};

        int[][] corners = {{0, 0}, {0, 2}, {2, 0}, {2, 2}};
        for (int[] corner : corners)
            if (buttons[corner[0]][corner[1]].getText().toString().equals(""))
                return corner;

        return getRandomMove();
    }

    private int[] getBestMove(String[][] board) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j].equals("")) {
                    board[i][j] = "O";
                    int score = minimax(board, 0, false);
                    board[i][j] = "";
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new int[]{i, j};
                    }
                }

        return bestMove;
    }

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
                    } //test
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

    private boolean checkWinner(String[][] board, String symbol) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0].equals(symbol) && board[i][1].equals(symbol) && board[i][2].equals(symbol)) return true;
            if (board[0][i].equals(symbol) && board[1][i].equals(symbol) && board[2][i].equals(symbol)) return true;
        }

        if (board[0][0].equals(symbol) && board[1][1].equals(symbol) && board[2][2].equals(symbol)) return true;
        if (board[0][2].equals(symbol) && board[1][1].equals(symbol) && board[2][0].equals(symbol)) return true;

        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (buttons[i][j].getText().toString().equals(""))
                    return false;
        return true;
    }

    private boolean isSimulatedBoardFull(String[][] board) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j].equals(""))
                    return false;
        return true;
    }

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
            turnChangeListener.onTurnChanged("دورك (أنت X)");
        }
    }

    public boolean isPlayerXTurn() {
        return playerXTurn;
    }

    public interface OnTurnChangeListener {
        void onTurnChanged(String message);
    }

    public interface OnGameEndListener {
        void onPlayerWin();
        void onBotWin();
        void onDraw();
    }

    private OnGameEndListener gameEndListener;

    public void setGameEndListener(OnGameEndListener listener) {
        this.gameEndListener = listener;
    }

}