package com.varun.trivia.model;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PrefsClass {
    private SharedPreferences sharedPreferences;

    public PrefsClass(Activity activity) {
        this.sharedPreferences = activity.getPreferences(activity.MODE_PRIVATE);
    }
    public void updateHighScore(int score){
        int lastscore=sharedPreferences.getInt("highScore",0);
        if(score>lastscore){
            sharedPreferences.edit().putInt("highScore",score).apply();
        }
    }
    public void resetHighScore(){
        sharedPreferences.edit().putInt("highScore",0).apply();
    }
    public void updateQuestionIndex(int index){
        sharedPreferences.edit().putInt("index",index).apply();
    }
    public int getLastIndex(){
        return sharedPreferences.getInt("index",0);
    }
    public int getHighScore(){
        return sharedPreferences.getInt("highScore",0);
    }
}
