package dk.au.mad22spring.appproject.trivialtrivia.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dk.au.mad22spring.appproject.trivialtrivia.Constants.Constants;
import dk.au.mad22spring.appproject.trivialtrivia.Models.ActiveGame;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Player;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Question;
import dk.au.mad22spring.appproject.trivialtrivia.R;
import dk.au.mad22spring.appproject.trivialtrivia.ViewModels.ActiveGameViewModel;

public class ActiveGameActivity extends AppCompatActivity {

    private String documentId, whoIsCalling, correctAnswer;

    private Button buttonAnswer1, buttonAnswer2, buttonAnswer3, buttonAnswer4;
    private Button buttonLeave, buttonAnswerSelected, buttonNextQuestion;
    private TextView textCurrentRound, textViewAnswerQuestion, textCategoryBanner, textScore, timeLeft;

    private CountDownTimer countDownTimer;
    private Question question;
    private ActiveGameViewModel vm;
    private Player playerReference;
    private ActiveGame activeGame;
    private List<Player> playerList;
    private boolean isClickable;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_game);

        //region widget setup
        buttonAnswer1 = findViewById(R.id.txt_aGame_answer1);
        buttonAnswer2 = findViewById(R.id.txt_aGame_answer2);
        buttonAnswer3 = findViewById(R.id.txt_aGame_answer3);
        buttonAnswer4 = findViewById(R.id.txt_aGame_answer4);

        buttonNextQuestion = findViewById(R.id.buttonNextQuestion);
        buttonNextQuestion.setVisibility(View.GONE);
        buttonNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActiveGameActivity.this, "Next Question", Toast.LENGTH_SHORT).show();
                isClickable = true;
                vm.setNextRound(documentId, activeGame.getCurrentRound());
            }
        });

        isClickable = true;

        textViewAnswerQuestion = findViewById(R.id.txt_aGame_Question);
        textCurrentRound = findViewById(R.id.textCurrentRound);
        textCategoryBanner = findViewById(R.id.textCategoryBanner);
        timeLeft = findViewById(R.id.textTimeLeft);
        textScore = findViewById(R.id.txt_aGame_scorePoint);
        //endregion

        executorService = Executors.newSingleThreadExecutor();

        documentId = (String) getIntent().getSerializableExtra(Constants.DOC_OBJ); //this is null for the player
        whoIsCalling = (String) getIntent().getSerializableExtra(Constants.PLAYER_OBJ);
        playerReference = (Player) getIntent().getSerializableExtra(Constants.PLAYER_REF); //wrong playerObject is sent with this one

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

        vm.getPlayer(documentId, playerReference.getPlayerRef()).observe(this, new Observer<Player>() {
            @Override
            public void onChanged(Player playerToGet) {
                if (playerReference == null) {
                    return;
                }
                playerReference = playerToGet;
            }
        });

        vm.getQuestions(documentId).observe(this, new Observer<List<Question>>() {
            @Override
            public void onChanged(List<Question> questions) {
                List<String> answerList = new ArrayList<>();
                for (Question q : questions) {
                    answerList.add(q.getIncorrectAnswer1());
                    answerList.add(q.getIncorrectAnswer2());
                    answerList.add(q.getIncorrectAnswer3());
                    answerList.add(q.getCorrectAnswer());
                    answerList.add(q.getQuestion());
                }

                buttonAnswer1.setText(answerList.get(0));
                buttonAnswer2.setText(answerList.get(1));
                buttonAnswer3.setText(answerList.get(2));
                buttonAnswer4.setText(answerList.get(3));
                correctAnswer = answerList.get(3);
                textViewAnswerQuestion.setText(answerList.get(4));

                /*
                buttonAnswer1.setText(questions.get(0).getCorrectAnswer());
                buttonAnswer2.setText(questions.get(0).getIncorrectAnswer1());
                buttonAnswer3.setText(questions.get(0).getIncorrectAnswer2());
                buttonAnswer4.setText(questions.get(0).getIncorrectAnswer3());
                textViewAnswerQuestion.setText(questions.get(0).getQuestion());
                 */
                textCategoryBanner.setText("Category: " + questions.get(0).getCategory());

                startTimer(activeGame.getTimePerRound());
                textCurrentRound.setText(("Current Round: " + Integer.toString(activeGame.getCurrentRound())));
                textScore.setText(String.valueOf(playerReference.getScore()));
            }
        });

        //region Skal nok slettes
