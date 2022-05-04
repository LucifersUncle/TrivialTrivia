package dk.au.mad22spring.appproject.trivialtrivia.Models;

import java.io.Serializable;

public class Player implements Serializable {
    private String playerName, role, playerReference;
    private int score;

    Player() {
        //empty constructor for accessing object
    }

    public Player(String playerName, String role, String playerReference, int score) {
        this.playerName = playerName;
        this.role = role;
        this.playerReference = playerReference;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public String getPlayerRef() {
        return playerReference;
    }
    public void setPlayerRef(String playerRef) {
        this.playerReference = playerRef;
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }



}
