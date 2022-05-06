package dk.au.mad22spring.appproject.trivialtrivia.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import dk.au.mad22spring.appproject.trivialtrivia.Database.Database;

public class GameViewModel extends AndroidViewModel {

    private Database db;

    public GameViewModel(@NonNull Application application) {
        super(application);
        db = Database.getInstance(application);
    }





}
