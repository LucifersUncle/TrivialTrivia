package dk.au.mad22spring.appproject.trivialtrivia.ViewModels;

import android.app.Application;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import dk.au.mad22spring.appproject.trivialtrivia.Database.Database;
import dk.au.mad22spring.appproject.trivialtrivia.Database.Repository;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Game;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Player;

public class LobbyViewModel extends AndroidViewModel {
    private Repository repository;
    private Database db;

    public LobbyViewModel(@NonNull Application application){
        super(application);
        db = Database.getInstance();
        //repository = Repository.getInstance(application);
    }

    public LiveData<List<Player>> getPlayersInLobby(String documentName){
        return db.getPlayersInLobby(documentName);
    }

}
