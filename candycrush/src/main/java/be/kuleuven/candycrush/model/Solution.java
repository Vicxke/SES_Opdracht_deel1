package be.kuleuven.candycrush.model;

import java.util.ArrayList;

public class Solution {
    private ArrayList<Swap> swaps;
    private int score;
    private Board bord;
    private boolean completed;

    public Solution(int score, ArrayList<Swap> swaps, Board bord) {
        this.swaps = swaps;
        this.score = score;
        this.bord = bord;
        this.completed = false;
    }

    public boolean isBetterThan(Solution other) {
        if (this.score > other.score) {
            return true;
        }
        if(this.score == other.score) {
            if(this.swaps.size() < other.swaps.size()) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Swap> getSwaps() {
        return swaps;
    }

    public void setSwaps(ArrayList<Swap> swaps) {
        this.swaps = swaps;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Board getBord() {
        return bord;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted() {
        this.completed = true;
    }
}