/*
        vm.getCurrentQuestion(documentId).observe(this, new Observer<Question>() {
            @Override
            public void onChanged(Question question) {

                if (question == null ) {
                    return;
                }

                if (question.getCategory().equals(""))
                    return;

                textCategoryBanner.setText("Category:" + question.getCategory());

                List<String> answerList = new ArrayList<>();
                answerList.add(question.getIncorrectAnswer1());
                answerList.add(question.getIncorrectAnswer2());
                answerList.add(question.getIncorrectAnswer3());
                answerList.add(question.getCorrectAnswer());

                correctAnswer = question.getCorrectAnswer();

                Collections.shuffle(answerList);

                buttonAnswer1.setText(answerList.get(0));
                buttonAnswer2.setText(answerList.get(1));
                buttonAnswer3.setText(answerList.get(2));
                buttonAnswer4.setText(answerList.get(3));


                if (activeGame == null)
                    return;

                startTimer(activeGame.getTimePerRound());
                textCurrentRound.setText(String.valueOf(activeGame.getCurrentRound()));
                textScore.setText(String.valueOf(playerReference.getScore()));

            }
        });

 */
        //endregion
    }

    private void onAnswerPicked(Button button) {
        if (isClickable) {
            switch (button.getId()) {
                case R.id.txt_aGame_answer1:
                    if (buttonAnswer1.getText().equals(correctAnswer)) {
                        button.setBackgroundColor(Color.GREEN);
                    } else {
                        button.setBackgroundColor(Color.RED);
                    }
                    break;
                case R.id.txt_aGame_answer2:
                    if (buttonAnswer2.getText().equals(correctAnswer)) {
                        button.setBackgroundColor(Color.GREEN);
                    } else {
                        button.setBackgroundColor(Color.RED);
                    }
                    break;
                case R.id.txt_aGame_answer3:
                    if (buttonAnswer3.getText().equals(correctAnswer)) {
                        button.setBackgroundColor(Color.GREEN);
                    } else {
                        button.setBackgroundColor(Color.RED);
                    }
                    break;
                case R.id.txt_aGame_answer4:
                    if (buttonAnswer4.getText().equals(correctAnswer)) {
                        button.setBackgroundColor(Color.GREEN);
                    } else {
                        button.setBackgroundColor(Color.RED);
                    }
                    break;
            }
            buttonAnswerSelected = button;
            isClickable = false;
        }
    }

    private void resetAnswerButtonBackground(Button view) {
        view.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.question_default_background));
    }


    public void optionOne(View button) {
        if (isClickable){
            onAnswerPicked((Button) button);
        }
    }

    public void optionTwo(View button) {
        if (isClickable){
            onAnswerPicked((Button) button);
        }
    }

    public void optionThree(View button) {
        if (isClickable){
            onAnswerPicked((Button) button);
        }
    }

    public void optionFour(View button) {
        if (isClickable){
            onAnswerPicked((Button) button);
        }
    }

    private void startTimer(int timePerRound) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer((timePerRound*1000) + 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                timeLeft.setText("Time Out!");

                if (activeGame.getNumberOfRounds() == activeGame.getCurrentRound()) {
                    if (buttonAnswerSelected != null && correctAnswer == buttonAnswerSelected.getText().toString())
                            vm.setScore(documentId, playerReference.getPlayerRef(), playerReference.getScore());
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ie) {
                                ie.printStackTrace();
                            }
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    //display winner dialog
                                    //needs to be implemented in the result stage!
                                }
                            });
                        }
                    });
                } else {
                    if (isClickable) {
                        //display wrong answer
                    } else if (buttonAnswerSelected != null) {
                        if (correctAnswer == buttonAnswerSelected.getText().toString()) {
                            //display correct answer
                            resetAnswerButtonBackground(buttonAnswerSelected);
                            isClickable = true;
                        } else {
                            //display wrong answer
                            resetAnswerButtonBackground(buttonAnswerSelected);
                            isClickable = true;
                        }
                    }
                }
            }
        }.start();
    }

    private void onWrongAnswer() {
        if (whoIsCalling.equals("host")) {
            buttonNextQuestion.setVisibility(View.VISIBLE);
        }
    }

    private void onCorrectAnswer() {
        if (whoIsCalling.equals("host")) {
            buttonNextQuestion.setVisibility(View.VISIBLE);
        }
    }
}