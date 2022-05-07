package dk.au.mad22spring.appproject.trivialtrivia.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import dk.au.mad22spring.appproject.trivialtrivia.Database.Database;
import dk.au.mad22spring.appproject.trivialtrivia.Models.ActiveGame;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Player;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Question;

public class ActiveGameViewModel extends AndroidViewModel {

    private Database db;
    public ActiveGameViewModel(@NonNull Application application) {
        super(application);
        db = Database.getInstance(application);
    }


    public LiveData<ActiveGame> getActiveGame(String documentId) {
        return db.getActiveGame(documentId);
    }

    public LiveData<List<Question>> getQuestions(String documentId){
        return db.fetchQuestions(documentId);
    }

    public LiveData<Question> getCurrentQuestion(String documentId) {
        return db.getCurrentQuestion(documentId);
    }

    public LiveData<Boolean> getActiveState(String documentID) {
        return db.getActiveState(documentID);
    }

    public LiveData<List<Player>> getPlayers(String documentID) {
        return db.getPlayersInGame(documentID);
    }

    public LiveData<Player> getPlayer(String documentID, String playerReference) {
        return db.getPlayer(documentID, playerReference);
    }


}
