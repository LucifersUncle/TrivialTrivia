package dk.au.mad22spring.appproject.trivialtrivia.Activities;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import dk.au.mad22spring.appproject.trivialtrivia.Adapters.JoinGameAdapter;
import dk.au.mad22spring.appproject.trivialtrivia.Adapters.LobbyAdapter;
import dk.au.mad22spring.appproject.trivialtrivia.Constants.Constants;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Game;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Player;
import dk.au.mad22spring.appproject.trivialtrivia.Models.User;
import dk.au.mad22spring.appproject.trivialtrivia.R;
import dk.au.mad22spring.appproject.trivialtrivia.ViewModels.JoinGameViewModel;
import dk.au.mad22spring.appproject.trivialtrivia.ViewModels.LobbyViewModel;

public class JoinGameActivity extends AppCompatActivity implements JoinGameAdapter.IJoinGameItemClickedListener {

    //widgets
    private Button buttonBack;
    private RecyclerView rcvList;
    private JoinGameAdapter adapter;

    private JoinGameViewModel joinGameViewModel;
    private List<Game> lobbies;

    private User userProfile = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        adapter = new JoinGameAdapter(this);
        rcvList = findViewById(R.id.rcvAvailableGames);
        rcvList.setLayoutManager(new LinearLayoutManager(this));
        rcvList.setAdapter(adapter);


        joinGameViewModel = new ViewModelProvider(this).get(JoinGameViewModel.class);
        joinGameViewModel.getGames().observe(this, new Observer<List<Game>>() {
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
    }

    @Override
    public void onJoinGameClicked(int index) {

        joinGameViewModel.getPlayer(userProfile);

        String documentName = lobbies.get(index).getDocumentName();
        String playerName = userProfile.getUsername();

        joinGameViewModel.addPlayerToLobby(playerName, documentName);

        Intent i = new Intent(this, LobbyActivity.class);
        i.putExtra(Constants.LOBBY_INDEX, index);
        launcher.launch(i);
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),   //default contract
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                }
            });
}