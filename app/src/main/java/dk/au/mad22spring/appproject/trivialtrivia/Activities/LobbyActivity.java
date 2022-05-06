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

import java.io.Serializable;
import java.util.ArrayList;
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

    private boolean gameStartedForPlayer = false;
    /*
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    */

    private RecyclerView rcvList;
    private LobbyAdapter adapter;
    private LobbyViewModel vm;

    private Button buttonStartGame, buttonLeaveLobby;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        // Get data from the other activity

        String game = (String) getIntent().getSerializableExtra(Constants.GAME_OBJ);
        player = (String) getIntent().getSerializableExtra(Constants.PLAYER_OBJ);

        playerName = (String) getIntent().getSerializableExtra(Constants.PLAYER_NAME);
        documentName = (String) getIntent().getSerializableExtra(Constants.DOC_OBJ);

        buttonStartGame = (Button) findViewById(R.id.btn_lobby_startGame);
        buttonStartGame.setOnClickListener(this);

        buttonLeaveLobby = (Button) findViewById(R.id.button_lobby_leave);
        buttonLeaveLobby.setOnClickListener(this);


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

        vm.getActiveState(documentName).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean active) {
                if (!active) {
                    if (player.equals("player"))
                        finish();
                }
            }
        });

        vm.getStartedState(documentName).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean started) {
                if (started)
                    if (player.equals("player") && !gameStartedForPlayer) {
                        gameStartedForPlayer = true;
                        Intent intent = new Intent(getApplicationContext(), ActiveGameActivity.class);
                        intent.putExtra(Constants.GAME_OBJ, documentName);
                        intent.putExtra(Constants.PLAYER_OBJ, "player");
                        intent.putExtra(Constants.PLAYER_NAME, playerName);

                        Player playerToSend = validatePlayer(playerName);
                        intent.putExtra(Constants.PLAYER_REF, (Serializable) playerToSend);

                        launcher.launch(intent);
                    }
            }
        });

        if (playersList == null) {
            playersList = new ArrayList<>();
        }

        //updates the Recycler View with lists of Players
        adapter.updateUserList(playersList);

        if (player.equals("host")){
            buttonStartGame.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        closeLobby();
        super.onDestroy();
    }

    private Player validatePlayer(String playerName) {
        Player player = new Player();
        for (Player p : playersList) {
            if (p.getPlayerName().equals(playerName)) {
                player.setPlayerName(playerName);
                player.setPlayerRef(player.getPlayerRef());
                player.setScore(player.getScore());
                player.setRole(player.getRole());
            }
        }
        return player;
    }

    private void closeLobby() {
        try {
            if (player.equals("host")) {
                vm.setActiveState(false, documentName);
                vm.setStartedState(false, documentName);
                vm.removeGame(documentName);
            } else if (player.equals("player")) {
                vm.removePlayer(playerName, documentName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_lobby_startGame:
                Intent intent = new Intent(getApplicationContext(), ActiveGameActivity.class);
                intent.putExtra(Constants.DOC_OBJ, documentName);
                intent.putExtra(Constants.PLAYER_NAME, playerName);
                intent.putExtra(Constants.PLAYER_OBJ, "host");

                Player playerToSend = validatePlayer(playerName);
                intent.putExtra(Constants.PLAYER_REF, playerToSend);
                //startForResultActivity(intent, ActiveGameActivity.class));
                launcher.launch(intent);
                break;
            case R.id.button_lobby_leave:
                finish();

        }


    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                //Handle the result here
            }
        }
    });
}