package dk.au.mad22spring.appproject.trivialtrivia.Database;

import android.mtp.MtpConstants;
import android.provider.ContactsContract;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dk.au.mad22spring.appproject.trivialtrivia.Models.Game;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Player;

public class Database {
    private static Database instance;
    private DatabaseReference mDatabase;

    private MutableLiveData<List<Game>> listOfGames;
    private MutableLiveData<List<Player>> listOfPlayers;
    private MutableLiveData<Player> player;
    private MutableLiveData<Integer> currentRound;

    private MutableLiveData<Boolean> gameState;
    private MutableLiveData<Boolean> gameStarted;

    public static Database getInstance() {
        if (instance == null) {
            synchronized (Database.class) {
                if (instance == null) {
                    instance = new Database();
                }
            }
        }
        return instance;
    }

    private Database() {
        //db = FirebaseDatabase.getInstance();

        listOfGames = new MutableLiveData<>();
        listOfGames.setValue(new ArrayList<>());

        listOfPlayers = new MutableLiveData<>();
        listOfPlayers.setValue(new ArrayList<>());

        player = new MutableLiveData<>();

        gameState = new MutableLiveData<>();
        gameStarted = new MutableLiveData<>();
    }

    public String addGame(String gameName, int timePerRound, int numberOfRounds, String playerName) {
        mDatabase = FirebaseDatabase.getInstance().getReference("Lobby");

        String documentName = UUID.randomUUID().toString();

        Game game = new Game(gameName, timePerRound, numberOfRounds, playerName, documentName, true, false);

        mDatabase.child(documentName).setValue(game);

        return documentName;
    }
}
