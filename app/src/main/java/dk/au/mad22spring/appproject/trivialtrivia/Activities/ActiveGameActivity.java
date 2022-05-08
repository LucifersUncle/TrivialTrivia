package dk.au.mad22spring.appproject.trivialtrivia.Activities;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    //region Global Variables
    private String documentId, playerObj, correctAnswer;
    private boolean isClickable;
    //endregion

    //region Widgets
    private Button buttonAnswer1, buttonAnswer2, buttonAnswer3, buttonAnswer4;
    private Button buttonLeave, buttonAnswerSelected, buttonNextQuestion;
    private TextView textCurrentRound, textViewAnswerQuestion, textCategoryBanner, textScore, timeLeft;
    private CountDownTimer countDownTimer;
    //endregion


    private ActiveGameViewModel vm;
    private Player playerReference;
    private List<Player> playerList;
    private ActiveGame activeGame;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_game);

        //region Buttons

        //region Buttons handling the answers
        buttonAnswer1 = findViewById(R.id.txt_aGame_answer1);
        buttonAnswer2 = findViewById(R.id.txt_aGame_answer2);
        buttonAnswer3 = findViewById(R.id.txt_aGame_answer3);
        buttonAnswer4 = findViewById(R.id.txt_aGame_answer4);
        //endregion

        //region Button for moving to the next question
        buttonNextQuestion = findViewById(R.id.buttonNextQuestion);
        buttonNextQuestion.setVisibility(View.GONE);
        buttonNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClickable = true;
                Toast.makeText(ActiveGameActivity.this, "Go To Next Round", Toast.LENGTH_SHORT).show();
                vm.setNextRound(documentId, activeGame.getCurrentRound());
            }
        });
        //endregion

        //region Leave Button
        buttonLeave = findViewById(R.id.buttonLeaveActiveGame);
        buttonLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //endregion

        //Button state
        isClickable = true;

        //endregion

        //region TextViews
        textViewAnswerQuestion = findViewById(R.id.txt_aGame_Question);
        textCurrentRound = findViewById(R.id.textCurrentRound);
        textCategoryBanner = findViewById(R.id.textCategoryBanner);
        timeLeft = findViewById(R.id.textTimeLeft);
        textScore = findViewById(R.id.txt_aGame_scorePoint);
        //endregion

        //Executor Service used in connection with the time, to deploy a new Thread to handle logic - currently not doing much if anything
        executorService = Executors.newSingleThreadExecutor();

        //region Receive Data from previous activity using Intents
        documentId = (String) getIntent().getSerializableExtra(Constants.DOC_OBJ);
        playerObj = (String) getIntent().getSerializableExtra(Constants.PLAYER_OBJ);
        playerReference = (Player) getIntent().getSerializableExtra(Constants.PLAYER_REF);
        //endregion

        //region ViewModels being Set Up and Observed Upon - Looking at the RealTimeDatabase and Data Changes
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
                    if (playerObj.equals("player")) {
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
                textScore.setText(String.valueOf(playerReference.getScore()));
            }
        });

        vm.getQuestions(documentId).observe(this, new Observer<List<Question>>() {
            @Override
            public void onChanged(List<Question> questions) {
                List<Question> answerList = new ArrayList<>(); //size 10 at 2 questions
                for (Question q : questions) {
                    answerList.add(q);
                }

                Question currentQuestion = answerList.get(activeGame.getCurrentRound());
                List<String> randomQuestion = new ArrayList<>();
                randomQuestion.add(currentQuestion.getIncorrectAnswer1());
                randomQuestion.add(currentQuestion.getIncorrectAnswer2());
                randomQuestion.add(currentQuestion.getIncorrectAnswer3());
                randomQuestion.add(currentQuestion.getCorrectAnswer());

                Collections.shuffle(randomQuestion);


                    buttonAnswer1.setText(randomQuestion.get(0));
                    buttonAnswer2.setText(randomQuestion.get(1));
                    buttonAnswer3.setText(randomQuestion.get(2));
                    buttonAnswer4.setText(randomQuestion.get(3));
                    correctAnswer = currentQuestion.getCorrectAnswer();
                    textViewAnswerQuestion.setText(currentQuestion.getQuestion());


                textCategoryBanner.setText("Category: " + questions.get(0).getCategory());

                if (activeGame == null) {
                    return;
                }

                startTimer(activeGame.getTimePerRound());
                textCurrentRound.setText(("Current Round: " + activeGame.getCurrentRound()));
                textScore.setText(String.valueOf(playerReference.getScore()));
            }
        });
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

    private void resetAnswerButtonBackground(Button view, int index) {
        view.setBackgroundColor(Color.parseColor("orange"));
    }

    //region Question Options and Interaction with the UI
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

    private void onWrongAnswer() {
        if (playerObj.equals("host")) {
            buttonNextQuestion.setVisibility(View.VISIBLE);
        } else {
            buttonNextQuestion.setVisibility(View.GONE);
        }
    }

    private void onCorrectAnswer() {
        vm.setScore(documentId, playerReference.getPlayerRef(), playerReference.getScore());
        if (playerObj.equals("host")) {
            buttonNextQuestion.setVisibility(View.VISIBLE);
        } else {
            buttonNextQuestion.setVisibility(View.GONE);
        }
    }
    //endregion

    //region Timer Logic - Currently not working quite as intended
    //***************************************************************//
    //https://developer.android.com/reference/android/os/CountDownTimer
    //***************************************************************//
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
                //on timer expiry
                timeLeft.setText("Time Out!");
                /*
                if (playerReference.equals("host")){
                    buttonNextQuestion.setVisibility(View.VISIBLE);
                }
                 */

                //if last round is reached and if the player chose the right answer, then set the score using the ViewModel object
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
                                    Toast.makeText(ActiveGameActivity.this, "SOMEONE WON!", Toast.LENGTH_SHORT).show();
                                    //launch the result intent
                                }
                            });
                        }
                    });
                } else {
                    //handles the right and wrong answer logic, not debugged thoroughly
                    if (isClickable) {
                        onWrongAnswer();
                    } else if (buttonAnswerSelected != null) {
                        if (correctAnswer == buttonAnswerSelected.getText().toString()) {
                            onCorrectAnswer();
                            resetAnswerButtonBackground(buttonAnswerSelected, buttonAnswerSelected.getId());
                            isClickable = true;
                        } else {
                            onWrongAnswer();
                            resetAnswerButtonBackground(buttonAnswerSelected, buttonAnswerSelected.getId());
                            isClickable = true;
                        }
                    }
                }
            }
        }.start();
    }
    //endregion

    //****************************//
    //https://stackoverflow.com/questions/20950066/how-implement-comparator-in-to-sort-objects-in-android-or-java
    //
    // Not Used, but kept for possible future development
    //***************************//
    private void sortPlayersByScore() {
        Collections.sort(playerList, new Comparator<Player>() {
            @Override
            public int compare(Player player1, Player player2) {
                return player1.getScore() - player2.getScore();
            }
        });
    }
}