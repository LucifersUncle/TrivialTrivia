package dk.au.mad22spring.appproject.trivialtrivia.Models;

public class ActiveGame {
    private String gameName;
    private String documentId;
    private int timePerRound;
    private int numberOfRounds;
    private int currentRound;

    public ActiveGame() {}

    public ActiveGame(String gameName, String documentId, int timePerRound, int numberOfRounds, int currentRound) {
        this.gameName = gameName;
        this.documentId = documentId;
        this.timePerRound = timePerRound;
        this.numberOfRounds = numberOfRounds;
        this.currentRound = currentRound;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public int getTimePerRound() {
        return timePerRound;
    }

    public void setTimePerRound(int timePerRound) {
        this.timePerRound = timePerRound;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setNumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }
}
