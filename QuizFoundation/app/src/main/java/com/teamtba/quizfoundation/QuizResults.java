package com.teamtba.quizfoundation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class QuizResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_results);

        //create recycler view
        RecyclerView incorrect_answers_recycler = findViewById(R.id.incorrect_answers_recycler);
        incorrect_answers_recycler.setHasFixedSize(false);
        RecyclerView.LayoutManager newLayoutManager = new LinearLayoutManager(this);
        incorrect_answers_recycler.setLayoutManager(newLayoutManager);

        ArrayList<String> incorrect_answers = new ArrayList<>();
        for (int i = 0; i < 10; i++)
        {
            incorrect_answers.add("Wrong Answer" + i);
        }

        //RecyclerView.Adapter adapter = new RecyclerView.Adapter(incorrect_answers) {
        };


}

