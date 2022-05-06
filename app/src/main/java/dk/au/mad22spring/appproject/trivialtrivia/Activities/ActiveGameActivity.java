package dk.au.mad22spring.appproject.trivialtrivia.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dk.au.mad22spring.appproject.trivialtrivia.Constants.Constants;
import dk.au.mad22spring.appproject.trivialtrivia.Models.ActiveGame;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Player;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Question;
import dk.au.mad22spring.appproject.trivialtrivia.R;
import dk.au.mad22spring.appproject.trivialtrivia.ViewModels.ActiveGameViewModel;

public class ActiveGameActivity extends AppCompatActivity {

    private String documentId, whoIsCalling, correctAnswer;

    private Button buttonAnswer1, buttonAnswer2, buttonAnswer3, buttonAnswer4;
    private Button buttonLeave;
    private TextView textViewScore, textViewAnswerQuestion;
    private ProgressBar progressBarTime;

    private Question question;
    private ActiveGameViewModel vm;
    private Player player;
    private ActiveGame activeGame;
    private List<Player> playerList;

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

        documentId = (String) getIntent().getSerializableExtra(Constants.DOC_OBJ);
        whoIsCalling = (String) getIntent().getSerializableExtra(Constants.PLAYER_OBJ);
        player = (Player) getIntent().getSerializableExtra(Constants.PLAYER_REF);

        buttonLeave = findViewById(R.id.buttonLeaveActiveGame);
        buttonLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        vm = new ViewModelProvider(this).get(ActiveGameViewModel.class);

        vm.getActiveGame(documentId).observe(this, new Observer<ActiveGame>() {
            @Override
            public void onChanged(ActiveGame game) {
                if (game == null) {
                    return;
                }
                activeGame = game;
            }
        });

        vm.getPlayers(documentId).observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {
                playerList = players;
            }
        });

        vm.getActiveState(documentId).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean active) {
                if (!active) {
                    if (whoIsCalling.equals("player")) {
                        Intent intent = new Intent();
                        setResult(RESULT_CANCELED, intent);
                        finish();
                    }
                }
            }
        });

        vm.getPlayer(documentId, player.getPlayerRef()).observe(this, new Observer<Player>() {
            @Override
            public void onChanged(Player playerToGet) {
                if (player == null) {
                    return;
                }
                player = playerToGet;
            }
        });

        /*
        vm.getQuestions(documentId).observe(this, new Observer<List<Question>>() {
            @Override
            public void onChanged(List<Question> questions) {

            }
        });

         */



        /*
        vm.getCurrentQuestion(documentId).observe(this, new Observer<Question>() {
            @Override
            public void onChanged(Question question) {
                if (question == null) {
                    return;
                }

                List<String> allQuestions = new ArrayList<>();
                allQuestions.add(question.getCorrectAnswer());
                allQuestions.add(question.getIncorrectAnswer1());
                allQuestions.add(question.getIncorrectAnswer2());
                allQuestions.add(question.getIncorrectAnswer3());

                correctAnswer = question.getCorrectAnswer();

                Collections.shuffle(allQuestions);

                buttonAnswer1.setText(allQuestions.get(0));
                buttonAnswer2.setText(allQuestions.get(1));
                buttonAnswer3.setText(allQuestions.get(2));
                buttonAnswer4.setText(allQuestions.get(3));

                if (activeGame == null) {
                    return;
                }
            }
        });
         */
        //region virker ikke her


        vm.getQuestions(documentId).observe(this, new Observer<List<Question>>() {
            @Override
            public void onChanged(List<Question> questions) {


                buttonAnswer1.setText(questions.get(0).getCorrectAnswer());
                buttonAnswer2.setText(questions.get(0).getIncorrectAnswer1());
                buttonAnswer3.setText(questions.get(0).getIncorrectAnswer2());
                buttonAnswer4.setText(questions.get(0).getIncorrectAnswer3());
                textViewAnswerQuestion.setText(questions.get(0).getQuestion());


            }
        });






    }
}