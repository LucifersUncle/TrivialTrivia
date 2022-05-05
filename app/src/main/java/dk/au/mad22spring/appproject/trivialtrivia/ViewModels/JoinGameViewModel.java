package dk.au.mad22spring.appproject.trivialtrivia.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import dk.au.mad22spring.appproject.trivialtrivia.Database.Database;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Game;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Player;
import dk.au.mad22spring.appproject.trivialtrivia.Models.User;

public class JoinGameViewModel extends AndroidViewModel {

    private Database db;

    public JoinGameViewModel(@NonNull Application application) {
        super(application);
        db = Database.getInstance();
    }


    public LiveData<List<Game>> getGames(){
        return db.getGames();
    }

    public void addPlayerToLobby(String playerName, String documentName){
        db.addPlayerToLobby(playerName, documentName);
    }

    public void getPlayerName(Player playerObj){
        db.getPlayerName(playerObj);
    }

}
