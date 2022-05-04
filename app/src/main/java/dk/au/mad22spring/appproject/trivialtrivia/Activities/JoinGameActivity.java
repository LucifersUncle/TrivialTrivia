package dk.au.mad22spring.appproject.trivialtrivia.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import dk.au.mad22spring.appproject.trivialtrivia.Adapters.JoinGameAdapter;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Game;
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

    }
}