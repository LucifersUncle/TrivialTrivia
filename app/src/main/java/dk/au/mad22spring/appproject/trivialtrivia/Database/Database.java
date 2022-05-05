package dk.au.mad22spring.appproject.trivialtrivia.Database;

import android.app.Application;
import android.mtp.MtpConstants;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dk.au.mad22spring.appproject.trivialtrivia.Models.Game;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Player;
import dk.au.mad22spring.appproject.trivialtrivia.Models.User;

public class Database {
    private static Database instance;
    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;

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
        listOfGames = new MutableLiveData<>();
        listOfGames.setValue(new ArrayList<>());

        listOfPlayers = new MutableLiveData<>();
        listOfPlayers.setValue(new ArrayList<>());

        player = new MutableLiveData<>();
        gameState = new MutableLiveData<>();
        gameStarted = new MutableLiveData<>();
    }

    public String addGame(String gameName, int timePerRound, int numberOfRounds, String playerName, String category, String difficulty) {
        mDatabase = FirebaseDatabase.getInstance("https://trivialtrivia-group20-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Lobbies");

        String documentName = UUID.randomUUID().toString();

        Game game = new Game(gameName, timePerRound, numberOfRounds, playerName, documentName, true, false, category, difficulty);

        mDatabase.child(documentName).setValue(game);

        return documentName;
    }

    public void removeGame(String documentName) {
        mDatabase = FirebaseDatabase.getInstance("https://trivialtrivia-group20-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Lobbies").child(documentName);
        mDatabase.removeValue();
    }

    public LiveData<List<Game>> getGames(){
        mDatabase = FirebaseDatabase.getInstance("https://trivialtrivia-group20-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Lobbies");

        ValueEventListener gameListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> lobbies = snapshot.getChildren();

                List<Game> lobbiesList = new ArrayList<Game>();

                for(DataSnapshot lobby : lobbies){
                    String documentName = lobby.getKey();
                    String gameName = lobby.child("gameName").getValue().toString();
                    String category = lobby.child("category").getValue().toString();
                    String difficulty = lobby.child("difficulty").getValue().toString();

                    Game game = new Game(gameName, 10, 10, "guffestav", documentName, true, false, category, difficulty);
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


    public void addPlayerToLobby(String playerName, String documentName) {
        String uniqueId = UUID.randomUUID().toString();
        mDatabase = FirebaseDatabase.getInstance("https://trivialtrivia-group20-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Lobbies")
                .child(documentName)
                .child("players")
                .child(uniqueId);

        Player player = new Player(playerName, "player", uniqueId, 0);

        mDatabase.setValue(player);
    }

    //region Funktion - Dette skal være den som sætter playerName - OBS: Virker ikke
    public void getPlayerName(Player playerObj) {
        user = mAuth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid();

            mDatabase = FirebaseDatabase.getInstance("https://trivialtrivia-group20-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("Users").child(userId);

            Query usernameQuery = mDatabase.orderByChild("username");

            usernameQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = snapshot.child("username").getValue().toString();
                    //player2.setPlayerName(name); //har fjernet player2 initialiseringen
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    //endregion

    public LiveData<List<Player>> getPlayersInLobby(String documentName){
        mDatabase = FirebaseDatabase.getInstance("https://trivialtrivia-group20-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Lobbies").child(documentName).child("players");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Iterable<DataSnapshot> players = snapshot.getChildren();
                List<Player> playersList = new ArrayList<>();

                for(DataSnapshot p : players){
                    String playerDocument = p.child("documentName").toString();
                    String playerName = p.child("playerName").getValue().toString();
                    String playerRole = p.child("role").getValue().toString();
                    int playerScore = Integer.parseInt(p.child("score").getValue().toString());

                    Player player = new Player(playerName, documentName,playerRole,playerScore); //Overvej at bruge playerDocument i stedet for documentName
                    playersList.add(player);
                }
                listOfPlayers.setValue(playersList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        return listOfPlayers;
    }



}
