package dk.au.mad22spring.appproject.trivialtrivia.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import dk.au.mad22spring.appproject.trivialtrivia.Constants.Constants;
import dk.au.mad22spring.appproject.trivialtrivia.R;
import dk.au.mad22spring.appproject.trivialtrivia.Services.QuizService;
import dk.au.mad22spring.appproject.trivialtrivia.ViewModels.GameSettingsViewModel;


public class GameSettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //**************************************************************//
    //https://medium.com/@sc71/android-numberpickers-3ef535c45487
    //**************************************************************//
    private NumberPicker roundsPicker, timePicker;
    private Button buttonHostGame, buttonsSettingsBack;
    //************************************************************//
    //https://developer.android.com/guide/topics/ui/controls/spinner
    //*************************************************************//
    private Spinner spinnerCategory, spinnerDifficulty;
    private EditText gameName;

    private String difficultySelected="", categorySelected="", categoryString="";

    private GameSettingsViewModel vm;
    private int roundsPicked, timePickedPerRound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings);

        String playerName = (String) getIntent().getSerializableExtra(Constants.PLAYER_NAME);

        vm = new ViewModelProvider(this).get(GameSettingsViewModel.class);

        //region Buttons
        buttonsSettingsBack = (Button) findViewById(R.id.button_setting_back);
        buttonsSettingsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonHostGame = (Button) findViewById(R.id.buttonHostGame);
        buttonHostGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String game = gameName.getText().toString();
                String category = categorySelected;
                String difficulty = difficultySelected;

                if (game.isEmpty()) {
                    gameName.setError("Game name cannot be empty!");
                    gameName.requestFocus();
                } else {
                    String documentName = vm.addGameToDb(game, timePickedPerRound, roundsPicked, playerName, category, difficulty);
                    vm.fetchQuestions(documentName, roundsPicked, category, difficulty); //rename function to something more appropriate
                    vm.addHostToLobby(playerName, documentName);

                    Intent intent = new Intent(getApplicationContext(), LobbyActivity.class);

                    intent.putExtra(Constants.PLAYER_OBJ, "host");
                    intent.putExtra(Constants.GAME_OBJ, game);
                    intent.putExtra(Constants.CATEGORY_OBJ, category);
                    intent.putExtra(Constants.DIFFICULTY_OBJ, difficulty);
                    intent.putExtra(Constants.DOC_OBJ, documentName);
                    intent.putExtra(Constants.PLAYER_NAME, playerName);

                    launcher.launch(intent);
                }


                startForegroundService(getApplication(), categoryString, difficulty);

            }
        });
        //endregion

        //region Category Dropdown menu
        //Set up drop down with ArrayAdapter for Category
        spinnerCategory = (Spinner) findViewById(R.id.dropDownCategory);
        spinnerCategory.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                                        R.array.Category_array,
                                        android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);
        //endregion

        //region Difficulty Dropdown menu
        //Set up drop down with ArrayAdapter for Category
        spinnerDifficulty = (Spinner) findViewById(R.id.dropDownDifficulty);
        spinnerDifficulty.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> difficultyAdapter = ArrayAdapter.createFromResource(this,
                R.array.difficulty_array,
                android.R.layout.simple_spinner_item);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(difficultyAdapter);
        //endregion

        //region Edit Texts
        gameName = findViewById(R.id.editTextGameName);
        //endregion

        //region Rounds Picker
        roundsPicker = (NumberPicker) findViewById(R.id.numberOfRoundsPicker);
        roundsPicked = 1;
        roundsPicker.setMaxValue(10);
        roundsPicker.setMinValue(1);
        roundsPicker.setValue(1);
        roundsPicker.setWrapSelectorWheel(false);
        roundsPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                roundsPicked = i1;
            }
        });
        //endregion

        //region Time Picker
        timePicker = (NumberPicker) findViewById(R.id.numberOfSecondsPicker);
        timePickedPerRound = 10;
        timePicker.setMinValue(1);
        timePicker.setMaxValue(12);
        timePicker.setValue(1);
        String[] seconds = {"5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60"};
        timePicker.setDisplayedValues(seconds);
        timePicker.setWrapSelectorWheel(false);
        timePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                timePickedPerRound = (i1 != 0 ? i1 : i)*5;
            }
        });
        //endregion

    }

    //To start foreground service
    private void startForegroundService(Application application, String category, String difficulty) {
        Intent foregroundServiceIntent = new Intent(application, QuizService.class);
        foregroundServiceIntent.putExtra(Constants.CATEGORY_STRING, category);
        foregroundServiceIntent.putExtra(Constants.DIFFICULTY_OBJ, difficulty);
        startService(foregroundServiceIntent);
    }

    //region
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.dropDownCategory:
                categorySelected = adapterView.getItemAtPosition(i).toString().toLowerCase();
                categoryString = categorySelected;
                if(categorySelected.equals("any category")){
                    categorySelected="";}
                else if(categorySelected.equals("general knowledge")){
                    categorySelected="9";}
                else if(categorySelected.equals("entertainment: books")){
                    categorySelected="10";}
                else if(categorySelected.equals("entertainment: film")){
                    categorySelected="11";}
                else if(categorySelected.equals("entertainment: music")){
                    categorySelected="12";}
                else if(categorySelected.equals("entertainment: musicals and theatres")){
                    categorySelected="13";}
                else if(categorySelected.equals("entertainment: television")){
                    categorySelected="14";}
                else if(categorySelected.equals("entertainment: video games")){
                    categorySelected="15";}
                else if(categorySelected.equals("entertainment: board games")){
                    categorySelected="16";}
                else if(categorySelected.equals("science and nature")){
                    categorySelected="17";}
                else if(categorySelected.equals("science: computers")){
                    categorySelected="18";}
                else if(categorySelected.equals("science: mathematics")){
                    categorySelected="19";}
                else if(categorySelected.equals("mythology")){
                    categorySelected="20";}
                else if(categorySelected.equals("sports")){
                    categorySelected="21";}
                else if(categorySelected.equals("geography")){
                    categorySelected="22";}
                else if(categorySelected.equals("history")){
                    categorySelected="23";}
                else if(categorySelected.equals("politics")){
                    categorySelected="24";}
                else if(categorySelected.equals("art")){
                    categorySelected="25";}
                else if(categorySelected.equals("celebrities")){
                    categorySelected="26";}
                else if(categorySelected.equals("animals")){
                    categorySelected="27";}
                else if(categorySelected.equals("vehicles")){
                    categorySelected="28";}
                else if(categorySelected.equals("entertainment: comics")){
                    categorySelected="29";}
                else if(categorySelected.equals("entertainment: science: gadgets")){
                    categorySelected="30";}
                else if(categorySelected.equals("entertainment: japanese anime and mange")){
                    categorySelected="31";}
                else if(categorySelected.equals("entertainment: cartoon and animations")){
                    categorySelected="32";}
                else{
                    categorySelected="";}
                break;

            case R.id.dropDownDifficulty:
                difficultySelected = adapterView.getItemAtPosition(i).toString().toLowerCase();
                if(difficultySelected.equals("any difficulty")){
                    difficultySelected="";
                }
                break;
        }
        Log.d("GUSTAV", "Difficulty: " + difficultySelected + " Category:" + categorySelected);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Constants.LOBBY_ACTIVITY) {
                if (result.getResultCode() == RESULT_CANCELED) {
                    finish();
                }
            }
        }
    });
}