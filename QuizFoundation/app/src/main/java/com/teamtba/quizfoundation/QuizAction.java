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
    static QuizTaker myQuiz;

    //Declare Buttons and TextViews
    static Button answerZero;
    static Button answerOne;
    static Button answerTwo;
    static Button answerThree;
    static TextView questionNumber;
    static TextView questionDisplay;

    //string pulled from Extra
    String selectedQuiz;

    //arrayList will hold the located Questions
    static QuizDatabase.Subcategory quizCategory;

    //use int to track selected answer
    static int questionID;
    static int answerSubmit = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_action);

        //questionID is set to 0, as anytime we return to this screen, we will need
        //to start at the beginning of the selected quiz
        questionID = 0;

        quizCategory = new QuizDatabase.Subcategory();
        //quizCategory.questions = new ArrayList<QuizDatabase.Question>();
        selectedQuiz = getIntent().getStringExtra("selectedQuiz");
        //quizCategory = QuizDatabase.getSubcategory(selectedQuiz);
        myQuiz = new QuizTaker();

        List<String> dummychoices1 = new ArrayList<>();
        dummychoices1.add("True");
        dummychoices1.add("False");

        QuizDatabase.Question newQuestion = new QuizDatabase.Question();
        newQuestion.text = "This Quiz is awesome";
        //newQuestion.choices.add("True");
        newQuestion.choices = (dummychoices1.toArray(new String[0]));
        newQuestion.answer = 0;

        quizCategory.questions.add(newQuestion);

        List<String> dummychoices2 = new ArrayList<>();
        dummychoices2.add("Not too Long at all");
        dummychoices2.add("Way too Long");
        dummychoices2.add("Almost too long");

        QuizDatabase.Question secondQuestion = new QuizDatabase.Question();
        secondQuestion.text = "Is this quiz too long?";
        secondQuestion.choices = (dummychoices2.toArray(new String[0]));
        secondQuestion.answer = 0;
        myQuiz.totalQuestions = quizCategory.questions.size();

        quizCategory.questions.add(secondQuestion);
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
        updateQuestion(questionID);
        answerZero.setOnClickListener(this);
        answerOne.setOnClickListener(this);
        answerTwo.setOnClickListener(this);
        answerThree.setOnClickListener(this);

    }





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
        if (answerSubmit != quizCategory.questions.get(questionID).answer) {
            myQuiz.incorrectAnswers.add(quizCategory.questions.get(questionID));
        }
        questionID+=1;
        if (questionID < quizCategory.questions.size()) {
            updateQuestion(questionID);
        }
        else
        {
            Intent intent = new Intent(QuizAction.this, QuizResults.class);
            intent.putExtra("myQuiz", myQuiz);
            startActivity(intent);
        }
    }

    public void updateQuestion(int questionID)
    {
        questionNumber.setText("Question " + (questionID + 1));
        questionDisplay.setText(quizCategory.questions.get(questionID).text);
        //only buttons with possible answers will remain visible
        if (quizCategory.questions.get(questionID).choices.length == 3) {
            answerTwo.setVisibility(View.VISIBLE);
            answerTwo.setText(quizCategory.questions.get(questionID).choices[2]);
        } else if (quizCategory.questions.get(questionID).choices.length == 4) {
            answerTwo.setVisibility(View.VISIBLE);
            answerThree.setVisibility(View.VISIBLE);
            answerTwo.setText(quizCategory.questions.get(questionID).choices[2]);
            answerThree.setText(quizCategory.questions.get(questionID).choices[3]);

        } else {
            answerTwo.setVisibility(View.INVISIBLE);
            answerThree.setVisibility(View.INVISIBLE);
        }
        //cycle through the possible answers and store them into button texts
        answerZero.setText(quizCategory.questions.get(questionID).choices[0]);
        answerOne.setText(quizCategory.questions.get(questionID).choices[1]);
    }

}
