package com.example.evan.leap.model;


/**
 * Created by Evan on 4/22/2017.
 * Item that holds the quiz name and length.
 */

public class QuizItem {
    private String quizFileName, quizFilePath;


    public QuizItem(String quizFileName, String quizFilePath) {
        this.quizFileName = quizFileName;
        this.quizFilePath = quizFilePath;

    }


    public String getQuizFileName() {
        return quizFileName;
    }

    public void setQuizFileName(String quizFileName) {
        this.quizFileName = quizFileName;
    }

    public String getQuizFilePath() {
        return quizFilePath;
    }

    public void setQuizFilePath(String quizFilePath) {
        this.quizFilePath = quizFilePath;
    }
}
