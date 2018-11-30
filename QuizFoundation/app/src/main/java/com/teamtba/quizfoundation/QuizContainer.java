package com.teamtba.quizfoundation;

import java.util.ArrayList;

public class QuizContainer {

    ArrayList<QuizDatabase> newQuiz;
    int totalQuestions;
    String categoryType;
    String subjectType;


    public QuizContainer(String category, String subjectType){
        newQuiz = new ArrayList<QuizDatabase>();
        int correctAnswers = 0;
        int totalQuestions = newQuiz.size();
        categoryType = category;
    }
}
