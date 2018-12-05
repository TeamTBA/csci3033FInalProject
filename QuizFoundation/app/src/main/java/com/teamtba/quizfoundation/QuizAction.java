package com.teamtba.quizfoundation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class QuizAction extends AppCompatActivity {

    //QuizTaker class used to track # of questions taken
    //number incorrect, and store the incorrect Answers
    static QuizTaker myQuiz;

    //Declare Buttons and TextViews
    Button[] answerButtons;

    TextView questionNumber;
    TextView questionDisplay;

    //string pulled from Extra
    String selectedQuiz;

    //arrayList will hold the located Questions
    static QuizDatabase.Subcategory quizCategory;

    //use int to track selected answer
    static int questionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_action);

        //questionID is set to 0, as anytime we return to this screen, we will need
        //to start at the beginning of the selected quiz
        questionID = 0;

        quizCategory = new QuizDatabase.Subcategory();
        selectedQuiz = getIntent().getStringExtra("selectedQuiz");
        quizCategory = QuizDatabase.getSubcategory(selectedQuiz);
        myQuiz = new QuizTaker();

        myQuiz.totalQuestions = quizCategory.questions.size();

        //set buttons and textviews to their views
        answerButtons = new Button[]
        {
                findViewById(R.id.answer0),
                findViewById(R.id.answer1),
                findViewById(R.id.answer2),
                findViewById(R.id.answer3),
        };

        questionNumber = findViewById(R.id.questionNumber);
        questionDisplay = findViewById(R.id.questionDisplay);

        //Begin a for loop that is driving algorithm for the QuizAction class
        //For each question included in our SubCategory List 'Questions'
        //we will update the textviews on each question
        updateQuestion(questionID);

        for (int i = 0; i < answerButtons.length; ++i)
        {
            int __i = i; // used for by-value lambda capture
            answerButtons[i].setOnClickListener(e -> submitAnswer(__i));
        }
    }

    private void submitAnswer(int answer)
    {
        if (answer != quizCategory.questions.get(questionID).answer) {
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

        QuizDatabase.Question question = quizCategory.questions.get(questionID);

        // only buttons with possible answers will remain visible
        for (int i = 0; i < answerButtons.length; ++i)
        {
            if (i < question.choices.length)
            {
                answerButtons[i].setVisibility(View.VISIBLE);
                answerButtons[i].setText(question.choices[i]);
            }
            else answerButtons[i].setVisibility(View.INVISIBLE);
        }
    }

}
