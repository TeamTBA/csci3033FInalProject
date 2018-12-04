package com.teamtba.quizfoundation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class QuizAction extends AppCompatActivity implements View.OnClickListener {

    //QuizTaker class used to track # of questions taken
    //number incorrect, and store the incorrect Answers
    QuizTaker myQuiz;

    //Declare Buttons and TextViews
    Button answerZero;
    Button answerOne;
    Button answerTwo;
    Button answerThree;
    TextView questionNumber;
    TextView questionDisplay;

    //string pulled from Extra
    String selectedQuiz;

    //arrayList will hold the located Questions
    QuizDatabase.Subcategory quizCategory;

    //track number of questions taken
    int totalQuestions = 0;

    //use int to track selected answer
    int answerSubmit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_action);


        quizCategory.questions = new ArrayList<QuizDatabase.Question>();
        selectedQuiz = getIntent().getStringExtra("selectedQuiz");
        quizCategory = QuizDatabase.getSubcategory(selectedQuiz);
        myQuiz = new QuizTaker();
        myQuiz.totalQuestions = quizCategory.questions.size();


        //set buttons and textviews to their views
        answerZero = findViewById(R.id.answer0);
        answerOne = findViewById(R.id.answer1);
        answerTwo = findViewById(R.id.answer2);
        answerThree = findViewById(R.id.answer3);
        questionNumber = findViewById(R.id.questionNumber);
        questionDisplay = findViewById(R.id.questionDisplay);

        //Begin a for loop that is driving algorithm for the QuizAction class
        //For each question included in our SubCategory List 'Questions'
        //we will update the textviews on each question
        for (int i = 0; i < quizCategory.questions.size(); i++) {
            //update textviews and buttons
            questionNumber.setText("Question " + totalQuestions);
            questionDisplay.setText(quizCategory.questions.get(i).text);
            //only buttons with possible answers will remain visible
            if (quizCategory.questions.get(i).choices.length == 3) {
                answerTwo.setVisibility(View.VISIBLE);
                answerTwo.setText(quizCategory.questions.get(i).choices[2]);
            } else if (quizCategory.questions.get(i).choices.length == 4) {
                answerTwo.setVisibility(View.VISIBLE);
                answerThree.setVisibility(View.VISIBLE);
                answerTwo.setText(quizCategory.questions.get(i).choices[2]);
                answerThree.setText(quizCategory.questions.get(i).choices[3]);

            } else {
                answerTwo.setVisibility(View.INVISIBLE);
                answerThree.setVisibility(View.INVISIBLE);
            }
            //cycle through the possible answers and store them into button texts
            answerZero.setText(quizCategory.questions.get(i).choices[0]);
            answerOne.setText(quizCategory.questions.get(i).choices[1]);

            answerZero.setOnClickListener(this);
            answerOne.setOnClickListener(this);
            answerTwo.setOnClickListener(this);
            answerThree.setOnClickListener(this);

            if (answerSubmit != quizCategory.questions.get(i).answer)
            {
                myQuiz.incorrectAnswers.add(quizCategory.questions.get(i));
            }
        }

        //after 4 loop is completed, we have completed our quiz questions and will move to the quiz
        //results page
        Intent intent = new Intent(QuizAction.this,QuizResults.class);
        intent.putExtra("myQuiz", myQuiz);
        startActivity(intent);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.answer0: {
                answerSubmit = 0;
                break;
            }
            case R.id.answer1: {
                answerSubmit = 1;
                break;
            }
            case R.id.answer2: {
                answerSubmit = 2;
                break;
            }
            case R.id.answer3: {
                answerSubmit = 3;
                break;
            }
        }
    }
}
