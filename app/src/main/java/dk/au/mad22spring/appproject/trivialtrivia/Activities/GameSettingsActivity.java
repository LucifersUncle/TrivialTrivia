package dk.au.mad22spring.appproject.trivialtrivia.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import dk.au.mad22spring.appproject.trivialtrivia.R;

public class GameSettingsActivity extends AppCompatActivity implements View.OnClickListener{

    NumberPicker roundsPicker, timePicker;
    Button buttonHostGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings);

        roundsPicker = (NumberPicker) findViewById(R.id.numberOfRoundsPicker);
        roundsPicker.setMinValue(0);
        roundsPicker.setMaxValue(10);

        timePicker = (NumberPicker) findViewById(R.id.numberOfSecondsPicker);
        timePicker.setMinValue(1);
        timePicker.setMaxValue(60);

        buttonHostGame = (Button) findViewById(R.id.buttonHostGame);
        buttonHostGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}