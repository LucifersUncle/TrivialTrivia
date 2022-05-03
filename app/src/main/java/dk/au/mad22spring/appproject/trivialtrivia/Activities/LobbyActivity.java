package dk.au.mad22spring.appproject.trivialtrivia.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import dk.au.mad22spring.appproject.trivialtrivia.Adapters.LobbyAdapter;
import dk.au.mad22spring.appproject.trivialtrivia.R;
import dk.au.mad22spring.appproject.trivialtrivia.ViewModels.LobbyViewModel;

public class LobbyActivity extends AppCompatActivity {

    private RecyclerView rcvList;
    private LobbyAdapter adapter;

    private LobbyViewModel lobbyvm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        //setup for recyclerview with adapter and layout manager
        adapter = new LobbyAdapter();
        rcvList = findViewById(R.id.rcv_lobby_players);
        rcvList.setLayoutManager(new LinearLayoutManager(this));
        rcvList.setAdapter(adapter);

        lobbyvm = new ViewModelProvider(this).get(LobbyViewModel.class);


    }
}