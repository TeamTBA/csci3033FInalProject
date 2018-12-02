package com.teamtba.quizfoundation;

import java.io.Serializable;
import java.util.ArrayList;

public class QuizTaker implements Serializable {
    public ArrayList<QuizDatabase.Question> incorrectAnswers;
    int totalQuestions;
    double quizScore;


    public QuizTaker(){
        totalQuestions = 0;
        incorrectAnswers = new ArrayList<QuizDatabase.Question>();
        quizScore = 0.00;
    }

    public double getQuizScore()
    { return (totalQuestions - incorrectAnswers.size()) / totalQuestions;}


}
