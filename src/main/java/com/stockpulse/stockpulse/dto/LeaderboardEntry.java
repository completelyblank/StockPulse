package com.stockpulse.stockpulse.dto;

public class LeaderboardEntry {
    private int rank;
    private String username;
    private double totalValue;
    private double gainLoss;

    public LeaderboardEntry(int rank, String username, double totalValue, double gainLoss) {
        this.rank = rank;
        this.username = username;
        this.totalValue = totalValue;
        this.gainLoss = gainLoss;
    }

    public int getRank() {
        return rank;
    }

    public String getUsername() {
        return username;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public double getGainLoss() {
        return gainLoss;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public void setGainLoss(double gainLoss) {
        this.gainLoss = gainLoss;
    }
}
