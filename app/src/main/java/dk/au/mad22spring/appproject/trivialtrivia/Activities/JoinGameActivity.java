package dk.au.mad22spring.appproject.trivialtrivia.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import dk.au.mad22spring.appproject.trivialtrivia.Adapters.JoinGameAdapter;
import dk.au.mad22spring.appproject.trivialtrivia.R;

public class JoinGameActivity extends AppCompatActivity implements JoinGameAdapter.IJoinGameItemClickedListener {

    //widgets
    private RecyclerView rcvList;
    private JoinGameAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        adapter = new JoinGameAdapter(this);
        rcvList = findViewById(R.id.rcvAvailableGames);
        rcvList.setLayoutManager(new LinearLayoutManager(this));
        rcvList.setAdapter(adapter);


    }

    @Override
    public void onJoinGameClicked(int index) {

    }
}