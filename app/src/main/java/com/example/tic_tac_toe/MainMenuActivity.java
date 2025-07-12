package com.example.tic_tac_toe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button btnPvP = findViewById(R.id.btnPvP);
        Button btnBot = findViewById(R.id.btnPvBot);

        btnPvP.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
            intent.putExtra("bot_mode", false); // لاعب ضد لاعب
            startActivity(intent);
        });

        btnBot.setOnClickListener(v -> {
            String[] levels = {"easy", "medium", "hard"};

            new AlertDialog.Builder(MainMenuActivity.this)
                    .setTitle("Choose the difficuty level")
                    .setItems(levels, (dialog, which) -> {
                        Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
                        intent.putExtra("bot_mode", true);
                        intent.putExtra("bot_level", levels[which]);  // نرسل المستوى المختار
                        startActivity(intent);
                    })
                    .show();
        });

        Button btnQuit = findViewById(R.id.btnQuit);
        btnQuit.setOnClickListener(v -> {
            new AlertDialog.Builder(MainMenuActivity.this)
                    .setTitle("Exit Game")
                    .setMessage("Are you sure you want to quit?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        finishAffinity(); // يغلق التطبيق بالكامل
                    })
                    .setNegativeButton("No", null) // لا يفعل شيئًا إذا ضغط "No"
                    .show();
        });
    }
}