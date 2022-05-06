package dk.au.mad22spring.appproject.trivialtrivia.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad22spring.appproject.trivialtrivia.Adapters.JoinGameAdapter;
import dk.au.mad22spring.appproject.trivialtrivia.Constants.Constants;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Game;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Player;
import dk.au.mad22spring.appproject.trivialtrivia.Models.User;
import dk.au.mad22spring.appproject.trivialtrivia.R;
import dk.au.mad22spring.appproject.trivialtrivia.ViewModels.JoinGameViewModel;

public class JoinGameActivity extends AppCompatActivity implements JoinGameAdapter.IJoinGameItemClickedListener {

    //widgets
    private Button buttonBack;
    private RecyclerView rcvList;
    private JoinGameAdapter adapter;

    private JoinGameViewModel vm;
    private List<Game> lobbies;
    private ArrayList<User> users;

    private String playerName;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private Player playerObj = new Player();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        // Get User Data from DB
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://trivialtrivia-group20-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");
        userID = user.getUid();

        //Get the username of the Current User of the App
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    playerName = userProfile.username;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new JoinGameAdapter(this);
        rcvList = findViewById(R.id.rcvAvailableGames);
        rcvList.setLayoutManager(new LinearLayoutManager(this));
        rcvList.setAdapter(adapter);

        vm = new ViewModelProvider(this).get(JoinGameViewModel.class);
        vm.getGames().observe(this, new Observer<List<Game>>() {
            @Override
            public void onChanged(List<Game> games) {
                lobbies = games;
                adapter.updatePlayerList(lobbies);
            }
        });

        buttonBack = findViewById(R.id.buttonAvailableGamesBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //vm.getQuestions();
    }

    @Override
    public void onJoinGameClicked(int index) {


        //setupFirebaseListener();
        String documentName = lobbies.get(index).getDocumentName();
        String gameName = lobbies.get(index).getGameName();


        vm.addPlayerToLobby(playerName, documentName); //Outcommented to show that we can go to LobbyActivity

        Intent i = new Intent(getApplicationContext(), LobbyActivity.class);
        i.putExtra(Constants.PLAYER_NAME, playerName); //kan være at den skal med
        i.putExtra(Constants.LOBBY_INDEX, index);
        i.putExtra(Constants.DOC_OBJ, documentName);
        i.putExtra(Constants.GAME_OBJ, gameName);
        i.putExtra(Constants.PLAYER_OBJ, "player");
        launcher.launch(i);
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),   //default contract
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                }
            });
}