// MainMenuActivity.java
// This class represents the main menu screen – המסך הראשי שבו המשתמש בוחר בין המצבים של המשחק

package com.example.tic_tac_toe;

// Imports for Android components used in this Activity – יבוא מחלקות שקשורות לאנדרואיד
import android.content.Intent; // לעבור בין מסכים – מעבר בין Activities
import android.os.Bundle;// Used to pass data and manage activity state // משמש להעברת נתונים וניהול מצב הפעילות
import android.widget.Button;
import androidx.appcompat.app.AlertDialog; // תיבת דיאלוג מודרנית
import androidx.appcompat.app.AppCompatActivity; // מחלקת בסיס לכל Activity באפליקציה

// MainMenuActivity extends AppCompatActivity – יורש ממחלקת Activity רגילה
public class MainMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu); // טוען את קובץ ה־XML של העיצוב למסך הזה

        // יצירת קישור בין משתני ג'אווה לכפתורים במסך לפי ה־id שלהם
        Button btnPvP = findViewById(R.id.btnPvP);         // כפתור: שחקן מול שחקן
        Button btnBot = findViewById(R.id.btnPvBot);       // כפתור: שחקן מול בוט

        // כאשר המשתמש לוחץ על כפתור שחקן מול שחקן
        btnPvP.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, GameActivity.class); // יצירת אינטנט למסך המשחק
            intent.putExtra("bot_mode", false); // שולח מידע לפעילות הבאה: לא מצב בוט
            startActivity(intent); // התחלת הפעילות הבאה
        });

        // כאשר המשתמש לוחץ על כפתור שחקן מול בוט
        btnBot.setOnClickListener(v -> {
            String[] levels = {"easy", "medium", "hard"}; // הגדרת רמות קושי

            // הצגת דיאלוג בחירה לרמת קושי
            new AlertDialog.Builder(MainMenuActivity.this)
                    .setTitle("Choose the difficulty level") // כותרת
                    .setItems(levels, (dialog, which) -> {
                        Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
                        intent.putExtra("bot_mode", true); // שולח מידע שזה כן מצב בוט
                        intent.putExtra("bot_level", levels[which]);  // שולח את רמת הקושי שנבחרה
                        startActivity(intent); // התחלת המשחק
                    })
                    .show(); // הצגת הדיאלוג
        });

        // כפתור יציאה מהמשחק
        Button btnQuit = findViewById(R.id.btnQuit);
        btnQuit.setOnClickListener(v -> {
            // הצגת דיאלוג אישור האם באמת לצאת
            new AlertDialog.Builder(MainMenuActivity.this)
                    .setTitle("Exit Game") // כותרת של הדיאלוג
                    .setMessage("Are you sure you want to quit?") // הודעת אישור
                    .setPositiveButton("Yes", (dialog, which) -> {
                        finishAffinity(); // סוגר את כל הפעילויות ומסיים את האפליקציה
                    })
                    .setNegativeButton("No", null) // לא עושה כלום אם בוחרים לא
                    .show(); // הצגת הדיאלוג
        });
    }
}