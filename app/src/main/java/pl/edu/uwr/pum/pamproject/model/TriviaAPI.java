package pl.edu.uwr.pum.pamproject.model;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TriviaAPI {
    @GET("api_category.php")
    Single<TriviaCategory> getCategories();

    @GET("api.php")
    Single<TriviaQuestions> getQuestions(@Query("amount") int amount, @Query("category") int category, @Query("type") String type);
}
