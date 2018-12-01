package com.teamtba.quizfoundation;

import java.util.ArrayList;

public class QuizTaker {
    public ArrayList<QuizDatabase.Question> incorrectAnswers;
    int totalQuestions;
    double quizScore;


    public QuizTaker(){
        totalQuestions = 0;
        incorrectAnswers = new ArrayList<QuizDatabase.Question>();
        quizScore = 0.00;
    }


}
