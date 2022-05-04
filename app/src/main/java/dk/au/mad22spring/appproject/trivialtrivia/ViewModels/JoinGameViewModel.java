package dk.au.mad22spring.appproject.trivialtrivia.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import dk.au.mad22spring.appproject.trivialtrivia.Database.Database;
import dk.au.mad22spring.appproject.trivialtrivia.Database.Repository;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Game;

public class JoinGameViewModel extends AndroidViewModel {

    private Repository repository;
    private Database db;

    public JoinGameViewModel(@NonNull Application application) {
        super(application);
        db = Database.getInstance();
    }


    public LiveData<List<Game>> getGames(){
        return db.getGames();
    }

    public void addPlayerToLobby(){
       // db.addPlayerToLobby();
    }
}