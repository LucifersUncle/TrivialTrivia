package dk.au.mad22spring.appproject.trivialtrivia.Models;

import java.util.HashMap;
import java.util.UUID;

public class Game {
    private String gameName, category, difficulty;
    private String documentName;
    private int timePerRound, numberOfRounds, currentRound;

    private HashMap<String, Player> players;

    //game states
    private boolean gameIsActive, gameIsStarted;

    public Game() {
        //empty constructor to access object methods
    }

    public Game(String gameName, int timePerRound, int numberOfRounds, String hostName, String documentName, boolean gameIsActive, boolean gameIsStarted, String category, String difficulty) {
        this.gameName = gameName;
        this.timePerRound = timePerRound;
        this.numberOfRounds = numberOfRounds;
        this.currentRound = 1;
        this.gameIsActive = gameIsActive;
        this.gameIsStarted = gameIsStarted;
        this.category = category;
        this.difficulty = difficulty;
        this.documentName = documentName;

        String playerId = UUID.randomUUID().toString();

        Player playerHost = new Player(playerId, "host", hostName,0);

        this.players = new HashMap<>();
        players.put(playerId, playerHost);
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

    public void setCategory(String category) {
        this.category = category;
    }
    public String getCategory() {
        return category;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    public String getDifficulty() {
        return difficulty;
    }

    public boolean gameIsActive() {
        return gameIsActive;
    }
    public void setGameIsActive(boolean active) {
        gameIsActive = active;
    }

    public boolean gameIsStarted() {
        return gameIsStarted;
    }
    public void setGameIsStarted(boolean started) {
        gameIsStarted = started;
    }

    public String getDocumentName() {
        return documentName;
    }
    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

}
