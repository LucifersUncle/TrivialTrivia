package dk.au.mad22spring.appproject.trivialtrivia.Models;

public class Game {
    private String gameName;

    private int timePerRound, numberOfRounds, currentRound;

    //game states
    private boolean gameIsActive, gameIsStarted;

    Game() {
        //empty constructor to access object methods
    }

    Game(String gameName, int timePerRound, int numberOfRounds, boolean gameIsActive, boolean gameIsStarted) {
        this.gameName = gameName;
        this.timePerRound = timePerRound;
        this.numberOfRounds = numberOfRounds;
        this.currentRound = 1;
        this.gameIsActive = gameIsActive;
        this.gameIsStarted = gameIsStarted;
    }
}
