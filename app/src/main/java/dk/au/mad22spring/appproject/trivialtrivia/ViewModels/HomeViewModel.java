package dk.au.mad22spring.appproject.trivialtrivia.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import dk.au.mad22spring.appproject.trivialtrivia.Database.Database;

public class HomeViewModel extends AndroidViewModel {
    private Database db;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        db = Database.getInstance(application);

    }

}
