package dk.au.mad22spring.appproject.trivialtrivia.Database;

import android.app.Application;
import android.content.Context;
import android.mtp.MtpConstants;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dk.au.mad22spring.appproject.trivialtrivia.Models.ActiveGame;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Game;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Player;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Questions.QuestionData;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Questions.Result;
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

    private MutableLiveData<Boolean> gameActive;
    private MutableLiveData<Boolean> gameStarted;
    private MutableLiveData<ActiveGame> aQuestion;

    Context context;
    RequestQueue queue;

    public static Database getInstance(Application application) {
        if (instance == null) {
            synchronized (Database.class) {
                if (instance == null) {
                    instance = new Database(application);
                }
            }
        }
        return instance;
    }

    private Database(Application application) {
        listOfGames = new MutableLiveData<>();
        listOfGames.setValue(new ArrayList<>());

        listOfPlayers = new MutableLiveData<>();
        listOfPlayers.setValue(new ArrayList<>());

        //listOfQuestions;
        aQuestion = new MutableLiveData<>();

        player = new MutableLiveData<>();
        gameActive = new MutableLiveData<>();
        gameStarted = new MutableLiveData<>();
        currentRound = new MutableLiveData<>();
        context = application.getApplicationContext();
    }

    //region Games
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
                    //int numberOfRounds = Integer.parseInt(lobby.child("numberOfRounds").getValue().toString()); //should be added
                    //int timePerRound = Integer.parseInt(lobby.child("timePerRound").getValue().toString()); //should be added

                    Game game = new Game(gameName, 10, 10, "Tests", documentName, true, false, category, difficulty);
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
    //endregion

    //region Players
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

    public void removePlayer(String playerName, String documentID) {
        mDatabase = FirebaseDatabase.getInstance("https://trivialtrivia-group20-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Lobbies").child(documentID).child("players");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> allPlayers = snapshot.getChildren();

                for (DataSnapshot player : allPlayers) {
                    try {
                        String playerNameFound = player.child("playerName").getValue().toString();
                        String playerDocument = player.getKey();

                        if (playerName.equals(playerNameFound)) {
                            mDatabase = FirebaseDatabase.getInstance("https://trivialtrivia-group20-default-rtdb.europe-west1.firebasedatabase.app/")
                                    .getReference("Lobbies")
                                    .child(documentID)
                                    .child("players")
                                    .child(playerDocument);
                            mDatabase.removeValue();
                        }
                    } catch (Exception e) {
                        Log.d("Database", "onDataChange: Something went wrong when removing player!");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //endregion

    //region Questions
    public void getQuestions(String documentName, int roundsPicked, String categoryPicked, String difficultyPicked){
        mDatabase = FirebaseDatabase.getInstance("https://trivialtrivia-group20-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Lobbies").child(documentName);

        String amount= roundsPicked+"";
        String category = categoryPicked;
        String difficulty = difficultyPicked;

        //https://opentdb.com/api.php?amount=10&category=9&difficulty=easy&type=multiple
        String baseURLstart = "https://opentdb.com/api.php?";
        String baseURLend = "&type=multiple";
        String amountURL = "amount="+amount;
        String categoryURL = "&category="+category;
        String difficultyURL = "&difficulty="+difficulty;
        String URL="";

        if(!category.isEmpty() && !difficulty.isEmpty()){ //category sat; difficulty sat
            URL=baseURLstart+amountURL+categoryURL+difficultyURL+baseURLend;
        }
        else if(!category.isEmpty() && difficulty.isEmpty()){ //category sat; difficulty IKKE sat
            URL=baseURLstart+amountURL+categoryURL+baseURLend;
        }
        else if(category.isEmpty() && !difficulty.isEmpty()){ //category IKKE sat; difficulty sat
            URL=baseURLstart+amountURL+difficultyURL+baseURLend;
        }
        else if(category.isEmpty() && !difficulty.isEmpty()){ //category IKKE sat; difficulty IKKE sat
            URL=baseURLstart+amountURL+baseURLend;
        }
        else{
        }

        if(queue==null){
            queue = Volley.newRequestQueue(context);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                response -> {
                    Log.d("GUSTAV", "onResponse: " + response);
                    parseJson(response, documentName);

                }, error -> {
            Log.e("Gustav", "That did not work!", error);
        });
        queue.add(stringRequest);
    }

    private void parseJson(String json, String documentName){
        Gson gson = new GsonBuilder().create();
        QuestionData questionData = gson.fromJson(json, QuestionData.class);

        setQuestionsForGame(questionData, documentName);
    }

    private  void setQuestionsForGame(QuestionData questionData, String documentName){

        String lobbyID = documentName;

        List<Result> results = questionData.getResults();
        mDatabase = FirebaseDatabase.getInstance("https://trivialtrivia-group20-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Lobbies").child(lobbyID);

        for(Result r : results){
            String category = r.getCategory();
            String difficulty = r.getDifficulty();
            String question = r.getQuestion();
            String correctAnswer = r.getCorrectAnswer();
            List<String> incorrectAnswer = r.getIncorrectAnswers();

            mDatabase.child("questions").push().setValue(r);
        }
    }
    //endregion
    //endregion


    public LiveData<ActiveGame> getGameInfo(String documentName){
        mDatabase = FirebaseDatabase.getInstance("https://trivialtrivia-group20-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Lobbies").child(documentName);
        DatabaseReference questionRef = mDatabase.child("questions");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> results = snapshot.getChildren();
                List<ActiveGame> resultList = new ArrayList<>();
/*
                String category = snapshot.child("category").getValue().toString();
                int currentRound = snapshot.child("currentRound").getValue(int.class);
                String difficulty = snapshot.child("difficulty").getValue().toString();
                String documentName = snapshot.child("documentName").getValue().toString();
                String gameName = snapshot.child("gameName").getValue().toString();
                int numberOfRounds = snapshot.child("numberOfRounds").getValue(int.class);
*/
                /*for(DataSnapshot r : results){
                    String category = r.child("category").getValue().toString();
                    String correctAnswer = r.child("correctAnswer").getValue().toString();
                    String difficulty = r.child("difficulty").getValue().toString();
                    //List<String> incorrectAnswers = r.getChildren();
                    String incorrectAnswer1 = r.child("incorrectAnswers").child("0").getValue().toString();
                    String incorrectAnswer2 = r.child("incorrectAnswers").child("1").getValue().toString();
                    String incorrectAnswer3 = r.child("incorrectAnswers").child("2").getValue().toString();
                    String question = r.child("question").getValue().toString();
                    String type = r.child("type").getValue().toString();

                 */
                String category = snapshot.child("category").getValue().toString();
                String correctAnswer = snapshot.child("correctAnswer").getValue().toString();
                String difficulty = snapshot.child("difficulty").getValue().toString();
                //List<String> incorrectAnswers = r.getChildren();
                String incorrectAnswer1 = snapshot.child("incorrectAnswers").child("0").getValue().toString();
                String incorrectAnswer2 = snapshot.child("incorrectAnswers").child("1").getValue().toString();
                String incorrectAnswer3 = snapshot.child("incorrectAnswers").child("2").getValue().toString();
                String question = snapshot.child("question").getValue().toString();
                String type = snapshot.child("type").getValue().toString();


                ActiveGame activeGame = new ActiveGame(correctAnswer,incorrectAnswer1, incorrectAnswer2, incorrectAnswer3, question);
                    //resultList.add(activeGame);
               // }
            //listOfQuestions.setValue(resultList);
                aQuestion.setValue(activeGame);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return aQuestion;
    }

    //region State Management
    public void setActiveState(boolean state, String documentID) {
        mDatabase = FirebaseDatabase.getInstance("https://trivialtrivia-group20-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Lobbies")
                .child(documentID)
                .child("active");
        mDatabase.setValue(state);
    }

    public LiveData<Boolean> getActiveState(String documentID) {
        gameActive.setValue(true);

        mDatabase = FirebaseDatabase.getInstance("https://trivialtrivia-group20-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Lobbies").child(documentID).child("active");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null)
                    return;
                Boolean activeState = Boolean.parseBoolean(snapshot.getValue().toString());
                gameActive.setValue(activeState);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database", "onCancelled: Something went wrong in getting the Active State of the game!");
            }
        });
        return gameActive;
    }

    public void setStartedState(boolean state, String documentID) {
        mDatabase = FirebaseDatabase.getInstance("https://trivialtrivia-group20-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Lobbies").child(documentID).child("started");
        mDatabase.setValue(state);
    }

    public LiveData<Boolean> getStartedState(String documentID) {
        gameStarted.setValue(false);

        mDatabase = FirebaseDatabase.getInstance("https://trivialtrivia-group20-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Lobbies").child(documentID).child("started");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null)
                    return;

                Boolean state = Boolean.parseBoolean(snapshot.getValue().toString());
                gameStarted.setValue(state);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database", "onCancelled: Something went wrong getting the Gane Started state!");
            }
        });
        return gameStarted;
    }

    //endregion

    //region Active Game Player Attributes
    public void setPlayerScore(String documentID, String playerReference, int currentScore) {
        mDatabase = FirebaseDatabase.getInstance("https://trivialtrivia-group20-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Lobbies")
                .child(documentID)
                .child("players")
                .child(playerReference)
                .child("score");
        mDatabase.setValue(currentScore + 1);
    }

    public void setNextRound(String documentID, int currentRound) {
        mDatabase = FirebaseDatabase.getInstance("https://trivialtrivia-group20-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Lobbies")
                .child(documentID)
                .child("round");
        mDatabase.setValue(currentRound + 1);
    }

    public LiveData<Integer> getCurrentRound(String documentID) {
        currentRound.setValue(null);

        mDatabase = FirebaseDatabase.getInstance("https://trivialtrivia-group20-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Lobbies")
                .child(documentID)
                .child("round");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    return;
                }

                int roundNumber = Integer.parseInt(snapshot.getValue().toString());
                currentRound.setValue(roundNumber);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return currentRound;
    }

    //endregion
}
