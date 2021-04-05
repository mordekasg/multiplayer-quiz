package pl.edu.uwr.pum.pumproject2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Question {
    @SerializedName("question")
    public String question;

    @SerializedName("correct_answer")
    public String correct_answer;

    @SerializedName("incorrect_answers")
    public List<String> incorrect_answers;

    public List<String> allAnswers;

    public String selectedAnswer;
}
