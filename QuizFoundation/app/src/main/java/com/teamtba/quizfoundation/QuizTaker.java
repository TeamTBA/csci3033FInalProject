package com.teamtba.quizfoundation;

import java.util.ArrayList;

public class QuizTaker {
    public ArrayList<QuizDatabase> incorrectAnswers;
    int totalQuestions;
    double quizScore;


    public QuizTaker(){
        totalQuestions = 0;
        incorrectAnswers = new ArrayList<QuizDatabase>();
        quizScore = 0.00;
    }


}
