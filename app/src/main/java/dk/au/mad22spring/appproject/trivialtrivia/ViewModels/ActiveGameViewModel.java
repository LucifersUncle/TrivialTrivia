package dk.au.mad22spring.appproject.trivialtrivia.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import dk.au.mad22spring.appproject.trivialtrivia.Database.Database;
import dk.au.mad22spring.appproject.trivialtrivia.Models.ActiveGame;

public class ActiveGameViewModel extends AndroidViewModel {

    private Database db;
    public ActiveGameViewModel(@NonNull Application application) {
        super(application);
        db = Database.getInstance(application);
    }



    public LiveData<ActiveGame> getGameInfo(String documentName){
        return db.getGameInfo(documentName);
    }
}
