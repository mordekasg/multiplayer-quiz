package pl.edu.uwr.pum.pamproject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TriviaQuestions {
    @SerializedName("results")
    @Expose
    private List<Question> triviaQuestions = null;

    public List<Question> getTriviaQuestions() {
        return triviaQuestions;
    }

    public void setTriviaQuestions(List<Question> triviaQuestions) {
        this.triviaQuestions = triviaQuestions;
    }
}
