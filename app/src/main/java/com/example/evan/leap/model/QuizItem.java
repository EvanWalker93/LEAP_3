package com.example.evan.leap.model;

import java.util.Objects;

/**
 * Created by Evan on 4/22/2017.
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
