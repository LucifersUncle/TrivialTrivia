package dk.au.mad22spring.appproject.trivialtrivia.ViewModels;

import android.app.Application;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import dk.au.mad22spring.appproject.trivialtrivia.Database.Database;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Player;

public class GameViewModel extends AndroidViewModel {

    private Database db;

    public GameViewModel(@NonNull Application application) {
        super(application);

        db = Database.getInstance();

    }

    /*
    public LiveData<Player> getPlayer(String documentID, String playerReference) {
        return db.getPlayer(documentID, playerReference);
    }

    public LiveData<Player> getPlayers(String documentID) {
        return db.getPlayers(documentID);
    }

     */


}
