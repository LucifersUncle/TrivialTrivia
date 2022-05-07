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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import dk.au.mad22spring.appproject.trivialtrivia.Adapters.LobbyAdapter;
import dk.au.mad22spring.appproject.trivialtrivia.Constants.Constants;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Player;
import dk.au.mad22spring.appproject.trivialtrivia.R;
import dk.au.mad22spring.appproject.trivialtrivia.ViewModels.LobbyViewModel;

public class LobbyActivity extends AppCompatActivity implements View.OnClickListener{

    private String playerObj;
    private String documentId;
    private String playerName;
    private String playerRef;
    private List<Player> playersList;

    private boolean gameStartedForPlayer = false;

    private RecyclerView rcvList;
    private LobbyAdapter adapter;
    private LobbyViewModel vm;

    private Button buttonStartGame, buttonLeaveLobby;

    ActivityResultLauncher<Intent> hostLauncher;
    ActivityResultLauncher<Intent> playerLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        hostLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Constants.LOBBY_ACTIVITY) {
                    //finish();
                }
            }
        });

        playerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_CANCELED) {
                    finish();
                }
            }
        });

        // Get data from the other activity

        //String game = (String) getIntent().getSerializableExtra(Constants.GAME_OBJ);
        playerObj = (String) getIntent().getSerializableExtra(Constants.PLAYER_OBJ);

        playerName = (String) getIntent().getSerializableExtra(Constants.PLAYER_NAME);
        documentId = (String) getIntent().getSerializableExtra(Constants.DOC_OBJ);
        //playerRef = (String) getIntent().getSerializableExtra(Constants.PLAYER_REF);

        //region Buttons
        buttonStartGame = (Button) findViewById(R.id.btn_lobby_startGame);
        buttonStartGame.setOnClickListener(this);

        buttonLeaveLobby = (Button) findViewById(R.id.button_lobby_leave);
        buttonLeaveLobby.setOnClickListener(this);
        //endregion

        //setup for recyclerview with adapter and layout manager
        adapter = new LobbyAdapter();
        rcvList = findViewById(R.id.rcv_lobby_players);
        rcvList.setLayoutManager(new LinearLayoutManager(this));
        rcvList.setAdapter(adapter);

        vm = new ViewModelProvider(this).get(LobbyViewModel.class);

        vm.getPlayersInLobby(documentId).observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {
                playersList = players;
                adapter.updateUserList(playersList); //playerlist is null here
            }
        });

        //debug this
        vm.getActiveState(documentId).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean active) {
                if (!active) {
                    Intent intent = new Intent(getApplicationContext(), ActiveGameActivity.class);
                    //intent.putExtra(Constants.)
                    playerLauncher.launch(intent);
                    /*
                    if (playerObj.equals("player"))
                        Toast.makeText(LobbyActivity.this, "Error in getting activeState", Toast.LENGTH_SHORT).show();
                        finish();

                     */
                }
            }
        });

        vm.getStartedState(documentId).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean started) {
                if (started)
                    //if (playerObj.equals("player") && !gameStartedForPlayer) {

                        gameStartedForPlayer = true;
                        Intent intent = new Intent(getApplicationContext(), ActiveGameActivity.class);
                        /*
                        intent.putExtra(Constants.GAME_OBJ, documentId);
                        intent.putExtra(Constants.PLAYER_OBJ, "player");
                        intent.putExtra(Constants.PLAYER_NAME, playerName);

                        //Player playerToSend = validatePlayer(playerName);
                        intent.putExtra(Constants.PLAYER_REF, playerRef);

                         */

                        playerLauncher.launch(intent);
                    //}
            }
        });

        //updates the Recycler View with lists of Players
        adapter.updateUserList(playersList);

        if (playerObj.equals("host")){
            buttonStartGame.setVisibility(View.VISIBLE);
        } else if (playerObj.equals("player")){
            buttonStartGame.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        closeLobby();
        super.onDestroy();
    }

    private void closeLobby() {
        try {
            if (playerObj.equals("host")) {
                vm.setActiveState(false, documentId);
                vm.setStartedState(false, documentId);
                vm.removeGame(documentId);
            } else if (playerObj.equals("player")) {
                vm.removePlayer(playerName, documentId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_lobby_startGame:
                vm.setStartedState(true, documentId); //host has started the game and therefore state changes

                Intent intent = new Intent(getApplicationContext(), ActiveGameActivity.class);
                /*
                intent.putExtra(Constants.DOC_OBJ, documentId);
                intent.putExtra(Constants.PLAYER_NAME, playerName);
                intent.putExtra(Constants.PLAYER_OBJ, "host");

                //Player playerToSend = validatePlayer(playerName);
                intent.putExtra(Constants.PLAYER_REF, playerRef);
                */
                hostLauncher.launch(intent);


                break;
            case R.id.button_lobby_leave:
                finish();

        }


    }

    /*
    private Player validatePlayer(String name) {
        Player player = new Player();
        for (Player p : playersList) {
            if (p.getPlayerName().equals(name)) {
                player.setPlayerName(name);
                player.setPlayerRef(player.getPlayerRef());
                player.setScore(player.getScore());
                player.setRole(player.getRole());
            }
        }
        return player;
    }

     */

    /*
    ActivityResultLauncher<Intent> hostLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Constants.LOBBY_ACTIVITY) {
                finish();
            }
        }
    });

     */

    /*
    ActivityResultLauncher<Intent> playerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Constants.LOBBY_ACTIVITY) {
                finish();
            }
        }
    });

     */
}