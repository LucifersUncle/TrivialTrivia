package dk.au.mad22spring.appproject.trivialtrivia.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dk.au.mad22spring.appproject.trivialtrivia.Constants.Constants;
import dk.au.mad22spring.appproject.trivialtrivia.Models.User;
import dk.au.mad22spring.appproject.trivialtrivia.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private String playerName;

    private Button buttonLogOut, buttonCreateGame, buttonJoinGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //region Buttons
        buttonLogOut = (Button) findViewById(R.id.buttonLogOut);
        buttonLogOut.setOnClickListener(this);

        buttonCreateGame = (Button) findViewById(R.id.buttonCreateGame);
        buttonCreateGame.setOnClickListener(this);

        buttonJoinGame = (Button) findViewById(R.id.buttonJoinGame);
        buttonJoinGame.setOnClickListener(this);
        //endregion

        //region User Credentials from Firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://trivialtrivia-group20-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");
        userID = user.getUid();
        //endregion

        final TextView greeting = (TextView) findViewById(R.id.textUserGreeting);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String username = userProfile.username;

                    greeting.setText("Welcome, " + username + "!");
                    playerName = username;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Something went wrong...", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonCreateGame:
                Intent intent = new Intent(getApplicationContext(),GameSettingsActivity.class);
                intent.putExtra(Constants.PLAYER_NAME, playerName); //playerName of Host
                launcher.launch(intent);
                break;

            case R.id.buttonLogOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                break;

            case R.id.buttonJoinGame:
                Intent i = new Intent(getApplicationContext(), JoinGameActivity.class);
                //i.putExtra(Constants.PLAYER_NAME, playerName); //recently added for test purposes
                startActivity(new Intent(HomeActivity.this, JoinGameActivity.class));
                break;
        }
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

        }
    });
}