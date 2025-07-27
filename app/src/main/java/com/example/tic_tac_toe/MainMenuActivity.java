// MainMenuActivity.java
// This class manages the main screen – המסך הראשי של המשחק
// Allows user to choose game mode (PvP / PvBot) or quit the game

package com.example.tic_tac_toe;

// ========== Imports ==========
import android.content.Intent;         // Used to navigate between activities – מעבר בין מסכים (Intent)
import android.os.Bundle;              // Used for passing data and restoring activity state – אובייקט נתונים בעת יצירת Activity
import android.widget.Button;          // UI element for user interaction – כפתור
import androidx.appcompat.app.AlertDialog;   // A modern dialog box – תיבת דיאלוג
import androidx.appcompat.app.AppCompatActivity; // Base class for activities – מחלקת בסיס לכל מסך (Activity)

public class MainMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        // Connects the activity to its XML layout – קישור בין הקוד לקובץ העיצוב (layout)

        // ========== Button Initialization ==========
        Button btnPvP = findViewById(R.id.btnPvP);     // Button: Player vs Player – שחקן מול שחקן
        Button btnBot = findViewById(R.id.btnPvBot);   // Button: Player vs Bot – שחקן מול מחשב
        Button btnQuit = findViewById(R.id.btnQuit);   // Button: Quit – יציאה

        // ========== Player vs Player Mode ==========
        btnPvP.setOnClickListener(v -> {
            // Create an Intent to move to GameActivity – יצירת מעבר בין מסכים
            Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
            intent.putExtra("bot_mode", false); // Indicates PvP mode – שליחת פרמטר שמבטל מצב בוט
            startActivity(intent);              // Start the GameActivity – התחלת המסך הבא
        });

        // ========== Player vs Bot Mode with Difficulty Selection ==========
        btnBot.setOnClickListener(v -> {
            String[] levels = {"easy", "medium", "hard"};  // Difficulty levels – רמות קושי

            new AlertDialog.Builder(MainMenuActivity.this)
                    .setTitle("Choose the difficulty level")  // Dialog title – כותרת הדיאלוג
                    .setItems(levels, (dialog, which) -> {
                        // Create an intent and send bot_mode and selected difficulty – שליחת מצב בוט ורמה
                        Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
                        intent.putExtra("bot_mode", true);
                        intent.putExtra("bot_level", levels[which]);
                        startActivity(intent); // Start game in PvBot mode – התחלת משחק מול מחשב
                    })
                    .show(); // Display the dialog – הצגת הדיאלוג
        });

        // ========== Quit Button Logic ==========
        btnQuit.setOnClickListener(v -> {
            new AlertDialog.Builder(MainMenuActivity.this)
                    .setTitle("Exit Game")                      // Title – כותרת
                    .setMessage("Are you sure you want to quit?") // Confirmation message – הודעת אישור
                    .setPositiveButton("Yes", (dialog, which) -> {
                        finishAffinity(); // Exit the app completely – סוגר את כל המסכים והאפליקציה
                    })
                    .setNegativeButton("No", null) // Dismiss dialog – ביטול הפעולה
                    .show();
        });
    }
}