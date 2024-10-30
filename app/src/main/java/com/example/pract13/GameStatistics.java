package com.example.pract13;

public class GameStatistics {
    private String player;
    private int wins;

    public GameStatistics(String player, int wins) {
        this.player = player;
        this.wins = wins;
    }

    public String getPlayer() {
        return player;
    }

    public int getWins() {
        return wins;
    }
}
