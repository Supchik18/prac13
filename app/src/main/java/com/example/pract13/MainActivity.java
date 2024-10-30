package com.example.pract13;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SharedPreferences Themeset;
    SharedPreferences.Editor settingsEditor;
    ImageButton themeBtn;
    boolean isXTurn = true;
    boolean gameEnded = false;
    Button[][] buttons = new Button[3][3];

    int Xwin = 0;
    int Owin = 0;
    int draws = 0;

    RecyclerView recyclerView;
    StatisticsAdapter statisticsAdapter;
    List<GameStatistics> statistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Themeset = getSharedPreferences("SETTINGS", MODE_PRIVATE);

        if (!Themeset.contains("MODE_NIGHT_NO")) {
            settingsEditor = Themeset.edit();
            settingsEditor.putBoolean("MODE_NIGHT_NO", false);
            settingsEditor.apply();
        } else {
            setCurrentTheme();
        }

        setContentView(R.layout.activity_main);

        settingsEditor = Themeset.edit();
        themeBtn = findViewById(R.id.themeButton);
        updateImageTheme();

        themeBtn.setOnClickListener(v -> {
            boolean isLightMode = Themeset.getBoolean("MODE_NIGHT_NO", false);
            if (isLightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                settingsEditor.putBoolean("MODE_NIGHT_NO", false);
                Toast.makeText(MainActivity.this, "Включена тёмная тема", Toast.LENGTH_SHORT).show();
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                settingsEditor.putBoolean("MODE_NIGHT_NO", true);
                Toast.makeText(MainActivity.this, "Включена светлая тема", Toast.LENGTH_SHORT).show();
            }
            settingsEditor.apply();
            updateImageTheme();
        });

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int index = i * 3 + j;
                Button button = (Button) gridLayout.getChildAt(index);
                buttons[i][j] = button;
                int finalI = i;
                int finalJ = j;
                button.setOnClickListener(v -> onButtonClick(button, finalI, finalJ));
            }
        }

        findViewById(R.id.resetButton).setOnClickListener(v -> resetGame());

        loadStatistics();

        recyclerView = findViewById(R.id.recyclerView);
        statistics = new ArrayList<>();
        statistics.add(new GameStatistics("X", Xwin));
        statistics.add(new GameStatistics("O", Owin));
        statistics.add(new GameStatistics("Ничья", draws));

        statisticsAdapter = new StatisticsAdapter(statistics);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(statisticsAdapter);
    }

    private void onButtonClick(Button button, int row, int col) {
        if (gameEnded || !button.getText().toString().equals("")) return;

        button.setText(isXTurn ? "X" : "O");

        if (checkWinner()) {
            if (isXTurn) {
                Xwin++;
                Toast.makeText(this, "Победил X!", Toast.LENGTH_SHORT).show();
            } else {
                Owin++;
                Toast.makeText(this, "Победил O!", Toast.LENGTH_SHORT).show();
            }
            gameEnded = true;
            saveStatistics();
            updateStatistics();
        } else if (isBoardFull()) {
            draws++;
            Toast.makeText(this, "Ничья!", Toast.LENGTH_SHORT).show();
            gameEnded = true;
            saveStatistics();
            updateStatistics();
        } else {
            isXTurn = !isXTurn;
        }
    }

    private void updateStatistics() {
        statistics.clear();
        statistics.add(new GameStatistics("X", Xwin));
        statistics.add(new GameStatistics("O", Owin));
        statistics.add(new GameStatistics("Ничья", draws));
        statisticsAdapter.notifyDataSetChanged();
    }

    private boolean checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(buttons[i][1].getText()) &&
                    buttons[i][1].getText().equals(buttons[i][2].getText()) &&
                    !buttons[i][0].getText().toString().isEmpty()) return true;
            if (buttons[0][i].getText().equals(buttons[1][i].getText()) &&
                    buttons[1][i].getText().equals(buttons[2][i].getText()) &&
                    !buttons[0][i].getText().toString().isEmpty()) return true;
        }
        return (buttons[0][0].getText().equals(buttons[1][1].getText()) &&
                buttons[1][1].getText().equals(buttons[2][2].getText()) &&
                !buttons[0][0].getText().toString().isEmpty()) ||
                (buttons[0][2].getText().equals(buttons[1][1].getText()) &&
                        buttons[1][1].getText().equals(buttons[2][0].getText()) &&
                        !buttons[0][2].getText().toString().isEmpty());
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().toString().isEmpty()) return false;
            }
        }
        return true;
    }

    private void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        isXTurn = true;
        gameEnded = false;
    }

    private void saveStatistics() {
        SharedPreferences sharedPreferences = getSharedPreferences("GAME_STATS", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Xwin", Xwin);
        editor.putInt("Owin", Owin);
        editor.putInt("draws", draws);
        editor.apply();
    }

    private void loadStatistics() {
        SharedPreferences sharedPreferences = getSharedPreferences("GAME_STATS", MODE_PRIVATE);
        Xwin = sharedPreferences.getInt("Xwin", 0);
        Owin = sharedPreferences.getInt("Owin", 0);
        draws = sharedPreferences.getInt("draws", 0);
    }

    private void updateImageTheme() {
        if (Themeset.getBoolean("MODE_NIGHT_NO", false)) {
            themeBtn.setImageResource(R.drawable.sun);
        } else {
            themeBtn.setImageResource(R.drawable.icon);
        }
    }

    private void setCurrentTheme() {
        if (Themeset.getBoolean("MODE_NIGHT_NO", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }
}
