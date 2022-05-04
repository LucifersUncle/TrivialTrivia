package dk.au.mad22spring.appproject.trivialtrivia.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

import dk.au.mad22spring.appproject.trivialtrivia.Adapters.LobbyAdapter;
import dk.au.mad22spring.appproject.trivialtrivia.Constants.Constants;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Game;
import dk.au.mad22spring.appproject.trivialtrivia.R;
import dk.au.mad22spring.appproject.trivialtrivia.ViewModels.LobbyViewModel;

public class LobbyActivity extends AppCompatActivity {

    private String player;
    private String documentName;
    private String playerName;

    private RecyclerView rcvList;
    private LobbyAdapter adapter;
    private LobbyViewModel vm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        // Get data from the other activity

        String game = (String) getIntent().getSerializableExtra(Constants.GAME_OBJ);
        player = (String) getIntent().getSerializableExtra(Constants.PLAYER_OBJ);
        playerName = (String) getIntent().getSerializableExtra(Constants.PLAYER_NAME);
        documentName = (String) getIntent().getSerializableExtra(Constants.DOC_OBJ);


        vm = new ViewModelProvider(this).get(LobbyViewModel.class);
        // Get data from DB


        //setup for recyclerview with adapter and layout manager
        adapter = new LobbyAdapter();
        rcvList = findViewById(R.id.rcv_lobby_players);
        rcvList.setLayoutManager(new LinearLayoutManager(this));
        rcvList.setAdapter(adapter);



    }
}