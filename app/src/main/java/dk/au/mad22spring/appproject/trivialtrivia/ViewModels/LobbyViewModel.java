package dk.au.mad22spring.appproject.trivialtrivia.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import dk.au.mad22spring.appproject.trivialtrivia.Database.Database;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Player;

public class LobbyViewModel extends AndroidViewModel {

    private Database db;

    public LobbyViewModel(@NonNull Application application){
        super(application);
        db = Database.getInstance(application);
    }

    public LiveData<List<Player>> getPlayersInLobby(String documentName){
        return db.getPlayersInLobby(documentName);
    }

    public void removePlayer(String playerName, String documentID) {
        db.removePlayer(playerName, documentID);
    }

    public void removeGame(String documentID) {
        db.removeGame(documentID);
    }

    public void setActiveState(boolean state, String documentID) {
        db.setActiveState(state, documentID);
    }

    public LiveData<Boolean> getActiveState(String documentID) {
        return db.getActiveState(documentID);
    }

    public void setStartedState(boolean state, String documentID) {
        db.setStartedState(state, documentID);
    }

    public LiveData<Boolean> getStartedState(String documentID) {
        return db.getStartedState(documentID);
    }



}
