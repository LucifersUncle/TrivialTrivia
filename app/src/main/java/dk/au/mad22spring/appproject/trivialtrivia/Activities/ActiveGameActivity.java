package dk.au.mad22spring.appproject.trivialtrivia.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import dk.au.mad22spring.appproject.trivialtrivia.Constants.Constants;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Question;
import dk.au.mad22spring.appproject.trivialtrivia.R;
import dk.au.mad22spring.appproject.trivialtrivia.ViewModels.ActiveGameViewModel;

public class ActiveGameActivity extends AppCompatActivity {

    private String documentName;

    Button buttonAnswer1, buttonAnswer2, buttonAnswer3, buttonAnswer4;
    TextView textViewScore, textViewAnswerQuestion;
    ProgressBar progressBarTime;

    Question gameInfo;

    ActiveGameViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_game);

        //region widget setup
        buttonAnswer1 = findViewById(R.id.txt_aGame_answer1);
        buttonAnswer2 = findViewById(R.id.txt_aGame_answer2);
        buttonAnswer3 = findViewById(R.id.txt_aGame_answer3);
        buttonAnswer4 = findViewById(R.id.txt_aGame_answer4);

        textViewAnswerQuestion = findViewById(R.id.txt_aGame_Question);
        textViewScore = findViewById(R.id.txt_aGame_scorePoint);
        progressBarTime = findViewById(R.id.pBar_aGame_time);
        //endregion

        documentName = (String) getIntent().getSerializableExtra(Constants.DOC_OBJ);

        vm = new ViewModelProvider(this).get(ActiveGameViewModel.class);
        //region virker ikke her
        vm.getGameInfo(documentName).observe(this, new Observer<Question>() {
            @Override
            public void onChanged(Question question) {

                buttonAnswer1.setText(question.getCorrectAnswer());
                buttonAnswer2.setText(question.getIncorrectAnswer1());
                buttonAnswer3.setText(question.getIncorrectAnswer2());
                buttonAnswer4.setText(question.getIncorrectAnswer3());
                textViewAnswerQuestion.setText(question.getQuestion());
            }
        });
        //endregion





    }
}