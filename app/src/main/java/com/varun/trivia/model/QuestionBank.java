package com.varun.trivia.model;

import android.app.DownloadManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.varun.trivia.controller.AppController;
import com.varun.trivia.data.AnswerListAsyncResponse;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {
    private String url="https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";
    ArrayList<Question> questionArrayList=new ArrayList<>();
    public List<Question> getQuestions(final AnswerListAsyncResponse callBack){
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0;i<response.length();i++){
                    try {
                        //Declare Question type and save the questions and the answer to the question object
                        Question question=new Question();
                        question.setAnswer(response.getJSONArray(i).get(0).toString());
                        question.setAnswerTrue(response.getJSONArray(i).getBoolean(1));
                        //Save the question object into the craeted arraylist
                        questionArrayList.add(question);
                        Log.d("JSON","Hello"+question);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(callBack !=null)  callBack.processFinished(questionArrayList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questionArrayList;
    }
}
