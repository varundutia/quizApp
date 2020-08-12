package com.varun.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.varun.trivia.controller.AppController;
import com.varun.trivia.data.AnswerListAsyncResponse;
import com.varun.trivia.model.PrefsClass;
import com.varun.trivia.model.Question;
import com.varun.trivia.model.QuestionBank;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private PrefsClass prefsClass;
    private TextView highScoreView;
    private TextView questionTextView;
    private TextView questionCounterTextView;
    private TextView scoreView;
    private Button trueButton;
    private Button falseButton;
    private Button reset;
    private int currentIndex=0;
    private int scoreCounter=0;
    private int highScore=0;
    private final String id="121212";
    private List<Question> questionList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefsClass=new PrefsClass(MainActivity.this);
        highScoreView=findViewById(R.id.high_score);
        scoreView=findViewById(R.id.score_view);
        questionTextView=findViewById(R.id.questions_text);
        questionCounterTextView=findViewById(R.id.question_counter);
        trueButton=findViewById(R.id.true_button);
        falseButton=findViewById(R.id.false_button);
        reset=findViewById(R.id.reset_button);
        highScoreView.setText("High Score: 0");

        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);
        reset.setOnClickListener(this);
        questionList =new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                questionTextView.setText(questionArrayList.get(currentIndex).getAnswer().toString());
                questionCounterTextView.setText(String.valueOf((currentIndex=prefsClass.getLastIndex())+1)+" out of "+questionList.size());
                scoreView.setText("Score: "+String.valueOf(scoreCounter));
                Log.d("MainQuestion","List="+questionArrayList);
            }
        });
        showSharedPref();
    }

    private void showSharedPref() {
        highScore = prefsClass.getHighScore();
        highScoreView.setText("High Score: "+String.valueOf(highScore));
    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){
            case R.id.true_button:
                checkAnswer(true);
                currentIndex=(currentIndex+1)%questionList.size();
                break;
            case R.id.false_button:
                checkAnswer(false);
                currentIndex=(currentIndex+1)%questionList.size();
                break;
           case R.id.reset_button:
               prefsClass.updateQuestionIndex(0);
               prefsClass.resetHighScore();
               highScoreView.setText("High Score: 0");
               currentIndex=0;
               scoreCounter=0;
       }
        updateQuestion();
    }
    private void checkAnswer(boolean b) {
        boolean isAnswerTrue=questionList.get(currentIndex).isAnswerTrue();
        if(b==isAnswerTrue){
            scoreCounter+=10;
            if(highScore<scoreCounter){
                highScore=scoreCounter;
                prefsClass.updateHighScore(highScore);
                prefsClass.getHighScore();
                updateSharedPref();
                showSharedPref();
            }
            updateQuestion();
            Toast.makeText(MainActivity.this,"You're Awesome",Toast.LENGTH_SHORT)
                    .show();
            fadeView();
        }else{
            if(scoreCounter>0)
                scoreCounter-=5;
            Toast.makeText(MainActivity.this,"Try Again",Toast.LENGTH_SHORT)
                    .show();
            shakeAnimation();
        }
    }

    private void updateSharedPref() {
        prefsClass.updateHighScore(highScore);
    }

    //  private void showSharedPref() {
    //     SharedPreferences getHighScore=getSharedPreferences(id,MODE_PRIVATE);
    //    highScore = getHighScore.getInt("highScore",0);
    //    highScoreView.setText("High Score: "+String.valueOf(highScore));
    // }

    // private void updateSharedPref() {
    //    SharedPreferences getHighScore=getSharedPreferences(id,MODE_PRIVATE);
    //    int highScoreGot = getHighScore.getInt("highScore",0);
    //    highScoreView.setText("High Score: "+String.valueOf(highScoreGot));
    //    SharedPreferences sharedPreferences=getSharedPreferences(id,MODE_PRIVATE);
    //    SharedPreferences.Editor editor=sharedPreferences.edit();
    //    editor.putInt("highScore",highScore);
    //    editor.apply();
    // }

    private void updateQuestion() {
        String question=questionList.get(currentIndex).getAnswer();
        questionTextView.setText(question);
        questionCounterTextView.setText(String.valueOf(currentIndex+1)+" out of "+questionList.size());
        scoreView.setText("Score: "+String.valueOf(scoreCounter));
        prefsClass.updateQuestionIndex(currentIndex);
    }

    private void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,
                R.anim.shake_animation);
        final CardView cardView=findViewById(R.id.card_view);
        cardView.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void fadeView(){
        final CardView cardView=findViewById(R.id.card_view);
        AlphaAnimation alphaAnimation =new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        cardView.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
