package com.teamtba.quizfoundation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class QuizResults extends AppCompatActivity implements View.OnClickListener {

    TextView passNotice;
    TextView failNotice;
    TextView gradePercentage;
    TextView incorrectAnswersNotice;
    Button takeNewQuiz;

    QuizTaker takenQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_results);

        //pull in the stored QuizTaker from the quizAction page
        takenQuiz = (QuizTaker) getIntent().getExtras().getSerializable("myQuiz");


        //set our view variables to their respective view Id's
        passNotice = findViewById(R.id.pass_notice);
        failNotice = findViewById(R.id.fail_notice);
        gradePercentage = findViewById(R.id.grade_percentage);
        incorrectAnswersNotice = findViewById(R.id.incorrect_answers_amount);
        takeNewQuiz = findViewById(R.id.startNewQuiz);


        //if the quizscore is under 70%, then quiz is considered failed
        //and will edit visibility on our pass/fail accordingly
        if (takenQuiz.getQuizScore() < 0.70)
        {
            passNotice.setVisibility(View.INVISIBLE);
            failNotice.setVisibility(View.VISIBLE);
        }
        else
        {
            passNotice.setVisibility(View.VISIBLE);
            failNotice.setVisibility(View.INVISIBLE);
        }

        gradePercentage.setText(String.valueOf(takenQuiz.getQuizScore() * 100.00) + "%");
        incorrectAnswersNotice.setText("You answered " + String.valueOf(takenQuiz.incorrectAnswers.size()) + "/" + String.valueOf(takenQuiz.totalQuestions) + " Questions Incorrectly");

        //listen to see if user chooses to go back to Select a New Quiz
        takeNewQuiz.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent goHome = new Intent(QuizResults.this,QuizSelector.class);
        goHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goHome);
    }
}

