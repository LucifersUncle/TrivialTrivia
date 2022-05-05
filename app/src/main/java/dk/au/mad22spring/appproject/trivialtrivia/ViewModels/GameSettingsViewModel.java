package dk.au.mad22spring.appproject.trivialtrivia.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import dk.au.mad22spring.appproject.trivialtrivia.Database.Database;

public class GameSettingsViewModel extends AndroidViewModel {
    private Database db;

    public GameSettingsViewModel(@NonNull Application application) {
        super(application);

        db  = Database.getInstance(application);
    }

    public String addGameToDb(String gameName, int timePerRound, int numberOfRounds, String playerName, String category, String difficulty) {
        return db.addGame(gameName, timePerRound, numberOfRounds, playerName, category, difficulty);
    }


}
