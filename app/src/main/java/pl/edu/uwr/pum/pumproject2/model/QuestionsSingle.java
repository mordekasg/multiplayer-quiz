package pl.edu.uwr.pum.pumproject2.model;

import android.content.Context;
import android.text.Html;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionsSingle {
    private static QuestionsSingle questionsSingle;
    private List<Question> questions;
    public int correct = 0;
    public int wrong = 0;

    private QuestionsSingle(Context context){
        questions = new ArrayList<>();
    }

    public static QuestionsSingle get(Context context) {
        if(questionsSingle == null) {
            questionsSingle = new QuestionsSingle(context);
        }

        return questionsSingle;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setCrimes(List<Question> questions) {
        this.questions = questions;

        for (Question question : questions) {
            question.question = Html.fromHtml(question.question).toString();

            question.correct_answer = Html.fromHtml(question.correct_answer).toString();
            for (String answer : question.incorrect_answers) {
                answer = Html.fromHtml(answer).toString();
            }
            question.allAnswers = question.incorrect_answers;
            question.allAnswers.add(question.correct_answer);
        }
    }

    public void clear() {
        questions = new ArrayList<>();
        correct = 0;
        wrong = 0;
    }
}
