package com.teamtba.quizfoundation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class QuizAction extends AppCompatActivity {


    QuizTaker myQuiz;

    //Declare Buttons and TextViews
    Button answerZero;
    Button answerOne;
    Button answerTwo;
    Button answerThree;
    Button answerSubmit;
    TextView questionNumber;
    TextView questionDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_action);




        //initialize our Buttons and
        //page includes 4 buttons, 2 default to invisible
        //a button is marked visible for each question as needed
        //load the string answer into the question button
        //on user click, the button is highlighted
        //on highlight, change button to red or darker color
        //user has the option to select or highlight a new answer
        //user then hits the "Submit Answer Button"
        //toast saying "Answer Submitted"


        /*when filling in the QuizAction activity
        if choices.size == 2;, no action on buttons
        if choices.size == 3; make button for AnswerTwo set to visible
        if choices.size == 4; make button for answerThree set to visible
        ****Make sure to revert back to invisible for 2/3***/
    }
}
