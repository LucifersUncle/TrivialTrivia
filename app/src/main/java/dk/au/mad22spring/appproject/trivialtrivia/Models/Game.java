package dk.au.mad22spring.appproject.trivialtrivia.Models;

import java.util.HashMap;

public class Game {
    private String gameName;

    private int timePerRound, numberOfRounds, currentRound;

    private HashMap<String, Player> players;
    //game states
    private boolean gameIsActive, gameIsStarted;

    public Game() {
        //empty constructor to access object methods
    }

    public Game(String gameName, int timePerRound, int numberOfRounds, String hostName, String documentName, boolean gameIsActive, boolean gameIsStarted) {
        this.gameName = gameName;
        this.timePerRound = timePerRound;
        this.numberOfRounds = numberOfRounds;
        this.currentRound = 1;
        this.gameIsActive = gameIsActive;
        this.gameIsStarted = gameIsStarted;
    }

    public void setNumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }
    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setTimePerRound(int timePerRound) {
        this.timePerRound = timePerRound;
    }
    public int getTimePerRound() {
        return timePerRound;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
    public String getGameName() {
        return gameName;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }
    public int getCurrentRound() {
        return currentRound;
    }
}
