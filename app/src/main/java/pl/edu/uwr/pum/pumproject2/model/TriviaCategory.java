package pl.edu.uwr.pum.pumproject2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TriviaCategory {
    @SerializedName("trivia_categories")
    @Expose
    private List<Category> triviaCategories = null;

    public List<Category> getTriviaCategories() {
        return triviaCategories;
    }

    public void setTriviaCategories(List<Category> triviaCategories) {
        this.triviaCategories = triviaCategories;
    }
}
