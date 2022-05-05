package dk.au.mad22spring.appproject.trivialtrivia.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import dk.au.mad22spring.appproject.trivialtrivia.Adapters.LobbyAdapter;
import dk.au.mad22spring.appproject.trivialtrivia.Constants.Constants;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Game;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Player;
import dk.au.mad22spring.appproject.trivialtrivia.R;
import dk.au.mad22spring.appproject.trivialtrivia.ViewModels.LobbyViewModel;

public class LobbyActivity extends AppCompatActivity implements View.OnClickListener{

    private String player;
    private String documentName;
    private String playerName;
    private List<Player> playersList;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;


    private RecyclerView rcvList;
    private LobbyAdapter adapter;
    private LobbyViewModel vm;

    private Button buttonStartGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        // Get data from the other activity

        String game = (String) getIntent().getSerializableExtra(Constants.GAME_OBJ);
        player = (String) getIntent().getSerializableExtra(Constants.PLAYER_OBJ);
        //playerName = (String) getIntent().getSerializableExtra(Constants.PLAYER_NAME);
        documentName = (String) getIntent().getSerializableExtra(Constants.DOC_OBJ);

        buttonStartGame = (Button) findViewById(R.id.btn_lobby_startGame);
        buttonStartGame.setOnClickListener(this);

        // Get data from DB
        /*
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://trivialtrivia-group20-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");
        userID = user.getUid();

         */

        //setup for recyclerview with adapter and layout manager
        adapter = new LobbyAdapter();
        rcvList = findViewById(R.id.rcv_lobby_players);
        rcvList.setLayoutManager(new LinearLayoutManager(this));
        rcvList.setAdapter(adapter);

        vm = new ViewModelProvider(this).get(LobbyViewModel.class);
        vm.getPlayersInLobby(documentName).observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {
                playersList = players;
                adapter.updateUserList(playersList);
            }
        });
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                //Handle the result here
            }
        }
    });


    @Override
    public void onClick(View v) {
        startActivity(new Intent(LobbyActivity.this, ActiveGameActivity.class));
    }
}