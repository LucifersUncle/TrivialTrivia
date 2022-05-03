package dk.au.mad22spring.appproject.trivialtrivia.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import dk.au.mad22spring.appproject.trivialtrivia.Database.Repository;

public class LobbyViewModel extends AndroidViewModel {
    private Repository repository;

    public LobbyViewModel(@NonNull Application application){
        super(application);
        //repository = Repository.getInstance(application);
    }



}
