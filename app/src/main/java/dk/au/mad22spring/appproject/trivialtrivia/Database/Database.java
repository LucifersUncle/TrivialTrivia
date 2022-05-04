package dk.au.mad22spring.appproject.trivialtrivia.Database;

import android.mtp.MtpConstants;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        mDatabase = FirebaseDatabase.getInstance("https://trivialtrivia-group20-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Lobby");

        String documentName = UUID.randomUUID().toString();

        Game game = new Game(gameName, timePerRound, numberOfRounds, playerName, documentName, true, false);

        mDatabase.child(documentName).setValue(game);

        return documentName;
    }

    public LiveData<List<Game>> getGames(){
        mDatabase = FirebaseDatabase.getInstance("https://trivialtrivia-group20-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Lobby");

        ValueEventListener gameListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> lobbies = snapshot.getChildren();

                List<Game> lobbiesList = new ArrayList<Game>();

                for(DataSnapshot lobby : lobbies){
                    String gameName = lobby.child("gameName").getValue().toString();
                    String documentName = UUID.randomUUID().toString();

                    Game game = new Game(gameName, 10, 10, "guffestav", documentName, true, false);
                    lobbiesList.add(game);
                }

                listOfGames.setValue(lobbiesList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "onCancelled: Something went wrong here :(");
            }
        };
        mDatabase.addValueEventListener(gameListener);
        return listOfGames;
    }

 /*   public void addPlayerToLobby(String playerName, ){

    }*/
}
