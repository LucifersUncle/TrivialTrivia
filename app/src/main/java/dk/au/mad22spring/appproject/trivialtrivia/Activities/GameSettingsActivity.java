package dk.au.mad22spring.appproject.trivialtrivia.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;

import dk.au.mad22spring.appproject.trivialtrivia.R;



public class GameSettingsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    NumberPicker roundsPicker, timePicker;
    Button buttonHostGame;
    Spinner spinnerCategory, spinnerDifficulty;

    //Set to Any difficulty/category
    String difficultySelected="", categorySelected="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings);

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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.dropDownCategory:
                categorySelected = adapterView.getItemAtPosition(i).toString().toLowerCase();
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
        Log.d("GUSTAV", "Difficulty: "+difficultySelected +" Category:" + categorySelected);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}