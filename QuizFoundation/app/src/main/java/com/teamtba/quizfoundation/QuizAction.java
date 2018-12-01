package com.teamtba.quizfoundation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class QuizAction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_action);

        //page includes 4 buttons, 2 default to invisible
        //a button is marked visible for each question as needed
        //load the string answer into the question button
        //on user click, the button is highlighted
        //on highlight, change button to red or darker color
        //user has the option to select or highlight a new answer
        //user then hits the "Submit Answer Button"
        //toast saying "Answer Submitted"
    }
}
